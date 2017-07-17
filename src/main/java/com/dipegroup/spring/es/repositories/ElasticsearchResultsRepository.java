package com.dipegroup.spring.es.repositories;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.exceptions.services.messages.MessagesService;
import com.dipegroup.spring.es.models.beans.ResultsBean;
import com.dipegroup.spring.es.models.fields.SortWorkflowField;
import com.dipegroup.spring.es.models.objects.EsModel;
import com.dipegroup.spring.es.models.results.Results;
import com.dipegroup.spring.es.services.app.AppFieldsService;
import com.dipegroup.spring.es.services.es.ElasticClientService;
import com.dipegroup.spring.es.services.es.ElasticsearchCriteriaBuilder;
import com.dipegroup.spring.es.services.es.ElasticsearchHighlightingService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class ElasticsearchResultsRepository<E extends EsModel<K>, K>
        extends ElasticsearchFiltersRepository<E, K> {

    private final MessagesService messagesService;
    protected final Class<E> responseType;

    public ElasticsearchResultsRepository(ElasticClientService clientService,
                                          MessagesService messagesService,
                                          AppFieldsService<E> fieldsService,
                                          ElasticsearchCriteriaBuilder criteriaBuilder, Class<E> type,
                                          ElasticsearchHighlightingService<E, K> highlightingService) {
        super(clientService, criteriaBuilder, fieldsService, highlightingService);
        this.messagesService = messagesService;
        this.responseType = type;
    }

    public Results<E> loadResults(ResultsBean resultsBean) throws AppException {

        SortWorkflowField sortField = fieldsService.getSorting().stream()
                .filter(s -> s.getField().equals(resultsBean.getSort())).findFirst()
                .orElseThrow(() -> new AppException(messagesService.getMessage("error.repository.sort",
                        resultsBean.getSort())));

        SortOrder sortOrder = StringUtils.isNotBlank(resultsBean.getOrder())
                ? SortOrder.fromString(resultsBean.getOrder()) : SortOrder.DESC;

        SearchRequestBuilder requestBuilder = client.prepareSearch(index).setTypes(type)
                .addSort(sortField.getPath(), sortOrder)
                .setFrom(resultsBean.getPage() * resultsBean.getSize())
                .setSize(resultsBean.getSize());

        if (StringUtils.isBlank(resultsBean.getSearch()) && resultsBean.getFilters().isEmpty()
                && resultsBean.getLimit().isEmpty()) {
            requestBuilder.setQuery(QueryBuilders.matchAllQuery());
        } else {
            BoolQueryBuilder query = QueryBuilders.boolQuery();

            BoolQueryBuilder searchCriteria = criteriaBuilder
                    .addSearchCriteria(resultsBean.getSearch(), fieldsService.getSearches());
            if (searchCriteria.hasClauses()) {
                query.must(searchCriteria);
            }
            BoolQueryBuilder limitCriteria = criteriaBuilder.addLimitCriteria(resultsBean.getLimit());
            if (limitCriteria.hasClauses()) {
                query.must(limitCriteria);
            }
            BoolQueryBuilder filterCriteria = criteriaBuilder
                    .addFilterCriteria(resultsBean.getFilters(), fieldsService.getFilters());
            if (filterCriteria.hasClauses()) {
                query.filter(filterCriteria);
            }

            requestBuilder.setQuery(query);
            requestBuilder.highlighter(criteriaBuilder.getHighlightBuilder());
        }

        return readResponse(requestBuilder.get(), responseType);
    }
}
