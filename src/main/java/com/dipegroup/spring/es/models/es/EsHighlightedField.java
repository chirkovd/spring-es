package com.dipegroup.spring.es.models.es;

import com.dipegroup.spring.es.models.objects.EsModel;

import java.util.function.BiConsumer;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public interface EsHighlightedField<E extends EsModel<K>, K> {

    String getName();

    BiConsumer<E, String> getConsumer();
}
