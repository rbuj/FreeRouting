/*
 * Copyright (C) 2017 Robert Antoni Buj Gelonch {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Robert Buj
 */
class PathHelper {
    
    static PathHelper extract_width_and_coordinates(Collection<Object> corner_list)
            throws ReadScopeException {

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
        return new PathHelper(new_width, new_corner_arr);
    }

    private final double h_width;
    private final double[] h_coordinate_arr;

    private PathHelper(double p_width, double[] p_coordinate_arr) {
        this.h_width = p_width;
        this.h_coordinate_arr = (p_coordinate_arr == null) ? null : p_coordinate_arr.clone();
    }

    double get_width() {
        return h_width;
    }

    double[] get_coordinates() {
        return h_coordinate_arr;
    }
}
