/*
 * The MIT License
 *
 * Copyright 2017 H-K.
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
package com.github.ptitnoony.components.fxtreemap;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ahamon
 */
public class ConcreteMapDataTest {

    private static final Logger LOG = Logger.getGlobal();

    /**
     * Test of getValue method, of class AggredatedData.
     */
    @Test
    public void testGetValue() {
        ConcreteMapData instance = new ConcreteMapData();
        double expResult = 0.0;
        double result = instance.getValue();
        // assert initial value is 0
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setValue method, of class AggredatedData.
     */
    @Test
    public void testSetValue() {
        double newValue = 12.0;
        ConcreteMapData instance = new ConcreteMapData();
        instance.setValue(newValue);
        assertEquals(newValue, instance.getValue(), TreeMapUtils.EPSILON);
    }

    /**
     * Test of getName method, of class AggredatedData.
     */
    @Test
    public void testGetName() {
        ConcreteMapData instance = new ConcreteMapData();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class AggredatedData.
     */
    @Test
    public void testSetName() {
        String newName = "myNewName";
        ConcreteMapData instance = new ConcreteMapData();
        instance.setName(newName);
        assertEquals(newName, instance.getName());
    }

    /**
     * Test of getChildrenData method, of class AggredatedData.
     */
    @Test
    public void testGetChildrenData() {
        ConcreteMapData instance = new ConcreteMapData();
        List<MapData> result = instance.getChildrenData();
        assertEquals(0, result.size());
    }

    /**
     * Test of getChildrenData method, of class AggredatedData, returns an
     * unmodifiable list.
     */
    @Test
    public void testGetChildrenDataModify() {
        ConcreteMapData instance = new ConcreteMapData();
        List<MapData> result = instance.getChildrenData();
        try {
            result.add(new ConcreteMapData(12));
            fail("Should not be able to add directly to the return of getChildrenData");
        } catch (Exception e) {
            LOG.log(Level.FINE, "testGetChildrenDataModify: normal behavior :: {0}", e);
        }
    }

    /**
     * Test of hasChildrenData method, of class AggredatedData.
     */
    @Test
    public void testHasChildrenData() {
        ConcreteMapData instance = new ConcreteMapData();
        boolean expResult = false;
        boolean result = instance.hasChildrenData();
        assertEquals(expResult, result);
    }

    /**
     * Test of addChildrenData method, of class AggredatedData.
     */
    @Test
    public void testAddChildrenData() {
        MapData data1 = new ConcreteMapData("a", 1);
        MapData data2 = new ConcreteMapData("b", 2);
        MapData data3 = new ConcreteMapData("c", 3);
        MapData data4 = new ConcreteMapData("d", 4);
        ConcreteMapData instance = new ConcreteMapData("d_abcd", data1, data2, data3, data4);
        List<MapData> allData = instance.getChildrenData();
        assertEquals(4, allData.size());
        assertTrue(allData.contains(data1));
        assertTrue(allData.contains(data2));
        assertTrue(allData.contains(data3));
        assertTrue(allData.contains(data4));
    }

    /**
     * Test of setValue, of class AggredatedData, when children added.
     */
    @Test
    public void testSetValueWithChildren() {
        double value1 = 1.0;
        double value2 = 2.0;
        double value3 = 3.0;
        double value4 = 4.0;
        MapData data1 = new ConcreteMapData("1.", value1);
        MapData data2 = new ConcreteMapData("2.", value2);
        MapData data3 = new ConcreteMapData("3.", value3);
        MapData data4 = new ConcreteMapData("4.", value4);
        ConcreteMapData instance = new ConcreteMapData("d_1234", data1, data2, data3, data4);
        double initValue = 10;
        assertEquals(initValue, instance.getValue(), TreeMapUtils.EPSILON);
        for (int i = 1; i < 100; i++) {
            double multiplier = i;
            instance.setValue(initValue * multiplier);
            assertEquals(value1 * multiplier, data1.getValue(), TreeMapUtils.EPSILON);
            assertEquals(value2 * multiplier, data2.getValue(), TreeMapUtils.EPSILON);
            assertEquals(value3 * multiplier, data3.getValue(), TreeMapUtils.EPSILON);
            assertEquals(value4 * multiplier, data4.getValue(), TreeMapUtils.EPSILON);
            instance.getChildrenData().forEach(data -> {
                if (data.equals(data1)) {
                    assertEquals(value1 * multiplier, data.getValue(), TreeMapUtils.EPSILON);
                } else if (data.equals(data2)) {
                    assertEquals(value2 * multiplier, data.getValue(), TreeMapUtils.EPSILON);
                } else if (data.equals(data3)) {
                    assertEquals(value3 * multiplier, data.getValue(), TreeMapUtils.EPSILON);
                } else if (data.equals(data4)) {
                    assertEquals(value4 * multiplier, data.getValue(), TreeMapUtils.EPSILON);
                }
            });
        }
        // testing for 0 value
        instance.setValue(0);
        instance.getChildrenData().forEach(data -> {
            if (data.equals(data1)) {
                assertEquals(0, data.getValue(), TreeMapUtils.EPSILON);
            } else if (data.equals(data2)) {
                assertEquals(0, data.getValue(), TreeMapUtils.EPSILON);
            } else if (data.equals(data3)) {
                assertEquals(0, data.getValue(), TreeMapUtils.EPSILON);
            } else if (data.equals(data4)) {
                assertEquals(0, data.getValue(), TreeMapUtils.EPSILON);
            }
        });

    }

    @Test
    public void testPropertyChangeSimple() {
        double value1 = 1.0;
        double value2 = 2.0;
        double value3 = 3.0;
        double value4 = 4.0;
        MapData data1 = new ConcreteMapData("1", value1);
        MapData data2 = new ConcreteMapData("2", value2);
        MapData data3 = new ConcreteMapData("3", value3);
        MapData data4 = new ConcreteMapData("4", value4);
        ConcreteMapData instance = new ConcreteMapData("d_1234", data1, data2, data3, data4);

        data1.setValue(5);
        assertEquals(14, instance.getValue(), TreeMapUtils.EPSILON);

    }

}
