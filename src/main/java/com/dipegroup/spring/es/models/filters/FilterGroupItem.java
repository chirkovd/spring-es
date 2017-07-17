package com.dipegroup.spring.es.models.filters;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class FilterGroupItem {

    private String group;
    private Long count;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
