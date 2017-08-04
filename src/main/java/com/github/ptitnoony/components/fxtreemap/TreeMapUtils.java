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

/**
 *
 * @author ahamon
 */
public class TreeMapUtils {

    public static final int DEFAULT_TIMER_DELAY = 100;
    public static final double DEFAULT_WIDTH = 1200;
    public static final double DEFAULT_HEIGHT = 800;

    public static final double DEFAULT_DATA_VALUE = 0.0;

    public static final String ITEM_CLICKED = "mapItemClicked";

    /**
     * arbitrary randomly small number
     */
    public static final double EPSILON = 0.00000001;

    private TreeMapUtils() {
        // private utility constructor
    }

    public static void quickSortDesc(MapItem[] inputArr, int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
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
    }
}
