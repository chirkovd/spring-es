package com.dipegroup.spring.es.repositories;

import com.dipegroup.exceptions.models.AppErrorImp;
import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.spring.es.models.beans.FiltersBean;
import com.dipegroup.spring.es.models.filters.FilterGroup;
import com.dipegroup.spring.es.models.filters.FilterGroupItem;
import com.dipegroup.spring.es.models.objects.EsModel;
import com.dipegroup.spring.es.services.app.page.AppFieldsService;
import com.dipegroup.spring.es.services.es.ElasticsearchClientService;
import com.dipegroup.spring.es.services.es.ElasticsearchCriteriaBuilder;
import com.dipegroup.spring.es.services.es.ElasticsearchHighlightingService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: spring-es
 * Description: this instance implemented common logic for loading filter groups by ES aggregations, collection of
 *              filters is configurable and provided by {@code fieldsService}. Besides, filter groups depends on
 *              different constraints: search term or selected item for limit/exclude from results.
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class ElasticsearchFiltersRepository<E extends EsModel<K>, K>
        extends ElasticsearchCrudRepository<E, K> {

    protected final ElasticsearchCriteriaBuilder criteriaBuilder;
    protected final AppFieldsService<E> fieldsService;

    protected ElasticsearchFiltersRepository(ElasticsearchClientService clientService,
                                             ElasticsearchCriteriaBuilder criteriaBuilder,
                                             AppFieldsService<E> fieldsService,
                                             ElasticsearchHighlightingService<E, K> highlightingService) {
        super(clientService, highlightingService);
        this.criteriaBuilder = criteriaBuilder;
        this.fieldsService = fieldsService;
    }

    public Map<String, FilterGroup> loadFilters(FiltersBean filtersBean) throws AppException {
        SearchRequestBuilder requestBuilder = client.prepareSearch(index).setTypes(type).setSize(0);
        if (StringUtils.isBlank(filtersBean.getSearch()) && filtersBean.getLimit().isEmpty()) {
            requestBuilder.setQuery(QueryBuilders.matchAllQuery());
        } else {
            BoolQueryBuilder query = QueryBuilders.boolQuery();

            BoolQueryBuilder searchCriteria = criteriaBuilder
                    .addSearchCriteria(filtersBean.getSearch(), fieldsService.getSearches());
            if (searchCriteria.hasClauses()) {
                query.must(searchCriteria);
            }
            BoolQueryBuilder limitCriteria = criteriaBuilder.addLimitCriteria(filtersBean.getLimit());
            if (limitCriteria.hasClauses()) {
                query.must(limitCriteria);
            }
            requestBuilder.setQuery(query);
        }

        List<TermsAggregationBuilder> aggregations = addAggregations(filtersBean.getFilters());
        aggregations.forEach(requestBuilder::addAggregation);

        SearchResponse response = requestBuilder.get();
        return readResponse(response, filtersBean.getFilters());
    }

    private List<TermsAggregationBuilder> addAggregations(List<String> filters) throws AppException {
        List<TermsAggregationBuilder> aggregations = new ArrayList<>();
        fieldsService.getFilters().stream()
                .filter(f -> filters.contains(f.getField()))
                .forEach(f -> {
                    Terms.Order termsOrder = f.isByValue()
                            ? Terms.Order.term(f.isAscending())
                            : Terms.Order.count(f.isAscending());
                    TermsAggregationBuilder terms = AggregationBuilders.terms(f.getField())
                            .field(f.getPath()).order(termsOrder);
                    aggregations.add(terms);
                });
        return aggregations;
    }

    private Map<String, FilterGroup> readResponse(SearchResponse response, List<String> filters) throws AppException {
        if (response.status().equals(RestStatus.OK)) {
            Map<String, FilterGroup> result = new HashMap<>();
            filters.stream().forEach(filter -> {
                FilterGroup filterGroup = new FilterGroup();
                Terms terms = response.getAggregations().get(filter);
                if (terms == null) {
                    return;
                }
                terms.getBuckets().stream().forEach(bucket -> {
                    FilterGroupItem item = new FilterGroupItem();
                    item.setGroup(bucket.getKeyAsString());
                    item.setCount(bucket.getDocCount());
                    filterGroup.getGroups().add(item);
                });
                filterGroup.setGroupsCount(terms.getBuckets().size());
                result.put(filter, filterGroup);
            });
            return result;
        }
        throw new AppException(AppErrorImp.APPLICATION_ERROR);
    }
}
