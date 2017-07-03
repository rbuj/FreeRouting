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
 * BoardSelectedItemToolbar.java
 *
 * Created on 16. Februar 2005, 05:59
 */
package net.freerouting.freeroute;

import java.util.Locale;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

/**
 * Describes the toolbar of the board frame, when it is in the selected item
 * state.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
final class BoardToolbarSelectedItem extends javax.swing.JToolBar {

    private final BoardFrame board_frame;
    private final java.util.ResourceBundle resources;

    /**
     * Creates a new instance of BoardSelectedItemToolbar. If p_extended, some
     * additional buttons are generated.
     */
    BoardToolbarSelectedItem(BoardFrame p_board_frame, boolean p_extended) {
        this.board_frame = p_board_frame;

        this.resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardToolbarSelectedItem", Locale.getDefault());

        javax.swing.JButton cancel_button = new javax.swing.JButton(
                resources.getString("cancel"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/process-stop.png")));
        cancel_button.setToolTipText(resources.getString("cancel_tooltip"));
        cancel_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        cancel_button.setHorizontalTextPosition(AbstractButton.CENTER);
        cancel_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.cancel_state();
        });
        this.add(cancel_button);

        javax.swing.JButton info_button = new javax.swing.JButton(
                resources.getString("info"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/help-info.png")));
        info_button.setToolTipText(resources.getString("info_tooltip"));
        info_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        info_button.setHorizontalTextPosition(AbstractButton.CENTER);
        info_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.display_selected_item_info();
        });
        this.add(info_button);

        javax.swing.JButton delete_button = new javax.swing.JButton(
                resources.getString("delete"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/edit-delete.png")));
        delete_button.setToolTipText(resources.getString("delete_tooltip"));
        delete_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        delete_button.setHorizontalTextPosition(AbstractButton.CENTER);
        delete_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.delete_selected_items();
        });
        this.add(delete_button);

        javax.swing.JButton cutout_button = new javax.swing.JButton(
                resources.getString("cutout"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/edit-cut.png")));
        cutout_button.setToolTipText(resources.getString("cutout_tooltip"));
        cutout_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        cutout_button.setHorizontalTextPosition(AbstractButton.CENTER);
        cutout_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.cutout_selected_items();
        });
        this.add(cutout_button);

        javax.swing.JButton fix_button = new javax.swing.JButton(
                resources.getString("fix"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/emblems/emblem-readonly.png")));
        fix_button.setToolTipText(resources.getString("fix_tooltip"));
        fix_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        fix_button.setHorizontalTextPosition(AbstractButton.CENTER);
        fix_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.fix_selected_items();
        });
        this.add(fix_button);

        javax.swing.JButton unfix_button = new javax.swing.JButton(
                resources.getString("unfix"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/emblems/emblem-colors-green.png")));
        unfix_button.setToolTipText(resources.getString("unfix_tooltip"));
        unfix_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        unfix_button.setHorizontalTextPosition(AbstractButton.CENTER);
        unfix_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.unfix_selected_items();
        });
        this.add(unfix_button);

        ImageIcon system_run = new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/system-run.png"));

        javax.swing.JButton autoroute_button = new javax.swing.JButton(
                resources.getString("autoroute"), system_run);
        autoroute_button.setToolTipText(resources.getString("autoroute_tooltip"));
        autoroute_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        autoroute_button.setHorizontalTextPosition(AbstractButton.CENTER);
        autoroute_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.autoroute_selected_items();
        });
        this.add(autoroute_button);

        javax.swing.JButton tidy_button = new javax.swing.JButton(
                resources.getString("pull_tight"), system_run);
        tidy_button.setToolTipText(resources.getString("pull_tight_tooltip"));
        tidy_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        tidy_button.setHorizontalTextPosition(AbstractButton.CENTER);
        tidy_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.optimize_selected_items();
        });
        this.add(tidy_button);

        javax.swing.JButton fanout_button = new javax.swing.JButton(
                resources.getString("fanout"), system_run);
        fanout_button.setToolTipText(resources.getString("fanout_tooltip"));
        fanout_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        fanout_button.setHorizontalTextPosition(AbstractButton.CENTER);
        fanout_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.fanout_selected_items();
        });
        this.add(fanout_button);

        javax.swing.JButton clearance_class_button = new javax.swing.JButton(
                resources.getString("spacing"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/apps/ooo-calc.png")));
        clearance_class_button.setToolTipText(resources.getString("spacing_tooltip"));
        clearance_class_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        clearance_class_button.setHorizontalTextPosition(AbstractButton.CENTER);
        clearance_class_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            assign_clearance_class();
        });
        this.add(clearance_class_button);

        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        jLabel5.setMaximumSize(new java.awt.Dimension(10, 10));
        jLabel5.setPreferredSize(new java.awt.Dimension(10, 10));
        this.add(jLabel5);

        ImageIcon easytag = new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/apps/catfish.png"));

        javax.swing.JButton whole_nets_button = new javax.swing.JButton(
                resources.getString("nets"), easytag);
        whole_nets_button.setToolTipText(resources.getString("nets_tooltip"));
        whole_nets_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        whole_nets_button.setHorizontalTextPosition(AbstractButton.CENTER);
        whole_nets_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.extend_selection_to_whole_nets();
        });
        this.add(whole_nets_button);

        javax.swing.JButton whole_connected_sets_button = new javax.swing.JButton(
                resources.getString("conn_sets"), easytag);
        whole_connected_sets_button.setToolTipText(resources.getString("conn_sets_tooltip"));
        whole_connected_sets_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        whole_connected_sets_button.setHorizontalTextPosition(AbstractButton.CENTER);
        whole_connected_sets_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.extend_selection_to_whole_connected_sets();
        });
        this.add(whole_connected_sets_button);

        javax.swing.JButton whole_connections_button = new javax.swing.JButton(
                resources.getString("connections"), easytag);
        whole_connections_button.setToolTipText(resources.getString("connections_tooltip"));
        whole_connections_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        whole_connections_button.setHorizontalTextPosition(AbstractButton.CENTER);
        whole_connections_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.extend_selection_to_whole_connections();
        });
        this.add(whole_connections_button);

        javax.swing.JButton whole_groups_button = new javax.swing.JButton(
                resources.getString("components"), easytag);
        whole_groups_button.setToolTipText(resources.getString("components_tooltip"));
        whole_groups_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        whole_groups_button.setHorizontalTextPosition(AbstractButton.CENTER);
        whole_groups_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.extend_selection_to_whole_components();
        });
        this.add(whole_groups_button);

        if (p_extended) {
            ImageIcon newtag = new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/tag-new.png"));

            javax.swing.JButton new_net_button = new javax.swing.JButton(
                    resources.getString("new_net"), newtag);
            new_net_button.setToolTipText(resources.getString("new_net_tooltip"));
            new_net_button.setVerticalTextPosition(AbstractButton.BOTTOM);
            new_net_button.setHorizontalTextPosition(AbstractButton.CENTER);
            new_net_button.addActionListener((java.awt.event.ActionEvent evt) -> {
                board_frame.board_panel.board_handling.assign_selected_to_new_net();
            });
            this.add(new_net_button);

            javax.swing.JButton new_group_button = new javax.swing.JButton(
                    resources.getString("new_component"), newtag);
            new_group_button.setToolTipText(resources.getString("new_component_tooltip"));
            new_group_button.setVerticalTextPosition(AbstractButton.BOTTOM);
            new_group_button.setHorizontalTextPosition(AbstractButton.CENTER);
            new_group_button.addActionListener((java.awt.event.ActionEvent evt) -> {
                board_frame.board_panel.board_handling.assign_selected_to_new_group();
            });
            this.add(new_group_button);
        }

        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        jLabel6.setMaximumSize(new java.awt.Dimension(10, 10));
        jLabel6.setPreferredSize(new java.awt.Dimension(10, 10));
        this.add(jLabel6);

        javax.swing.JButton violation_button = new javax.swing.JButton(
                resources.getString("violations"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/status/dialog-error.png")));
        violation_button.setToolTipText(resources.getString("violations_tooltip"));
        violation_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        violation_button.setHorizontalTextPosition(AbstractButton.CENTER);
        violation_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.toggle_selected_item_violations();
        });
        this.add(violation_button);

        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        jLabel7.setMaximumSize(new java.awt.Dimension(10, 10));
        jLabel7.setPreferredSize(new java.awt.Dimension(10, 10));
        this.add(jLabel7);

        javax.swing.JButton display_selection_button = new javax.swing.JButton(
                resources.getString("zoom_selection"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/zoom-fit-best.png")));
        display_selection_button.setToolTipText(resources.getString("zoom_selection_tooltip"));
        display_selection_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        display_selection_button.setHorizontalTextPosition(AbstractButton.CENTER);
        display_selection_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.zoom_selection();
        });
        this.add(display_selection_button);

        javax.swing.JButton display_all_button = new javax.swing.JButton(
                resources.getString("zoom_all"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/view-fullscreen.png")));
        display_all_button.setToolTipText(resources.getString("zoom_all_tooltip"));
        display_all_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        display_all_button.setHorizontalTextPosition(AbstractButton.CENTER);
        display_all_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.zoom_all();
        });
        this.add(display_all_button);

        javax.swing.JButton display_region_button = new javax.swing.JButton(
                resources.getString("zoom_region"),
                new ImageIcon(BoardToolbarSelectedItem.class.getResource("/icons/32/actions/view-restore.png")));
        display_region_button.setToolTipText(resources.getString("zoom_region_tooltip"));
        display_region_button.setVerticalTextPosition(AbstractButton.BOTTOM);
        display_region_button.setHorizontalTextPosition(AbstractButton.CENTER);
        display_region_button.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.board_panel.board_handling.zoom_region();
        });
        this.add(display_region_button);
    }

    private void assign_clearance_class() {
        if (board_frame.board_panel.board_handling.is_board_read_only()) {
            return;
        }
        net.freerouting.freeroute.rules.ClearanceMatrix clearance_matrix = board_frame.board_panel.board_handling.get_routing_board().rules.clearance_matrix;
        Object[] class_name_arr = new Object[clearance_matrix.get_class_count()];
        for (int i = 0; i < class_name_arr.length; ++i) {
            class_name_arr[i] = clearance_matrix.get_name(i);
        }
        Object selected_value = javax.swing.JOptionPane.showInputDialog(null,
                resources.getString("select_clearance_class"), resources.getString("assign_clearance_class"),
                javax.swing.JOptionPane.INFORMATION_MESSAGE, null, class_name_arr, class_name_arr[0]);
        if (selected_value == null || !(selected_value instanceof String)) {
            return;
        }
        int class_index = clearance_matrix.get_no((String) selected_value);
        if (class_index < 0) {
            return;
        }
        board_frame.board_panel.board_handling.assign_clearance_classs_to_selected_items(class_index);
    }
}
