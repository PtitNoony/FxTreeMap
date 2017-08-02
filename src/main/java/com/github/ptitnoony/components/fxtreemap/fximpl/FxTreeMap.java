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
import com.github.ptitnoony.components.fxtreemap.Rect;
import com.github.ptitnoony.components.fxtreemap.TreeMap;
import com.github.ptitnoony.components.fxtreemap.TreeMapLayout;
import com.github.ptitnoony.components.fxtreemap.TreeMapStyle;
import com.github.ptitnoony.components.fxtreemap.TreeMapUtils;
import impl.org.controlsfx.skin.BreadCrumbBarSkin;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static javafx.application.Platform.runLater;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.BreadCrumbBar.BreadCrumbActionEvent;

/**
 *
 * @author ahamon
 */
public class FxTreeMap extends TreeMap {

    private static final Logger LOG = Logger.getGlobal();

    private static final boolean DEFAULT_IS_INTERACTIVE = true;

    private final FxMapModel model;
    private final TreeMapLayout treeMapLayout;

    private VBox layout;
    private Pane pane;
    private BreadCrumbBar<MapData> breadCrumbBar;
    private FxMapModel currentModel;
    private MapData currentData = null;
    private Map<MapData, TreeItem<MapData>> treeItems;
    private Map<MapData, FxMapModel> mapLevels;

    private TreeMapStyle style = new TreeMapStyle();

    public FxTreeMap(MapData mapData, boolean withLayoutDelay) {
        super(withLayoutDelay);
        //
        treeMapLayout = new TreeMapLayout();
        mapLevels = new HashMap<>();
        treeItems = new HashMap<>();
        model = new FxMapModel(FxTreeMap.this, mapData, getWidth(), getHeight());
        mapLevels.put(model.getData(), model);
        currentModel = model;
        model.setTreeMapStyle(style);
        //
        layout = new VBox(8);
        //
        breadCrumbBar = new BreadCrumbBar();
        pane = new Pane();
        pane.getChildren().addAll(model.getFxItems().stream().map(i -> i.getNode()).collect(Collectors.toList()));
        layout.getChildren().add(breadCrumbBar);
        layout.getChildren().add(pane);
        VBox.setVgrow(breadCrumbBar, Priority.NEVER);
        VBox.setVgrow(pane, Priority.ALWAYS);
        getContainer().getChildren().add(layout);
        AnchorPane.setBottomAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setRightAnchor(layout, 0.0);
        AnchorPane.setTopAnchor(layout, 0.0);
        //
        createBar();
        //
        breadCrumbBar.setAutoNavigationEnabled(true);
        breadCrumbBar.setOnCrumbAction((BreadCrumbActionEvent<MapData> bae) -> handleBreadCrumbEvent(bae));
        breadCrumbBar.setCrumbFactory((TreeItem<MapData> param) -> {
            String label = param != null && param.getValue() != null ? param.getValue().getName() : "?";
            return new BreadCrumbBarSkin.BreadCrumbButton(label);
        });
        //
        runLater(() -> requestLayoutUpdate());
    }

    public FxTreeMap(MapData data) {
        this(data, false);
    }

    @Override
    public MapData getData() {
        return model.getData();
    }

    @Override
    public void setBackgroundColor(Color newBackgroundColor) {
        style.setFillColor(newBackgroundColor);
    }

    @Override
    public void setStoke(Color newStrokeColor) {
        style.setStrokeColor(newStrokeColor);
    }

    @Override
    public void setStokeWidth(double newStrokeWidth) {
        style.setStokeWidth(newStrokeWidth);
    }

    @Override
    public void setBorderRadius(double newBorderRadius) {
        style.setBorderRadius(newBorderRadius);
    }

    @Override
    public void setPadding(double newPaddingValue) {
        style.setPadding(newPaddingValue);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TreeMapUtils.ITEM_CLICKED.equals(evt.getPropertyName())) {
            MapData data = (MapData) evt.getNewValue();
            if (!mapLevels.containsKey(data)) {
                FxMapModel newDataModel = new FxMapModel(this, data, 0, 0);
                newDataModel.setTreeMapStyle(style);
                mapLevels.put(data, newDataModel);
                currentModel = newDataModel;
            } else {
                currentModel = mapLevels.get(data);
            }
            currentData = data;
            updateBreadCrumbBar();
            pane.getChildren().setAll(currentModel.getFxItems().stream().map(i -> i.getNode()).collect(Collectors.toList()));
            requestLayoutUpdate();
        }
    }

    /**
     * Set the spacing value between the BreadCrumbBar and the treemap items
     *
     * @param spacing new spacing value
     */
    public void setSpacing(double spacing) {
        layout.setSpacing(spacing);
    }

    @Override
    protected void applyLayout() {
        LOG.log(Level.FINE, "Applying layout update");
        double width = pane != null ? pane.getWidth() : 0;
        double height = pane != null ? pane.getHeight() : 0;
        currentModel.setSize(width, height);
        treeMapLayout.layout(currentModel, new Rect(0, 0, width, height));
        currentModel.getFxItems().forEach(FxMapItem::applyLayout);
    }

    private void handleBreadCrumbEvent(BreadCrumbActionEvent<MapData> bae) {
        MapData clickedData = bae.getSelectedCrumb().getValue();
        if (!clickedData.equals(currentData)) {
            currentData = clickedData;
            if (!mapLevels.containsKey(currentData)) {
                LOG.log(Level.SEVERE, "Could not find map item for data :: {0}", currentData.getName());
            } else {
                currentModel = mapLevels.get(currentData);
            }
            pane.getChildren().setAll(currentModel.getFxItems().stream().map(i -> i.getNode()).collect(Collectors.toList()));
            requestLayoutUpdate();
        }
    }

    private void createBar() {
        TreeItem<MapData> root = new TreeItem<>(model.getData());
        createDataChildrenItems(model.getData(), root);
        breadCrumbBar.setSelectedCrumb(root);
    }

    private void createDataChildrenItems(MapData parentData, TreeItem<MapData> parentTreeItem) {
        parentData.getChildrenData().stream()
                .filter(childData -> childData.hasChildrenData())
                .map(childData -> {
                    TreeItem<MapData> item = new TreeItem<>(childData);
                    treeItems.put(childData, item);
                    createDataChildrenItems(childData, item);
                    return item;
                }).forEachOrdered(item -> parentTreeItem.getChildren().add(item)
        );
    }

    private void updateBreadCrumbBar() {
        breadCrumbBar.setSelectedCrumb(treeItems.get(currentData));
    }
}
