package com.dipegroup.spring.es.models.fields;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class WorkflowFieldsContainer {

    private List<FilterWorkflowField> filters = new ArrayList<>();
    private List<SortWorkflowField> sorting = new ArrayList<>();
    private List<SearchWorkflowField> searches = new ArrayList<>();

    public List<FilterWorkflowField> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterWorkflowField> filters) {
        this.filters = filters;
    }

    public List<SortWorkflowField> getSorting() {
        return sorting;
    }

    public void setSorting(List<SortWorkflowField> sorting) {
        this.sorting = sorting;
    }

    public List<SearchWorkflowField> getSearches() {
        return searches;
    }

    public void setSearches(List<SearchWorkflowField> searches) {
        this.searches = searches;
    }
}
