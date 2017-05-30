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

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.swing.JOptionPane;
import static net.freerouting.freeroute.DesignFile.read_rules_file;
import static net.freerouting.freeroute.Filename.GUI_DEFAULTS_FILE_NAME;
import static net.freerouting.freeroute.Filename.LOG_FILE_EXTENSIONS;
import net.freerouting.freeroute.board.BoardObserverAdaptor;
import net.freerouting.freeroute.board.BoardObservers;
import net.freerouting.freeroute.board.ItemIdNoGenerator;
import net.freerouting.freeroute.board.TestLevel;
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
@SuppressWarnings("serial")
public final class BoardFrame extends javax.swing.JFrame {

    /**
     * true, if the frame is created by an application running under Java Web
     * Start
     */
    static javax.help.HelpSet help_set = null;
    static javax.help.HelpBroker help_broker = null;
    /**
     * The windows above stored in an array
     */
    static final CustomFileFilter LOGFILE_FILTER = new CustomFileFilter(LOG_FILE_EXTENSIONS);

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

    private final BoardObservers board_observers;
    private final IdNoGenerator item_id_no_generator;

    SavableSubwindows savable_subwindows;
    TemporarySubwindows temporary_subwindows = new TemporarySubwindows();

    EnumMap<SnapshotSubwindowKey, WindowObjectListWithFilter> snapshot_subwindows;

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
    public BoardFrame(DesignFile p_design, LaunchMode p_launch_mode, TestLevel p_test_level,
            boolean p_confirm_cancel) {
        this(p_design, p_launch_mode, p_test_level, new BoardObserverAdaptor(),
                new ItemIdNoGenerator(), p_confirm_cancel);
        try {
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
                                    Locale.getDefault());
                    read_rules_file(name_parts[0], design_file.get_parent(),
                            board_panel.board_handling,
                            rb.getString("confirm_import_rules"));
                } catch (DsnFileException ex) {
                    JOptionPane.showMessageDialog(this, ex.toString(), resources.getString("no rules file"), JOptionPane.INFORMATION_MESSAGE);
                }
                refresh_windows();
                setVisible(true);
            }
        } catch (BoardFrameException | IllegalArgumentException | IOException | BoardHandlingException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), resources.getString("error_1"), JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
     * Creates new form BoardFrame. The parameters p_item_observers and
     * p_item_id_no_generator are used for syncronizing purposes, if the frame
     * is embedded into a host system,
     */
    BoardFrame(DesignFile p_design, LaunchMode p_launch_mode, TestLevel p_test_level, BoardObservers p_observers,
            IdNoGenerator p_item_id_no_generator, boolean p_confirm_cancel) {
        design_file = p_design;
        test_level = p_test_level;

        confirm_cancel = p_confirm_cancel;
        board_observers = p_observers;
        item_id_no_generator = p_item_id_no_generator;
        resources = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.BoardFrame", Locale.getDefault());
        boolean session_file_option = (p_launch_mode == LaunchMode.SESSION_FILE);
        menubar = BoardMenuBar.get_instance(this, session_file_option);
        setJMenuBar(menubar);

        toolbar_panel = new BoardToolbar(this);
        add(toolbar_panel, java.awt.BorderLayout.NORTH);

        message_panel = new BoardPanelStatus();
        add(message_panel, java.awt.BorderLayout.SOUTH);

        select_toolbar = new BoardToolbarSelectedItem(this, p_launch_mode == LaunchMode.EXTENDED_TOOL_BAR);

        screen_messages
                = new ScreenMessages(message_panel.status_message, message_panel.add_message,
                        message_panel.current_layer, message_panel.mouse_position);

        scroll_pane = new javax.swing.JScrollPane();
        scroll_pane.setPreferredSize(new java.awt.Dimension(1_150, 800));
        scroll_pane.setVerifyInputWhenFocusTarget(false);
        add(scroll_pane, java.awt.BorderLayout.CENTER);

        board_panel = new BoardPanel(screen_messages, this);
        scroll_pane.setViewportView(board_panel);

        setTitle(resources.getString("title"));
        addWindowListener(new WindowStateListener());

        pack();
    }

    /**
     * Reads interactive actions from a logfile.
     */
    void read_logfile(Reader p_reader) {
        board_panel.board_handling.read_logfile(p_reader);
    }

    /**
     * Reads an existing board design from file. If p_is_import, the design is
     * read from a scpecctra dsn file. Throws BoardFrameException, if the file
     * is invalid.
     */
    private void read() throws BoardFrameException, FileNotFoundException,
            IllegalArgumentException, IOException, BoardHandlingException {
        java.awt.Point viewport_position = null;
        if (design_file.is_created_from_text_file()) {
            board_panel.board_handling.import_design(
                    new java.io.InputStreamReader(design_file.get_input_stream(), StandardCharsets.UTF_8),
                    board_observers,
                    item_id_no_generator,
                    test_level);
            viewport_position = new java.awt.Point(0, 0);
            initialize_windows();
        } else {
            try (ObjectInputStream object_stream = new ObjectInputStream(design_file.get_input_stream())) {
                board_panel.board_handling.read_design(object_stream, test_level);
                java.awt.Point frame_location;
                Rectangle2D frame_bounds;

                viewport_position = (java.awt.Point) object_stream.readObject();
                frame_location = (java.awt.Point) object_stream.readObject();
                frame_bounds = (Rectangle2D) object_stream.readObject();

                setLocation(frame_location);
                setBounds(frame_bounds.getBounds());

                allocate_permanent_subwindows();

                savable_subwindows.read_all(object_stream);
            } catch (ClassNotFoundException ex) {
                throw new BoardFrameException("the file is invalid", ex);
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
            if (defaults_file.exists()) {
                try (Reader reader = new InputStreamReader(new FileInputStream(defaults_file), StandardCharsets.UTF_8)) {
                    GUIDefaultsFile.read(this, board_panel.board_handling, reader);
                } catch (IOException | GUIDefaultsFileException ex) {
                    Logger.getLogger(BoardFrame.class.getName()).log(Level.SEVERE, null, ex);
                    screen_messages.set_status_message(resources.getString("error_1"));
                    JOptionPane.showMessageDialog(this, ex.toString(), resources.getString("error_1"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        zoom_all();
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
            savable_subwindows.save_all(object_stream);
            return true;
        } catch (IOException e) {
            Logger.getLogger(BoardFrame.class.getName()).log(Level.SEVERE, null, e);
            screen_messages.set_status_message(resources.getString("error_2"));
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR, resources.getString("error_2"));
                alert.showAndWait();
            });
            return false;
        } catch (java.security.AccessControlException e) {
            Logger.getLogger(BoardFrame.class.getName()).log(Level.SEVERE, null, e);
            screen_messages.set_status_message(resources.getString("error_3"));
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR, resources.getString("error_3"));
                alert.showAndWait();
            });
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
        Rectangle2D display_rect = board_panel.get_viewport_bounds().getBounds();
        Rectangle2D design_bounds = board_panel.board_handling.graphics_context.get_design_bounds().getBounds();
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
        if (savable_subwindows != null) {
            savable_subwindows.dispose_all();
        }
        if (temporary_subwindows != null) {
            temporary_subwindows.dispose_all();
        }
        if (board_panel.board_handling != null) {
            board_panel.board_handling.dispose();
            board_panel.board_handling = null;
        }
        design_file = null;
        super.dispose();
    }

    private void allocate_permanent_subwindows() {
        savable_subwindows = new SavableSubwindows();

        WindowAbout about_window = new WindowAbout();
        savable_subwindows.put(SavableSubwindowKey.ABOUT, about_window);

        WindowRouteParameter route_parameter_window = new WindowRouteParameter(this);
        savable_subwindows.put(SavableSubwindowKey.ROUTE_PARAMETER, route_parameter_window);

        WindowAutorouteParameter autoroute_parameter_window = new WindowAutorouteParameter(this);
        savable_subwindows.put(SavableSubwindowKey.AUTOROUTE_PARAMETER, autoroute_parameter_window);

        WindowSelectParameter select_parameter_window = new WindowSelectParameter(this);
        savable_subwindows.put(SavableSubwindowKey.SELECT_PARAMETER, select_parameter_window);

        WindowMoveParameter move_parameter_window = new WindowMoveParameter(this);
        savable_subwindows.put(SavableSubwindowKey.MOVE_PARAMETER, move_parameter_window);

        WindowClearanceMatrix clearance_matrix_window = new WindowClearanceMatrix(this);
        savable_subwindows.put(SavableSubwindowKey.CLEARANCE_MATRIX, clearance_matrix_window);

        WindowVia via_window = new WindowVia(this);
        savable_subwindows.put(SavableSubwindowKey.VIA, via_window);

        WindowEditVias edit_vias_window = new WindowEditVias(this);
        savable_subwindows.put(SavableSubwindowKey.EDIT_VIAS, edit_vias_window);

        WindowNetClasses edit_net_rules_window = new WindowNetClasses(this);
        savable_subwindows.put(SavableSubwindowKey.EDIT_NET_RULES, edit_net_rules_window);

        WindowAssignNetClass assign_net_classes_window = new WindowAssignNetClass(this);
        savable_subwindows.put(SavableSubwindowKey.ASSIGN_NET_CLASSES, assign_net_classes_window);

        WindowPadstacks padstacks_window = new WindowPadstacks(this);
        savable_subwindows.put(SavableSubwindowKey.PADSTACKS, padstacks_window);

        WindowPackages packages_window = new WindowPackages(this);
        savable_subwindows.put(SavableSubwindowKey.PACKAGES, packages_window);

        WindowIncompletes incompletes_window = new WindowIncompletes(this);
        savable_subwindows.put(SavableSubwindowKey.INCOMPLETES, incompletes_window);

        WindowNets net_info_window = new WindowNets(this);
        savable_subwindows.put(SavableSubwindowKey.NET_INFO, net_info_window);

        WindowClearanceViolations clearance_violations_window = new WindowClearanceViolations(this);
        savable_subwindows.put(SavableSubwindowKey.CLEARANCE_VIOLATIONS, clearance_violations_window);

        WindowLengthViolations length_violations_window = new WindowLengthViolations(this);
        savable_subwindows.put(SavableSubwindowKey.LENGHT_VIOLATIONS, length_violations_window);

        WindowUnconnectedRoute unconnected_route_window = new WindowUnconnectedRoute(this);
        savable_subwindows.put(SavableSubwindowKey.UNCONNECTED_ROUTE, unconnected_route_window);

        WindowRouteStubs route_stubs_window = new WindowRouteStubs(this);
        savable_subwindows.put(SavableSubwindowKey.ROUTE_STUBS, route_stubs_window);

        WindowComponents components_window = new WindowComponents(this);
        savable_subwindows.put(SavableSubwindowKey.COMPONENTS, components_window);

        WindowLayerVisibility layer_visibility_window = WindowLayerVisibility.get_instance(this);
        savable_subwindows.put(SavableSubwindowKey.LAYER_VISIBILITY, layer_visibility_window);

        WindowObjectVisibility object_visibility_window = WindowObjectVisibility.get_instance(this);
        savable_subwindows.put(SavableSubwindowKey.OBJECT_VISIBILITY, object_visibility_window);

        WindowDisplayMisc display_misc_window = new WindowDisplayMisc(this);
        savable_subwindows.put(SavableSubwindowKey.DISPLAY_MISC, display_misc_window);

        WindowSnapshot snapshot_window = new WindowSnapshot(this);
        savable_subwindows.put(SavableSubwindowKey.SNAPSHOT, snapshot_window);

        ColorManager color_manager = new ColorManager(this);
        savable_subwindows.put(SavableSubwindowKey.COLOR_MANAGER, color_manager);

        snapshot_subwindows = new EnumMap<>(SnapshotSubwindowKey.class);
        snapshot_subwindows.put(SnapshotSubwindowKey.PACKAGES, packages_window);
        snapshot_subwindows.put(SnapshotSubwindowKey.PADSTACKS, padstacks_window);
        snapshot_subwindows.put(SnapshotSubwindowKey.NET_INFO, net_info_window);
        snapshot_subwindows.put(SnapshotSubwindowKey.INCOMPLETES, incompletes_window);
        snapshot_subwindows.put(SnapshotSubwindowKey.COMPONENTS, components_window);
    }

    /**
     * Creates the additional frames of the board frame.
     */
    private void initialize_windows() {
        allocate_permanent_subwindows();

        setLocation(120, 0);

        savable_subwindows.get(SavableSubwindowKey.SELECT_PARAMETER).setLocation(0, 0);
        savable_subwindows.get(SavableSubwindowKey.SELECT_PARAMETER).setVisible(true);

        savable_subwindows.get(SavableSubwindowKey.ROUTE_PARAMETER).setLocation(0, 100);
        savable_subwindows.get(SavableSubwindowKey.AUTOROUTE_PARAMETER).setLocation(0, 200);
        savable_subwindows.get(SavableSubwindowKey.MOVE_PARAMETER).setLocation(0, 50);
        savable_subwindows.get(SavableSubwindowKey.CLEARANCE_MATRIX).setLocation(0, 150);
        savable_subwindows.get(SavableSubwindowKey.VIA).setLocation(50, 150);
        savable_subwindows.get(SavableSubwindowKey.EDIT_VIAS).setLocation(100, 150);
        savable_subwindows.get(SavableSubwindowKey.EDIT_NET_RULES).setLocation(100, 200);
        savable_subwindows.get(SavableSubwindowKey.ASSIGN_NET_CLASSES).setLocation(100, 250);
        savable_subwindows.get(SavableSubwindowKey.PADSTACKS).setLocation(100, 30);
        savable_subwindows.get(SavableSubwindowKey.PACKAGES).setLocation(200, 30);
        savable_subwindows.get(SavableSubwindowKey.COMPONENTS).setLocation(300, 30);
        savable_subwindows.get(SavableSubwindowKey.INCOMPLETES).setLocation(400, 30);
        savable_subwindows.get(SavableSubwindowKey.CLEARANCE_VIOLATIONS).setLocation(500, 30);
        savable_subwindows.get(SavableSubwindowKey.LENGHT_VIOLATIONS).setLocation(550, 30);
        savable_subwindows.get(SavableSubwindowKey.NET_INFO).setLocation(350, 30);
        savable_subwindows.get(SavableSubwindowKey.UNCONNECTED_ROUTE).setLocation(650, 30);
        savable_subwindows.get(SavableSubwindowKey.ROUTE_STUBS).setLocation(600, 30);
        savable_subwindows.get(SavableSubwindowKey.SNAPSHOT).setLocation(0, 250);
        savable_subwindows.get(SavableSubwindowKey.LAYER_VISIBILITY).setLocation(0, 450);
        savable_subwindows.get(SavableSubwindowKey.OBJECT_VISIBILITY).setLocation(0, 550);
        savable_subwindows.get(SavableSubwindowKey.DISPLAY_MISC).setLocation(0, 350);
        savable_subwindows.get(SavableSubwindowKey.COLOR_MANAGER).setLocation(0, 600);
        savable_subwindows.get(SavableSubwindowKey.ABOUT).setLocation(200, 200);
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
        if (null != savable_subwindows) {
            savable_subwindows.refresh_all();
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
        WindowSnapshot snapshot_window = (WindowSnapshot) savable_subwindows.get(SavableSubwindowKey.SNAPSHOT);
        if (snapshot_window != null) {
            snapshot_window.goto_selected();
        }
    }

    /**
     * Selects the snapshot, which is previous to the current selected snapshot.
     * Thecurent selected snapshot will be no more selected.
     */
    public void select_previous_snapshot() {
        WindowSnapshot snapshot_window = (WindowSnapshot) savable_subwindows.get(SavableSubwindowKey.SNAPSHOT);
        if (snapshot_window != null) {
            snapshot_window.select_previous_item();
        }
    }

    /**
     * Selects the snapshot, which is next to the current selected snapshot.
     * Thecurent selected snapshot will be no more selected.
     */
    public void select_next_snapshot() {
        WindowSnapshot snapshot_window = (WindowSnapshot) savable_subwindows.get(SavableSubwindowKey.SNAPSHOT);
        if (snapshot_window != null) {
            snapshot_window.select_next_item();
        }
    }

    /**
     * Used for storing the subwindowfilters in a snapshot.
     */
    public SubwindowSelections get_snapshot_subwindow_selections() {
        SubwindowSelections result = new SubwindowSelections(snapshot_subwindows);
        return result;
    }

    /**
     * Used for restoring the subwindowfilters from a snapshot.
     */
    public void set_snapshot_subwindow_selections(SubwindowSelections p_filters) {
        p_filters.set_snapshot_info(snapshot_subwindows);
    }

    /**
     * Repaints this board frame and all the subwindows of the board.
     */
    public void repaint_all() {
        repaint();
        savable_subwindows.repaint_all();
    }

    private class WindowStateListener extends java.awt.event.WindowAdapter {

        @Override
        public void windowClosing(java.awt.event.WindowEvent evt) {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            int option = javax.swing.JOptionPane.showConfirmDialog(null, resources.getString("confirm_cancel"),
                    null, javax.swing.JOptionPane.YES_NO_OPTION);
            if (option == javax.swing.JOptionPane.NO_OPTION) {
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            }
        }

        @Override
        public void windowIconified(java.awt.event.WindowEvent evt) {
            savable_subwindows.iconifed_all();
            temporary_subwindows.iconifed_all();
        }

        @Override
        public void windowDeiconified(java.awt.event.WindowEvent evt) {
            savable_subwindows.deiconifed_all();
            temporary_subwindows.deiconifed_all();
        }
    }
}
