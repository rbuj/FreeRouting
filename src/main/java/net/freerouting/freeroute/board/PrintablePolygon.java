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
package net.freerouting.freeroute.board;

import java.util.Locale;
import net.freerouting.freeroute.geometry.planar.FloatPoint;

/**
 *
 * @author Robert Buj
 */
public class PrintablePolygon extends PrintableShape {

    private final FloatPoint[] corner_arr;

    PrintablePolygon(FloatPoint[] p_corners) {
        corner_arr = p_corners;
    }

    @Override
    public String toString() {
        Locale locale = Locale.getDefault();
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.board.resources.ObjectInfoPanel", locale);
        StringBuilder sb = new StringBuilder();
        sb.append(resources.getString("polygon")).append(": ");
        for (int i = 0; i < corner_arr.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(corner_arr[i].to_string(locale));
        }
        return sb.toString();
    }
}
