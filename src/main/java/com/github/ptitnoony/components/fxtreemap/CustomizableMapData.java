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

/**
 *
 * @author ahamon
 * @param <T> Class of the object contained in the adapter
 */
public interface CustomizableMapData<T> extends MapData {

    /**
     * Method to get the function used to calculate the object(s) value.
     *
     * @return the function used to calculated the value of the object(s)
     */
    DataValueFunction<T> getValueFunction();

    /**
     * Method to set the function used to calculate the object(s) value.
     *
     * @param function the function to be used to calculated the value of the
     * object(s)
     */
    void setValueFunction(DataValueFunction<T> function);

    /**
     * Method to get the object represented in the MapData.
     *
     * @return the object represented in the MapData
     */
    T getObject();

    /**
     * Method to get the children object represented in the MapData.
     *
     * @return an unmodifiable list of the children data aggregated
     */
    List<T> getChildrenObjects();

    /**
     * Method to force the update of the MapData value.
     */
    void update();
}
