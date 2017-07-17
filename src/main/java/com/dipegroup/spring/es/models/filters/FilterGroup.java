package com.dipegroup.spring.es.models.filters;

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
public class FilterGroup {

    private long groupsCount;
    private List<FilterGroupItem> groups = new ArrayList<>();

    public long getGroupsCount() {
        return groupsCount;
    }

    public void setGroupsCount(long groupsCount) {
        this.groupsCount = groupsCount;
    }

    public List<FilterGroupItem> getGroups() {
        return groups;
    }

    public void setGroups(List<FilterGroupItem> groups) {
        this.groups = groups;
    }
}
