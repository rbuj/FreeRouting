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
 * WindowAutorouteParameter.java
 *
 * Created on 24. Juli 2006, 07:20
 *
 */
package net.freerouting.freeroute;

import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import net.freerouting.freeroute.board.LayerStructure;

/**
 * Window handling parameters of the automatic routing.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public final class WindowAutorouteParameter extends BoardSavableSubWindow {

    private final net.freerouting.freeroute.interactive.BoardHandling board_handling;
    private final ArrayList<JLabel> signal_layer_name_arr;
    private final ArrayList<JCheckBox> signal_layer_active_arr;
    private final ArrayList<JComboBox<String>> combo_box_arr;
    private final JCheckBox vias_allowed;
    private final JRadioButton fanout_pass_button;
    private final JRadioButton autoroute_pass_button;
    private final JRadioButton postroute_pass_button;
    private final WindowAutorouteDetailParameter detail_window;
    private final DetailListener detail_listener;
    private final String horizontal;
    private final String vertical;

    /**
     * Creates a new instance of WindowAutorouteParameter
     */
    WindowAutorouteParameter(BoardFrame p_board_frame) {
        this.board_handling = p_board_frame.board_panel.board_handling;
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.WindowAutorouteParameter", Locale.getDefault());
        this.setTitle(resources.getString("title"));

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // create main panel
        final javax.swing.JPanel main_panel = new javax.swing.JPanel();
        getContentPane().add(main_panel);
        java.awt.GridBagLayout gridbag = new java.awt.GridBagLayout();
        main_panel.setLayout(gridbag);
        java.awt.GridBagConstraints gridbag_constraints = new java.awt.GridBagConstraints();
        gridbag_constraints.anchor = java.awt.GridBagConstraints.WEST;
        gridbag_constraints.insets = new java.awt.Insets(1, 10, 1, 10);

        gridbag_constraints.gridwidth = 3;
        JLabel layer_label = new JLabel(resources.getString("layer"));
        gridbag.setConstraints(layer_label, gridbag_constraints);
        main_panel.add(layer_label);

        JLabel active_label = new JLabel(resources.getString("active"));
        gridbag.setConstraints(active_label, gridbag_constraints);
        main_panel.add(active_label);

        gridbag_constraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        JLabel preferred_direction_label = new JLabel(resources.getString("preferred_direction"));
        gridbag.setConstraints(preferred_direction_label, gridbag_constraints);
        main_panel.add(preferred_direction_label);

        horizontal = resources.getString("horizontal");
        vertical = resources.getString("vertical");

        LayerStructure layer_structure = board_handling.get_routing_board().layer_structure;
        int signal_layer_count = layer_structure.signal_layer_count();
        signal_layer_name_arr = new ArrayList<>(signal_layer_count);
        signal_layer_active_arr = new ArrayList<>(signal_layer_count);
        combo_box_arr = new ArrayList<>(signal_layer_count);
        String[] jComboBoxItems = {horizontal, vertical};

        for (int i = 0; i < signal_layer_count; ++i) {
            net.freerouting.freeroute.board.Layer curr_signal_layer = layer_structure.get_signal_layer(i);

            JLabel jLabel = new JLabel(curr_signal_layer.name);
            gridbag_constraints.gridwidth = 3;
            gridbag.setConstraints(jLabel, gridbag_constraints);
            main_panel.add(jLabel);
            signal_layer_name_arr.add(i, jLabel);

            JCheckBox jCheckBox = new JCheckBox();
            jCheckBox.addActionListener(new LayerActiveListener(i));
            gridbag.setConstraints(jCheckBox, gridbag_constraints);
            main_panel.add(jCheckBox);
            signal_layer_active_arr.add(i, jCheckBox);

            JComboBox<String> jComboBox = new JComboBox<>(jComboBoxItems);
            jComboBox.addActionListener(new PreferredDirectionListener(i));
            gridbag_constraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
            gridbag.setConstraints(jComboBox, gridbag_constraints);
            main_panel.add(jComboBox);
            combo_box_arr.add(i, jComboBox);
        }

        JLabel separator = new JLabel("----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        JLabel vias_allowed_label = new JLabel(resources.getString("vias_allowed"));
        gridbag_constraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridbag.setConstraints(vias_allowed_label, gridbag_constraints);
        main_panel.add(vias_allowed_label);

        this.vias_allowed = new JCheckBox();
        this.vias_allowed.addActionListener(new ViasAllowedListener());
        gridbag_constraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridbag.setConstraints(vias_allowed, gridbag_constraints);
        main_panel.add(vias_allowed);

        separator = new JLabel("----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        JLabel passes_label = new JLabel(resources.getString("passes"));
        gridbag_constraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridbag_constraints.gridheight = 3;
        gridbag.setConstraints(passes_label, gridbag_constraints);
        main_panel.add(passes_label);

        fanout_pass_button = new JRadioButton(resources.getString("fanout"));
        fanout_pass_button.addActionListener(new FanoutListener());
        fanout_pass_button.setSelected(false);
        gridbag_constraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridbag_constraints.gridheight = 1;
        gridbag.setConstraints(fanout_pass_button, gridbag_constraints);
        main_panel.add(fanout_pass_button, gridbag_constraints);

        autoroute_pass_button = new JRadioButton(resources.getString("autoroute"));
        autoroute_pass_button.addActionListener(new AutorouteListener());
        autoroute_pass_button.setSelected(true);
        gridbag.setConstraints(autoroute_pass_button, gridbag_constraints);
        main_panel.add(autoroute_pass_button, gridbag_constraints);

        postroute_pass_button = new JRadioButton(resources.getString("postroute"));
        postroute_pass_button.addActionListener(new PostrouteListener());
        autoroute_pass_button.setSelected(true);
        gridbag.setConstraints(postroute_pass_button, gridbag_constraints);
        main_panel.add(postroute_pass_button, gridbag_constraints);

        separator = new JLabel("----------------------------------------  ");
        gridbag.setConstraints(separator, gridbag_constraints);
        main_panel.add(separator, gridbag_constraints);

        detail_window = new WindowAutorouteDetailParameter(p_board_frame);
        javax.swing.JButton detail_button = new javax.swing.JButton(resources.getString("detail_parameter"));
        this.detail_listener = new DetailListener();
        detail_button.addActionListener(detail_listener);
        gridbag.setConstraints(detail_button, gridbag_constraints);

        main_panel.add(detail_button);

        p_board_frame.set_context_sensitive_help(this, "WindowAutorouteParameter");

        this.refresh();
        this.pack();
        this.setResizable(false);
    }

    /**
     * Recalculates all displayed values
     */
    @Override
    public void refresh() {
        net.freerouting.freeroute.interactive.AutorouteSettings settings = this.board_handling.settings.autoroute_settings;
        net.freerouting.freeroute.board.LayerStructure layer_structure = this.board_handling.get_routing_board().layer_structure;

        this.vias_allowed.setSelected(settings.get_vias_allowed());
        this.fanout_pass_button.setSelected(settings.get_with_fanout());
        this.autoroute_pass_button.setSelected(settings.get_with_autoroute());
        this.postroute_pass_button.setSelected(settings.get_with_postroute());

        for (JCheckBox jCheckBox : signal_layer_active_arr) {
            jCheckBox.setSelected(settings.get_layer_active(layer_structure.get_layer_no(signal_layer_active_arr.indexOf(jCheckBox))));
        }

        for (JComboBox<String> jComboBox : combo_box_arr) {
            if (settings.get_preferred_direction_is_horizontal(layer_structure.get_layer_no(combo_box_arr.indexOf(jComboBox)))) {
                jComboBox.setSelectedItem(this.horizontal);
            } else {
                jComboBox.setSelectedItem(this.vertical);
            }
        }
        this.detail_window.refresh();
    }

    @Override
    public void dispose() {
        detail_window.dispose();
        super.dispose();
    }

    @Override
    public void parent_iconified() {
        detail_window.parent_iconified();
        super.parent_iconified();
    }

    @Override
    public void parent_deiconified() {
        detail_window.parent_deiconified();
        super.parent_deiconified();
    }

    private class DetailListener implements java.awt.event.ActionListener {

        private boolean first_time = true;

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            if (first_time) {
                java.awt.Point location = getLocation();
                detail_window.setLocation((int) location.getX() + 200, (int) location.getY() + 100);
                first_time = false;
            }
            detail_window.setVisible(true);
        }
    }

    private class LayerActiveListener implements java.awt.event.ActionListener {

        private final int signal_layer_no;

        LayerActiveListener(int p_layer_no) {
            signal_layer_no = p_layer_no;
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            int curr_layer_no = board_handling.get_routing_board().layer_structure.get_layer_no(this.signal_layer_no);
            board_handling.settings.autoroute_settings.set_layer_active(curr_layer_no, signal_layer_active_arr.get(this.signal_layer_no).isSelected());
        }
    }

    private class PreferredDirectionListener implements java.awt.event.ActionListener {

        private final int signal_layer_no;

        PreferredDirectionListener(int p_layer_no) {
            signal_layer_no = p_layer_no;
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            int curr_layer_no = board_handling.get_routing_board().layer_structure.get_layer_no(this.signal_layer_no);
            board_handling.settings.autoroute_settings.set_preferred_direction_is_horizontal(curr_layer_no,
                    combo_box_arr.get(signal_layer_no).getSelectedItem().equals(horizontal));
        }
    }

    private class ViasAllowedListener implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            board_handling.settings.autoroute_settings.set_vias_allowed(vias_allowed.isSelected());
        }
    }

    private class FanoutListener implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            net.freerouting.freeroute.interactive.AutorouteSettings autoroute_settings = board_handling.settings.autoroute_settings;
            autoroute_settings.set_with_fanout(fanout_pass_button.isSelected());
            autoroute_settings.set_pass_no(1);
        }
    }

    private class AutorouteListener implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            net.freerouting.freeroute.interactive.AutorouteSettings autoroute_settings = board_handling.settings.autoroute_settings;
            autoroute_settings.set_with_autoroute(autoroute_pass_button.isSelected());
            autoroute_settings.set_pass_no(1);
        }
    }

    private class PostrouteListener implements java.awt.event.ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent p_evt) {
            net.freerouting.freeroute.interactive.AutorouteSettings autoroute_settings = board_handling.settings.autoroute_settings;
            autoroute_settings.set_with_postroute(postroute_pass_button.isSelected());
            autoroute_settings.set_pass_no(1);
        }
    }
}
