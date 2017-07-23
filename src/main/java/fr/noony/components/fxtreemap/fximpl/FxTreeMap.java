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
import fr.noony.components.fxtreemap.TreeMap;
import fr.noony.components.fxtreemap.TreeMapLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static javafx.application.Platform.runLater;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javax.swing.Timer;

/**
 *
 * @author ahamon
 */
public class FxTreeMap implements TreeMap {

    private static final Logger LOG = Logger.getGlobal();

    private static final int DEFAULT_TIMER_DELAY = 100;
    private static final double DEFAULT_WIDTH = 1200;
    private static final double DEFAULT_HEIGHT = 800;

    private final Pane mainNode;
    private final FxMapModel model;
    private final TreeMapLayout treeMapLayout;
    private final Timer timer;

    private boolean withDelay;

    private double width = DEFAULT_WIDTH;
    private double height = DEFAULT_HEIGHT;

    public FxTreeMap(List<MapData> data, boolean withLayoutDelay) {
        mainNode = new Pane();
        withDelay = withLayoutDelay;
        //
        treeMapLayout = new TreeMapLayout();
        model = new FxMapModel(data, width, height);
        mainNode.getChildren().addAll(model.getFxItems().stream().map(i -> i.getRectangle()).collect(Collectors.toList()));
        //
        mainNode.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            LOG.log(Level.FINEST, "{0} changed from {1} to {2}", new Object[]{observable, oldValue, newValue});
            width = newValue.doubleValue();
            requestLayoutUpdate();
        });
        mainNode.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            LOG.log(Level.FINEST, "{0} changed from {1} to {2}", new Object[]{observable, oldValue, newValue});
            height = newValue.doubleValue();
            requestLayoutUpdate();
        });
        timer = new Timer(DEFAULT_TIMER_DELAY, (ActionEvent e) -> {
            LOG.log(Level.INFO, "Restart layout update timer on event {0}", e);
            runLater(this::applyLayout);
        });
        timer.setRepeats(false);
        requestLayoutUpdate();
    }

    public FxTreeMap(List<MapData> data) {
        this(data, false);
    }

    public Node getNode() {
        return mainNode;
    }

    private void requestLayoutUpdate() {
        if (withDelay) {
            triggerTimer();
        } else {
            applyLayout();
        }
    }

    private void triggerTimer() {
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }

    private void applyLayout() {
        LOG.log(Level.INFO, "Applying layout update");
        model.setSize(width, height);
        treeMapLayout.layout(model, new Rect(0, 0, width, height));
        model.getFxItems().forEach(FxMapItem::applyLayout);
    }
}
