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
 * PopupMenuDisplay.java
 *
 * Created on 22. Mai 2005, 09:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package net.freerouting.freeroute;

import java.util.Locale;

/**
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class PopupMenuDisplay extends javax.swing.JPopupMenu {

    protected final BoardPanel board_panel;

    /**
     * Creates a new instance of PopupMenuDisplay
     */
    PopupMenuDisplay(BoardFrame p_board_frame) {
        this.board_panel = p_board_frame.board_panel;
        initializeComponents();
    }

    private void initializeComponents() {
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.Default", Locale.getDefault());

        javax.swing.JMenuItem center_display_item = new javax.swing.JMenuItem();
        center_display_item.setText(resources.getString("center_display"));
        center_display_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_panel.center_display();
        });
        this.add(center_display_item);

        javax.swing.JMenu zoom_menu = new javax.swing.JMenu();
        zoom_menu.setText(resources.getString("zoom"));
        for (Zoom zoom : Zoom.values()) {
            javax.swing.JMenuItem zoom_item = new javax.swing.JMenuItem();
            zoom_item.setText(resources.getString(zoom.toString()));
            zoom_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                board_panel.zoom_in_out(zoom);
            });
            zoom_menu.add(zoom_item);
        }
        this.add(zoom_menu);
    }
}
