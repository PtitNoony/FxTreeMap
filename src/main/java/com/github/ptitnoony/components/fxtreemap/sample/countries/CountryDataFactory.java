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
package com.github.ptitnoony.components.fxtreemap.sample.countries;

import com.github.ptitnoony.components.fxtreemap.AggregatedCustomizableMapData;
import com.github.ptitnoony.components.fxtreemap.SimpleCustomizableMapData;

/**
 *
 * @author ahamon
 */
public class CountryDataFactory {

    private CountryDataFactory() {
        // private utility function
    }

    public static AggregatedCustomizableMapData<Country> createEUData() {
        AggregatedCustomizableMapData<Country> rootData = new AggregatedCustomizableMapData("European Union", Country.AREA_F);
        Country.EU.forEach(c -> rootData.addChildrenData(CountryDataFactory.createCountryTreeMapData(c)));
        return rootData;
    }

    public static SimpleCustomizableMapData<Country> createCountryTreeMapData(Country c) {
        SimpleCustomizableMapData<Country> data = new SimpleCustomizableMapData(c, Country.COUNTRY_NAME_F, Country.AREA_F);
        return data;
    }

}