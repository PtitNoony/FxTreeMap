# FxTreeMap
An JavaFx implementation of a treemap

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) 
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/384d2a1360004596a5af71a7440e696c)](https://www.codacy.com/gh/PtitNoony/FxTreeMap/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PtitNoony/FxTreeMap&amp;utm_campaign=Badge_Grade)
[![Dependency Status](https://www.versioneye.com/user/projects/597d42850fb24f005e87c771/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/597d42850fb24f005e87c771)

The original algorithm for the core code is derived from [javafx-chart-treemap](https://github.com/tasubo/javafx-chart-treemap).

## Java Version

This version is for Java 17.

## Disclaimer

This small project is in its very early days and features may not be stable.

SO, feel free to request changes!!
I welcome ideas for missing features/architectures...

## Usage

If using maven, one can add the following dependency:

```xml
<dependency>
  <groupId>com.github.ptitnoony.components</groupId>
  <artifactId>fxtreemap</artifactId>
  <version>0.5</version>
</dependency>
```

## Available tree maps components

### FxTreeMap (most complete at the moment)

This version uses a set a `javafx.scene.shape.Rectangle` to draw the treemap.
It also allows more flexibility (on the implementation side) to add new features.

![Alt text](/src/main/resources/readme/FxTreeMap_Example.png?raw=true "FxTreeMap")

### CanvasTreeMap

This component uses a `javafx.scene.canvas.Canvas` to draw the treemap.

![Alt text](/src/main/resources/readme/FxTreeMap_Example_canvas.png?raw=true "CanvasTreeMap")

## How to

The main examples are available in the `main` class. Here is an extract of the code

### Create the data set

```java
        ConcreteMapData data1 = new ConcreteMapData("data1", 6.0);
        ...
        ConcreteMapData data7 = new ConcreteMapData("data7", 1.0);
        ConcreteMapData data = new ConcreteMapData("data-set1", data1, data2, data3, data4, data5, data6, data7);
``` 

### Create the treemap component

```java
        FxTreeMap fxTreeMap = new FxTreeMap(data);
```

### Style the treemap (optional)
```java
        fxTreeMap.setBackgroundColor(Color.LIGHTGRAY);
        fxTreeMap.setStoke(Color.WHITESMOKE);
        fxTreeMap.setBorderRadius(10.0);
        fxTreeMap.setPadding(5);
```

### Add the treemap to the scene

```java
        Node fxTreeMapNode = fxTreeMap.getNode();
        rectAnchorPane.getChildren().add(fxTreeMapNode);
        AnchorPane.setBottomAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setLeftAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setRightAnchor(fxTreeMapNode, 4.0);
        AnchorPane.setTopAnchor(fxTreeMapNode, 4.0);
```

## Control view (for illustration)

The library provides a small example to illustrate the controls over the TreeMap component.

![Alt text](/src/main/resources/readme/FxTreeMap_ControlView.png?raw=true "FxTreeMap ControlView example")

## Next steps

  - ~~update UI when data model is changed~~
  - use css styles
  - draw recursively in canvas example
  - ~~draw names for each data item~~