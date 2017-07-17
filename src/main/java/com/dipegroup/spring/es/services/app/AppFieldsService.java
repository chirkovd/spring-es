package com.dipegroup.spring.es.services.app;

import com.dipegroup.spring.es.models.fields.FilterWorkflowField;
import com.dipegroup.spring.es.models.fields.SearchWorkflowField;
import com.dipegroup.spring.es.models.fields.SortWorkflowField;

import java.util.List;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public interface AppFieldsService<E> {

    List<FilterWorkflowField> getFilters();

    List<SortWorkflowField> getSorting();

    List<SearchWorkflowField> getSearches();

}
