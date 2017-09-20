/*
 * The MIT License
 *
 * Copyright 2017 H-K.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.ptitnoony.components.fxtreemap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahamon
 * @param <T> Class of the object contained in the adapter
 */
public class MapDataAdapter<T> implements CustomizableMapData<T> {

    private static final Logger LOG = Logger.getGlobal();
    private static final String UNKNOWN_TYPE = "Unknown datatype:: ";

    private final T dataObject;
    private final List<T> dataObjectList;
    private final List<MapDataAdapter<T>> dataAdapters;
    private DataValueFunction<T> dataValueFunction;
    private DataNameFunction<T> dataNameFunction;

    private final PropertyChangeSupport propertyChangeSupport;
    private String name;
    private double value;
    private double lastNotifiedValue;
    private final DataType dataType;

    /**
     * Creates a new MapDataAdapter for the given object.
     *
     * @param object the object to be used as data
     * @param valueFunction the function used to calculated the object's value
     * @param nameFunction the function used to get the object's name
     */
    public MapDataAdapter(T object, DataValueFunction<T> valueFunction, DataNameFunction<T> nameFunction) {
        if (object == null) {
            throw new IllegalArgumentException("object should not be null");
        }
        if (valueFunction == null) {
            throw new IllegalArgumentException("valueFunction should not be null");
        }
        if (nameFunction == null) {
            throw new IllegalArgumentException("nameFunction should not be null");
        }
        //
        propertyChangeSupport = new PropertyChangeSupport(MapDataAdapter.this);
        dataAdapters = Collections.EMPTY_LIST;
        dataObjectList = Collections.EMPTY_LIST;
        dataType = DataType.LEAF;
        dataObject = object;
        dataValueFunction = valueFunction;
        dataNameFunction = nameFunction;
        //
        name = dataNameFunction.getName(dataObject);
        value = dataValueFunction.getValue(dataObject);
    }

    /**
     * Creates a new MapDataAdapter from the given list of objects.
     *
     * @param dataCollectionName the name given to the objects group
     * @param dataObjects list containing data to be aggregated as children
     * @param valueFunction the function used to calculated the objects value
     * @param nameFunction the function used to get the objects name
     */
    public MapDataAdapter(String dataCollectionName, List<T> dataObjects, DataValueFunction<T> valueFunction, DataNameFunction<T> nameFunction) {
        if (dataObjects == null) {
            throw new IllegalArgumentException("dataObjects should not be null");
        }
        if (valueFunction == null) {
            throw new IllegalArgumentException("valueFunction should not be null");
        }
        propertyChangeSupport = new PropertyChangeSupport(MapDataAdapter.this);
        dataType = DataType.NODE;
        name = dataCollectionName;
        dataValueFunction = valueFunction;
        dataNameFunction = nameFunction;
        dataObject = null;
        dataAdapters = new LinkedList<>();
        dataObjectList = new LinkedList<>(dataObjects);
        dataObjectList.forEach(o -> dataAdapters.add(new MapDataAdapter<>(o, valueFunction, nameFunction)));
        recalculate();
        lastNotifiedValue = value;
    }

    /**
     * Creates a new MapDataAdapter from the given data elements.
     *
     * @param dataCollectionName the data name
     * @param valueFunction the function used to calculated the objects value
     * @param nameFunction the function used to get the objects name
     * @param dataElements the data elements to be added as children
     */
    public MapDataAdapter(String dataCollectionName, DataValueFunction<T> valueFunction, DataNameFunction<T> nameFunction, T... dataElements) {
        this(dataCollectionName, Arrays.asList(dataElements), valueFunction, nameFunction);
    }

    @Override
    public DataType getType() {
        return dataType;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void setValue(double newValue) {
        LOG.log(Level.WARNING, "Setting value in a MapAdapter is not allowed ({0})", name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String newName) {
        name = newName;
    }

    @Override
    public boolean hasChildrenData() {
        return !dataAdapters.isEmpty();
    }

    @Override
    public void addChildrenData(MapData data) {
        LOG.log(Level.WARNING, "Adding children in a MapAdapter is not allowed ({0})", name);
    }

    @Override
    public void removeChildrenData(MapData data) {
        LOG.log(Level.WARNING, "Removing children in a MapAdapter is not allowed ({0})", name);
    }

    @Override
    public List<MapData> getChildrenData() {
        return Collections.unmodifiableList(dataAdapters);
    }

    @Override
    public DataValueFunction<T> getValueFunction() {
        return dataValueFunction;
    }

    @Override
    public void setValueFunction(DataValueFunction<T> function) {
        dataValueFunction = function;
        dataAdapters.forEach(o -> o.setValueFunction(dataValueFunction));
        recalculate();
    }

    @Override
    public T getObject() {
        return dataObject;
    }

    @Override
    public List<T> getChildrenObjects() {
        return Collections.unmodifiableList(dataObjectList);
    }

    @Override
    public void update() {
        recalculate();
        notifyValueChanged();
    }

    private void recalculate() {
        switch (dataType) {
            case LEAF:
                value = dataValueFunction.getValue(dataObject);
                break;
            case NODE:
                dataAdapters.forEach(MapDataAdapter::recalculate);
                value = dataAdapters.stream().mapToDouble(MapDataAdapter::getValue).sum();
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_TYPE + dataType);
        }
        notifyValueChanged();
    }

    private void notifyValueChanged() {
        // to prevent overflowing upper levels with unnecessary notifications
        if (Math.abs(value - lastNotifiedValue) > TreeMapUtils.EPSILON) {
            propertyChangeSupport.firePropertyChange(TreeMapUtils.MAP_DATA_VALUE_CHANGED, null, value);
            lastNotifiedValue = value;
        }
    }

}
