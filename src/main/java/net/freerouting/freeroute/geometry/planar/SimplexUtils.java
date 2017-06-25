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
package net.freerouting.freeroute.geometry.planar;

/**
 *
 * @author Robert Buj
 */
@SuppressWarnings("serial")
public final class SimplexUtils implements java.io.Serializable {

    /**
     * Standard implementation for an empty Simplex.
     */
    public static final Simplex EMPTY = new Simplex(new Line[0]);

    /**
     * creates a Simplex as intersection of the halfplanes defined by an array
     * of directed lines
     */
    public static Simplex get_instance(Line[] p_line_arr) {
        if (p_line_arr.length <= 0) {
            return EMPTY;
        }
        Line[] curr_arr = new Line[p_line_arr.length];
        System.arraycopy(p_line_arr, 0, curr_arr, 0, p_line_arr.length);
        // sort the lines in ascending direction
        java.util.Arrays.parallelSort(curr_arr);
        Simplex curr_simplex = new Simplex(curr_arr);
        Simplex result = curr_simplex.remove_redundant_lines();
        return result;
    }

    private SimplexUtils() {
        // not called
    }
}
