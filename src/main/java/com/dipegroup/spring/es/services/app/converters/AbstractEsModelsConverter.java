package com.dipegroup.spring.es.services.app.converters;

import com.dipegroup.spring.es.dao.AppDao;
import com.dipegroup.spring.es.models.objects.DbModel;
import com.dipegroup.spring.es.models.objects.EsModel;

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
public abstract class AbstractEsModelsConverter<O extends DbModel<K>, T extends EsModel<K>, K>
        extends AbstractModelsConverter<O, T>  {

    private final AppDao<O, K> appDao;

    public AbstractEsModelsConverter(AppDao<O, K> appDao) {
        super();
        this.appDao = appDao;
    }

    @Override
    public List<O> targetToOriginal(List<T> items) {
        Map<K, O> result = items.stream().collect(Collectors.toMap(EsModel::getId, this::targetToOriginal));
        List<O> stored = appDao.get(result.keySet());
        if (stored != null) {
            stored.forEach(ctx -> merge(ctx, result.get(ctx.getId())));
        }
        return stored;
    }

    protected abstract void merge(O stored, O item);
}
