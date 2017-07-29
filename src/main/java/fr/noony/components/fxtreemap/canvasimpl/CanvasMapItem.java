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
package fr.noony.components.fxtreemap.canvasimpl;

import fr.noony.components.fxtreemap.MapData;
import fr.noony.components.fxtreemap.MapItem;
import fr.noony.components.fxtreemap.Rect;

/**
 *
 * @author ahamon
 */
public class CanvasMapItem implements MapItem {

    private final CanvasMapModel mapModel;
    private final MapData itemData;
    private final Rect rect;
    private final double itemPercentage;

    public CanvasMapItem(CanvasMapModel model, MapData data, double percentage) {
        mapModel = model;
        itemData = data;
        itemPercentage = percentage;
        rect = new Rect();
    }

    @Override
    public MapData getData() {
        return itemData;
    }

    @Override
    public double getPercentage() {
        return itemPercentage;
    }

    @Override
    public double getValue() {
        return itemData.getValue();
    }

    @Override
    public double getSize() {
        return itemPercentage * mapModel.getTotalArea();
    }

    @Override
    public void setBounds(Rect bounds) {
        setBounds(bounds.x, bounds.y, bounds.w, bounds.h);
    }

    @Override
    public void setBounds(double x, double y, double w, double h) {
        rect.setRect(x, y, w, h);
    }

    @Override
    public Rect getBounds() {
        return rect;
    }

}
