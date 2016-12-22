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
 * BoardFileMenu.java
 *
 * Created on 11. Februar 2005, 11:26
 */
package net.freerouting.freeroute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import static net.freerouting.freeroute.DesignFile.all_file_extensions;
import net.freerouting.freeroute.datastructures.FileFilter;

/**
 * Creates the file menu of a board frame.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class BoardMenuFile extends javax.swing.JMenu {

    /**
     * Returns a new file menu for the board frame.
     */
    public static BoardMenuFile get_instance(BoardFrame p_board_frame, boolean p_session_file_option) {
        final BoardMenuFile file_menu = new BoardMenuFile(p_board_frame, p_session_file_option);
        file_menu.setText(file_menu.resources.getString("file"));

        // Create the menu items.
        if (!p_session_file_option) {
            javax.swing.JMenuItem save_item = new javax.swing.JMenuItem();
            save_item.setText(file_menu.resources.getString("save"));
            save_item.setToolTipText(file_menu.resources.getString("save_tooltip"));
            save_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                boolean save_ok = file_menu.board_frame.save();
                file_menu.board_frame.board_panel.board_handling.close_files();
                if (save_ok) {
                    file_menu.board_frame.screen_messages.set_status_message(file_menu.resources.getString("save_message"));
                }
            });

            file_menu.add(save_item);
        }

        javax.swing.JMenuItem save_and_exit_item = new javax.swing.JMenuItem();
        save_and_exit_item.setText(file_menu.resources.getString("save_and_exit"));
        save_and_exit_item.setToolTipText(file_menu.resources.getString("save_and_exit_tooltip"));
        save_and_exit_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            if (file_menu.session_file_option) {
                file_menu.board_frame.design_file.write_specctra_session_file(file_menu.board_frame);
            } else {
                file_menu.board_frame.save();
            }
            file_menu.board_frame.dispose();
        });

        file_menu.add(save_and_exit_item);

        javax.swing.JMenuItem cancel_and_exit_item = new javax.swing.JMenuItem();
        cancel_and_exit_item.setText(file_menu.resources.getString("cancel_and_exit"));
        cancel_and_exit_item.setToolTipText(file_menu.resources.getString("cancel_and_exit_tooltip"));
        cancel_and_exit_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            file_menu.board_frame.dispose();
        });

        file_menu.add(cancel_and_exit_item);

        if (!file_menu.session_file_option) {
            javax.swing.JMenuItem save_as_item = new javax.swing.JMenuItem();
            save_as_item.setText(file_menu.resources.getString("save_as"));
            save_as_item.setToolTipText(file_menu.resources.getString("save_as_tooltip"));
            save_as_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                file_menu.save_as_action();
            });

            file_menu.add(save_as_item);

            javax.swing.JMenuItem write_logfile_item = new javax.swing.JMenuItem();
            write_logfile_item.setText(file_menu.resources.getString("generate_logfile"));
            write_logfile_item.setToolTipText(file_menu.resources.getString("generate_logfile_tooltip"));
            write_logfile_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                file_menu.write_logfile_action();
            });

            file_menu.add(write_logfile_item);

            javax.swing.JMenuItem replay_logfile_item = new javax.swing.JMenuItem();
            replay_logfile_item.setText(file_menu.resources.getString("replay_logfile"));
            replay_logfile_item.setToolTipText(file_menu.resources.getString("replay_logfile_tooltip"));
            replay_logfile_item.addActionListener((java.awt.event.ActionEvent evt) -> {
                file_menu.read_logfile_action();
            });

            file_menu.add(replay_logfile_item);
        }

        file_menu.add_save_settings_item();

        return file_menu;
    }
    private final BoardFrame board_frame;
    private final boolean session_file_option;
    private final java.util.ResourceBundle resources;

    /**
     * Creates a new instance of BoardFileMenu
     */
    private BoardMenuFile(BoardFrame p_board_frame, boolean p_session_file_option) {
        session_file_option = p_session_file_option;
        board_frame = p_board_frame;
        resources = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuFile", p_board_frame.get_locale());
    }

    public void add_design_dependent_items() {
        if (this.session_file_option) {
            return;
        }
        net.freerouting.freeroute.board.BasicBoard routing_board = this.board_frame.board_panel.board_handling.get_routing_board();
        boolean host_cad_is_eagle = routing_board.communication.host_cad_is_eagle();

        javax.swing.JMenuItem write_session_file_item = new javax.swing.JMenuItem();
        write_session_file_item.setText(resources.getString("session_file"));
        write_session_file_item.setToolTipText(resources.getString("session_file_tooltip"));
        write_session_file_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.design_file.write_specctra_session_file(board_frame);
        });

        if ((routing_board.get_test_level() != net.freerouting.freeroute.board.TestLevel.RELEASE_VERSION || !host_cad_is_eagle)) {
            this.add(write_session_file_item);
        }

        javax.swing.JMenuItem write_eagle_session_script_item = new javax.swing.JMenuItem();
        write_eagle_session_script_item.setText(resources.getString("eagle_script"));
        write_eagle_session_script_item.setToolTipText(resources.getString("eagle_script_tooltip"));
        write_eagle_session_script_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            board_frame.design_file.update_eagle(board_frame);
        });

        if (routing_board.get_test_level() != net.freerouting.freeroute.board.TestLevel.RELEASE_VERSION || host_cad_is_eagle) {
            this.add(write_eagle_session_script_item);
        }
    }

    /**
     * Adds a menu item for saving the current interactive settings as default.
     */
    private void add_save_settings_item() {
        javax.swing.JMenuItem save_settings_item = new javax.swing.JMenuItem();
        save_settings_item.setText(resources.getString("settings"));
        save_settings_item.setToolTipText(resources.getString("settings_tooltip"));
        save_settings_item.addActionListener((java.awt.event.ActionEvent evt) -> {
            save_defaults_action();
        });
        add(save_settings_item);
    }

    private void save_as_action() {
        if (board_frame.design_file != null) {
            final ResourceBundle resources
                    = ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuFile", board_frame.get_locale());
            String[] file_name_parts = board_frame.design_file.get_name().split("\\.", 2);
            String design_name = file_name_parts[0];

            String design_dir_name;
            if (board_frame.design_file.get_output_file() == null) {
                design_dir_name = null;
            } else {
                design_dir_name = board_frame.design_file.get_output_file().getParent();
            }
            javax.swing.JFileChooser file_chooser = new javax.swing.JFileChooser(design_dir_name);
            file_chooser.setFileFilter(new FileFilter(all_file_extensions));

            file_chooser.showSaveDialog(this);
            File new_file = file_chooser.getSelectedFile();

            board_frame.design_file.save_as(design_name, new_file, resources, board_frame);
        }
    }

    private void write_logfile_action() {
        javax.swing.JFileChooser file_chooser = new javax.swing.JFileChooser();
        java.io.File logfile_dir = board_frame.design_file.get_parent_file();
        file_chooser.setCurrentDirectory(logfile_dir);
        file_chooser.setFileFilter(BoardFrame.logfile_filter);
        file_chooser.showOpenDialog(this);
        java.io.File filename = file_chooser.getSelectedFile();
        if (filename == null) {
            board_frame.screen_messages.set_status_message(resources.getString("message_8"));
        } else {
            board_frame.screen_messages.set_status_message(resources.getString("message_9"));
            board_frame.board_panel.board_handling.start_logfile(filename);
        }
    }

    private void read_logfile_action() {
        javax.swing.JFileChooser file_chooser = new javax.swing.JFileChooser();
        java.io.File logfile_dir = board_frame.design_file.get_parent_file();
        file_chooser.setCurrentDirectory(logfile_dir);
        file_chooser.setFileFilter(BoardFrame.logfile_filter);
        file_chooser.showOpenDialog(this);
        java.io.File filename = file_chooser.getSelectedFile();
        if (filename == null) {
            board_frame.screen_messages.set_status_message(resources.getString("message_10"));
        } else {
            java.io.Reader reader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8);
            } catch (java.io.FileNotFoundException e) {
                return;
            }
            board_frame.read_logfile(reader);
        }
    }

    private void save_defaults_action() {
        java.io.OutputStream output_stream = null;
        java.io.File defaults_file = new java.io.File(board_frame.design_file.get_parent(), BoardFrame.GUI_DEFAULTS_FILE_NAME);
        if (defaults_file.exists()) {
            // Make a backup copy of the old defaulds file.
            java.io.File defaults_file_backup = new java.io.File(board_frame.design_file.get_parent(), BoardFrame.GUI_DEFAULTS_FILE_BACKUP_NAME);
            if (defaults_file_backup.exists()) {
                defaults_file_backup.delete();
            }
            defaults_file.renameTo(defaults_file_backup);
        }
        try {
            output_stream = new java.io.FileOutputStream(defaults_file);
        } catch (FileNotFoundException e) {
            output_stream = null;
        }
        boolean write_ok;
        if (output_stream == null) {
            write_ok = false;
        } else {
            write_ok = net.freerouting.freeroute.GUIDefaultsFile.write(board_frame, board_frame.board_panel.board_handling, output_stream);
        }
        if (write_ok) {
            board_frame.screen_messages.set_status_message(resources.getString("message_17"));
        } else {
            board_frame.screen_messages.set_status_message(resources.getString("message_18"));
        }

    }

}
