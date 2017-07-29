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
import fr.noony.components.fxtreemap.Rect;
import fr.noony.components.fxtreemap.SimpleMapData;
import fr.noony.components.fxtreemap.TreeMap;
import fr.noony.components.fxtreemap.TreeMapLayout;
import fr.noony.components.fxtreemap.TreeMapStyle;
import fr.noony.components.fxtreemap.TreeMapUtils;
import impl.org.controlsfx.skin.BreadCrumbBarSkin;
import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.List;
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
import javafx.util.Pair;

import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.BreadCrumbBar.BreadCrumbActionEvent;

/**
 *
 * @author ahamon
 */
public class FxTreeMap extends TreeMap {

    private static final Logger LOG = Logger.getGlobal();

    private final FxMapModel model;
    private final TreeMapLayout treeMapLayout;

    private VBox layout;
    private Pane pane;
    private BreadCrumbBar<MapData> breadCrumbBar;
    private TreeItem<MapData> barContent;

    private FxMapModel currentModel;
    private MapData currentData = null;
    private List<Pair<MapData, FxMapModel>> mapLevels;

    private TreeMapStyle style = new TreeMapStyle();

    public FxTreeMap(List<MapData> data, boolean withLayoutDelay) {
        super(withLayoutDelay);
        //
        treeMapLayout = new TreeMapLayout();
        mapLevels = new LinkedList<>();
        model = new FxMapModel(FxTreeMap.this, data, getWidth(), getHeight());
        MapData dummyRootData = new SimpleMapData("root", 0);
        mapLevels.add(new Pair<>(dummyRootData, model));
        currentModel = model;
        model.setTreeMapStyle(style);
        //
        layout = new VBox(8);
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
        barContent = BreadCrumbBar.buildTreeModel(dummyRootData);
        breadCrumbBar.setSelectedCrumb(barContent);
        //
        breadCrumbBar.setAutoNavigationEnabled(false);
        breadCrumbBar.setOnCrumbAction((BreadCrumbActionEvent<MapData> bae) -> handleBreadCrumbEvent(bae));
        breadCrumbBar.setCrumbFactory((TreeItem<MapData> param) -> {
            String label = param != null && param.getValue() != null ? param.getValue().getName() : "?";
            return new BreadCrumbBarSkin.BreadCrumbButton(label);
        });
        //
        runLater(() -> requestLayoutUpdate());
    }

    public FxTreeMap(List<MapData> data) {
        this(data, false);
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
            FxMapModel newDataModel = new FxMapModel(this, data.getChildrenData(), 0, 0);
            newDataModel.setTreeMapStyle(style);
            if (currentModel != null && !currentModel.equals(model)) {
                currentModel.removePropertyChangeListener(this);
            }
            currentModel = newDataModel;
            currentData = data;
            mapLevels.add(new Pair<>(data, newDataModel));
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
    public void set(double spacing) {
        layout.setSpacing(spacing);
    }

    @Override
    protected void applyLayout() {
        LOG.log(Level.INFO, "Applying layout update");
        double width = pane != null ? pane.getWidth() : 0;
        double height = pane != null ? pane.getHeight() : 0;
        currentModel.setSize(width, height);
        treeMapLayout.layout(currentModel, new Rect(0, 0, width, height));
        currentModel.getFxItems().forEach(FxMapItem::applyLayout);
    }

    private void handleBreadCrumbEvent(BreadCrumbActionEvent<MapData> bae) {
        MapData clickedData = bae.getSelectedCrumb().getValue();
        if (!clickedData.equals(currentData)) {
            int index = -1;
            // to be optimized
            for (int i = 0; i < mapLevels.size(); i++) {
                if (mapLevels.get(i).getKey() == clickedData) {
                    index = i;
                    Pair<MapData, FxMapModel> currentLevel = mapLevels.get(index);
                    currentData = currentLevel.getKey();
                    currentModel = currentLevel.getValue();
                    currentModel.addPropertyChangeListener(this);
                    break;
                }
            }
            mapLevels = mapLevels.subList(0, index + 1);
            pane.getChildren().setAll(currentModel.getFxItems().stream().map(i -> i.getNode()).collect(Collectors.toList()));
            updateBreadCrumbBar();
            requestLayoutUpdate();
        }
    }

    private void updateBreadCrumbBar() {
        barContent = BreadCrumbBar.buildTreeModel(mapLevels.stream().map(l -> l.getKey()).toArray(MapData[]::new));
        breadCrumbBar.setSelectedCrumb(barContent);
    }
}
