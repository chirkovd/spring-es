package com.dipegroup.spring.es.services.es;

import com.dipegroup.exceptions.models.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequest;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.client.Client;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class ElasticsearchClientService {

    private final Client client;
    private final ObjectMapper objectMapper;

    private boolean disabled;

    public ElasticsearchClientService(Client client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public void verifyConnection() {
        try {
            NodesStatsResponse response = client.admin().cluster().nodesStats(new NodesStatsRequest()).actionGet();
            this.disabled = response.getNodes().size() == 0;
        } catch (ElasticsearchException e) {
            this.disabled = true;
        }
    }

    public boolean isDisabled() {
        return disabled;
    }

    public Client client() {
        return client;
    }

    public Client getClient() throws AppException {
        if (isDisabled()) {
            throw new AppException("Search engine is not available");
        } else {
            return client;
        }
    }

    public String getSource(Object object) throws AppException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new AppException(e.getMessage());
        }
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
