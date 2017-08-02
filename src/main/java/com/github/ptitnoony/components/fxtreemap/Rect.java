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
 * A simple class implementing a geometric rectangle
 *
 * @author tadas-subonis
 */
public class Rect {

    public static final double DEFAULT_WIDTH = 1.0;
    public static final double DEFAULT_HEIGHT = 1.0;

    public double x, y, w, h;

    /**
     * Creates a new Rect at (0,0) with default width and height.
     */
    public Rect() {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Creates a new Rect using the same attributes values as the Rect given as
     * parameter.
     *
     * @param r the Rect to be used for the attributes
     */
    public Rect(Rect r) {
        setRect(r.x, r.y, r.w, r.h);
    }

    /**
     * Creates a new Rect with the given parameters.
     *
     * @param x coordinates along the X axis
     * @param y coordinates along the Y axis
     * @param w width
     * @param h height
     */
    public Rect(double x, double y, double w, double h) {
        setRect(x, y, w, h);
    }

    /**
     * Calculates the Rect aspect ratio.
     *
     * @return the Rect aspect ratio
     */
    public double aspectRatio() {
        return Math.max(w / h, h / w);
    }

    /**
     * Updates the Rect with the given parameters.
     *
     * @param x coordinates along the X axis
     * @param y coordinates along the Y axis
     * @param w width
     * @param h height
     */
    public final void setRect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
