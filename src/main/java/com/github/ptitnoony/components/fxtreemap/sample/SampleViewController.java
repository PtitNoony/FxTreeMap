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

import com.github.ptitnoony.components.fxtreemap.AggredatedData;
import com.github.ptitnoony.components.fxtreemap.MapData;
import com.github.ptitnoony.components.fxtreemap.SimpleMapData;
import com.github.ptitnoony.components.fxtreemap.canvasimpl.CanvasTreeMap;
import com.github.ptitnoony.components.fxtreemap.fximpl.FxTreeMap;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author ahamon
 */
public final class SampleViewController implements Initializable {

    private static final Logger LOG = Logger.getGlobal();

    /**
     * Value to translate the control stage from the view stage so they do not
     * overlap
     */
    private static final double DELTA_SCREEN_POSITION = 150;

    @FXML
    private AnchorPane rectAnchorPane;
    @FXML
    private AnchorPane canvasAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MapData data1 = createDataSet1();
        MapData data2 = createDataSet2();
        createFxTreeMap(data1);
        createCanvasTreeMap(data2);
        createControlView(data1, data2);
    }

    private MapData createDataSet2() {
        SimpleMapData data1 = new SimpleMapData("data1", 6.0);
        SimpleMapData data2 = new SimpleMapData("data2", 6.0);
        SimpleMapData data3 = new SimpleMapData("data3", 4.0);
        SimpleMapData data4 = new SimpleMapData("data4", 3.0);
        SimpleMapData data5 = new SimpleMapData("data5", 2.0);
        SimpleMapData data6 = new SimpleMapData("data6", 2.0);
        SimpleMapData data7 = new SimpleMapData("data7", 1.0);
        //
        return new AggredatedData("data-set2", data1, data2, data3, data4, data5, data6, data7);
    }

    private MapData createDataSet1() {
        AggredatedData data1 = new AggredatedData("data1");
        AggredatedData data1_1 = new AggredatedData("data1_1");
        SimpleMapData data1_1_1 = new SimpleMapData("data1_1_1", 0.5);
        SimpleMapData data1_1_2 = new SimpleMapData("data1_1_2", 1.0);
        SimpleMapData data1_1_3 = new SimpleMapData("data1_1_3", 2.0);
        data1_1.addChildrenData(data1_1_1);
        data1_1.addChildrenData(data1_1_2);
        data1_1.addChildrenData(data1_1_3);
        SimpleMapData data1_2 = new SimpleMapData("data1_2", 6.0);
        SimpleMapData data1_3 = new SimpleMapData("data1_3", 3.0);
        SimpleMapData data1_4 = new SimpleMapData("data1_4", 1.0);
        data1.addChildrenData(data1_1);
        data1.addChildrenData(data1_2);
        data1.addChildrenData(data1_3);
        data1.addChildrenData(data1_4);
        SimpleMapData data2 = new SimpleMapData("data2", 3.0);
        SimpleMapData data3 = new SimpleMapData("data3", 2.0);
        SimpleMapData data4 = new SimpleMapData("data4", 2.0);
        SimpleMapData data5 = new SimpleMapData("data5", 1.0);
        //
        return new AggredatedData("data-set1", data1, data2, data3, data4, data5);
    }

    private void createFxTreeMap(MapData data) {
        FxTreeMap fxTreeMap = new FxTreeMap(data);
        Node fxTreeMapNode = fxTreeMap.getNode();
        rectAnchorPane.getChildren().add(fxTreeMapNode);
        AnchorPane.setBottomAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setLeftAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setRightAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setTopAnchor(fxTreeMapNode, 4.0);
        //
        fxTreeMap.setBackgroundColor(Color.LIGHTGRAY);
        fxTreeMap.setStoke(Color.WHITESMOKE);
        fxTreeMap.setBorderRadius(10.0);
        fxTreeMap.setPadding(5);
    }

    private void createCanvasTreeMap(MapData data) {
        CanvasTreeMap canvasTreeMap = new CanvasTreeMap(data);
        Node canvasTreeMapNode = canvasTreeMap.getNode();
        canvasAnchorPane.getChildren().add(canvasTreeMapNode);
        AnchorPane.setBottomAnchor(canvasTreeMapNode, 4.0);
        AnchorPane.setLeftAnchor(canvasTreeMapNode, 4.0);
        AnchorPane.setRightAnchor(canvasTreeMapNode, 4.0);
        AnchorPane.setTopAnchor(canvasTreeMapNode, 4.0);
        //
        canvasTreeMap.setBackgroundColor(Color.CORNFLOWERBLUE);
        canvasTreeMap.setStoke(Color.DIMGREY);
        canvasTreeMap.setBorderRadius(8.0);
        canvasTreeMap.setPadding(3);
    }

    private void createControlView(MapData data1, MapData data2) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DataControl.fxml"));
            Parent root = loader.load();
            DataControlController controller = loader.getController();
            controller.addData(data1);
            controller.addData(data2);
            Scene scene = new Scene(root);
            stage.setTitle("FxTreeMap Data control view");
            stage.setScene(scene);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception while loading control view: {0}", e);
        }
        stage.show();
        stage.setX(stage.getX() + DELTA_SCREEN_POSITION);
        stage.setY(stage.getY() + DELTA_SCREEN_POSITION);
    }
}
