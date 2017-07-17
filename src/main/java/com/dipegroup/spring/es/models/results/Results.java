package com.dipegroup.spring.es.models.results;

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
public class Results<E> {

    private List<E> hits = new ArrayList<>();
    private long count;
    private String type;

    public List<E> getHits() {
        return hits;
    }

    public void setHits(List<E> hits) {
        this.hits = hits;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(RepositoryType repositoryType) {
        this.type = repositoryType.getType();
    }

    public void setType(String type) {
        this.type = type;
    }
}
