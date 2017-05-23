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
 * WindowSnapshotSettings.java
 *
 * Created on 17. September 2005, 07:23
 *
 */
package net.freerouting.freeroute;

import java.util.EnumMap;
import java.util.Locale;
import javax.swing.JCheckBox;

/**
 * Window for the settinngs of interactive snapshots.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class WindowSnapshotSettings extends BoardSavableSubWindow {

    private final net.freerouting.freeroute.interactive.BoardHandling board_handling;

    EnumMap<SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY, javax.swing.JCheckBox> snapshot_attributes_map;

    /**
     * Creates a new instance of WindowSnapshotSettings
     */
    public WindowSnapshotSettings(BoardFrame p_board_frame) {
        this.board_handling = p_board_frame.board_panel.board_handling;

        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.WindowSnapshotSettings", Locale.getDefault());
        this.setTitle(resources.getString("title"));

        // create main panel
        final javax.swing.JPanel main_panel = new javax.swing.JPanel();
        getContentPane().add(main_panel);
        java.awt.GridBagLayout gridbag = new java.awt.GridBagLayout();
        main_panel.setLayout(gridbag);
        java.awt.GridBagConstraints gridbag_constraints = new java.awt.GridBagConstraints();
        gridbag_constraints.anchor = java.awt.GridBagConstraints.WEST;
        gridbag_constraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridbag_constraints.insets = new java.awt.Insets(1, 10, 1, 10);

        snapshot_attributes_map = new EnumMap<>(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.class);
        for (SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY key : SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.values()) {
            JCheckBox check_box = new JCheckBox(resources.getString(key.name().toLowerCase(Locale.ENGLISH)));
            gridbag.setConstraints(check_box, gridbag_constraints);
            check_box.setActionCommand(key.name());
            snapshot_attributes_map.put(key, check_box);
        }

        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.OBJECT_COLORS), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.OBJECT_VISIBILITY), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.LAYER_VISIBILITY), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.DISPLAY_REGION), gridbag_constraints);

        javax.swing.JLabel separator = new javax.swing.JLabel("  ----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.INTERACTIVE_STATE), gridbag_constraints);

        separator = new javax.swing.JLabel("  ----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.SELECTION_LAYERS), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.SELECTABLE_ITEMS), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.CURRENT_LAYER), gridbag_constraints);

        separator = new javax.swing.JLabel("  ----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.RULE_SELECTION), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.MANUAL_RULE_SETTINGS), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.PUSH_AND_SHOVE_ENABLED), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.DRAG_COMPONENTS_ENABLED), gridbag_constraints);
        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.PULL_TIGHT_REGION), gridbag_constraints);

        separator = new javax.swing.JLabel("  ----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.COMPONENT_GRID), gridbag_constraints);

        separator = new javax.swing.JLabel("  ----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        main_panel.add(snapshot_attributes_map.get(SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.INFO_LIST_SELECTIONS), gridbag_constraints);

        p_board_frame.set_context_sensitive_help(this, "WindowSnapshots_SnapshotSettings");

        CheckBoxListener checkBoxListener = new CheckBoxListener();
        for (SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY key : SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.values()) {
            snapshot_attributes_map.get(key).addActionListener(checkBoxListener);
        }

        this.refresh();
        this.pack();
        this.setResizable(false);
    }

    /**
     * Recalculates all displayed values
     */
    @Override
    public void refresh() {
        SnapshotAttributes snapshot_attributes = this.board_handling.settings.get_snapshot_attributes();
        for (SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY key : SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.values()) {
            snapshot_attributes_map.get(key).setSelected(snapshot_attributes.get(key));
        }
    }

    private class CheckBoxListener implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY key
                    = SnapshotAttributes.SNAPSHOT_ATTRIBUTE_KEY.valueOf(p_evt.getActionCommand());
            board_handling.settings.get_snapshot_attributes().set(
                    key, snapshot_attributes_map.get(key).isSelected());
        }
    }
}
