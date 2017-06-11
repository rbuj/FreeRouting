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
 * PopupMenuMove.java
 *
 * Created on 15. Mai 2005, 11:21
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package net.freerouting.freeroute;

import java.util.Locale;
import java.util.Map;
import static java.util.Map.entry;

/**
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class PopupMenuMove extends PopupMenuDisplay {

    /**
     * Creates a new instance of PopupMenuMove
     */
    PopupMenuMove(BoardFrame p_board_frame) {
        super(p_board_frame);
        initializeComponents();
    }

    private void initializeComponents() {
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.PopupMenuMove", Locale.getDefault());

        // Add menu for turning the items by a multiple of 90 degree
        javax.swing.JMenuItem rotate_menu = new javax.swing.JMenu();
        rotate_menu.setText(resources.getString("turn"));
        Map<String, Integer> menu_rotate_items = Map.ofEntries(
                entry("90_degree", 2),
                entry("180_degree", 4),
                entry("-90_degree", 6),
                entry("45_degree", 1),
                entry("135_degree", 3),
                entry("-135_degree", 5),
                entry("-45_degree", 7));
        for (Map.Entry<String, Integer> entry : menu_rotate_items.entrySet()) {
            javax.swing.JMenuItem menu_item = new javax.swing.JMenuItem();
            menu_item.setText(resources.getString(entry.getKey()));
            menu_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                turn_45_degree(entry.getValue());
            });
            rotate_menu.add(menu_item);
        }
        this.add(rotate_menu, 0);

        javax.swing.JMenuItem change_side_item = new javax.swing.JMenuItem();
        change_side_item.setText(resources.getString("change_side"));
        change_side_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_panel.board_handling.change_placement_side();
        });

        this.add(change_side_item, 1);

        javax.swing.JMenuItem reset_rotation_item = new javax.swing.JMenuItem();
        reset_rotation_item.setText(resources.getString("reset_rotation"));
        reset_rotation_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            net.freerouting.freeroute.interactive.InteractiveState interactive_state = board_panel.board_handling.get_interactive_state();
            if (interactive_state instanceof net.freerouting.freeroute.interactive.MoveItemState) {
                ((net.freerouting.freeroute.interactive.MoveItemState) interactive_state).reset_rotation();
            }
        });

        this.add(reset_rotation_item, 2);

        javax.swing.JMenuItem insert_item = new javax.swing.JMenuItem();
        insert_item.setText(resources.getString("insert"));
        insert_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_panel.board_handling.return_from_state();
        });

        this.add(insert_item, 3);

        javax.swing.JMenuItem cancel_item = new javax.swing.JMenuItem();
        cancel_item.setText(resources.getString("cancel"));
        cancel_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_panel.board_handling.cancel_state();
        });

        this.add(cancel_item, 4);
    }

    private void turn_45_degree(int p_factor) {
        board_panel.board_handling.turn_45_degree(p_factor);
        board_panel.move_mouse();
    }
}
