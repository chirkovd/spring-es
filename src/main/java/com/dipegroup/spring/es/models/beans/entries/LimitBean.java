package com.dipegroup.spring.es.models.beans.entries;

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
public class LimitBean {

    private List<String> limit = new ArrayList<>();
    private List<String> exclude = new ArrayList<>();

    public List<String> getLimit() {
        return limit;
    }

    public void setLimit(List<String> limit) {
        this.limit = limit;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    public boolean isEmpty() {
        return limit.isEmpty() && exclude.isEmpty();
    }

    @Override
    public String toString() {
        return "LimitBean{" +
                "limit=" + limit +
                ", exclude=" + exclude +
                '}';
    }
}
