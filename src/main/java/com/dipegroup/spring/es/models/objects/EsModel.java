package com.dipegroup.spring.es.models.objects;

/**
 * Project: spring-es
 * Description: this instance is stored as Elasticsearch document
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public interface EsModel<K> {

    String getEsIndex();

    String getEsType();

    K getId();

}
