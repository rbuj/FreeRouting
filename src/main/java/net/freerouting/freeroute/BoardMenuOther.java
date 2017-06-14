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
 * BoardMenuOther.java
 *
 * Created on 19. Oktober 2005, 08:34
 *
 */
package net.freerouting.freeroute;

import java.util.Locale;

/**
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
final class BoardMenuOther extends BoardMenu {

    /**
     * Returns a new other menu for the board frame.
     */
    static BoardMenuOther get_instance(BoardFrame p_board_frame) {
        BoardMenuOther other_menu = new BoardMenuOther(p_board_frame);
        java.util.ResourceBundle resources = java.util.ResourceBundle.getBundle(
                BoardMenuOther.class.getPackageName() + ".resources.BoardMenuOther",
                Locale.getDefault());

        other_menu.setText(resources.getString("other"));

        javax.swing.JMenuItem snapshots = new javax.swing.JMenuItem();
        snapshots.setText(resources.getString("snapshots"));
        snapshots.setToolTipText(resources.getString("snapshots_tooltip"));
        snapshots.addActionListener((java.awt.event.ActionEvent evt) -> {
            other_menu.board_frame.savable_subwindows.get(SavableSubwindowKey.SNAPSHOT).setVisible(true);
        });

        other_menu.add(snapshots);

        return other_menu;
    }

    /**
     * Creates a new instance of BoardMenuOther
     */
    private BoardMenuOther(BoardFrame p_board_frame) {
        super(p_board_frame);
    }
}
