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
 * BoardWindowsMenu.java
 *
 * Created on 12. Februar 2005, 06:08
 */
package net.freerouting.freeroute;

import java.util.Locale;
import java.util.Map;
import static java.util.Map.entry;
import java.util.ResourceBundle;
import net.freerouting.freeroute.SavableSubwindows.SAVABLE_SUBWINDOW_KEY;

/**
 * Creates the parameter menu of a board frame.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
final class BoardMenuParameter extends javax.swing.JMenu {

    private final BoardFrame board_frame;
    private final ResourceBundle resources;

    /**
     * Creates a new instance of BoardSelectMenu
     */
    private BoardMenuParameter(BoardFrame p_board_frame) {
        board_frame = p_board_frame;
        resources = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuParameter", Locale.getDefault());
    }

    /**
     * Returns a new windows menu for the board frame.
     */
    public static BoardMenuParameter get_instance(BoardFrame p_board_frame) {
        BoardMenuParameter parameter_menu = new BoardMenuParameter(p_board_frame);

        parameter_menu.setText(parameter_menu.resources.getString("parameter"));

        Map<SAVABLE_SUBWINDOW_KEY, String> menu_items = Map.ofEntries(
                entry(SAVABLE_SUBWINDOW_KEY.SELECT_PARAMETER, "select"),
                entry(SAVABLE_SUBWINDOW_KEY.ROUTE_PARAMETER, "route"),
                entry(SAVABLE_SUBWINDOW_KEY.AUTOROUTE_PARAMETER, "autoroute"),
                entry(SAVABLE_SUBWINDOW_KEY.MOVE_PARAMETER, "move"));

        for (Map.Entry<SAVABLE_SUBWINDOW_KEY, String> entry : menu_items.entrySet()) {
            javax.swing.JMenuItem menu_item = new javax.swing.JMenuItem();
            menu_item.setText(parameter_menu.resources.getString(entry.getValue()));
            menu_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                parameter_menu.board_frame.savable_subwindows.get(entry.getKey()).setVisible(true);
            });
            parameter_menu.add(menu_item);
        }

        return parameter_menu;
    }
}
