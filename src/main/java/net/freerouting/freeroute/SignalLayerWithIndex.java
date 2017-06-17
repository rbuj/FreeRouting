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

/**
 *
 * @author Robert Buj
 */
final class SignalLayerWithIndex {

    final String name;

    /**
     * The index in the board layer_structure, -1 for the layers with name "all"
     * or "inner"
     */
    final int index;

    SignalLayerWithIndex(String p_name, int p_index) {
        name = p_name;
        index = p_index;
    }

    @Override
    public String toString() {
        return name;
    }
}
