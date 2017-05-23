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
 * BoardLibraryMenu.java
 *
 * Created on 6. Maerz 2005, 05:37
 */
package net.freerouting.freeroute;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.Map.entry;

/**
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class BoardMenuInfo extends javax.swing.JMenu {

    private final BoardFrame board_frame;
    private final java.util.ResourceBundle resources;

    /**
     * Creates a new instance of BoardLibraryMenu
     */
    private BoardMenuInfo(BoardFrame p_board_frame) {
        board_frame = p_board_frame;
        resources = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuInfo", Locale.getDefault());
    }

    /**
     * Returns a new info menu for the board frame.
     */
    public static BoardMenuInfo get_instance(BoardFrame p_board_frame) {
        BoardMenuInfo info_menu = new BoardMenuInfo(p_board_frame);

        info_menu.setText(info_menu.resources.getString("info"));

        Map<BoardFrame.SAVABLE_SUBWINDOW_KEY, String> menu_items = Map.ofEntries(
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.PACKAGES, "library_packages"),
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.PADSTACKS, "library_padstacks"),
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.COMPONENTS, "board_components"),
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.INCOMPLETES, "incompletes"),
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.LENGHT_VIOLATIONS, "length_violations"),
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.CLEARANCE_VIOLATIONS, "clearance_violations"),
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.UNCONNECTED_ROUTE, "unconnected_route"),
                entry(BoardFrame.SAVABLE_SUBWINDOW_KEY.ROUTE_STUBS, "route_stubs"));

        for (Entry<BoardFrame.SAVABLE_SUBWINDOW_KEY, String> entry : menu_items.entrySet()) {
            javax.swing.JMenuItem menu_item = new javax.swing.JMenuItem();
            menu_item.setText(info_menu.resources.getString(entry.getValue()));
            menu_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                info_menu.board_frame.savable_subwindows.get(entry.getKey()).setVisible(true);
            });
            info_menu.add(menu_item);
        }

        return info_menu;
    }
}
