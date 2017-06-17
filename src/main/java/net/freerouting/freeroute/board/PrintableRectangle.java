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
public class PrintableRectangle extends PrintableShape {

    private final FloatPoint lower_left;
    private final FloatPoint upper_right;

    PrintableRectangle(FloatPoint p_lower_left, FloatPoint p_upper_right) {
        lower_left = p_lower_left;
        upper_right = p_upper_right;
    }

    @Override
    public String toString() {
        Locale locale = Locale.getDefault();
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.board.resources.ObjectInfoPanel", locale);
        StringBuilder sb = new StringBuilder();
        sb.append(resources.getString("rectangle")).append(": ").append(resources.getString("lower_left")).append(" = ").append(lower_left.to_string(locale)).append(", ").append(resources.getString("upper_right")).append(" = ").append(upper_right.to_string(locale));
        return sb.toString();
    }
}
