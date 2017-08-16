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

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ahamon
 * @param <T>
 */
public class AggregatedCustomizableMapData<T> extends AggredatedData implements CustomizableMapData<T> {

    private DataValueFunction<T> valueFunction;

    public AggregatedCustomizableMapData(String name, DataValueFunction<T> valueFunction) {
        super(name);
        this.valueFunction = valueFunction;
    }

    public AggregatedCustomizableMapData(String name) {
        this(name, TreeMapUtils.DEFAULT_DATA_VALUE_FUNCTION);
    }

    @Override
    public void addChildrenData(MapData data) {
        if (data instanceof CustomizableMapData) {
            // cannot test here that instance of CustomizableMapData<T>
            super.addChildrenData(data);
            ((CustomizableMapData) data).setValueFunction(valueFunction);
        }
    }

    @Override
    public DataValueFunction<T> getValueFunction() {
        return valueFunction;
    }

    @Override
    public void setValueFunction(DataValueFunction<T> function) {
        valueFunction = function;
        getChildrenData().stream()
                .filter(child -> child instanceof CustomizableMapData)
                .forEach(child -> ((CustomizableMapData<T>) child).setValueFunction(function));
    }

    @Override
    public T getObject() {
        return null;
    }

    @Override
    public List<T> getChildrenObjects() {
        return getChildrenData().stream()
                .map(child -> ((CustomizableMapData<T>) child).getObject())
                .collect(Collectors.toList());
    }

}
