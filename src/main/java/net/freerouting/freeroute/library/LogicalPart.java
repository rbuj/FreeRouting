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
 * LogicalPart.java
 *
 * Created on 26. Maerz 2005, 06:14
 */
package net.freerouting.freeroute.library;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains contain information for gate swap and pin swap for a single
 * component.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class LogicalPart implements net.freerouting.freeroute.board.ObjectInfoPanel.Printable, java.io.Serializable {

    public final String name;
    public final int no;
    private final PartPin[] part_pin_arr;

    /**
     * Creates a new instance of LogicalPart. The part pins are sorted by
     * pin_no. The pin_no's of the part pins must be the same number as in the
     * componnents library package.
     */
    LogicalPart(String p_name, int p_no, PartPin[] p_part_pin_arr) {
        name = p_name;
        no = p_no;
        part_pin_arr = (p_part_pin_arr == null) ? null : p_part_pin_arr.clone();
    }

    public int pin_count() {
        return part_pin_arr.length;
    }

    /**
     * Returns the pim with index p_no. Pin numbers are from 0 to pin_count - 1
     */
    public PartPin get_pin(int p_no) {
        if (p_no < 0 || p_no >= part_pin_arr.length) {
            Logger.getLogger(LogicalPart.class.getName()).log(Level.INFO, "LogicalPart.get_pin: p_no out of range");
            return null;
        }
        return part_pin_arr[p_no];
    }

    @Override
    public void print_info(net.freerouting.freeroute.board.ObjectInfoPanel p_window) {
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.board.resources.ObjectInfoPanel", Locale.getDefault());
        p_window.append_bold(resources.getString("logical_part_2") + " ");
        p_window.append_bold(this.name);
        for (int i = 0; i < this.part_pin_arr.length; ++i) {
            PartPin curr_pin = this.part_pin_arr[i];
            p_window.newline();
            p_window.indent();
            p_window.append(resources.getString("pin") + " ");
            p_window.append(curr_pin.pin_name);
            p_window.append(", " + resources.getString("gate") + " ");
            p_window.append(curr_pin.gate_name);
            p_window.append(", " + resources.getString("swap_code") + " ");
            Integer gate_swap_code = curr_pin.gate_swap_code;
            p_window.append(gate_swap_code.toString());
            p_window.append(", " + resources.getString("gate_pin") + " ");
            p_window.append(curr_pin.gate_pin_name);
            p_window.append(", " + resources.getString("swap_code") + " ");
            Integer pin_swap_code = curr_pin.gate_pin_swap_code;
            p_window.append(pin_swap_code.toString());
        }
        p_window.newline();
        p_window.newline();
    }
}
