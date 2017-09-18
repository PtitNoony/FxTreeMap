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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author ahamon
 */
public class AggredatedData implements MapData {

    private static final Logger LOG = Logger.getGlobal();

    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup lookup = new AbstractLookup(lookupContents);
    private final List<MapData> datas;
    private String name;
    private double value;
    private double lastNotifiedValue;

    /**
     * Creates an aggregated data from the given list of MapData.
     *
     * @param dataCollectionName the data name
     * @param dataElements list containing data to be aggregated as children
     */
    public AggredatedData(String dataCollectionName, List<MapData> dataElements) {
        propertyChangeSupport = new PropertyChangeSupport(AggredatedData.this);
        name = dataCollectionName;
        datas = new LinkedList<>(dataElements);
        datas.forEach(data -> data.addPropertyChangeListener(this::handleChildValueChanged));
        recalculate();
        lastNotifiedValue = value;
    }

    /**
     * Creates an aggregated data from the given data elements.
     *
     * @param dataCollectionName the data name
     * @param dataElements the data elements to be added as children
     */
    public AggredatedData(String dataCollectionName, MapData... dataElements) {
        this(dataCollectionName, Arrays.asList(dataElements));
    }

    /**
     * Creates a new AggredatedData with TreeMapUtils.DEFAULT_DATA_VALUE.
     *
     * @param dataCollectionName the data name
     */
    public AggredatedData(String dataCollectionName) {
        propertyChangeSupport = new PropertyChangeSupport(AggredatedData.this);
        name = dataCollectionName;
        datas = new LinkedList<>();
        value = TreeMapUtils.DEFAULT_DATA_VALUE;
        lastNotifiedValue = value;
    }

    /**
     * Creates a new AggredatedData with TreeMapUtils.DEFAULT_DATA_VALUE.
     */
    public AggredatedData() {
        this("");
    }

    protected final void addToLookup(Object o) {
        lookupContents.add(o);
    }

    @Override
    public AbstractLookup getLookup() {
        return lookup;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void setValue(double newValue) {
        if (newValue < 0.0) {
            return;
        }
        double percentage;
        if (newValue < TreeMapUtils.EPSILON) {
            percentage = 0.0;
        } else {
            percentage = newValue / value;
        }
        //
        value = newValue;
        datas.forEach(data -> data.setValue(data.getValue() * percentage));
        //
        // not necessary since children already send notifications -> to be optimized at component level
        notifyValueChanged();
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
        return Collections.unmodifiableList(datas);
    }

    @Override
    public boolean hasChildrenData() {
        return !datas.isEmpty();
    }

    @Override
    public void addChildrenData(MapData data) {
        if (data != null) {
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
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
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
