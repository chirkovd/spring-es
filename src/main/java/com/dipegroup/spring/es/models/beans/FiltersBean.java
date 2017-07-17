package com.dipegroup.spring.es.models.beans;

import com.dipegroup.spring.es.models.beans.entries.LimitBean;

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
public class FiltersBean {

    private String search;
    private LimitBean limit = new LimitBean();
    private List<String> filters = new ArrayList<>();

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

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "FiltersBean{" +
                "search='" + search + '\'' +
                ", limit=" + limit +
                ", filters=" + filters +
                '}';
    }
}
