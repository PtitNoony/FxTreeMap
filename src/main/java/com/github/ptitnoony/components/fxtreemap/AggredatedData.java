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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ahamon
 */
public class AggredatedData implements MapData {

    private final List<MapData> datas;
    private String name;
    private double value;

    /**
     * Creates an aggregated data from the given list of MapData.
     *
     * @param dataCollectionName the data name
     * @param dataElements list containing data to be aggregated as children
     */
    public AggredatedData(String dataCollectionName, List<MapData> dataElements) {
        name = dataCollectionName;
        datas = new LinkedList<>(dataElements);
        recalculate();
    }

    /**
     * Creates an aggregated data from the given data elements.
     *
     * @param dataCollectionName the data name
     * @param dataElements the data elements to be added as children
     */
    public AggredatedData(String dataCollectionName, MapData... dataElements) {
        name = dataCollectionName;
        datas = new LinkedList<>();
        datas.addAll(Arrays.asList(dataElements));
        recalculate();
    }

    /**
     * Creates a new AggredatedData with TreeMapUtils.DEFAULT_DATA_VALUE.
     *
     * @param dataCollectionName the data name
     */
    public AggredatedData(String dataCollectionName) {
        name = dataCollectionName;
        datas = new LinkedList<>();
        value = TreeMapUtils.DEFAULT_DATA_VALUE;
    }

    /**
     * Creates a new AggredatedData with TreeMapUtils.DEFAULT_DATA_VALUE.
     */
    public AggredatedData() {
        this("");
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
        Map<MapData, Double> percentages = new HashMap<>();
        if (Math.abs(value) < TreeMapUtils.EPSILON) {
            datas.forEach(data -> percentages.put(data, 0.0));
        } else {
            datas.forEach(data -> percentages.put(data, data.getValue() / value));
        }
        value = newValue;
        percentages.forEach((data, percentage) -> data.setValue(value * percentage));
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
            datas.add(data);
            recalculate();
        }
    }

    @Override
    public void removeChildrenData(MapData data) {
        if (datas.contains(data)) {
            datas.remove(data);
            recalculate();
        }
    }

    private void recalculate() {
        value = datas.stream().mapToDouble(MapData::getValue).sum();
    }
}
