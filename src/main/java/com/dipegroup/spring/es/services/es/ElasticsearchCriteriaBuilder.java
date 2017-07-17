package com.dipegroup.spring.es.services.es;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.exceptions.services.messages.MessagesService;
import com.dipegroup.spring.es.models.beans.entries.FilterGroupBean;
import com.dipegroup.spring.es.models.beans.entries.LimitBean;
import com.dipegroup.spring.es.models.fields.FilterWorkflowField;
import com.dipegroup.spring.es.models.fields.SearchWorkflowField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.util.List;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class ElasticsearchCriteriaBuilder {

    private final HighlightBuilder highlightBuilder;
    private final MessagesService messagesService;

    public ElasticsearchCriteriaBuilder(HighlightBuilder highlightBuilder, MessagesService messagesService) {
        this.highlightBuilder = highlightBuilder;
        this.messagesService = messagesService;
    }

    public HighlightBuilder getHighlightBuilder() {
        return highlightBuilder;
    }

    public BoolQueryBuilder addSearchCriteria(String search, List<SearchWorkflowField> searches) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(search) && CollectionUtils.isNotEmpty(searches)) {
            searches.forEach(item -> {
                if (item.isPhrase()) {
                    builder.should(QueryBuilders.matchQuery(item.getPath(), search));
                } else {
                    builder.should(QueryBuilders.prefixQuery(item.getPath(), search));
                }
                if (highlightBuilder != null) {
                    highlightBuilder.field(item.getField(), 0, 0);
                }
            });
        }
        return builder;
    }

    public BoolQueryBuilder addLimitCriteria(LimitBean limitBean) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (!limitBean.getLimit().isEmpty()) {
            TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("id", limitBean.getLimit());
            builder.must(termsQuery);
        } else if (!limitBean.getExclude().isEmpty()) {
            TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("id", limitBean.getExclude());
            builder.mustNot(termsQuery);
        }
        return builder;
    }

    public BoolQueryBuilder addFilterCriteria(List<FilterGroupBean> filters, List<FilterWorkflowField> workflowFields)
            throws AppException {

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (!filters.isEmpty()) {
            for (FilterGroupBean groupBean : filters) {
                FilterWorkflowField filterItem = workflowFields.stream()
                        .filter(f -> f.getField().equals(groupBean.getField())).findFirst().orElse(null);
                if (filterItem != null) {
                    TermsQueryBuilder termsQuery = QueryBuilders
                            .termsQuery(filterItem.getField(), groupBean.getTerms());
                    if (!groupBean.isExclude() && highlightBuilder != null) {
                        highlightBuilder.field(filterItem.getField(), 0, 1);
                    }
                    if (groupBean.isExclude()) {
                        builder.mustNot(termsQuery);
                    } else {
                        builder.must(termsQuery);
                    }
                } else {
                    throw new AppException(messagesService.getMessage("error.repository.filter", groupBean.getField()));
                }
            }
        }
        return builder;
    }

}
