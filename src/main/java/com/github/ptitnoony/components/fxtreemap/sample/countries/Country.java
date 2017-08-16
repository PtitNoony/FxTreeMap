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

import com.github.ptitnoony.components.fxtreemap.DataNameFunction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ahamon
 */
public class Country {

    public static final Country AUSTRIA = new Country("Austria", 83858, 8169929);
    public static final Country BELGIUM = new Country("Belgium", 30510, 1100702);
    public static final Country BULGARIA = new Country("Bulgaria", 110910, 7621337);
    public static final Country CROATIA = new Country("Croatia", 56542, 4490751);
    public static final Country CYPRUS = new Country("Cyprus", 9248, 803147);
    public static final Country CZECH_REPUBLIC = new Country("Czech Republic", 78866, 10674947);
    public static final Country DENMARK = new Country("Denmark", 43094, 5368854);
    public static final Country ESTONIA = new Country("Estonia", 45226, 1294236);
    public static final Country FINLAND = new Country("Finland", 338424, 5410233);
    public static final Country FRANCE = new Country("France", 643801, 66996000);
    public static final Country GERMANY = new Country("Germany", 357021, 81799600);
    public static final Country GREECE = new Country("Greece", 131940, 11606813);
    public static final Country HUNGARY = new Country("Hungary", 93030, 10075034);
    public static final Country IRELAND = new Country("Ireland", 70280, 4581269);
    public static final Country ITALY = new Country("Italy", 301230, 60665625);
    public static final Country LATVIA = new Country("Latvia", 64589, 1973127);
    public static final Country LITHUANIA = new Country("Lithuania", 65200, 2881020);
    public static final Country LUXEMBBOURG = new Country("Luxembourg", 2586, 512000);
    public static final Country MALTA = new Country("Malta", 316, 397499);
    public static final Country NETHERLANDS = new Country("Netherlands", 41526, 17144600);
    public static final Country POLAND = new Country("Poland", 312685, 38625478);
    public static final Country PORTUGAL = new Country("Portugal", 92391, 10617999);
    public static final Country ROMANIA = new Country("Romania", 238391, 19043767);
    public static final Country SLOVAKIA = new Country("Slovakia", 48845, 5422366);
    public static final Country SLOVENIA = new Country("Slovenia", 20273, 2048847);
    public static final Country SPAIN = new Country("Spain", 505782, 46777373);
    public static final Country SWEDEN = new Country("Sweden", 449964, 9515744);
    // NOT FOR LONG...
    public static final Country UNITED_KINGDOM = new Country("United Kingdom", 243610, 65110000);

    public static final DataNameFunction<Country> COUNTRY_NAME_F = c -> c.name;
    public static final CountryFunction AREA_F = c -> c.area;
    public static final CountryFunction POPULATION_F = c -> c.population;

    public static final List<Country> EU = Collections.unmodifiableList(Arrays.asList(
            AUSTRIA, BELGIUM, BULGARIA, CROATIA, CYPRUS, CZECH_REPUBLIC, DENMARK, ESTONIA, FINLAND,
            FRANCE, GERMANY, GREECE, HUNGARY, IRELAND, ITALY, LATVIA, LITHUANIA, LUXEMBBOURG,
            MALTA, NETHERLANDS, POLAND, PORTUGAL, ROMANIA, SLOVAKIA, SLOVENIA, SPAIN, SWEDEN, UNITED_KINGDOM
    ));

    private final String name;
    private final double area;
    private final double population;

    public Country(String name, double area, double population) {
        this.name = name;
        this.area = area;
        this.population = population;
    }

    public double getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public double getPopulation() {
        return population;
    }

}
