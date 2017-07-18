package com.dipegroup.spring.es.dao;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.spring.es.models.objects.DbModel;

import java.util.Collection;
import java.util.List;

/**
 * Project: spring-es
 * Description: common DAO contract for {@code DbModel}, that describes basic CRUD operations and some specific,
 *              that are required for application logic.
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public interface AppDao<N extends DbModel<K>, K> {

    N create(N dbModel) throws AppException;

    N update(N dbModel);

    void remove(K id);

    N get(K id);

    List<N> get(Collection<K> ids);

    N findByKey(String key);

    boolean exist(K id);

    void clean();

}
