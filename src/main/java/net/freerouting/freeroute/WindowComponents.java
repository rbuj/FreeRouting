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
 * ComponentsWindow.java
 *
 * Created on 8. Maerz 2005, 05:56
 */
package net.freerouting.freeroute;

import java.util.List;
import java.util.Locale;
import net.freerouting.freeroute.board.Component;
import net.freerouting.freeroute.board.Components;

/**
 * Window displaying the components on the board.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public final class WindowComponents extends WindowObjectListWithFilter {

    /**
     * Creates a new instance of ComponentsWindow
     */
    WindowComponents(BoardFrame p_board_frame) {
        super(p_board_frame);
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.Default", Locale.getDefault());
        this.setTitle(resources.getString("components"));
        p_board_frame.set_context_sensitive_help(this, "WindowObjectList_BoardComponents");
    }

    /**
     * Fills the list with the board components.
     */
    @Override
    void fill_list() {
        Components components = this.board_frame.board_panel.board_handling.get_routing_board().components;
        Component[] sorted_arr = new Component[components.count()];
        for (int i = 0; i < sorted_arr.length; ++i) {
            sorted_arr[i] = components.get(i + 1);
        }
        java.util.Arrays.parallelSort(sorted_arr);
        for (int i = 0; i < sorted_arr.length; ++i) {
            this.add_to_list(sorted_arr[i]);
        }
        this.list.setVisibleRowCount(Math.min(components.count(), DEFAULT_TABLE_SIZE));
    }

    @Override
    void select_instances() {
        List<?> selected_components = list.getSelectedValuesList();
        if (selected_components.isEmpty()) {
            return;
        }
        net.freerouting.freeroute.board.RoutingBoard routing_board = board_frame.board_panel.board_handling.get_routing_board();
        java.util.Set<net.freerouting.freeroute.board.Item> selected_items = new java.util.TreeSet<>();
        java.util.Collection<net.freerouting.freeroute.board.Item> board_items = routing_board.get_items();
        for (net.freerouting.freeroute.board.Item curr_item : board_items) {
            if (curr_item.get_component_no() > 0) {
                net.freerouting.freeroute.board.Component curr_component = routing_board.components.get(curr_item.get_component_no());
                if (selected_components.contains(curr_component)) {
                    selected_items.add(curr_item);
                }
            }
        }
        board_frame.board_panel.board_handling.select_items(selected_items);
        board_frame.board_panel.board_handling.zoom_selection();
    }
}
