package com.dipegroup.spring.es.repositories;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.exceptions.services.messages.MessagesService;
import com.dipegroup.spring.es.models.objects.EsModel;
import com.dipegroup.spring.es.services.app.page.AppFieldsService;
import com.dipegroup.spring.es.services.es.ElasticsearchClientService;
import com.dipegroup.spring.es.services.es.ElasticsearchCriteriaBuilder;
import com.dipegroup.spring.es.services.es.ElasticsearchHighlightingService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: spring-es
 * Description: this instance implement logic for quick search functionality: can be used for search items by prefix
 *              in fields that are marked as searchable (provided by {@code fieldsService})
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class ElasticsearchExpandRepository<E extends EsModel<K>, K> extends ElasticsearchResultsRepository<E, K> {

    public ElasticsearchExpandRepository(ElasticsearchClientService clientService,
                                         MessagesService messagesService,
                                         AppFieldsService<E> fieldsService,
                                         ElasticsearchCriteriaBuilder criteriaBuilder, Class<E> type,
                                         ElasticsearchHighlightingService<E, K> highlightingService) {
        super(clientService, messagesService, fieldsService, criteriaBuilder, type, highlightingService);
    }

    public List<E> search(String term) throws AppException {
        if (StringUtils.isEmpty(term)) {
            return new ArrayList<>();
        }
        SearchRequestBuilder requestBuilder = client.prepareSearch(index).setTypes(type).setSize(10);
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        fieldsService.getSearches().stream()
                .filter(item -> !item.isPhrase())
                .forEach(item -> builder.should(QueryBuilders.prefixQuery(item.getField(), term)));
        if (builder.hasClauses()) {
            query.must(builder);
        }
        requestBuilder.setQuery(query);

        return readResponse(requestBuilder.get(), responseType).getHits();
    }
}
