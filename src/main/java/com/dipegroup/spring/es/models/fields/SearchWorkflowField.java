package com.dipegroup.spring.es.models.fields;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class SearchWorkflowField extends AbstractWorkflowField {

    private boolean phrase;

    public SearchWorkflowField() {
    }

    public SearchWorkflowField(String field, String label, String path) {
        super(field, label, path);
    }

    public boolean isPhrase() {
        return phrase;
    }

    public void setPhrase(boolean phrase) {
        this.phrase = phrase;
    }
}
