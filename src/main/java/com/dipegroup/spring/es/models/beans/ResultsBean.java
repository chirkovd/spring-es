package com.dipegroup.spring.es.models.beans;

import com.dipegroup.spring.es.models.beans.entries.FilterGroupBean;
import com.dipegroup.spring.es.models.beans.entries.LimitBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class ResultsBean {

    private int page;
    private int size;

    private String sort;
    private String order;

    private String search;
    private LimitBean limit = new LimitBean();
    private List<FilterGroupBean> filters = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public LimitBean getLimit() {
        return limit;
    }

    public void setLimit(LimitBean limit) {
        this.limit = limit;
    }

    public List<FilterGroupBean> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterGroupBean> filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "ResultsBean{" +
                "page=" + page +
                ", size=" + size +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                ", search='" + search + '\'' +
                ", limit=" + limit +
                ", filters=" + filters.stream().map(FilterGroupBean::toString).collect(Collectors.joining("; ")) +
                '}';
    }
}
