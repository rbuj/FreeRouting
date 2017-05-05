/*
 *  Copyright (C) 2014  Alfons Wirtz  
 *   website www.freerouting.net
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License at <http://www.gnu.org/licenses/> 
 *   for more details.
 *
 * ColorIntensityTable.java
 *
 * Created on 1. August 2004, 07:46
 */
package net.freerouting.freeroute.boardgraphics;

import java.util.EnumMap;

/**
 * The color intensities for each item type. The values are between 0
 * (invisible) and 1 (full intensity).
 *
 * @author alfons
 */
@SuppressWarnings("serial")
public class ColorIntensityTable implements java.io.Serializable {

    private final EnumMap<ObjectNames,Double> arr = new EnumMap<>(ObjectNames.class);

    /**
     * Creates a new instance of ColorIntensityTable. The elements of
     * p_intensities are expected between 0 and 1.
     */
    public ColorIntensityTable() {
        arr.put(ObjectNames.TRACES, 0.4);
        arr.put(ObjectNames.VIAS, 0.6);
        arr.put(ObjectNames.PINS, 0.6);
        arr.put(ObjectNames.CONDUCTION_AREAS, 0.2);
        arr.put(ObjectNames.KEEPOUTS, 0.2);
        arr.put(ObjectNames.VIA_KEEPOUTS, 0.2);
        arr.put(ObjectNames.PLACE_KEEPOUTS, 0.2);
        arr.put(ObjectNames.COMPONENT_OUTLINES, 1.0);
        arr.put(ObjectNames.HILIGHT, 0.8);
        arr.put(ObjectNames.INCOMPLETES, 1.0);
        arr.put(ObjectNames.LENGTH_MATCHING_AREAS, 0.1);
    }

    /**
     * Copy constructor.
     */
    public ColorIntensityTable(ColorIntensityTable p_color_intesity_table) {
        arr.putAll(p_color_intesity_table.arr);
    }

    public double get_value(ObjectNames o) {
        return arr.get(o);
    }

    public void set_value(ObjectNames o, double p_value) {
        arr.put(o, p_value);
    }

    public enum ObjectNames {
        TRACES, VIAS, PINS, CONDUCTION_AREAS, KEEPOUTS, VIA_KEEPOUTS, PLACE_KEEPOUTS, COMPONENT_OUTLINES,
        HILIGHT, INCOMPLETES, LENGTH_MATCHING_AREAS
    }
}
