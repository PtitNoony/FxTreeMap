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
import fr.noony.components.fxtreemap.MapModel;
import fr.noony.components.fxtreemap.TreeMapStyle;
import fr.noony.components.fxtreemap.TreeMapUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ahamon
 */
public class FxMapModel implements MapModel {

    private final List<FxMapItem> mapItems;
    private final List<MapData> modelData;
    private final PropertyChangeSupport propertyChangeSupport;
    private double totalArea;
    private double totalValue;
    private TreeMapStyle style;

    public FxMapModel(FxTreeMap treeMap, List<MapData> data, double width, double height) {
        modelData = data;
        mapItems = new LinkedList<>();
        propertyChangeSupport = new PropertyChangeSupport(FxMapModel.this);
        propertyChangeSupport.addPropertyChangeListener(treeMap);
        style = new TreeMapStyle();
        totalArea = width * height;
        totalValue = data.stream().mapToDouble(item -> item.getValue()).sum();
        modelData.forEach(d -> {
            FxMapItem mapItem = new FxMapItem(FxMapModel.this, d, d.getValue() / totalValue);
            mapItems.add(mapItem);
        });
    }

    @Override
    public List<FxMapItem> getItems() {
        return mapItems;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case TreeMapUtils.ITEM_CLICKED:
                propertyChangeSupport.firePropertyChange(evt);
                break;
            case TreeMapStyle.STYLE_CHANGED:
                mapItems.forEach(item -> item.applyStyle(style));
                break;
            default:
                throw new UnsupportedOperationException(evt.getPropertyName());
        }
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void setTreeMapStyle(TreeMapStyle newStyle) {
        if (style != null) {
            style.removePropertyChangeListener(this);
        }
        style = newStyle;
        style.addPropertyChangeListener(this);
        mapItems.forEach(item -> item.applyStyle(style));
    }

    public TreeMapStyle getStyle() {
        return style;
    }

    public double getTotal() {
        return totalValue;
    }

    public void setSize(double width, double height) {
        totalArea = width * height;
    }

    public double getTotalArea() {
        return totalArea;
    }

    public List<MapData> getData() {
        return modelData;
    }

    protected List<FxMapItem> getFxItems() {
        return mapItems;
    }
}
