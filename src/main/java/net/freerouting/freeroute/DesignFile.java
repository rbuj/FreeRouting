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
 * DesignFile.java
 *
 * Created on 25. Oktober 2006, 07:48
 *
 */
package net.freerouting.freeroute;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.freerouting.freeroute.datastructures.FileFilter;

/**
 * File functionality with security restrictions used, when the application is
 * opened with Java Webstart
 *
 * @author Alfons Wirtz
 */
public class DesignFile {

    protected static final String[] all_file_extensions = new String[]{"bin", "dsn"};
    protected static final String[] text_file_extensions = new String[]{"dsn"};
    protected static final String binary_file_extension = "bin";
    protected static final String RULES_FILE_EXTENSION = ".rules";

    private File output_file;
    private File input_file;
    private String design_dir_name;

    private static final FileFilter file_filter = new FileFilter(all_file_extensions);
    private javax.swing.JFileChooser file_chooser;

    DesignFile(File p_design_file, String p_design_dir_name) {
        if (p_design_file != null) {
            input_file = p_design_file;
            String file_name = p_design_file.getName();
            String[] name_parts = file_name.split("\\.");
            if (name_parts[name_parts.length - 1].compareToIgnoreCase(binary_file_extension) != 0) {
                String binfile_name = name_parts[0] + "." + binary_file_extension;
                output_file = new File(p_design_file.getParent(), binfile_name);
            } else {
                output_file = null;
            }
        } else {
            input_file = null;
            output_file = null;
        }
        if (p_design_dir_name != null) {
            design_dir_name = p_design_dir_name;
        } else if (input_file != null) {
            design_dir_name = input_file.getParent();
        } else {
            design_dir_name = null;
        }
    }

    DesignFile(File p_design_file) {
        this.input_file = p_design_file;
        this.output_file = p_design_file;
        if (p_design_file != null) {
            String file_name = p_design_file.getName();
            String[] name_parts = file_name.split("\\.");
            if (name_parts[name_parts.length - 1].compareToIgnoreCase(binary_file_extension) != 0) {
                String binfile_name = name_parts[0] + "." + binary_file_extension;
                this.output_file = new File(p_design_file.getParent(), binfile_name);
            }
        }
    }

    public static DesignFile get_instance(String p_design_file_name) {
        if (p_design_file_name == null) {
            return null;
        }
        return new DesignFile(new File(p_design_file_name));
    }

    public static boolean read_rules_file(String p_design_name, String p_parent_name,
            net.freerouting.freeroute.interactive.BoardHandling p_board_handling, String p_confirm_message) {
        boolean result = true;
        String rule_file_name = p_design_name + ".rules";
        boolean dsn_file_generated_by_host = p_board_handling.get_routing_board().communication.specctra_parser_info.dsn_file_generated_by_host;
        File rules_file = new File(p_parent_name, rule_file_name);
        try (InputStream input_stream = new FileInputStream(rules_file)) {
            if (input_stream != null && dsn_file_generated_by_host && WindowMessage.confirm(p_confirm_message)) {
                result = net.freerouting.freeroute.designformats.specctra.RulesFile.read(input_stream, p_design_name, p_board_handling);
                rules_file.delete();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /**
     * Gets an InputStream from the file. Returns null, if the algorithm failed.
     */
    public InputStream get_input_stream() {
        InputStream result;
        if (this.input_file == null) {
            return null;
        }
        try {
            result = new java.io.FileInputStream(this.input_file);
        } catch (FileNotFoundException e) {
            result = null;
        }
        return result;
    }

    /**
     * Gets the file name as a String. Returns null on failure.
     */
    public String get_name() {

        String result;
        if (this.input_file != null) {
            result = this.input_file.getName();
        } else {
            result = null;
        }
        return result;
    }

    public void save_as_dialog(java.awt.Component p_parent, BoardFrame p_board_frame) {
        final java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuFile", p_board_frame.get_locale());
        String[] file_name_parts = this.get_name().split("\\.", 2);
        String design_name = file_name_parts[0];

        if (this.file_chooser == null) {
            String design_dir_name;
            if (this.output_file == null) {
                design_dir_name = null;
            } else {
                design_dir_name = this.output_file.getParent();
            }
            this.file_chooser = new javax.swing.JFileChooser(design_dir_name);
            this.file_chooser.setFileFilter(file_filter);
        }

        this.file_chooser.showSaveDialog(p_parent);
        File new_file = file_chooser.getSelectedFile();
        if (new_file == null) {
            p_board_frame.screen_messages.set_status_message(resources.getString("message_1"));
            return;
        }
        String new_file_name = new_file.getName();
        String[] new_name_parts = new_file_name.split("\\.");
        String found_file_extension = new_name_parts[new_name_parts.length - 1];
        if (found_file_extension.compareToIgnoreCase(binary_file_extension) == 0) {
            p_board_frame.screen_messages.set_status_message(resources.getString("message_2") + " " + new_file.getName());
            this.output_file = new_file;
            p_board_frame.save();
        } else {
            if (found_file_extension.compareToIgnoreCase("dsn") != 0) {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_3"));
                return;
            }
            try (OutputStream output_stream = new FileOutputStream(new_file)) {
                if (p_board_frame.board_panel.board_handling.export_to_dsn_file(output_stream, design_name, false)) {
                    p_board_frame.screen_messages.set_status_message(resources.getString("message_4") + " " + new_file_name + " " + resources.getString("message_5"));
                } else {
                    p_board_frame.screen_messages.set_status_message(resources.getString("message_6") + " " + new_file_name + " " + resources.getString("message_7"));
                }
            } catch (IOException ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Writes a Specctra Session File to update the design file in the host
     * system. Returns false, if the write failed
     */
    public boolean write_specctra_session_file(BoardFrame p_board_frame) {
        final java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuFile", p_board_frame.get_locale());
        String design_file_name = this.get_name();
        String[] file_name_parts = design_file_name.split("\\.", 2);
        String design_name = file_name_parts[0];
        String output_file_name = design_name + ".ses";
        File curr_output_file = new File(get_parent(), output_file_name);
        try (OutputStream output_stream = new FileOutputStream(curr_output_file)) {
            if (p_board_frame.board_panel.board_handling.export_specctra_session_file(design_file_name, output_stream)) {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_11") + " "
                        + output_file_name + " " + resources.getString("message_12"));
            } else {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_13") + " "
                        + output_file_name + " " + resources.getString("message_7"));
                return false;
            }
            if (WindowMessage.confirm(resources.getString("confirm"))) {
                return write_rules_file(design_name, p_board_frame.board_panel.board_handling);
            }
        } catch (IOException ex) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Saves the board rule to file, so that they can be reused later on.
     */
    private boolean write_rules_file(String p_design_name, net.freerouting.freeroute.interactive.BoardHandling p_board_handling) {
        String rules_file_name = p_design_name + RULES_FILE_EXTENSION;
        File rules_file = new File(this.get_parent(), rules_file_name);
        try (OutputStream output_stream = new FileOutputStream(rules_file)) {
            net.freerouting.freeroute.designformats.specctra.RulesFile.write(p_board_handling, output_stream, p_design_name);
        } catch (IOException ex) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void update_eagle(BoardFrame p_board_frame) {
        final java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardMenuFile", p_board_frame.get_locale());
        String design_file_name = get_name();
        String[] file_name_parts = design_file_name.split("\\.", 2);
        String design_name = file_name_parts[0];
        String output_file_name = design_name + ".scr";
        File curr_output_file = new File(get_parent(), output_file_name);
        try (ByteArrayOutputStream session_output_stream = new ByteArrayOutputStream();
                InputStream input_stream = new ByteArrayInputStream(session_output_stream.toByteArray());
                OutputStream output_stream = new FileOutputStream(curr_output_file);) {
            if (!p_board_frame.board_panel.board_handling.export_specctra_session_file(design_file_name, session_output_stream)) {
                return;
            }
            if (p_board_frame.board_panel.board_handling.export_eagle_session_file(input_stream, output_stream)) {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_14") + " " + output_file_name + " " + resources.getString("message_15"));
            } else {
                p_board_frame.screen_messages.set_status_message(resources.getString("message_16") + " " + output_file_name + " " + resources.getString("message_7"));
            }
            if (WindowMessage.confirm(resources.getString("confirm"))) {
                write_rules_file(design_name, p_board_frame.board_panel.board_handling);
            }
        } catch (IOException ex) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets the binary file for saving or null, if the design file is not
     * available because the application is run with Java Web Start.
     */
    public File get_output_file() {
        return this.output_file;
    }

    public File get_input_file() {
        return this.input_file;
    }

    public String get_parent() {
        if (input_file != null) {
            return input_file.getParent();
        }
        return null;
    }

    public File get_parent_file() {
        if (input_file != null) {
            return input_file.getParentFile();
        }
        return null;
    }

    public boolean is_created_from_text_file() {
        return this.input_file != this.output_file;
    }

    public void dispose() {
    }

    @Override
    protected void finalize() {
        try {
            dispose();
        } finally {
            try {
                super.finalize();
            } catch (Throwable ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}