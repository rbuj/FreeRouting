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
 */
package net.freerouting.freeroute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import net.freerouting.freeroute.board.BoardObserverAdaptor;
import net.freerouting.freeroute.board.BoardObservers;
import net.freerouting.freeroute.board.ItemIdNoGenerator;
import net.freerouting.freeroute.board.TestLevel;
import net.freerouting.freeroute.datastructures.FileFilter;
import net.freerouting.freeroute.datastructures.IdNoGenerator;
import net.freerouting.freeroute.designformats.specctra.DsnFileException;
import net.freerouting.freeroute.interactive.BoardHandlingException;
import net.freerouting.freeroute.interactive.ScreenMessages;

/**
 *
 * Graphical frame of for interactive editing of a routing board.
 *
 * @author Alfons Wirtz
 */
public class BoardFrame extends javax.swing.JFrame {

    /**
     * true, if the frame is created by an application running under Java Web
     * Start
     */
    static javax.help.HelpSet help_set = null;
    static javax.help.HelpBroker help_broker = null;
    /**
     * The windows above stored in an array
     */
    static final int SUBWINDOW_COUNT = 24;
    static final String[] log_file_extensions = {"log"};
    static final String GUI_DEFAULTS_FILE_NAME = "gui_defaults.par";
    static final String GUI_DEFAULTS_FILE_BACKUP_NAME = "gui_defaults.par.bak";
    static final FileFilter logfile_filter = new FileFilter(log_file_extensions);

    /**
     * The scroll pane for the panel of the routing board.
     */
    final javax.swing.JScrollPane scroll_pane;

    /**
     * The menubar of this frame
     */
    final BoardMenuBar menubar;

    /**
     * The panel with the graphical representation of the board.
     */
    final BoardPanel board_panel;

    /**
     * The panel with the toolbars
     */
    private final BoardToolbar toolbar_panel;

    /**
     * The toolbar used in the selected item state.
     */
    private final javax.swing.JToolBar select_toolbar;

    /**
     * The panel with the message line
     */
    private final BoardPanelStatus message_panel;

    final ScreenMessages screen_messages;

    private final TestLevel test_level;

    private final boolean confirm_cancel;

    private final java.util.ResourceBundle resources;
    private java.util.Locale locale;

    private final BoardObservers board_observers;
    private final IdNoGenerator item_id_no_generator;

    WindowAbout about_window = null;
    WindowRouteParameter route_parameter_window = null;
    WindowAutorouteParameter autoroute_parameter_window = null;
    WindowSelectParameter select_parameter_window = null;
    WindowMoveParameter move_parameter_window = null;
    WindowClearanceMatrix clearance_matrix_window = null;
    WindowVia via_window = null;
    WindowEditVias edit_vias_window = null;
    WindowNetClasses edit_net_rules_window = null;
    WindowAssignNetClass assign_net_classes_window = null;
    WindowPadstacks padstacks_window = null;
    WindowPackages packages_window = null;
    WindowIncompletes incompletes_window = null;
    WindowNets net_info_window = null;
    WindowClearanceViolations clearance_violations_window = null;
    WindowLengthViolations length_violations_window = null;
    WindowUnconnectedRoute unconnected_route_window = null;
    WindowRouteStubs route_stubs_window = null;
    WindowComponents components_window = null;
    WindowLayerVisibility layer_visibility_window = null;
    WindowObjectVisibility object_visibility_window = null;
    WindowDisplayMisc display_misc_window = null;
    WindowSnapshot snapshot_window = null;
    ColorManager color_manager = null;

    BoardSavableSubWindow[] permanent_subwindows = new BoardSavableSubWindow[SUBWINDOW_COUNT];

    java.util.Collection<BoardTemporarySubWindow> temporary_subwindows = new java.util.LinkedList<>();

    DesignFile design_file = null;

    /**
     * Creates new form BoardFrame. If p_option = FROM_START_MENU this frame is
     * created from a start menu frame. If p_option = SINGLE_FRAME, this frame
     * is created directly a single frame. If p_option = Option.IN_SAND_BOX, no
     * security sensitive actions like for example choosing If p_option =
     * Option.WEBSTART, the application has been started with Java Webstart.
     * files are allowed, so that the frame can be used in an applet. Currently
     * Option.EXTENDED_TOOL_BAR is used only if a new board is created by the
     * application from scratch. If p_test_level {@literal >} RELEASE_VERSION,
     * functionality not yet ready for release is included. Also the warning
     * output depends on p_test_level.
     */
    public BoardFrame(DesignFile p_design, Option p_option, TestLevel p_test_level,
            java.util.Locale p_locale, boolean p_confirm_cancel) throws BoardFrameException, DsnFileException {
        this(p_design, p_option, p_test_level, new BoardObserverAdaptor(),
                new ItemIdNoGenerator(), p_locale, p_confirm_cancel);
        read();
        menubar.add_design_dependent_items();
        if (design_file.is_created_from_text_file()) {
            try {
                // Read the file  with the saved rules, if it is existing.
                String file_name = design_file.get_name();
                String[] name_parts = file_name.split("\\.");
                java.util.ResourceBundle rb
                        = java.util.ResourceBundle.getBundle(
                                "net.freerouting.freeroute.resources.MainApp",
                                resources.getLocale());
                DesignFile.read_rules_file(name_parts[0], design_file.get_parent(),
                        board_panel.board_handling,
                        rb.getString("confirm_import_rules"));
            } catch (DsnFileException ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.INFO, "no rules file");
            }
            refresh_windows();
        }
    }

    /**
     * Creates new form BoardFrame. The parameters p_item_observers and
     * p_item_id_no_generator are used for syncronizing purposes, if the frame
     * is embedded into a host system,
     */
    BoardFrame(DesignFile p_design, Option p_option, TestLevel p_test_level, BoardObservers p_observers,
            IdNoGenerator p_item_id_no_generator, Locale p_locale, boolean p_confirm_cancel) {
        design_file = p_design;
        test_level = p_test_level;

        confirm_cancel = p_confirm_cancel;
        board_observers = p_observers;
        item_id_no_generator = p_item_id_no_generator;
        locale = p_locale;
        resources = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardFrame", p_locale);
        boolean session_file_option = (p_option == Option.SESSION_FILE);
        menubar = BoardMenuBar.get_instance(this, session_file_option);
        setJMenuBar(menubar);

        toolbar_panel = new BoardToolbar(this);
        add(toolbar_panel, java.awt.BorderLayout.NORTH);

        message_panel = new BoardPanelStatus(locale);
        add(message_panel, java.awt.BorderLayout.SOUTH);

        select_toolbar = new BoardToolbarSelectedItem(this, p_option == Option.EXTENDED_TOOL_BAR);

        screen_messages
                = new ScreenMessages(message_panel.status_message, message_panel.add_message,
                        message_panel.current_layer, message_panel.mouse_position, locale);

        scroll_pane = new javax.swing.JScrollPane();
        scroll_pane.setPreferredSize(new java.awt.Dimension(1150, 800));
        scroll_pane.setVerifyInputWhenFocusTarget(false);
        add(scroll_pane, java.awt.BorderLayout.CENTER);

        board_panel = new BoardPanel(screen_messages, this, p_locale);
        scroll_pane.setViewportView(board_panel);

        setTitle(resources.getString("title"));
        addWindowListener(new WindowStateListener());

        pack();
    }

    /**
     * Reads interactive actions from a logfile.
     */
    void read_logfile(InputStream p_input_stream) {
        board_panel.board_handling.read_logfile(p_input_stream);
    }

    /**
     * Reads an existing board design from file. If p_is_import, the design is
     * read from a scpecctra dsn file. Throws BoardFrameException, if the file
     * is invalid.
     */
    private void read() throws BoardFrameException {
        java.awt.Point viewport_position = null;
        if (design_file.is_created_from_text_file()) {
            try {
                board_panel.board_handling.import_design(
                        design_file.get_input_stream(),
                        board_observers,
                        item_id_no_generator,
                        test_level);
                viewport_position = new java.awt.Point(0, 0);
                initialize_windows();
            } catch (BoardHandlingException exc) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, exc);
                Alert alert = new Alert(AlertType.ERROR, exc.toString());
                alert.showAndWait();
                Runtime.getRuntime().exit(1);
            }
        } else {
            try (ObjectInputStream object_stream = new ObjectInputStream(design_file.get_input_stream())) {
                boolean read_ok = board_panel.board_handling.read_design(object_stream, test_level);
                if (!read_ok) {
                    Alert alert = new Alert(AlertType.ERROR, "the file is invalid");
                    alert.showAndWait();
                    Runtime.getRuntime().exit(1);
                }
                java.awt.Point frame_location;
                java.awt.Rectangle frame_bounds;
                try {
                    viewport_position = (java.awt.Point) object_stream.readObject();
                    frame_location = (java.awt.Point) object_stream.readObject();
                    frame_bounds = (java.awt.Rectangle) object_stream.readObject();

                    setLocation(frame_location);
                    setBounds(frame_bounds);

                    allocate_permanent_subwindows();

                    for (int i = 0; i < permanent_subwindows.length; ++i) {
                        permanent_subwindows[i].read(object_stream);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
                    Alert alert = new Alert(AlertType.ERROR, "the file is invalid");
                    alert.showAndWait();
                    Runtime.getRuntime().exit(1);
                }
            } catch (IOException ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(AlertType.ERROR, "the file is invalid");
                alert.showAndWait();
                Runtime.getRuntime().exit(1);
            }
        }

        java.awt.Dimension panel_size = board_panel.board_handling.graphics_context.get_panel_size();
        board_panel.setSize(panel_size);
        board_panel.setPreferredSize(panel_size);
        if (viewport_position != null) {
            board_panel.set_viewport_position(viewport_position);
        }
        board_panel.create_popup_menus();
        board_panel.init_colors();
        board_panel.board_handling.create_ratsnest();
        hilight_selected_button();
        toolbar_panel.unit_factor_field.setValue(board_panel.board_handling.coordinate_transform.user_unit_factor);
        toolbar_panel.unit_combo_box.setSelectedItem(board_panel.board_handling.coordinate_transform.user_unit);
        setVisible(true);
        if (design_file.is_created_from_text_file()) {
            // Read the default gui settings, if gui default file exists.
            File defaults_file = new File(design_file.get_parent(), GUI_DEFAULTS_FILE_NAME);
            try (InputStream input_stream = new FileInputStream(defaults_file)) {
                boolean read_ok = GUIDefaultsFile.read(this, board_panel.board_handling, input_stream);
                if (!read_ok) {
                    screen_messages.set_status_message(resources.getString("error_1"));
                    Alert alert = new Alert(AlertType.ERROR, resources.getString("error_1"));
                    alert.showAndWait();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(AlertType.ERROR, "the file is invalid");
                alert.showAndWait();
                Runtime.getRuntime().exit(1);
            } catch (IOException ex) {
                Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(AlertType.ERROR, "the file is invalid");
                alert.showAndWait();
                Runtime.getRuntime().exit(1);
            }
            zoom_all();
        }
    }

    /**
     * Saves the interactive settings and the design file to disk. Returns
     * false, if the save failed.
     */
    boolean save() {
        if (design_file == null) {
            return false;
        }
        try (OutputStream output_stream = new FileOutputStream(design_file.get_output_file());
                ObjectOutputStream object_stream = new ObjectOutputStream(output_stream);) {
            boolean save_ok = board_panel.board_handling.save_design_file(object_stream);
            if (!save_ok) {
                return false;
            }
            object_stream.writeObject(board_panel.get_viewport_position());
            object_stream.writeObject(getLocation());
            object_stream.writeObject(getBounds());
            for (int i = 0; i < permanent_subwindows.length; ++i) {
                permanent_subwindows[i].save(object_stream);
            }
            return true;
        } catch (IOException e) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, e);
            screen_messages.set_status_message(resources.getString("error_2"));
            Alert alert = new Alert(AlertType.ERROR, resources.getString("error_2"));
            alert.showAndWait();
            return false;
        } catch (java.security.AccessControlException e) {
            Logger.getLogger(DesignFile.class.getName()).log(Level.SEVERE, null, e);
            screen_messages.set_status_message(resources.getString("error_3"));
            Alert alert = new Alert(AlertType.ERROR, resources.getString("error_3"));
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Sets contexts sensitive help for the input component, if the help system
     * is used.
     */
    public void set_context_sensitive_help(java.awt.Component p_component, String p_help_id) {
//        if (help_system_used) {
//            java.awt.Component curr_component;
//            if (p_component instanceof javax.swing.JFrame) {
//                curr_component = ((javax.swing.JFrame) p_component).getRootPane();
//            } else {
//                curr_component = p_component;
//            }
//            String help_id = "html_files." + p_help_id;
//            javax.help.CSH.setHelpIDString(curr_component, help_id);
//            if (!is_web_start) {
//                help_broker.enableHelpKey(curr_component, help_id, help_set);
//            }
//        }
    }

    /**
     * Sets the toolbar to the buttons of the selected item state.
     */
    public void set_select_toolbar() {
        getContentPane().remove(toolbar_panel);
        getContentPane().add(select_toolbar, java.awt.BorderLayout.NORTH);
        repaint();
    }

    /**
     * Sets the toolbar buttons to the select. route and drag menu buttons of
     * the main menu.
     */
    public void set_menu_toolbar() {
        getContentPane().remove(select_toolbar);
        getContentPane().add(toolbar_panel, java.awt.BorderLayout.NORTH);
        repaint();
    }

    /**
     * Calculates the absolute location of the board frame in his outmost parent
     * frame.
     */
    java.awt.Point absolute_panel_location() {
        int x = scroll_pane.getX();
        int y = scroll_pane.getY();
        java.awt.Container curr_parent = scroll_pane.getParent();
        while (curr_parent != null) {
            x += curr_parent.getX();
            y += curr_parent.getY();
            curr_parent = curr_parent.getParent();
        }
        return new java.awt.Point(x, y);
    }

    /**
     * Sets the displayed region to the whole board.
     */
    public void zoom_all() {
        board_panel.board_handling.adjust_design_bounds();
        java.awt.Rectangle display_rect = board_panel.get_viewport_bounds();
        java.awt.Rectangle design_bounds = board_panel.board_handling.graphics_context.get_design_bounds();
        double width_factor = display_rect.getWidth() / design_bounds.getWidth();
        double height_factor = display_rect.getHeight() / design_bounds.getHeight();
        double zoom_factor = Math.min(width_factor, height_factor);
        java.awt.geom.Point2D zoom_center = board_panel.board_handling.graphics_context.get_design_center();
        board_panel.zoom(zoom_factor, zoom_center);
        java.awt.geom.Point2D new_vieport_center = board_panel.board_handling.graphics_context.get_design_center();
        board_panel.set_viewport_center(new_vieport_center);

    }

    /**
     * Actions to be taken when this frame vanishes.
     */
    @Override
    public void dispose() {
        for (int i = 0; i < permanent_subwindows.length; ++i) {
            if (permanent_subwindows[i] != null) {
                permanent_subwindows[i].dispose();
                permanent_subwindows[i] = null;
            }
        }
        for (BoardTemporarySubWindow curr_subwindow : temporary_subwindows) {
            if (curr_subwindow != null) {
                curr_subwindow.board_frame_disposed();
            }
        }
        if (board_panel.board_handling != null) {
            board_panel.board_handling.dispose();
            board_panel.board_handling = null;
        }
        design_file = null;
        super.dispose();
    }

    private void allocate_permanent_subwindows() {
        color_manager = new ColorManager(this);
        permanent_subwindows[0] = color_manager;
        object_visibility_window = WindowObjectVisibility.get_instance(this);
        permanent_subwindows[1] = object_visibility_window;
        layer_visibility_window = WindowLayerVisibility.get_instance(this);
        permanent_subwindows[2] = layer_visibility_window;
        display_misc_window = new WindowDisplayMisc(this);
        permanent_subwindows[3] = display_misc_window;
        snapshot_window = new WindowSnapshot(this);
        permanent_subwindows[4] = snapshot_window;
        route_parameter_window = new WindowRouteParameter(this);
        permanent_subwindows[5] = route_parameter_window;
        select_parameter_window = new WindowSelectParameter(this);
        permanent_subwindows[6] = select_parameter_window;
        clearance_matrix_window = new WindowClearanceMatrix(this);
        permanent_subwindows[7] = clearance_matrix_window;
        padstacks_window = new WindowPadstacks(this);
        permanent_subwindows[8] = padstacks_window;
        packages_window = new WindowPackages(this);
        permanent_subwindows[9] = packages_window;
        components_window = new WindowComponents(this);
        permanent_subwindows[10] = components_window;
        incompletes_window = new WindowIncompletes(this);
        permanent_subwindows[11] = incompletes_window;
        clearance_violations_window = new WindowClearanceViolations(this);
        permanent_subwindows[12] = clearance_violations_window;
        net_info_window = new WindowNets(this);
        permanent_subwindows[13] = net_info_window;
        via_window = new WindowVia(this);
        permanent_subwindows[14] = via_window;
        edit_vias_window = new WindowEditVias(this);
        permanent_subwindows[15] = edit_vias_window;
        edit_net_rules_window = new WindowNetClasses(this);
        permanent_subwindows[16] = edit_net_rules_window;
        assign_net_classes_window = new WindowAssignNetClass(this);
        permanent_subwindows[17] = assign_net_classes_window;
        length_violations_window = new WindowLengthViolations(this);
        permanent_subwindows[18] = length_violations_window;
        about_window = new WindowAbout(locale);
        permanent_subwindows[19] = about_window;
        move_parameter_window = new WindowMoveParameter(this);
        permanent_subwindows[20] = move_parameter_window;
        unconnected_route_window = new WindowUnconnectedRoute(this);
        permanent_subwindows[21] = unconnected_route_window;
        route_stubs_window = new WindowRouteStubs(this);
        permanent_subwindows[22] = route_stubs_window;
        autoroute_parameter_window = new WindowAutorouteParameter(this);
        permanent_subwindows[23] = autoroute_parameter_window;
    }

    /**
     * Creates the additional frames of the board frame.
     */
    private void initialize_windows() {
        allocate_permanent_subwindows();

        setLocation(120, 0);

        select_parameter_window.setLocation(0, 0);
        select_parameter_window.setVisible(true);

        route_parameter_window.setLocation(0, 100);
        autoroute_parameter_window.setLocation(0, 200);
        move_parameter_window.setLocation(0, 50);
        clearance_matrix_window.setLocation(0, 150);
        via_window.setLocation(50, 150);
        edit_vias_window.setLocation(100, 150);
        edit_net_rules_window.setLocation(100, 200);
        assign_net_classes_window.setLocation(100, 250);
        padstacks_window.setLocation(100, 30);
        packages_window.setLocation(200, 30);
        components_window.setLocation(300, 30);
        incompletes_window.setLocation(400, 30);
        clearance_violations_window.setLocation(500, 30);
        length_violations_window.setLocation(550, 30);
        net_info_window.setLocation(350, 30);
        unconnected_route_window.setLocation(650, 30);
        route_stubs_window.setLocation(600, 30);
        snapshot_window.setLocation(0, 250);
        layer_visibility_window.setLocation(0, 450);
        object_visibility_window.setLocation(0, 550);
        display_misc_window.setLocation(0, 350);
        color_manager.setLocation(0, 600);
        about_window.setLocation(200, 200);
    }

    /**
     * Returns the currently used locale for the language dependent output.
     */
    public java.util.Locale get_locale() {
        return locale;
    }

    /**
     * Sets the background of the board panel
     */
    public void set_board_background(java.awt.Color p_color) {
        board_panel.setBackground(p_color);
    }

    /**
     * Refreshs all displayed coordinates after the user unit has changed.
     */
    public void refresh_windows() {
        for (int i = 0; i < permanent_subwindows.length; ++i) {
            if (permanent_subwindows[i] != null) {
                permanent_subwindows[i].refresh();
            }
        }
    }

    /**
     * Sets the selected button in the menu button button group
     */
    public void hilight_selected_button() {
        toolbar_panel.hilight_selected_button();
    }

    /**
     * Restore the selected snapshot in the snapshot window.
     */
    public void goto_selected_snapshot() {
        if (snapshot_window != null) {
            snapshot_window.goto_selected();
        }
    }

    /**
     * Selects the snapshot, which is previous to the current selected snapshot.
     * Thecurent selected snapshot will be no more selected.
     */
    public void select_previous_snapshot() {
        if (snapshot_window != null) {
            snapshot_window.select_previous_item();
        }
    }

    /**
     * Selects the snapshot, which is next to the current selected snapshot.
     * Thecurent selected snapshot will be no more selected.
     */
    public void select_next_snapshot() {
        if (snapshot_window != null) {
            snapshot_window.select_next_item();
        }
    }

    /**
     * Used for storing the subwindowfilters in a snapshot.
     */
    public SubwindowSelections get_snapshot_subwindow_selections() {
        SubwindowSelections result = new SubwindowSelections();
        result.incompletes_selection = incompletes_window.get_snapshot_info();
        result.packages_selection = packages_window.get_snapshot_info();
        result.nets_selection = net_info_window.get_snapshot_info();
        result.components_selection = components_window.get_snapshot_info();
        result.padstacks_selection = padstacks_window.get_snapshot_info();
        return result;
    }

    /**
     * Used for restoring the subwindowfilters from a snapshot.
     */
    public void set_snapshot_subwindow_selections(SubwindowSelections p_filters) {
        incompletes_window.set_snapshot_info(p_filters.incompletes_selection);
        packages_window.set_snapshot_info(p_filters.packages_selection);
        net_info_window.set_snapshot_info(p_filters.nets_selection);
        components_window.set_snapshot_info(p_filters.components_selection);
        padstacks_window.set_snapshot_info(p_filters.padstacks_selection);
    }

    /**
     * Repaints this board frame and all the subwindows of the board.
     */
    public void repaint_all() {
        repaint();
        for (int i = 0; i < permanent_subwindows.length; ++i) {
            permanent_subwindows[i].repaint();
        }
    }

    /**
     * Used for storing the subwindow filters in a snapshot.
     */
    public static class SubwindowSelections implements java.io.Serializable {

        private WindowObjectListWithFilter.SnapshotInfo incompletes_selection;
        private WindowObjectListWithFilter.SnapshotInfo packages_selection;
        private WindowObjectListWithFilter.SnapshotInfo nets_selection;
        private WindowObjectListWithFilter.SnapshotInfo components_selection;
        private WindowObjectListWithFilter.SnapshotInfo padstacks_selection;
    }

    public enum Option {
        FROM_START_MENU, SINGLE_FRAME, SESSION_FILE, EXTENDED_TOOL_BAR
    }

    private class WindowStateListener extends java.awt.event.WindowAdapter {

        @Override
        public void windowClosing(java.awt.event.WindowEvent evt) {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            if (confirm_cancel) {
                int option = javax.swing.JOptionPane.showConfirmDialog(null, resources.getString("confirm_cancel"),
                        null, javax.swing.JOptionPane.YES_NO_OPTION);
                if (option == javax.swing.JOptionPane.NO_OPTION) {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        }

        @Override
        public void windowIconified(java.awt.event.WindowEvent evt) {
            for (int i = 0; i < permanent_subwindows.length; ++i) {
                permanent_subwindows[i].parent_iconified();
            }
            for (BoardSubWindow curr_subwindow : temporary_subwindows) {
                if (curr_subwindow != null) {
                    curr_subwindow.parent_iconified();
                }
            }
        }

        @Override
        public void windowDeiconified(java.awt.event.WindowEvent evt) {
            for (int i = 0; i < permanent_subwindows.length; ++i) {
                if (permanent_subwindows[i] != null) {
                    permanent_subwindows[i].parent_deiconified();
                }
            }
            for (BoardSubWindow curr_subwindow : temporary_subwindows) {
                if (curr_subwindow != null) {
                    curr_subwindow.parent_deiconified();
                }
            }
        }
    }
}
