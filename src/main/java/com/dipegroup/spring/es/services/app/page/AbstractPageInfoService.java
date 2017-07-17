package com.dipegroup.spring.es.services.app.page;

import com.dipegroup.spring.es.models.results.ResultsPageInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Project: spring-es
 * Description: this service provides common information about sorting, page sizes, filters -
 *              that can be used for current {@code E} object
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class AbstractPageInfoService<E> {

    private final AppCounterService<E> counterService;
    private final AppFieldsService<E> filtersService;

    protected AbstractPageInfoService(AppCounterService<E> counterService, AppFieldsService<E> filtersService) {
        this.counterService = counterService;
        this.filtersService = filtersService;
    }

    public ResultsPageInfo getInfo() {
        ResultsPageInfo info = new ResultsPageInfo();
        info.setCount(counterService.count());
        info.setType(counterService.getRepositoryType());
        info.setFilters(filtersService.getFilters());
        info.setSizes(getSizes());
        info.setSorts(filtersService.getSorting());
        return info;
    }

    protected List<Integer> getSizes() {
        return Arrays.asList(5, 10, 25, 50);
    }

}
