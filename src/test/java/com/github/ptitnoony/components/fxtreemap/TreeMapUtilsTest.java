/*
 * The MIT License
 *
 * Copyright 2017 Arnaud Hamon.
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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author ahamon
 */
public class TreeMapUtilsTest {

    /**
     * Test of quickSortDesc method, of class TreeMapUtils.
     */
    @Test
    public void testQuickSortDesc() {
        for (int i = 0; i < 25; i++) {
            MapItem[] data = new MapItem[i];
            for (int k = 0; k < i; k++) {
                MapItem item = Mockito.mock(MapItem.class);
                final double value = Math.random();
                Mockito.when(item.getSize()).thenReturn(value);
                data[k] = item;
            }
            int lowerIndex = 0;
            int higherIndex = Math.max(0, data.length - 1);
            boolean isSorted = TreeMapUtils.quickSortDesc(data, lowerIndex, higherIndex);
            if (isSorted) {
                for (int j = 0; j < i - 1; j++) {
                    Assert.assertTrue(data[j].getSize() >= data[j + 1].getSize());
                }
            }
        }
    }

}
