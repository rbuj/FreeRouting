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
 * PadstacksWindow.java
 *
 * Created on 6. Maerz 2005, 06:47
 */
package net.freerouting.freeroute;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.freerouting.freeroute.datastructures.UndoableObjects;
import net.freerouting.freeroute.library.Padstack;
import net.freerouting.freeroute.library.Padstacks;

/**
 * Window displaying the library padstacks.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public final class WindowPadstacks extends WindowObjectListWithFilter {

    /**
     * Creates a new instance of PadstacksWindow
     */
    WindowPadstacks(BoardFrame p_board_frame) {
        super(p_board_frame);
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.Default", Locale.getDefault());
        this.setTitle(resources.getString("padstacks"));
        p_board_frame.set_context_sensitive_help(this, "WindowObjectList_LibraryPadstacks");
    }

    /**
     * Fills the list with the library padstacks.
     */
    @Override
    void fill_list() {
        Padstacks padstacks = this.board_frame.board_panel.board_handling.get_routing_board().library.padstacks;
        Padstack[] sorted_arr = new Padstack[padstacks.count()];
        for (int i = 0; i < sorted_arr.length; ++i) {
            sorted_arr[i] = padstacks.get(i + 1);
        }
        java.util.Arrays.parallelSort(sorted_arr);
        for (int i = 0; i < sorted_arr.length; ++i) {
            this.add_to_list(sorted_arr[i]);
        }
        this.list.setVisibleRowCount(Math.min(padstacks.count(), DEFAULT_TABLE_SIZE));
    }

    @Override
    void select_instances() {
        List<?> selected_padstacks = list.getSelectedValuesList();
        if (selected_padstacks.isEmpty()) {
            return;
        }
        java.util.Collection<Padstack> padstack_list = new java.util.LinkedList<>();
        for (Iterator<?> it = selected_padstacks.iterator(); it.hasNext();) {
            Padstack selected_pad = (Padstack) it.next();
            padstack_list.add(selected_pad);
        }
        net.freerouting.freeroute.board.RoutingBoard routing_board = board_frame.board_panel.board_handling.get_routing_board();
        java.util.Set<net.freerouting.freeroute.board.Item> board_instances = new java.util.TreeSet<>();
        java.util.Iterator<UndoableObjects.UndoableObjectNode> it = routing_board.item_list.start_read_object();
        for (;;) {
            net.freerouting.freeroute.datastructures.UndoableObjects.Storable curr_object = routing_board.item_list.read_object(it);
            if (curr_object == null) {
                break;
            }
            if (curr_object instanceof net.freerouting.freeroute.board.DrillItem) {
                net.freerouting.freeroute.library.Padstack curr_padstack = ((net.freerouting.freeroute.board.DrillItem) curr_object).get_padstack();
                for (Padstack curr_selected_padstack : padstack_list) {
                    if (curr_padstack == curr_selected_padstack) {
                        board_instances.add((net.freerouting.freeroute.board.Item) curr_object);
                        break;
                    }
                }
            }
        }
        board_frame.board_panel.board_handling.select_items(board_instances);
        board_frame.board_panel.board_handling.zoom_selection();
    }
}
