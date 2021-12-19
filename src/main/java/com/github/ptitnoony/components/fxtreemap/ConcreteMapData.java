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

import java.beans.PropertyChangeEvent;
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
 */
public class ConcreteMapData implements MapData {

    private static final Logger LOG = Logger.getGlobal();
    private static final String UNKNOWN_TYPE = "Unknown datatype:: ";

    private final PropertyChangeSupport propertyChangeSupport;
    private final List<MapData> datas;
    private String name;
    private double value;
    private double lastNotifiedValue;
    private final DataType dataType;

    /**
     * Create a new ConcreteMapData with the given name and value.
     *
     * @param dataName the data name
     * @param dataValue the data value
     */
    public ConcreteMapData(String dataName, double dataValue) {
        if (dataValue < 0.0) {
            throw new IllegalArgumentException("value shall be positive, but was " + dataValue);
        }
        if (dataName == null) {
            throw new IllegalArgumentException("name should not be null");
        }
        propertyChangeSupport = new PropertyChangeSupport(ConcreteMapData.this);
        name = dataName;
        value = dataValue;
        datas = Collections.EMPTY_LIST;
        dataType = DataType.LEAF;
    }

    /**
     * Create a new ConcreteMapData with the given value and an empty name.
     *
     * @param dataValue the data value
     */
    public ConcreteMapData(double dataValue) {
        this("", dataValue);
    }

    /**
     * Create a new ConcreteMapData with the default value and an empty name.
     *
     */
    public ConcreteMapData() {
        this("", 0);
    }

    /**
     * Creates a new ConcreteMapData from the given list of MapData.
     *
     * @param dataCollectionName the data name
     * @param dataElements list containing data to be aggregated as children
     */
    public ConcreteMapData(String dataCollectionName, List<MapData> dataElements) {
        propertyChangeSupport = new PropertyChangeSupport(ConcreteMapData.this);
        name = dataCollectionName;
        datas = new LinkedList<>(dataElements);
        datas.forEach(data -> data.addPropertyChangeListener(this::handleChildValueChanged));
        recalculate();
        lastNotifiedValue = value;
        dataType = DataType.NODE;
    }

    /**
     * Creates a new ConcreteMapData from the given data elements.
     *
     * @param dataCollectionName the data name
     * @param dataElements the data elements to be added as children
     */
    public ConcreteMapData(String dataCollectionName, MapData... dataElements) {
        this(dataCollectionName, Arrays.asList(dataElements));
    }

    /**
     * Creates a new ConcreteMapData with TreeMapUtils.DEFAULT_DATA_VALUE.
     *
     * @param dataCollectionName the data name
     * @param type the DataType
     */
    public ConcreteMapData(String dataCollectionName, DataType type) {
        propertyChangeSupport = new PropertyChangeSupport(ConcreteMapData.this);
        dataType = type;
        name = dataCollectionName;
        switch (dataType) {
            case LEAF ->
                datas = Collections.EMPTY_LIST;
            case NODE ->
                datas = new LinkedList<>();
            default ->
                throw new IllegalArgumentException(UNKNOWN_TYPE + type);
        }
        value = TreeMapUtils.DEFAULT_DATA_VALUE;
        lastNotifiedValue = value;
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
        if (newValue < 0.0) {
            LOG.log(Level.WARNING, "Ignoring set of a negative value ({0} for {1}", new Object[]{newValue, name});
            return;
        }
        switch (dataType) {
            case LEAF ->
                value = newValue;
            case NODE -> {
                double percentage;
                if (newValue < TreeMapUtils.EPSILON) {
                    percentage = 0.0;
                } else {
                    percentage = newValue / value;
                }
                value = newValue;
                datas.forEach(data -> data.setValue(data.getValue() * percentage));
            }
            default ->
                throw new IllegalArgumentException(UNKNOWN_TYPE + dataType);
        }
        propertyChangeSupport.firePropertyChange(TreeMapUtils.MAP_DATA_VALUE_CHANGED, null, value);
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
        return !datas.isEmpty();
    }

    @Override
    public void addChildrenData(MapData data) {
        if (dataType == DataType.NODE && data != null) {
            data.addPropertyChangeListener(this::handleChildValueChanged);
            datas.add(data);
            recalculate();
            notifyValueChanged();
        }
    }

    @Override
    public void removeChildrenData(MapData data) {
        if (datas.contains(data)) {
            datas.remove(data);
            recalculate();
        }
    }

    @Override
    public List<MapData> getChildrenData() {
        return Collections.unmodifiableList(datas);
    }

    private void recalculate() {
        value = datas.stream().mapToDouble(MapData::getValue).sum();
    }

    private void handleChildValueChanged(PropertyChangeEvent event) {
        LOG.log(Level.FINE, "updating value after a change in child {0}", event.getSource());
        recalculate();
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
