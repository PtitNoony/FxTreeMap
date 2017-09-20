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
import java.util.List;

/**
 *
 * @author ahamon
 */
public interface MapData {

    /**
     * Data type that represents whether a data is represents a single object
     * (ie LEAF) or an aggregation of objects (ie NODE)
     */
    enum DataType {
        LEAF, NODE
    };

    DataType getType();

    /**
     * Get the data value.
     *
     * @return the data value
     */
    double getValue();

    /**
     * Set the data value. If the data has children data, their value will be
     * set with the same percentage of the value they use to have before the
     * setValue is applied. The value must be equal or greater to 0.
     *
     * @param newValue the new data value
     */
    void setValue(double newValue);

    /**
     * Get the data name.
     *
     * @return the data name
     */
    String getName();

    /**
     * Set the data name.
     *
     * @param newName the new data name
     */
    void setName(String newName);

    /**
     * If the data is an aggregation of children data.
     *
     * @return if the data is an aggregation of children data
     */
    boolean hasChildrenData();

    /**
     * Get the children aggregated data if any.
     *
     * @return the list of aggregated data
     */
    List<MapData> getChildrenData();

    /**
     * Add a child data. If the data had no child before, adding a child data
     * will override the previously set data value.
     *
     * @param data the data to be added as a child data to aggregate
     */
    void addChildrenData(MapData data);

    /**
     * Remove a child data.
     *
     * @param data the data to be removed
     */
    void removeChildrenData(MapData data);

    /**
     * Add a property change listener.
     *
     * @param listener the listener to be added
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a property change listener.
     *
     * @param listener the listener to be removed
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
