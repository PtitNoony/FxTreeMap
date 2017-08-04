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
 * Implements the Squarified Treemap layout published by Mark Bruls, Kees
 * Huizing, and Jarke J. van Wijk
 *
 * Squarified Treemaps https://www.win.tue.nl/~vanwijk/stm.pdf
 *
 * @author ahamon
 * @author tadas-subonis
 */
public class TreeMapLayout {

    // Do not make it a local variable, introduces a bug
    private int mid = 0;

    public void layout(MapModel model, Rect bounds) {
        layout(model.getItems(), bounds);
    }

    public void layout(List<? extends MapItem> items, Rect bounds) {
        //
        MapItem[] array = new MapItem[items.size()];
        //ugly
        for (int i = 0; i < items.size(); i++) {
            array[i] = items.get(i);
        }
        //
        layout(
                sortDescending(array),
                0,
                items.size() - 1,
                bounds);
    }

    public MapItem[] sortDescending(MapItem[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        MapItem[] inputArr = new MapItem[items.length];
        System.arraycopy(items, 0, inputArr, 0, items.length);

        TreeMapUtils.quickSortDesc(inputArr, 0, inputArr.length - 1);

        return inputArr;
    }

    public void layout(MapItem[] items, int start, int end, Rect bounds) {
        if (start > end) {
            return;
        }
        if (start == end) {
            items[start].setBounds(bounds);
        }

        mid = start;
        while (mid < end) {
            if (highestAspect(items, start, mid, bounds) > highestAspect(items, start, mid + 1, bounds)) {
                mid++;
            } else {
                Rect newBounds = layoutRow(items, start, mid, bounds);
                layout(items, mid + 1, end, newBounds);
            }
        }
    }

    public double highestAspect(MapItem[] items, int start, int end, Rect bounds) {
        layoutRow(items, start, end, bounds);
        double max = Double.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            if (items[i].getBounds().aspectRatio() > max) {
                max = items[i].getBounds().aspectRatio();
            }
        }
        return max;
    }

    public Rect layoutRow(MapItem[] items, int start, int end, Rect bounds) {
        boolean isHorizontal = bounds.getWidth() > bounds.getWidth();
        double total = bounds.getWidth() * bounds.getHeight();
        double rowSize = totalSize(items, start, end);
        double rowRatio = rowSize / total;
        double offset = 0;

        for (int i = start; i <= end; i++) {
            Rect r = new Rect();
            double ratio = items[i].getSize() / rowSize;

            if (isHorizontal) {
                r.setRect(
                        bounds.getX(),
                        bounds.getY() + bounds.getHeight() * offset,
                        bounds.getWidth() * rowRatio,
                        bounds.getHeight() * ratio);
            } else {
                r.setRect(
                        bounds.getX() + bounds.getWidth() * offset,
                        bounds.getY(),
                        bounds.getWidth() * ratio,
                        bounds.getHeight() * rowRatio);
            }
            items[i].setBounds(r);
            offset += ratio;
        }
        if (isHorizontal) {
            return new Rect(bounds.getX() + bounds.getWidth() * rowRatio, bounds.getY(), bounds.getWidth() - bounds.getWidth() * rowRatio, bounds.getHeight());
        } else {
            return new Rect(bounds.getX(), bounds.getY() + bounds.getHeight() * rowRatio, bounds.getWidth(), bounds.getHeight() - bounds.getHeight() * rowRatio);
        }
    }

    public static double totalSize(MapItem[] items) {
        return totalSize(items, 0, items.length - 1);
    }

    public static double totalSize(MapItem[] items, int start, int end) {
        double sum = 0;
        for (int i = start; i <= end; i++) {
            sum += items[i].getSize();
        }
        return sum;
    }

    protected int getMID() {
        return mid;
    }

}
