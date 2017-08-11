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

import com.github.ptitnoony.components.fxtreemap.TreeMap;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ahamon
 */
public final class DataControlController implements Initializable {

    private static final Logger LOG = Logger.getGlobal();

    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing to do  here
    }

    /**
     * Add a TreeMap to be controlled.
     *
     * @param treeMap the TreeMap to be added
     */
    protected void addData(TreeMap treeMap) {
        AnchorPane container = createTabContent(treeMap);
        Tab tab = new Tab(treeMap.getData().getName(), container);
        tab.setClosable(false);
        tabPane.getTabs().add(tab);
    }

    private AnchorPane createTabContent(TreeMap treeMap) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DataControlTab.fxml"));
            AnchorPane root = loader.load();
            DataControlTabController controller = loader.getController();
            controller.setData(treeMap);
            return root;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception while loading control tab: {0}", e);
        }
        return new AnchorPane();
    }

}
