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
package com.github.ptitnoony.components.fxtreemap.canvasimpl;

import com.github.ptitnoony.components.fxtreemap.MapData;
import com.github.ptitnoony.components.fxtreemap.Rect;
import com.github.ptitnoony.components.fxtreemap.TreeMap;
import com.github.ptitnoony.components.fxtreemap.TreeMapLayout;
import com.github.ptitnoony.components.fxtreemap.TreeMapStyle;
import com.github.ptitnoony.components.fxtreemap.TreeMapUtils;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author ahamon
 */
public class CanvasTreeMap extends TreeMap {

    private static final Logger LOG = Logger.getGlobal();

    private final Canvas canvas;
    private final GraphicsContext gContext;
    private final CanvasMapModel model;
    private final TreeMapLayout treeMapLayout;

    // are kept there for the moment, to be used is color function used ?
    private Color fillColor = TreeMapStyle.DEFAULT_BACKGROUND_COLOR;
    private Color strokeColor = TreeMapStyle.DEFAULT_STOKE_COLOR;

    private double padding = 0;
    private double borderRadius = 0;
    private double strokeWidth = 1;

    public CanvasTreeMap(MapData data, boolean withLayoutDelay) {
        super(withLayoutDelay);
        canvas = new Canvas(getWidth(), getHeight());
        gContext = canvas.getGraphicsContext2D();
        //
        treeMapLayout = new TreeMapLayout();
        model = new CanvasMapModel(data, getWidth(), getHeight());
        data.addPropertyChangeListener(CanvasTreeMap.this);
        getContainer().getChildren().add(canvas);
        requestLayoutUpdate();
    }

    public CanvasTreeMap(MapData data) {
        this(data, false);
    }

    @Override
    public MapData getData() {
        return model.getData();
    }

    @Override
    public void setBackgroundColor(Color newBackgroundColor) {
        fillColor = newBackgroundColor;
        requestLayoutUpdate();
    }

    @Override
    public void setStoke(Color newStrokeColor) {
        strokeColor = newStrokeColor;
        requestLayoutUpdate();
    }

    @Override
    public void setBorderRadius(double newBorderRadius) {
        borderRadius = newBorderRadius;
        requestLayoutUpdate();
    }

    @Override
    public void setStokeWidth(double newStrokeWidth) {
        strokeWidth = newStrokeWidth;
        requestLayoutUpdate();
    }

    @Override
    public void setPadding(double newPaddingValue) {
        padding = newPaddingValue;
        requestLayoutUpdate();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TreeMapUtils.MAP_DATA_VALUE_CHANGED.equals(evt.getPropertyName())) {
            requestLayoutUpdate();
        }
    }

    @Override
    protected void applyLayout() {
        LOG.log(Level.FINE, "Applying layout update");
        model.setSize(getWidth(), getHeight());
        treeMapLayout.layout(model, new Rect(0, 0, getWidth(), getHeight()));
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        draw();
    }

    private void draw() {
        //TODO change
        gContext.setFill(Color.BLACK);
        gContext.fillRect(0, 0, getWidth(), getHeight());
        gContext.beginPath();
        gContext.setStroke(strokeColor);
        gContext.setFill(fillColor);
        gContext.setLineWidth(strokeWidth);
        model.getCanvasItems().forEach(
                item -> {
                    gContext.fillRoundRect(
                            item.getBounds().getX() + padding,
                            item.getBounds().getY() + padding,
                            item.getBounds().getWidth() - 2 * padding,
                            item.getBounds().getHeight() - 2 * padding,
                            borderRadius,
                            borderRadius);
                    gContext.strokeRoundRect(
                            item.getBounds().getX() + padding,
                            item.getBounds().getY() + padding,
                            item.getBounds().getWidth() - 2 * padding,
                            item.getBounds().getHeight() - 2 * padding,
                            borderRadius,
                            borderRadius);
                }
        );
        gContext.closePath();
        gContext.stroke();
    }

}
