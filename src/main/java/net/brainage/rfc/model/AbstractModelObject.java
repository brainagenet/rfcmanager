/*
 * net.brainage.rfc.model.AbstractModelObject.java
 * Created on 2011. 6. 19.
 */
package net.brainage.rfc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public abstract class AbstractModelObject
{

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     * @param listener
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param listener
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * @param propertyName
     * @param index
     * @param oldValue
     * @param newValue
     */
    public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue,
            Object newValue) {
        pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * @param propertyName
     * @param index
     * @param oldValue
     * @param newValue
     */
    public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
        pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * @param propertyName
     * @param index
     * @param oldValue
     * @param newValue
     */
    public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue,
            boolean newValue) {
        pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

}
