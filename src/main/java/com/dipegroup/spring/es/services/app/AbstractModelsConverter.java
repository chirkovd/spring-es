package com.dipegroup.spring.es.services.app;

import com.dipegroup.exceptions.models.AppException;

/**
 * Project: spring-es
 * Description:
 * Date: 7/17/2017
 *
 * @author Dmitriy_Chirkov
 * @since 1.8
 */
public abstract class AbstractModelsConverter<O, T> {

    public O execute(O original, ConvertedAction<T> action) throws AppException {
        if (original == null) {
            return null;
        }
        T target = originalToTarget(original);
        if (action != null) {
            target = action.action(target);
        }
        return targetToOriginal(target);
    }

    public void submit(O original, ConvertedActionWithoutResult<T> action) throws AppException {
        if (original == null) {
            return;
        }
        if (action != null) {
            T target = originalToTarget(original);
            action.action(target);
        }
    }

    protected abstract O targetToOriginal(T target);

    protected abstract T originalToTarget(O original);

    public interface ConvertedAction<N> {
        N action(N object) throws AppException;
    }

    public interface ConvertedActionWithoutResult<N> {
        void action(N object) throws AppException;
    }
}
