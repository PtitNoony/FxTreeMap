/*
 * The MIT License
 *
 * Copyright 2017 Arnaud Hamon
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
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahamon
 */
public class SimpleMapData implements MapData {

    private static final Logger LOG = Logger.getGlobal();

    private final PropertyChangeSupport propertyChangeSupport;
    private String name;
    private double value;

    /**
     * Create a new SimpleMapData with the given name and value.
     *
     * @param dataName the data name
     * @param dataValue the data value
     */
    public SimpleMapData(String dataName, double dataValue) {
        if (dataValue < 0.0) {
            throw new IllegalArgumentException("value shall be positive, but was " + dataValue);
        }
        if (dataName == null) {
            throw new IllegalArgumentException("name should not be null");
        }
        propertyChangeSupport = new PropertyChangeSupport(SimpleMapData.this);
        name = dataName;
        value = dataValue;
    }

    /**
     * Create a new SimpleMapData with the given value and an empty name.
     *
     * @param dataValue the data value
     */
    public SimpleMapData(double dataValue) {
        this("", dataValue);
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
        value = newValue;
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
    public List<MapData> getChildrenData() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean hasChildrenData() {
        return false;
    }

    @Override
    public void addChildrenData(MapData data) {
        String toBeAddedName = data != null ? data.getName() : null;
        LOG.log(Level.WARNING, "Ignoring the added child [{0}] in {1}", new Object[]{toBeAddedName, name});
    }

    @Override
    public void removeChildrenData(MapData data) {
        String toBeRemovedName = data != null ? data.getName() : null;
        LOG.log(Level.WARNING, "Ignoring removal of child [{0}] from {1}", new Object[]{toBeRemovedName, name});
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
