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

/**
 *
 * @author robert
 */
public class Area implements AreaReadable, AreaTransformable {

    String area_name; // may be generated later on, if area_name is null.
    final Collection<Shape> shape_list;
    final String clearance_class_name;

    Area(String p_area_name, Collection<Shape> p_shape_list, String p_clearance_class_name) {
        area_name = p_area_name;
        shape_list = p_shape_list;
        clearance_class_name = p_clearance_class_name;
    }
}
