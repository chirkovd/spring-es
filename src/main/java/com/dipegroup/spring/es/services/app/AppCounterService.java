package com.dipegroup.spring.es.services.app;

import com.dipegroup.spring.es.models.results.RepositoryType;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public interface AppCounterService<E> {

    long count();

    RepositoryType getRepositoryType();

}
