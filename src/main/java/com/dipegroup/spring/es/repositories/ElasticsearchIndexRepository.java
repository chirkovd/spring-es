package com.dipegroup.spring.es.repositories;

import com.dipegroup.spring.es.models.objects.EsModel;
import com.dipegroup.spring.es.services.es.ElasticClientService;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class ElasticsearchIndexRepository<E extends EsModel<K>, K> {

    private final IndicesAdminClient adminClient;

    private String mapping;
    private String setting;

    protected String index;
    protected String type;

    public ElasticsearchIndexRepository(ElasticClientService clientService) {
        this.adminClient = clientService.client().admin().indices();
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public void setMapping(InputStream mappingSource) throws IOException {
        setMapping(IOUtils.toString(mappingSource, StandardCharsets.UTF_8.name()));
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public void setSetting(InputStream settingsSource) throws IOException {
        setSetting(IOUtils.toString(settingsSource, StandardCharsets.UTF_8.name()));
    }

    protected void setIndex(String index) {
        this.index = index;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public void create() {
        adminClient.prepareCreate(index)
                .addMapping(type, mapping, XContentType.JSON)
                .setSettings(setting, XContentType.JSON).get();
    }

    public void delete() {
        adminClient.prepareDelete(index).get();
    }

    public void update() {
        adminClient.prepareRefresh(index).get();
    }

    public boolean exists(){
        return adminClient.prepareExists(index).get().isExists();
    }

}
