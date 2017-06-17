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
package net.freerouting.freeroute;

import net.freerouting.freeroute.rules.ClearanceMatrix;

/**
 *
 * @author Robert Buj
 */
final class ClearanceClassBuilder {

    private ClearanceClassBuilder() {
        // no code
    }

    static ClearanceClass[] build_clearance_class_array(ClearanceMatrix p_clearance_matrix) {
        ClearanceClass[] class_arr = new ClearanceClass[p_clearance_matrix.get_class_count()];
        for (int i = 0; i < class_arr.length; ++i) {
            class_arr[i] = new ClearanceClass(p_clearance_matrix.get_name(i), i);
        }
        return class_arr;
    }
}
