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
package fr.noony.components.fxtreemap;

import fr.noony.components.fxtreemap.canvasimpl.CanvasTreeMap;
import fr.noony.components.fxtreemap.fximpl.FxTreeMap;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

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
        List<MapData> data = createData();
        createFxTreeMap(data);
        createCanvasTreeMap(data);
    }

    private List<MapData> createData() {
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

    private void createFxTreeMap(List<MapData> data) {
        FxTreeMap fxTreeMap = new FxTreeMap(data);
        Node fxTreeMapNode = fxTreeMap.getNode();
        rectAnchorPane.getChildren().add(fxTreeMapNode);
        AnchorPane.setBottomAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setLeftAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setRightAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setTopAnchor(fxTreeMapNode, 4.0);
    }

    private void createCanvasTreeMap(List<MapData> data) {
        CanvasTreeMap canvasTreeMap = new CanvasTreeMap(data);
        Node canvasTreeMapNode = canvasTreeMap.getNode();
        canvasAnchorPane.getChildren().add(canvasTreeMapNode);
        AnchorPane.setBottomAnchor(canvasTreeMapNode, 4.0);
        AnchorPane.setLeftAnchor(canvasTreeMapNode, 4.0);
        AnchorPane.setRightAnchor(canvasTreeMapNode, 4.0);
        AnchorPane.setTopAnchor(canvasTreeMapNode, 4.0);
    }
}
