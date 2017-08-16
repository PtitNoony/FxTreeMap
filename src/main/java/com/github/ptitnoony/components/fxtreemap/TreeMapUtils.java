/*
 * The MIT License
 *
 * Copyright 2017 Arnaud Hamon.
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

import java.util.logging.Level;
import java.util.logging.Logger;
import sun.rmi.runtime.Log;

/**
 *
 * @author ahamon
 */
public class TreeMapUtils {

    private static final Logger LOG = Logger.getGlobal();

    /**
     * Default timer value for delayed layout update in a TreeMap.
     */
    public static final int DEFAULT_TIMER_DELAY = 100;

    /**
     * Default width of a TreeMap component.
     */
    public static final double DEFAULT_WIDTH = 1200;

    /**
     * Default height of a TreeMap component.
     */
    public static final double DEFAULT_HEIGHT = 800;

    /**
     * Default value of a MapData is its value is not specified.
     */
    public static final double DEFAULT_DATA_VALUE = 0.0;

    /**
     * Name of the property change event fired when a map item is clicked on.
     */
    public static final String ITEM_CLICKED = "mapItemClicked";

    /**
     * Name of the property change event fired when a MapData value is changed.
     */
    public static final String MAP_DATA_VALUE_CHANGED = "mapDataValueChanged";
    /**
     * Name of the property change event fired when a data value function is changed.
     */
    public static final String MAP_DATA_VALUE_FUNCTION_CHANGED = "mapDataValueFunctionChanged";

    /**
     * Arbitrary randomly small number.
     */
    public static final double EPSILON = 0.00000001;

    public static final DataNameFunction DEFAULT_DATA_NAME_FUNCTION = o -> {
        LOG.log(Level.INFO, "Invoking default name function on {0}", o);
        return "?";
    };
    public static final DataValueFunction DEFAULT_DATA_VALUE_FUNCTION = o -> {
        LOG.log(Level.INFO, "Invoking default value function on {0}", o);
        return 0;
    };

    private TreeMapUtils() {
        // private utility constructor
    }

    public static boolean quickSortDesc(MapItem[] inputArr, int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        if (inputArr == null || lowerIndex >= higherIndex || lowerIndex < 0 || higherIndex > inputArr.length - 1) {
            return false;
        }
        // calculate pivot number
        double pivot = inputArr[lowerIndex + (higherIndex - lowerIndex) / 2].getSize();
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a
             * number from right side which is less then the pivot value. Once
             * the search is done, then we exchange both numbers.
             */
            while (inputArr[i].getSize() > pivot) {
                i++;
            }
            while (inputArr[j].getSize() < pivot) {
                j--;
            }
            if (i <= j) {
                MapItem temp = inputArr[i];
                inputArr[i] = inputArr[j];
                inputArr[j] = temp;
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j) {
            quickSortDesc(inputArr, lowerIndex, j);
        }
        if (i < higherIndex) {
            quickSortDesc(inputArr, i, higherIndex);
        }
        return true;
    }
}
