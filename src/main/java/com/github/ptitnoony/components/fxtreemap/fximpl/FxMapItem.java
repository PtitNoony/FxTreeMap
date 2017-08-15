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
package com.github.ptitnoony.components.fxtreemap.fximpl;

import com.github.ptitnoony.components.fxtreemap.MapData;
import com.github.ptitnoony.components.fxtreemap.MapItem;
import com.github.ptitnoony.components.fxtreemap.Rect;
import com.github.ptitnoony.components.fxtreemap.TreeMapStyle;
import com.github.ptitnoony.components.fxtreemap.TreeMapUtils;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ahamon
 */
public class FxMapItem implements MapItem {

    private static final Logger LOG = Logger.getGlobal();

    private static final double TEXT_PADDING = 8;
    private static final double DEFAULT_TEXT_WIDTH = 50;
    private static final double DEFAULT_TEXT_HEIGHT = 20;
    private static final double DEFAULT_TOOLTIP_FONT_SIZE = 20;

    private final PropertyChangeSupport propertyChangeSupport;

    private final FxMapModel mapModel;
    private final MapData itemData;
    private final Rect rect;
    private final Group mainNode;
    private final Rectangle rectangle;
    private final Label label;
    private final Tooltip tooltip;
    private double padding = 0;

    private Color fillColor;
    private Color strokeColor;
    private Color fillOverColor;
    private Color strokeOverColor;
    private double strokeWidth = TreeMapStyle.DEFAULT_STROKE_WIDTH;

    public FxMapItem(FxMapModel model, MapData data) {
        mapModel = model;
        itemData = data;
        rect = new Rect();
        rectangle = new Rectangle();
        label = new Label(itemData.getName());
        tooltip = new Tooltip(itemData.getName());
        tooltip.setFont(new Font(DEFAULT_TOOLTIP_FONT_SIZE));
        mainNode = new Group(rectangle, label);
        Tooltip.install(label, tooltip);
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
        return itemData.getValue() / mapModel.getTotal();
    }

    @Override
    public double getValue() {
        return itemData.getValue();
    }

    @Override
    public double getSize() {
        return getPercentage() * mapModel.getTotalArea();
    }

    @Override
    public void setBounds(Rect bounds) {
        setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
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
        setStrokeWidth(style.getStrokeWidth());
        setFontColor(style.getFontColor());
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
     * Sets the stroke width for the map item.
     *
     * @param newStrokeWidth the new map item stroke width
     */
    public void setStrokeWidth(double newStrokeWidth) {
        strokeWidth = newStrokeWidth;
        rectangle.setStrokeWidth(strokeWidth);
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
     * Set font color for the item.
     *
     * @param fontColor the font color
     */
    public void setFontColor(Color fontColor) {
        label.setTextFill(fontColor);
    }

    /**
     *
     * @return the map item node to be added to the scene
     */
    protected Node getNode() {
        return mainNode;
    }

    /**
     * Applies the layout values set up using the setBounds methods.
     */
    protected void applyLayout() {
        rectangle.setX(rect.getX() + padding);
        rectangle.setY(rect.getY() + padding);
        rectangle.setWidth(rect.getWidth() - 2.0 * padding);
        rectangle.setHeight(rect.getHeight() - 2.0 * padding);
        //
        label.setRotate(0);
        Bounds textBounds = label.getLayoutBounds();
        boolean verticalText;
        double textHeight;
        double textWidth;
        if (textBounds != null) {
            textHeight = textBounds.getHeight();
            textWidth = textBounds.getWidth();
        } else {
            textHeight = DEFAULT_TEXT_HEIGHT;
            textWidth = DEFAULT_TEXT_WIDTH;
        }
        verticalText = textBounds != null ? textBounds.getWidth() > rectangle.getWidth() - 2 * TEXT_PADDING : false;
        //
        if (!verticalText) {
            label.setTranslateX(rectangle.getX() + TEXT_PADDING);
            label.setTranslateY(rectangle.getY() + TEXT_PADDING);
            label.setRotate(0);
            label.setTextAlignment(TextAlignment.LEFT);
            label.setMaxWidth(rectangle.getWidth() - 2 * TEXT_PADDING);
        } else {
            label.setTranslateX(rectangle.getX() + TEXT_PADDING - textHeight);
            label.setTranslateY(rectangle.getY() + rectangle.getHeight() - textWidth);
            label.setRotate(-90);
            label.setTextAlignment(TextAlignment.LEFT);
            label.setMaxWidth(rectangle.getHeight() - 2 * TEXT_PADDING);
        }
    }

    private void initInteractivity() {
        rectangle.setOnMouseEntered(this::handleMouseEntered);
        rectangle.setOnMouseExited(this::handleMouseExited);
        rectangle.setOnMouseClicked(this::handleMouseClicked);
        //
        label.setOnMouseEntered(this::handleLabelEntered);
        label.setOnMouseExited(this::handleLabelExited);
    }

    private void handleMouseEntered(MouseEvent event) {
        LOG.log(Level.FINE, "handleMouseEntered {0}:: {1}", new Object[]{itemData.getName(), event});
        displayOver();
    }

    private void handleMouseExited(MouseEvent event) {
        LOG.log(Level.FINE, "handleMouseExited {0}:: {1}", new Object[]{itemData.getName(), event});
        displayIdle();
    }

    private void handleMouseClicked(MouseEvent event) {
        LOG.log(Level.FINE, "handleMouseClicked {0}:: {1}", new Object[]{itemData.getName(), event});
        if (itemData.hasChildrenData()) {
            propertyChangeSupport.firePropertyChange(TreeMapUtils.ITEM_CLICKED, null, itemData);
        }
    }

    private void handleLabelEntered(MouseEvent event) {
        LOG.log(Level.FINE, "handleLabelEntered {0}:: {1}", new Object[]{itemData.getName(), event});
        displayOver();
        label.getScene().setCursor(Cursor.OPEN_HAND);
        tooltip.show(label.getScene().getWindow());
    }

    private void handleLabelExited(MouseEvent event) {
        LOG.log(Level.FINE, "handleLabelExited {0}:: {1}", new Object[]{itemData.getName(), event});
        label.getScene().setCursor(Cursor.DEFAULT);
        tooltip.hide();
    }

    private void displayIdle() {
        if (itemData.hasChildrenData()) {
            rectangle.setFill(fillColor);
            rectangle.setStroke(strokeColor);
            rectangle.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    private void displayOver() {
        if (itemData.hasChildrenData()) {
            rectangle.setFill(fillOverColor);
            rectangle.setStroke(strokeOverColor);
            rectangle.getScene().setCursor(Cursor.HAND);
        }
    }
}
