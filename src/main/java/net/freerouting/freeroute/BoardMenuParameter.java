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

/**
 * Creates the parameter menu of a board frame.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
final class BoardMenuParameter extends BoardMenu {

    /**
     * Returns a new windows menu for the board frame.
     */
    static BoardMenuParameter get_instance(BoardFrame p_board_frame) {
        BoardMenuParameter parameter_menu = new BoardMenuParameter(p_board_frame);
        java.util.ResourceBundle resources = java.util.ResourceBundle.getBundle(
                BoardMenuParameter.class.getPackageName() + ".resources.BoardMenuParameter",
                Locale.getDefault());

        parameter_menu.setText(resources.getString("parameter"));

        Map<SavableSubwindowKey, String> menu_items = Map.ofEntries(
                entry(SavableSubwindowKey.SELECT_PARAMETER, "select"),
                entry(SavableSubwindowKey.ROUTE_PARAMETER, "route"),
                entry(SavableSubwindowKey.AUTOROUTE_PARAMETER, "autoroute"),
                entry(SavableSubwindowKey.MOVE_PARAMETER, "move"));

        for (Map.Entry<SavableSubwindowKey, String> entry : menu_items.entrySet()) {
            javax.swing.JMenuItem menu_item = new javax.swing.JMenuItem();
            menu_item.setText(resources.getString(entry.getValue()));
            menu_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                parameter_menu.board_frame.savable_subwindows.get(entry.getKey()).setVisible(true);
            });
            parameter_menu.add(menu_item);
        }

        return parameter_menu;
    }

    /**
     * Creates a new instance of BoardSelectMenu
     */
    private BoardMenuParameter(BoardFrame p_board_frame) {
        super(p_board_frame);
    }
}
