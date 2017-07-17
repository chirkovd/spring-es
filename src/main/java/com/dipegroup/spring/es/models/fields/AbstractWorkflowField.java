package com.dipegroup.spring.es.models.fields;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class AbstractWorkflowField {

    private String field;
    private String label;
    private String path;

    public AbstractWorkflowField() {
    }

    public AbstractWorkflowField(String field, String label, String path) {
        this.field = field;
        this.label = label;
        this.path = path;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "AbstractWorkflowField{" +
                "field='" + field + '\'' +
                ", label='" + label + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
