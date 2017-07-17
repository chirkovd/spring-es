package com.dipegroup.spring.es.services.app;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.spring.es.dao.AppDao;
import com.dipegroup.spring.es.models.beans.FiltersBean;
import com.dipegroup.spring.es.models.beans.ResultsBean;
import com.dipegroup.spring.es.models.filters.FilterGroup;
import com.dipegroup.spring.es.models.objects.DbModel;
import com.dipegroup.spring.es.models.objects.EsModel;
import com.dipegroup.spring.es.models.objects.OriginalModel;
import com.dipegroup.spring.es.models.results.Results;
import com.dipegroup.spring.es.models.results.ResultsPageInfo;
import com.dipegroup.spring.es.services.app.converters.AbstractModelsConverter;
import com.dipegroup.spring.es.services.es.ElasticsearchService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class AbstractAppService<O extends OriginalModel<K>, T extends DbModel<K>, E extends EsModel<K>, K> {

    private final AppDao<T, K> appDao;
    private final AbstractModelsConverter<O, T> converter;
    private final ElasticsearchService<T, E, K> elasticsearchService;

    public AbstractAppService(AppDao<T, K> appDao, AbstractModelsConverter<O, T> converter,
                              ElasticsearchService<T, E, K> elasticsearchService) {
        this.appDao = appDao;
        this.elasticsearchService = elasticsearchService;
        this.converter = converter;
    }

    public ResultsPageInfo getInfo() {
        return elasticsearchService.getInfo();
    }

    public Results<O> loadResults(ResultsBean resultsBean) throws AppException {

        Results<T> appResults = elasticsearchService.loadResults(resultsBean);

        Results<O> results = new Results<>();
        results.setCount(appResults.getCount());
        results.setType(appResults.getType());
        results.setHits(appResults.getHits().stream().map(converter::targetToOriginal).collect(Collectors.toList()));

        return results;
    }

    public Map<String, FilterGroup> loadFilters(FiltersBean filtersBean) throws AppException {
        return elasticsearchService.loadFilters(filtersBean);
    }

    public List<O> searchByTerm(String term) throws AppException {
        return converter.targetToOriginal(elasticsearchService.searchByTerm(term));
    }

    public O addToSearch(O item) throws AppException {
        return converter.execute(item, dbItem -> {
            if (appDao.findByKey(item.getKey()) != null) {
                throw new AppException("error.object.duplicate");
            }
            T storedDbItem = appDao.create(dbItem);
            elasticsearchService.addToSearch(storedDbItem);
            return storedDbItem;
        });
    }

    public void updateInSearch(O item) throws AppException {
        converter.submit(item, dbItem -> {
            if (appDao.exist(item.getId())) {
                throw new AppException("error.object.not.found");
            }
            appDao.update(dbItem);
            elasticsearchService.updateInSearch(dbItem);
        });
    }

    public void deleteFromSearch(K id) throws AppException {
        appDao.remove(id);
        elasticsearchService.deleteFromSearch(id);
    }

    public O create(O original) throws AppException {
        if (appDao.findByKey(original.getKey()) != null) {
            throw new AppException("error.object.duplicate");
        }
        return converter.execute(original, appDao::create);
    }

    public void update(O original) throws AppException {
        T nativeObject = appDao.get(original.getId());
        if (nativeObject != null) {
            converter.submit(original, appDao::update);
        } else {
            throw new AppException("error.object.not.found");
        }
    }

    public O findById(K id) {
        return converter.targetToOriginal(appDao.get(id));
    }

    public void removeById(K id) throws AppException {
        appDao.remove(id);
    }

}
