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

/**
 *
 * @author ahamon
 */
public interface MapItem {

    /**
     * Get the MapItem data.
     *
     * @return the data the MapItem represents
     */
    MapData getData();

    /**
     * Get the MapItem' data value.
     *
     * @return the MapItem data value
     */
    double getValue();

    /**
     * Get the corresponding data percentage.
     *
     * @return the percentage of the represented data in its parent
     */
    double getPercentage();

    /**
     * Get the pixel area of the item.
     *
     * @return the pixel area of the item
     */
    double getSize();

    /**
     * Get the MapItem bounds.
     *
     * @return the MapItem bounds in its parent
     */
    Rect getBounds();

    /**
     * Update the MapItem bounds in its parent.
     *
     * @param bounds the bounds to be applied to the MapItem
     */
    void setBounds(Rect bounds);

    /**
     * Update the MapItem bounds in its parent.
     *
     * @param x new coordinate along the X axis
     * @param y new coordinate along the Y axis
     * @param w new width
     * @param h new height
     */
    void setBounds(double x, double y, double w, double h);

}
