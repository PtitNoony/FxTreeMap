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
package com.github.ptitnoony.components.fxtreemap.sample;

import com.github.ptitnoony.components.fxtreemap.MapData;
import com.github.ptitnoony.components.fxtreemap.TreeMap;
import com.github.ptitnoony.components.fxtreemap.TreeMapUtils;
import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * FXML Controller class
 *
 * @author ahamon
 */
public final class DataControlTabController implements Initializable {

    private static final Logger LOG = Logger.getGlobal();

    @FXML
    private Label dataNameLabel;
    @FXML
    private Slider valueSlider;
    @FXML
    private TextField valueField;
    @FXML
    private TreeView<MapData> dataView;
    //
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker itemFillColorPicker;
    @FXML
    private ColorPicker itemStrokePicker;
    @FXML
    private Slider itemStrokeSlider;
    @FXML
    private Slider itemCornerRadiusSlider;
    @FXML
    private Slider paddingSlider;

    private TreeMap treeMap;
    private MapData model;
    private MapData selectedData = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setControlsDisabled(true);
        dataView.setEditable(false);
        dataView.setCellFactory((TreeView<MapData> p) -> {
            LOG.log(Level.FINE, "Creating treeview cell factory:: {0}", p);
            return new TextFieldTreeCellImpl();
        });
        dataView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        dataView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<MapData>> observable, TreeItem<MapData> oldValue, TreeItem<MapData> newValue) -> {
            LOG.log(Level.FINE, "New data selected: {0} {1} {2}", new Object[]{observable, oldValue, newValue});
            selectedData = newValue != null ? newValue.getValue() : null;
            updateControls();
        });
        valueSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            LOG.log(Level.FINE, "New slider value: {0} {1} {2}", new Object[]{observable, oldValue, newValue});
            if (selectedData != null) {
                selectedData.setValue(newValue.doubleValue());
            }
        });
        initStyleListeners();
    }

    /**
     * Set the TreeMap to be controlled.
     *
     * @param treeMapComponent the TreeMape to be controlled
     */
    protected void setData(TreeMap treeMapComponent) {
        treeMap = treeMapComponent;
        model = treeMap.getData();
        model.addPropertyChangeListener(this::handleModelChange);
        TreeItem<MapData> root = new TreeItem<>(model);
        createDataChildrenItems(model, root);
        dataView.setRoot(root);
        //
        updateUI();
    }

    private void updateControls() {
        setControlsDisabled(selectedData == null);
        if (selectedData != null) {
            dataNameLabel.setText(selectedData.getName());
            valueSlider.setValue(selectedData.getValue());
            // redundant?
            valueField.setText(Double.toString(selectedData.getValue()));
        } else {
            dataNameLabel.setText("");
            valueField.setText("");
        }
    }

    private void setControlsDisabled(boolean disabled) {
        dataNameLabel.setDisable(disabled);
        valueSlider.setDisable(disabled);
        valueField.setDisable(disabled);
    }

    private void createDataChildrenItems(MapData parentData, TreeItem<MapData> parentTreeItem) {
        parentData.getChildrenData().stream()
                .map(childData -> {
                    TreeItem<MapData> item = new TreeItem<>(childData);
                    createDataChildrenItems(childData, item);
                    return item;
                }).forEachOrdered(item -> parentTreeItem.getChildren().add(item)
        );
    }

    private void handleModelChange(PropertyChangeEvent event) {
        if (TreeMapUtils.MAP_DATA_VALUE_CHANGED.equals(event.getPropertyName()) && selectedData != null) {
            valueField.setText(Double.toString(selectedData.getValue()));
        }
    }

    private void initStyleListeners() {
        backgroundColorPicker.setOnAction(event -> {
            LOG.log(Level.FINE, "User changed background color: {0}", event);
            treeMap.setBackgroundColor(backgroundColorPicker.getValue());
        });
        itemFillColorPicker.setOnAction(event -> {
            LOG.log(Level.FINE, "User changed data fill color: {0}", event);
            treeMap.setDataFill(itemFillColorPicker.getValue());
        });
        itemStrokePicker.setOnAction(event -> {
            LOG.log(Level.FINE, "User changed data stroke color: {0}", event);
            treeMap.setDataStroke(itemStrokePicker.getValue());
        });
        itemStrokeSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            LOG.log(Level.FINE, "New item stroke value: {0} {1} {2}", new Object[]{observable, oldValue, newValue});
            treeMap.setDataStrokeWidth(itemStrokeSlider.getValue());
        });
        itemCornerRadiusSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            LOG.log(Level.FINE, "New item corner radius value: {0} {1} {2}", new Object[]{observable, oldValue, newValue});
            treeMap.setDataBorderRadius(itemCornerRadiusSlider.getValue());
        });
        paddingSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            LOG.log(Level.FINE, "New padding value: {0} {1} {2}", new Object[]{observable, oldValue, newValue});
            treeMap.setPadding(paddingSlider.getValue());
        });
    }

    private void updateUI() {
        backgroundColorPicker.setValue(treeMap.getBackgroundColor());
        itemFillColorPicker.setValue(treeMap.getDataFill());
        itemStrokePicker.setValue(treeMap.getDataStroke());
        itemStrokeSlider.setValue(treeMap.getDataStrokeWidth());
        itemCornerRadiusSlider.setValue(treeMap.getDataBorderRadius());
        paddingSlider.setValue(treeMap.getPadding());
    }

    private static class TextFieldTreeCellImpl extends TreeCell<MapData> {

        private TextFieldTreeCellImpl() {
        }

        @Override
        public void updateItem(MapData item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
            }
        }

        private String getString() {
            return getItem() == null ? "" : getItem().getName() + ": " + getItem().getValue();
        }
    }

}
