package com.dipegroup.spring.es.services.es;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.spring.es.models.beans.FiltersBean;
import com.dipegroup.spring.es.models.beans.ResultsBean;
import com.dipegroup.spring.es.models.filters.FilterGroup;
import com.dipegroup.spring.es.models.objects.DbModel;
import com.dipegroup.spring.es.models.objects.EsModel;
import com.dipegroup.spring.es.models.results.RepositoryTypeImpl;
import com.dipegroup.spring.es.models.results.Results;
import com.dipegroup.spring.es.models.results.ResultsPageInfo;
import com.dipegroup.spring.es.repositories.ElasticsearchExpandRepository;
import com.dipegroup.spring.es.services.app.converters.AbstractEsModelsConverter;
import com.dipegroup.spring.es.services.app.page.AbstractPageInfoService;

import java.util.List;
import java.util.Map;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public class ElasticsearchService<O extends DbModel<K>, E extends EsModel<K>, K> {

    private final AbstractPageInfoService<E> pageInfoService;
    private final ElasticsearchExpandRepository<E, K> repository;
    private final AbstractEsModelsConverter<O, E, K> converter;

    public ElasticsearchService(AbstractPageInfoService<E> pageInfoService,
                                ElasticsearchExpandRepository<E, K> repository,
                                AbstractEsModelsConverter<O, E, K> converter) {
        this.pageInfoService = pageInfoService;
        this.repository = repository;
        this.converter = converter;
    }

    public ResultsPageInfo getInfo() {
        return pageInfoService.getInfo();
    }

    public Results<O> loadResults(ResultsBean resultsBean) throws AppException {
        Results<E> esResults = repository.loadResults(resultsBean);

        Results<O> results = new Results<>();
        results.setCount(esResults.getCount());
        results.setType(esResults.getType());
        results.setType(RepositoryTypeImpl.ELASTIC);
        results.setHits(converter.targetToOriginal(esResults.getHits()));

        return results;
    }

    public Map<String, FilterGroup> loadFilters(FiltersBean filtersBean) throws AppException {
        return repository.loadFilters(filtersBean);
    }

    public List<O> searchByTerm(String term) throws AppException {
        return converter.targetToOriginal(repository.search(term));
    }

    public void addToSearch(O item) throws AppException {
        converter.submit(item, repository::save);
    }

    public void updateInSearch(O item) throws AppException {
        converter.submit(item, repository::update);
    }

    public void deleteFromSearch(K id) throws AppException {
        repository.delete(id);
    }
}
