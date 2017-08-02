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
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 *
 * @author ahamon
 */
public class FXMLController implements Initializable {

    @FXML
    private AnchorPane rectAnchorPane;
    @FXML
    private AnchorPane canvasAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<MapData> data1 = createDataSet1();
        List<MapData> data2 = createDataSet2();
        createFxTreeMap(data2);
        createCanvasTreeMap(data1);
    }

    private List<MapData> createDataSet1() {
        SimpleMapData data1 = new SimpleMapData("data1", 6.0);
        SimpleMapData data2 = new SimpleMapData("data2", 6.0);
        SimpleMapData data3 = new SimpleMapData("data3", 4.0);
        SimpleMapData data4 = new SimpleMapData("data4", 3.0);
        SimpleMapData data5 = new SimpleMapData("data5", 2.0);
        SimpleMapData data6 = new SimpleMapData("data6", 2.0);
        SimpleMapData data7 = new SimpleMapData("data7", 1.0);
        List<MapData> data = new LinkedList<>();
        data.add(data1);
        data.add(data2);
        data.add(data3);
        data.add(data4);
        data.add(data5);
        data.add(data6);
        data.add(data7);
        return data;
    }

    private List<MapData> createDataSet2() {
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
        List<MapData> data = new LinkedList<>();
        data.add(data1);
        data.add(data2);
        data.add(data3);
        data.add(data4);
        data.add(data5);
        return data;
    }

    private void createFxTreeMap(List<MapData> data) {
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

    private void createCanvasTreeMap(List<MapData> data) {
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
}