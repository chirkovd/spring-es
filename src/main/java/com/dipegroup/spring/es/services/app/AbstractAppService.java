package com.dipegroup.spring.es.services.app;

import com.dipegroup.exceptions.models.AppException;
import com.dipegroup.spring.es.dao.AppDao;
import com.dipegroup.spring.es.models.objects.DbModel;
import com.dipegroup.spring.es.models.objects.OriginalModel;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class AbstractAppService<O extends OriginalModel<K>, T extends DbModel<K>, K> {

    private final AppDao<T, K> appDao;
    private final AbstractModelsConverter<O, T> converter;

    public AbstractAppService(AppDao<T, K> appDao, AbstractModelsConverter<O, T> converter) {
        this.appDao = appDao;
        this.converter = converter;
    }

    public O create(O coreObject) throws AppException {
        if (appDao.findByName(coreObject.getName()) != null) {
            throw new AppException("error.object.duplicate");
        }
        return converter.execute(coreObject, appDao::create);
    }

    public void update(O coreObject) throws AppException {
        T nativeObject = appDao.get(coreObject.getId());
        if (nativeObject != null) {
            converter.submit(coreObject, appDao::update);
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
