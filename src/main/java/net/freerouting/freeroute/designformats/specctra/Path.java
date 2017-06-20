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
 * Path.java
 *
 * Created on 30. Juni 2004, 09:28
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class for writing path scopes from dsn-files.
 *
 * @author alfons
 */
abstract class Path extends Shape {

    final double width;
    final double[] coordinate_arr;

    /**
     * Creates a new instance of Path
     */
    Path(LayerInfo p_layer, double p_width, double[] p_coordinate_arr) {
        super(p_layer);
        width = p_width;
        coordinate_arr = (p_coordinate_arr == null) ? null : p_coordinate_arr.clone();
    }

    Path(LayerInfo layer, Collection<Object> corner_list) throws ReadScopeException {
        super(layer);
        if (corner_list.size() < 5) {
            throw new ReadScopeException("To few numbers in scope");
        }
        Iterator<Object> it = corner_list.iterator();
        double new_width;
        Object next_object = it.next();
        if (next_object instanceof Double) {
            new_width = (double) next_object;
        } else if (next_object instanceof Integer) {
            new_width = ((Number) next_object).doubleValue();
        } else {
            throw new ReadScopeException("Number expected");
        }
        double[] new_corner_arr = new double[corner_list.size() - 1];
        for (int i = 0; i < new_corner_arr.length; ++i) {
            next_object = it.next();
            if (next_object instanceof Double) {
                new_corner_arr[i] = (double) next_object;
            } else if (next_object instanceof Integer) {
                new_corner_arr[i] = ((Number) next_object).doubleValue();
            } else {
                throw new ReadScopeException("Number expected");
            }
        }
        width = new_width;
        coordinate_arr = (new_corner_arr == null) ? null : new_corner_arr.clone();
    }
}
