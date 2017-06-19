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
 * ComponentPlacement.java
 *
 * Created on 20. Mai 2004, 07:43
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Describes the placement data of a library component
 *
 * @author alfons
 */
class ComponentPlacement {

    /**
     * The name of the corresponding library component
     */
    final String lib_name;

    /**
     * The list of ComponentLocations of the library component on the board.
     */
    final Collection<ComponentLocation> locations;

    /**
     * Creates a new instance of ComponentPlacement
     */
    ComponentPlacement(String p_lib_name) {
        lib_name = p_lib_name;
        locations = new LinkedList<>();
    }

    static class ComponentLocation {

        final String name;

        /**
         * the x- and the y-coordinate of the location.
         */
        final double[] coor;

        /**
         * True, if the component is placed at the component side. Else the
         * component is placed at the solder side.
         */
        final boolean is_front;

        /**
         * The rotation of the component in degree.
         */
        final double rotation;

        /**
         * If true, the component cannot be moved.
         */
        final boolean position_fixed;

        /**
         * The entries of this map are of type ItemClearanceInfo, the keys are
         * the pin names.
         */
        final Map<String, ItemClearanceInfo> pin_infos;

        final Map<String, ItemClearanceInfo> keepout_infos;

        final Map<String, ItemClearanceInfo> via_keepout_infos;

        final Map<String, ItemClearanceInfo> place_keepout_infos;

        ComponentLocation(String p_name, double[] p_coor, boolean p_is_front,
                double p_rotation, boolean p_position_fixed,
                Map<String, ItemClearanceInfo> p_pin_infos,
                Map<String, ItemClearanceInfo> p_keepout_infos,
                Map<String, ItemClearanceInfo> p_via_keepout_infos,
                Map<String, ItemClearanceInfo> p_place_keepout_infos) {
            name = p_name;
            coor = p_coor;
            is_front = p_is_front;
            rotation = p_rotation;
            position_fixed = p_position_fixed;
            pin_infos = p_pin_infos;
            keepout_infos = p_keepout_infos;
            via_keepout_infos = p_via_keepout_infos;
            place_keepout_infos = p_place_keepout_infos;

        }
    }

    static class ItemClearanceInfo {

        final String name;
        final String clearance_class;

        ItemClearanceInfo(String p_name, String p_clearance_class) {
            name = p_name;
            clearance_class = p_clearance_class;
        }
    }
}
