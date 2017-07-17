package com.dipegroup.spring.es.models.fields;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class FilterWorkflowField extends AbstractWorkflowField {

    private boolean byValue;
    private boolean ascending;

    public FilterWorkflowField() {
    }

    public FilterWorkflowField(String field, String label, String path) {
        super(field, label, path);
    }

    public boolean isByValue() {
        return byValue;
    }

    public void setByValue(boolean byValue) {
        this.byValue = byValue;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
