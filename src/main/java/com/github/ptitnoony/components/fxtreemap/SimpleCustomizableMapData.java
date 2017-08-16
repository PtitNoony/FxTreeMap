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

import java.util.Collections;
import java.util.List;

/**
 *
 * @author ahamon
 * @param <T>
 */
public class SimpleCustomizableMapData<T> extends SimpleMapData implements CustomizableMapData<T> {

    private final T object;
    // this function is final for the moment, but may be settable in future release
    private final DataNameFunction<T> nameFunction;
    // To avoid null pointer when creating sets of data
    private DataValueFunction<T> valueFunction = TreeMapUtils.DEFAULT_DATA_VALUE_FUNCTION;

    public SimpleCustomizableMapData(T object, DataNameFunction<T> nameFunction, DataValueFunction<T> valueFunction) {
        super(nameFunction.getName(object), valueFunction.getValue(object));
        this.object = object;
        this.nameFunction = nameFunction;
        this.valueFunction = valueFunction;
    }

    @Override
    public double getValue() {
        if (valueFunction == null) {
            return 0;
        }
        return valueFunction.getValue(object);
    }

    @Override
    public void setValue(double newValue) {
        // ignoring the value
    }

    @Override
    public String getName() {
        return nameFunction.getName(object);
    }

    @Override
    public DataValueFunction<T> getValueFunction() {
        return valueFunction;
    }

    @Override
    public void setValueFunction(DataValueFunction<T> function) {
        valueFunction = function;
        getPropertyChangeSupport().firePropertyChange(TreeMapUtils.MAP_DATA_VALUE_FUNCTION_CHANGED, null, valueFunction);
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public List<T> getChildrenObjects() {
        return Collections.EMPTY_LIST;
    }

}
