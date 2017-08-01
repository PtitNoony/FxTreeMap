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

    public AggredatedData(String dataCollectionName, List<MapData> dataElements) {
        name = dataCollectionName;
        datas = new LinkedList<>(dataElements);
        recalculate();
    }

    public AggredatedData(String dataCollectionName, MapData... dataElements) {
        name = dataCollectionName;
        datas = new LinkedList<>();
        datas.addAll(Arrays.asList(dataElements));
        recalculate();
    }

    public AggredatedData() {
        name = "";
        datas = new LinkedList<>();
        value = 0;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void setValue(double newValue) {
        Map<MapData, Double> percentages = new HashMap<>();
        datas.forEach(data -> percentages.put(data, data.getValue() / value));
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
        datas.add(data);
        recalculate();
    }

    private void recalculate() {
        value = datas.stream().mapToDouble(MapData::getValue).sum();
    }
}
