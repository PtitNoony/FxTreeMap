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
package fr.noony.components.fxtreemap.canvasimpl;

import fr.noony.components.fxtreemap.MapData;
import fr.noony.components.fxtreemap.Rect;
import fr.noony.components.fxtreemap.TreeMapLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.runLater;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.swing.Timer;

/**
 *
 * @author ahamon
 */
public class CanvasTreeMap {

    private static final Logger LOG = Logger.getGlobal();

    private static final int DEFAULT_TIMER_DELAY = 100;
    private static final double DEFAULT_WIDTH = 1200;
    private static final double DEFAULT_HEIGHT = 800;

    private final Pane mainNode;
    private final Canvas canvas;
    private final GraphicsContext gContext;
    private final CanvasMapModel model;
    private final TreeMapLayout treeMapLayout;
    private final Timer timer;

    private boolean withDelay;

    private double width = DEFAULT_WIDTH;
    private double height = DEFAULT_HEIGHT;

    public CanvasTreeMap(List<MapData> data, boolean withLayoutDelay) {
        mainNode = new Pane();
        canvas = new Canvas(width, height);
        gContext = canvas.getGraphicsContext2D();
        withDelay = withLayoutDelay;
        //
        treeMapLayout = new TreeMapLayout();
        model = new CanvasMapModel(data, width, height);
        mainNode.getChildren().add(canvas);
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

    public CanvasTreeMap(List<MapData> data) {
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
        canvas.setWidth(width);
        canvas.setHeight(height);
        draw();
    }

    private void draw() {
        gContext.setFill(Color.DARKGRAY);
        gContext.fillRect(0, 0, width, height);
        gContext.beginPath();
        gContext.setStroke(Color.LIGHTGRAY);
        gContext.setLineWidth(2.0);
        model.getCanvasItems().forEach(item -> gContext.rect(item.getBounds().x, item.getBounds().y, item.getBounds().w, item.getBounds().h));
        gContext.closePath();
        gContext.stroke();
    }
}
