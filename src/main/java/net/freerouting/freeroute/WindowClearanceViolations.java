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
 * ViolationsWindow.java
 *
 * Created on 22. Maerz 2005, 05:40
 */
package net.freerouting.freeroute;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.freerouting.freeroute.board.ClearanceViolation;
import net.freerouting.freeroute.geometry.planar.FloatPoint;
import net.freerouting.freeroute.interactive.ClearanceViolations;

/**
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public final class WindowClearanceViolations extends WindowObjectListWithFilter {

    private final java.util.ResourceBundle resources;

    /**
     * Creates a new instance of IncompletesWindow
     */
    WindowClearanceViolations(BoardFrame p_board_frame) {
        super(p_board_frame);
        this.resources = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.WindowClearanceViolations", Locale.getDefault());
        this.setTitle(resources.getString("title"));
        this.list_empty_message.setText(resources.getString("list_empty_message"));
        p_board_frame.set_context_sensitive_help(this, "WindowObjectList_ClearanceViolations");
    }

    @Override
    protected void fill_list() {
        net.freerouting.freeroute.interactive.BoardHandling board_handling = this.board_frame.board_panel.board_handling;

        ClearanceViolations clearance_violations
                = new ClearanceViolations(board_handling.get_routing_board().get_items());
        java.util.SortedSet<ViolationInfo> sorted_set = new java.util.TreeSet<>();
        for (ClearanceViolation curr_violation : clearance_violations.list) {
            sorted_set.add(new ViolationInfo(curr_violation));
        }
        for (ViolationInfo curr_violation : sorted_set) {
            this.add_to_list(curr_violation);
        }
        this.list.setVisibleRowCount(Math.min(sorted_set.size(), DEFAULT_TABLE_SIZE));
    }

    @Override
    protected void select_instances() {
        List<?> selected_violations = list.getSelectedValuesList();
        if (selected_violations.isEmpty()) {
            return;
        }
        java.util.Set<net.freerouting.freeroute.board.Item> selected_items = new java.util.TreeSet<>();
        for (Iterator<?> it = selected_violations.iterator(); it.hasNext();) {
            ViolationInfo violationInfo = (ViolationInfo) it.next();
            ClearanceViolation curr_violation = violationInfo.violation;
            selected_items.add(curr_violation.first_item);
            selected_items.add(curr_violation.second_item);
        }
        net.freerouting.freeroute.interactive.BoardHandling board_handling = board_frame.board_panel.board_handling;
        board_handling.select_items(selected_items);
        board_handling.toggle_selected_item_violations();
        board_handling.zoom_selection();
    }

    private String item_info(net.freerouting.freeroute.board.Item p_item) {
        String result;
        if (p_item instanceof net.freerouting.freeroute.board.Pin) {
            result = resources.getString("pin");
        } else if (p_item instanceof net.freerouting.freeroute.board.Via) {
            result = resources.getString("via");
        } else if (p_item instanceof net.freerouting.freeroute.board.Trace) {
            result = resources.getString("trace");
        } else if (p_item instanceof net.freerouting.freeroute.board.ConductionArea) {
            result = resources.getString("conduction_area");
        } else if (p_item instanceof net.freerouting.freeroute.board.ObstacleArea) {
            result = resources.getString("keepout");
        } else if (p_item instanceof net.freerouting.freeroute.board.ViaObstacleArea) {
            result = resources.getString("via_keepout");
        } else if (p_item instanceof net.freerouting.freeroute.board.ComponentObstacleArea) {
            result = resources.getString("component_keepout");
        } else if (p_item instanceof net.freerouting.freeroute.board.BoardOutline) {
            result = resources.getString("board_outline");
        } else {
            result = resources.getString("unknown");
        }
        return result;
    }

    private class ViolationInfo implements Comparable<ViolationInfo>, WindowObjectInfo.Printable {

        public final ClearanceViolation violation;
        public final FloatPoint location;

        ViolationInfo(ClearanceViolation p_violation) {
            this.violation = p_violation;
            FloatPoint board_location = p_violation.shape.centre_of_gravity();
            this.location = board_frame.board_panel.board_handling.coordinate_transform.board_to_user(board_location);
        }

        @Override
        public String toString() {
            net.freerouting.freeroute.board.LayerStructure layer_structure = board_frame.board_panel.board_handling.get_routing_board().layer_structure;
            String result = item_info(violation.first_item) + " - " + item_info(violation.second_item)
                    + " " + resources.getString("at") + " " + location.to_string(resources.getLocale()) + " "
                    + resources.getString("on_layer") + " " + layer_structure.get_name_layer(violation.layer_no);
            return result;
        }

        @Override
        public void print_info(net.freerouting.freeroute.board.ObjectInfoPanel p_window) {
            this.violation.print_info(p_window);
        }

        @Override
        public int compareTo(ViolationInfo p_other) {
            if (this.location.x > p_other.location.x) {
                return 1;
            }
            if (this.location.x < p_other.location.x) {
                return -1;
            }
            if (this.location.y > p_other.location.y) {
                return 1;
            }
            if (this.location.y < p_other.location.y) {
                return -1;
            }
            return this.violation.layer_no - p_other.violation.layer_no;
        }

    }
}
