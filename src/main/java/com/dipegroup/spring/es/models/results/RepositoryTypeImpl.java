package com.dipegroup.spring.es.models.results;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public enum RepositoryTypeImpl implements RepositoryType {

    ELASTIC("ES"),
    MONGO_DB("M");

    private String type;

    RepositoryTypeImpl(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
