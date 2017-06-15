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
 * ClearanceViolation.java
 *
 * Created on 4. Oktober 2004, 08:56
 */
package net.freerouting.freeroute.board;

import java.util.Locale;
import net.freerouting.freeroute.geometry.planar.ConvexShape;

/**
 * Information of a clearance violation between 2 items.
 *
 * @author alfons
 */
public class ClearanceViolation implements ObjectInfoPanel.Printable {

    /**
     * The first item of the clearance violation
     */
    public final Item first_item;
    /**
     * The second item of the clearance violation
     */
    public final Item second_item;
    /**
     * The shape of the clearance violation
     */
    public final ConvexShape shape;
    /**
     * The layer_no of the clearance violation
     */
    public final int layer_no;

    /**
     * Creates a new instance of ClearanceViolation
     */
    ClearanceViolation(Item p_first_item, Item p_second_item, ConvexShape p_shape, int p_layer) {
        first_item = p_first_item;
        second_item = p_second_item;
        shape = p_shape;
        layer_no = p_layer;
    }

    @Override
    public void print_info(ObjectInfoPanel p_window) {
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.board.resources..ObjectInfoPanel", Locale.getDefault());
        p_window.append_bold(resources.getString("clearance_violation_2"));
        p_window.append(" " + resources.getString("at") + " ");
        p_window.append(shape.centre_of_gravity());
        p_window.append(", " + resources.getString("width") + " ");
        p_window.append(2 * this.shape.smallest_radius());
        p_window.append(", " + resources.getString("layer") + " ");
        p_window.append(first_item.board.layer_structure.get_name_layer(this.layer_no));
        p_window.append(", " + resources.getString("between"));
        p_window.newline();
        p_window.indent();
        first_item.print_info(p_window);
        p_window.indent();
        second_item.print_info(p_window);
    }
}
