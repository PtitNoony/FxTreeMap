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
package fr.noony.components.fxtreemap.fximpl;

import fr.noony.components.fxtreemap.MapData;
import fr.noony.components.fxtreemap.MapItem;
import fr.noony.components.fxtreemap.Rect;
import fr.noony.components.fxtreemap.TreeMapStyle;
import fr.noony.components.fxtreemap.TreeMapUtils;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author ahamon
 */
public class FxMapItem implements MapItem {

    private static final Logger LOG = Logger.getGlobal();

    private final PropertyChangeSupport propertyChangeSupport;

    private final FxMapModel mapModel;
    private final MapData itemData;
    private final Rect rect;
    private final Rectangle rectangle;
    private final double itemPercentage;
    private double padding = 0;

    private Color fillColor;
    private Color strokeColor;
    private Color fillOverColor;
    private Color strokeOverColor;

    public FxMapItem(FxMapModel model, MapData data, double percentage) {
        mapModel = model;
        itemData = data;
        itemPercentage = percentage;
        rect = new Rect();
        rectangle = new Rectangle();
        applyStyle(model.getStyle());
        if (data.hasChildrenData()) {
            rectangle.setEffect(new Glow());
        }
        propertyChangeSupport = new PropertyChangeSupport(FxMapItem.this);
        propertyChangeSupport.addPropertyChangeListener(model);
        initInteractivity();
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

    /**
     *
     * @param style the style to be applied to the map item
     */
    public final void applyStyle(TreeMapStyle style) {
        setBackgroundColor(style.getFillColor());
        setStroke(style.getStrokeColor());
        setOverBackgroundColor(style.getOverFillColor());
        setOverStroke(style.getOverStrokeColor());
        setPadding(style.getPadding());
        setBorderRadius(style.getBorderRadius());
    }

    /**
     * Sets the background color for the map item.
     *
     * @param newBackgroundColor the new map item background color
     */
    public void setBackgroundColor(Color newBackgroundColor) {
        fillColor = newBackgroundColor;
        rectangle.setFill(fillColor);
    }

    /**
     * Sets the stroke color for the map item.
     *
     * @param newStrokeColor the new map item stroke color
     */
    public void setStroke(Color newStrokeColor) {
        strokeColor = newStrokeColor;
        rectangle.setStroke(strokeColor);
    }

    /**
     * Sets the background color for the map item, when the mouse is over the
     * item.
     *
     * @param newBackgroundOverColor the new map item background over color
     */
    public void setOverBackgroundColor(Color newBackgroundOverColor) {
        fillOverColor = newBackgroundOverColor;
    }

    /**
     * Sets the stroke color for the map item, when the mouse is over the item.
     *
     * @param newStrokeOverColor the new map item stroke over color
     */
    public void setOverStroke(Color newStrokeOverColor) {
        strokeOverColor = newStrokeOverColor;
    }

    /**
     * Sets the corder radius value for the map item.
     *
     * @param newBorderRadius the new border radius
     */
    public void setBorderRadius(double newBorderRadius) {
        rectangle.setArcWidth(newBorderRadius);
        rectangle.setArcHeight(newBorderRadius);
    }

    /**
     * Sets the padding value inside the map item.
     *
     * @param newPaddingValue the new padding value
     */
    public void setPadding(double newPaddingValue) {
        padding = newPaddingValue;
        applyLayout();
    }

    /**
     *
     * @return the map item node to be added to the scene
     */
    protected Node getNode() {
        return rectangle;
    }

    /**
     * Applies the layout values set up using the setBounds methods.
     */
    protected void applyLayout() {
        rectangle.setX(rect.x + padding);
        rectangle.setY(rect.y + padding);
        rectangle.setWidth(rect.w - 2.0 * padding);
        rectangle.setHeight(rect.h - 2.0 * padding);
    }

    private void initInteractivity() {
        rectangle.setOnMouseEntered(this::handleMouseEntered);
        rectangle.setOnMouseExited(this::handleMouseExited);
        rectangle.setOnMouseClicked(this::handleMouseClicked);
    }

    private void handleMouseEntered(MouseEvent event) {
        LOG.log(Level.FINE, "{0}:: {1}", new Object[]{itemData.getName(), event});
        if (itemData.hasChildrenData()) {
            rectangle.setFill(fillOverColor);
            rectangle.setStroke(strokeOverColor);
            rectangle.getScene().setCursor(Cursor.HAND);
        }
    }

    private void handleMouseExited(MouseEvent event) {
        LOG.log(Level.FINE, "{0}:: {1}", new Object[]{itemData.getName(), event});
        if (itemData.hasChildrenData()) {
            rectangle.setFill(fillColor);
            rectangle.setStroke(strokeColor);
            rectangle.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    private void handleMouseClicked(MouseEvent event) {
        LOG.log(Level.FINE, "{0}:: {1}", new Object[]{itemData.getName(), event});
        if (itemData.hasChildrenData()) {
            propertyChangeSupport.firePropertyChange(TreeMapUtils.ITEM_CLICKED, null, itemData);
        }
    }

}
