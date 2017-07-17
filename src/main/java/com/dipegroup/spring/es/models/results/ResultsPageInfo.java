package com.dipegroup.spring.es.models.results;

import com.dipegroup.spring.es.models.fields.FilterWorkflowField;
import com.dipegroup.spring.es.models.fields.SortWorkflowField;

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
public class ResultsPageInfo {

    private Long count;
    private String type;
    private List<Integer> sizes = new ArrayList<>();
    private List<SortWorkflowField> sorts = new ArrayList<>();
    private List<FilterWorkflowField> filters = new ArrayList<>();

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(RepositoryType repositoryType) {
        this.type = repositoryType.getType();
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(List<Integer> sizes) {
        this.sizes = sizes;
    }

    public List<SortWorkflowField> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortWorkflowField> sorts) {
        this.sorts = sorts;
    }

    public List<FilterWorkflowField> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterWorkflowField> filters) {
        this.filters = filters;
    }
}
