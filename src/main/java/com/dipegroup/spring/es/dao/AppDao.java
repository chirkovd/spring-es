package com.dipegroup.spring.es.dao;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.spring.es.models.objects.DbModel;

import java.util.Collection;
import java.util.List;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public interface AppDao<N extends DbModel, K> {

    N create(N nativeObject) throws AppException;

    N update(N nativeObject);

    void remove(K id);

    N get(K id);

    List<N> get(Collection<K> ids);

    N findByName(String name);

    boolean exist(K id);

    void clean();

}
