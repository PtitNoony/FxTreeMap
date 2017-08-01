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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.scene.paint.Color;

/**
 *
 * @author ahamon
 */
public class TreeMapStyle {

    public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGRAY;
    public static final Color DEFAULT_STOKE_COLOR = Color.LIGHTGRAY;

    public static final Color OVER_BACKGROUND_COLOR = Color.GRAY;
    public static final Color OVER_STOKE_COLOR = Color.CORNFLOWERBLUE;

    public static final double DEFAULT_PADDING = 0;
    public static final double DEFAULT_BORDER_RADIUS = 8;
    public static final double DEFAULT_STROKE_WIDTH = 1.5;

    public static final String STYLE_CHANGED = "treeStyleChanged";

    private final PropertyChangeSupport propertyChangeSupport;

    private double padding;
    private double borderRadius;
    private double strokeWidth;
    private Color fillColor;
    private Color strokeColor;
    private Color overFillColor;
    private Color overStrokeColor;

    public TreeMapStyle() {
        propertyChangeSupport = new PropertyChangeSupport(TreeMapStyle.this);
        //
        padding = DEFAULT_PADDING;
        borderRadius = DEFAULT_BORDER_RADIUS;
        strokeWidth = DEFAULT_STROKE_WIDTH;
        fillColor = DEFAULT_BACKGROUND_COLOR;
        strokeColor = DEFAULT_STOKE_COLOR;
        overFillColor = OVER_BACKGROUND_COLOR;
        overStrokeColor = OVER_STOKE_COLOR;
    }

    public double getPadding() {
        return padding;
    }

    public double getBorderRadius() {
        return borderRadius;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public Color getOverFillColor() {
        return overFillColor;
    }

    public Color getOverStrokeColor() {
        return overStrokeColor;
    }

    public void setPadding(double padding) {
        this.padding = padding;
        firePropertyChanged();
    }

    public void setBorderRadius(double borderRadius) {
        this.borderRadius = borderRadius;
        firePropertyChanged();
    }

    public void setStokeWidth(double newStrokeWidth) {
        this.strokeWidth = newStrokeWidth;
//        firePropertyChanged();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        firePropertyChanged();
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
        firePropertyChanged();
    }

    public void setOverFillColor(Color overFillColor) {
        this.overFillColor = overFillColor;
        firePropertyChanged();
    }

    public void setOverStrokeColor(Color overStrokeColor) {
        this.overStrokeColor = overStrokeColor;
        firePropertyChanged();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void firePropertyChanged() {
        // basic fire at the moment
        propertyChangeSupport.firePropertyChange(STYLE_CHANGED, null, null);
    }

}
