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
public class FilterGroupBean {

    private String field;
    private boolean exclude;
    private List<String> terms = new ArrayList<>();

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "FilterGroupBean{" +
                "field='" + field + '\'' +
                ", exclude=" + exclude +
                ", terms=" + terms +
                '}';
    }
}
