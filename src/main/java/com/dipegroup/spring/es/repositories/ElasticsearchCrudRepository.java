package com.dipegroup.spring.es.repositories;

import com.dipegroup.exceptions.models.AppErrorImp;
import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.spring.es.models.objects.EsModel;
import com.dipegroup.spring.es.models.results.RepositoryType;
import com.dipegroup.spring.es.models.results.RepositoryTypeImpl;
import com.dipegroup.spring.es.models.results.Results;
import com.dipegroup.spring.es.services.app.page.AppCounterService;
import com.dipegroup.spring.es.services.es.ElasticsearchClientService;
import com.dipegroup.spring.es.services.es.ElasticsearchHighlightingService;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
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
public abstract class ElasticsearchCrudRepository<E extends EsModel<K>, K>
        extends ElasticsearchIndexRepository<E, K> implements AppCounterService<E> {

    protected final ElasticsearchClientService clientService;
    protected final Client client;

    private final ElasticsearchHighlightingService<E, K> highlightingService;

    public ElasticsearchCrudRepository(ElasticsearchClientService clientService,
                                       ElasticsearchHighlightingService<E, K> highlightingService) {
        super(clientService);
        this.clientService = clientService;
        this.client = clientService.client();
        this.highlightingService = highlightingService;
    }

    public void save(E object) throws AppException {
        IndexResponse response = client.prepareIndex(object.getEsIndex(), object.getEsType(), object.getId().toString())
                .setSource(clientService.getSource(object), XContentType.JSON).get();
        if (!response.status().equals(RestStatus.CREATED)) {
            throw new AppException("search.engine.cannot.add.new");
        }
    }

    public void save(List<E> objects) throws AppException {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (E object : objects) {
            bulkRequest.add(new IndexRequest(object.getEsIndex(), object.getEsType(), object.getId().toString())
                    .source(clientService.getSource(object), XContentType.JSON));
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            throw new AppException("search.engine.batch.cannot.add.new");
        }
    }

    public void delete(E object) throws AppException {
        delete(object.getId(), object.getEsIndex(), object.getEsType());
    }

    public void delete(K key) throws AppException {
        delete(key, index, type);
    }

    public void delete(K key, String index, String type) throws AppException {
        DeleteResponse deleteResponse = client.prepareDelete(index, type, key.toString()).get();
        if (!deleteResponse.status().equals(RestStatus.OK)) {
            throw new AppException("search.engine.cannot.delete");
        }
    }

    public void delete(List<E> objects) throws AppException {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (E object : objects) {
            bulkRequest.add(new DeleteRequest(object.getEsIndex(), object.getEsType(), object.getId().toString()));
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            throw new AppException("search.engine.batch.cannot.delete");
        }
    }

    public void delete(List<K> keys, String index, String type) throws AppException {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (K key : keys) {
            bulkRequest.add(new DeleteRequest(index, type, key.toString()));
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            throw new AppException("search.engine.batch.cannot.delete");
        }
    }

    public void update(E object) throws AppException {
        UpdateResponse updateResponse = client
                .prepareUpdate(object.getEsIndex(), object.getEsType(), object.getId().toString())
                .setDoc(clientService.getSource(object), XContentType.JSON).get();
        if (!updateResponse.status().equals(RestStatus.CREATED)) {
            throw new AppException("search.engine.cannot.update");
        }
    }

    public void update(List<E> objects) throws AppException {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (E object : objects) {
            bulkRequest.add(new UpdateRequest(object.getEsIndex(), object.getEsType(), object.getId().toString())
                    .doc(clientService.getSource(object), XContentType.JSON));
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            throw new AppException("search.engine.batch.cannot.update");
        }
    }

    protected Results<E> readResponse(SearchResponse response, Class<E> type) throws AppException {
        Results<E> results = new Results<>();
        List<E> items = new ArrayList<>();
        if (response.status().equals(RestStatus.OK)) {
            results.setCount(response.getHits().getTotalHits());
            for (SearchHit hit : response.getHits()) {
                try {
                    E item = clientService.getObjectMapper().readValue(hit.getSourceAsString(), type);
                    if (highlightingService != null) {
                        hit.getHighlightFields()
                                .forEach((key, value) -> highlightingService.highlight(item, key, value));
                    }
                    items.add(item);
                } catch (IOException e) {
                    throw new AppException(e.getMessage(), AppErrorImp.APPLICATION_ERROR);
                }
            }
        } else {
            throw new AppException("search.engine.no.results");
        }
        results.setHits(items);
        results.setType(getRepositoryType());
        return results;
    }

    @Override
    public long count() {
        return client.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet().getHits().getTotalHits();
    }

    @Override
    public RepositoryType getRepositoryType() {
        return RepositoryTypeImpl.ELASTIC;
    }
}
