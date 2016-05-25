package com.ngeen.engine;

import com.ngeen.component.ComponentBase;
import com.ngeen.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @param <T>
 * @author Dragos
 * @composed n has * TypeObserver
 */
public class TypeObservable<T> {
    protected List<TypeObserver> _Observers = new ArrayList<TypeObserver>();

    public void addObserver(TypeObserver... entityObservers) {
        _Observers.addAll(Arrays.asList(entityObservers));
    }

    protected void NotifyAdd(ComponentBase obj) {
        for (TypeObserver observer : _Observers) {
            observer.Added(obj);
        }
    }

    protected void NotifyChange(ComponentBase obj) {
        for (TypeObserver observer : _Observers) {
            observer.ChangedComponent(obj);
        }
    }

    protected void NotifyParented(Entity obj, Entity parent) {
        for (TypeObserver observer : _Observers) {
            observer.Parented(obj, parent);
        }
    }

    protected void NotifyRemove(ComponentBase obj) {
        for (TypeObserver observer : _Observers) {
            observer.Removed(obj);
        }
    }

    protected void NotifyReorder(Entity obj1, Entity obj2) {
        for (TypeObserver observer : _Observers) {
            observer.Reorder(obj1, obj2);
        }
    }
}
