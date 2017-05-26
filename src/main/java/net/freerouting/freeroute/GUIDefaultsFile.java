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
 * GUIDefaultsFile.java
 *
 * Created on 26. Dezember 2004, 08:29
 */
package net.freerouting.freeroute;

import java.awt.geom.Rectangle2D;
import java.util.Locale;
import net.freerouting.freeroute.board.ItemSelectionFilter;
import net.freerouting.freeroute.datastructures.IndentFileWriter;
import net.freerouting.freeroute.interactive.BoardHandling;

/**
 * Description of a text file, where the board independent interactive settings
 * are stored.
 *
 * @author Alfons Wirtz
 */
public class GUIDefaultsFile {

    protected BoardFrame board_frame;
    protected BoardHandling board_handling;
    /**
     * Used, when reading a defaults file, null otherwise.
     */
    protected GUIDefaultsScanner scanner;
    /**
     * Used, when writing a defaults file; null otherwise.
     */
    protected IndentFileWriter out_file;

    /**
     * Writes the GUI setting of p_board_frame as default to p_file. Returns
     * false, if an error occured.
     */
    public static boolean write(BoardFrame p_board_frame, BoardHandling p_board_handling,
            java.io.OutputStream p_output_stream) throws GUIDefaultsFileException {
        if (p_output_stream == null) {
            return false;
        }

        IndentFileWriter output_file = new IndentFileWriter(p_output_stream);

        GUIDefaultsFile result = new GUIDefaultsFile(p_board_frame, p_board_handling, null, output_file);
        try {
            result.write_defaults_scope();
        } catch (java.io.IOException ex) {
            throw new GUIDefaultsFileException("Unable to write defaults file", ex);
        }

        try {
            output_file.close();
        } catch (java.io.IOException ex) {
            throw new GUIDefaultsFileException("Unable to close defaults file", ex);
        }
        return true;
    }

    /**
     * Reads the GUI setting of p_board_frame from file. Returns false, if an
     * error occured while reading the file.
     */
    public static void read(BoardFrame p_board_frame, BoardHandling p_board_handling,
            java.io.Reader p_reader) throws GUIDefaultsFileException {
        if (p_reader == null) {
            throw new GUIDefaultsFileException("reader == null");
        }
        GUIDefaultsScanner scanner = new GUIDefaultsScanner(p_reader);
        GUIDefaultsFile new_instance = new GUIDefaultsFile(p_board_frame, p_board_handling, scanner, null);
        try {
            new_instance.read_defaults_scope();
        } catch (java.io.IOException ex) {
            throw new GUIDefaultsFileException("Unable to read defaults file", ex);
        }
    }

    private GUIDefaultsFile(BoardFrame p_board_frame, BoardHandling p_board_handling,
            GUIDefaultsScanner p_scanner, IndentFileWriter p_output_file) {
        board_frame = p_board_frame;
        board_handling = p_board_handling;
        scanner = p_scanner;
        out_file = p_output_file;
    }

    private void write_defaults_scope() throws java.io.IOException {
        out_file.start_scope("gui_defaults");
        write_windows_scope();
        write_colors_scope();
        write_parameter_scope();
        out_file.end_scope();
    }

    private void read_defaults_scope() throws java.io.IOException, GUIDefaultsFileException {
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.OPEN_BRACKET);
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.GUI_DEFAULTS);
        // read the direct subscopes of the gui_defaults scope
        Object next_token = null;
        for (;;) {
            Object prev_token = next_token;
            next_token = this.scanner.next_token();
            if (next_token == null) {
                // end of file
                return;
            }
            if (next_token == GUIDefaultsFileKeyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }
            if (prev_token == GUIDefaultsFileKeyword.OPEN_BRACKET) {
                if (next_token == GUIDefaultsFileKeyword.COLORS) {
                    read_colors_scope();
                } else if (next_token == GUIDefaultsFileKeyword.WINDOWS) {
                    read_windows_scope();
                } else if (next_token == GUIDefaultsFileKeyword.PARAMETER) {
                    read_parameter_scope();
                } else {
                    // overread all scopes except the routes scope for the time being
                    GUIDefaultsFileKeyword.skip_scope(scanner);
                }
            }
        }
        this.board_frame.refresh_windows();
    }

    private void read_windows_scope() throws java.io.IOException, GUIDefaultsFileException {
        // read the direct subscopes of the windows scope
        Object next_token = null;
        for (;;) {
            Object prev_token = next_token;
            next_token = this.scanner.next_token();
            if (next_token == null) {
                throw new GUIDefaultsFileException("Unexpected end of file");
            }
            if (next_token == GUIDefaultsFileKeyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }
            if (prev_token == GUIDefaultsFileKeyword.OPEN_BRACKET) {
                if (next_token instanceof GUIDefaultsFileKeyword) {
                    GUIDefaultsFileKeyword.read_frame_scope((GUIDefaultsFileKeyword) next_token, scanner, board_frame);
                } else {
                    throw new GUIDefaultsFileException("Keyword expected");
                }
            }
        }
    }

    private void write_windows_scope() throws java.io.IOException {
        out_file.start_scope("windows");
        write_frame_scope(board_frame, "board_frame");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.COLOR_MANAGER), "color_manager");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.LAYER_VISIBILITY), "layer_visibility");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.OBJECT_VISIBILITY), "object_visibility");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.DISPLAY_MISC), "display_miscellanious");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.SNAPSHOT), "snapshots");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.SELECT_PARAMETER), "select_parameter");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.ROUTE_PARAMETER), "route_parameter");
        write_frame_scope(((WindowRouteParameter) board_frame.savable_subwindows.get(SavableSubwindowKey.ROUTE_PARAMETER)).manual_rule_window, "manual_rules");
        write_frame_scope(((WindowRouteParameter) board_frame.savable_subwindows.get(SavableSubwindowKey.ROUTE_PARAMETER)).detail_window, "route_details");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.MOVE_PARAMETER), "move_parameter");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.CLEARANCE_MATRIX), "clearance_matrix");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.VIA), "via_rules");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.EDIT_VIAS), "edit_vias");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.EDIT_NET_RULES), "edit_net_rules");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.ASSIGN_NET_CLASSES), "assign_net_rules");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.PADSTACKS), "padstack_info");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.PACKAGES), "package_info");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.COMPONENTS), "component_info");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.NET_INFO), "net_info");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.INCOMPLETES), "incompletes_info");
        write_frame_scope(board_frame.savable_subwindows.get(SavableSubwindowKey.CLEARANCE_VIOLATIONS), "violations_info");
        out_file.end_scope();
    }

    private void write_frame_scope(javax.swing.JFrame p_frame, String p_frame_name)
            throws java.io.IOException {
        out_file.start_scope(p_frame_name);
        out_file.new_line();
        GUIDefaultsFileKeyword.write_visible(out_file, p_frame.isVisible());
        write_bounds(p_frame.getBounds());
        out_file.end_scope();
    }

    private void write_bounds(Rectangle2D p_bounds) throws java.io.IOException {
        out_file.start_scope("bounds");
        out_file.new_line();
        out_file.write(Integer.toString(((Number) p_bounds.getX()).intValue()));
        out_file.write(" ");
        out_file.write(Integer.toString(((Number) p_bounds.getY()).intValue()));
        out_file.write(" ");
        out_file.write(Integer.toString(((Number) p_bounds.getWidth()).intValue()));
        out_file.write(" ");
        out_file.write(Integer.toString(((Number) p_bounds.getHeight()).intValue()));
        out_file.end_scope();
    }

    private void read_colors_scope() throws java.io.IOException, GUIDefaultsFileException {
        // read the direct subscopes of the colors scope
        Object next_token = null;
        for (;;) {
            Object prev_token = next_token;
            next_token = this.scanner.next_token();
            if (next_token == null) {
                throw new GUIDefaultsFileException("Unexpected end of file");
            }
            if (next_token == GUIDefaultsFileKeyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }

            if (prev_token == GUIDefaultsFileKeyword.OPEN_BRACKET) {
                if (next_token instanceof GUIDefaultsFileKeyword) {
                    ((GUIDefaultsFileKeyword) next_token).read_color_setting(scanner, board_handling);
                } else {
                    // skip unknown scope
                    GUIDefaultsFileKeyword.skip_scope(scanner);
                }
            }
        }
    }

    private void write_colors_scope() throws java.io.IOException {
        net.freerouting.freeroute.boardgraphics.GraphicsContext graphics_context = this.board_handling.graphics_context;
        out_file.start_scope("colors");

        out_file.start_scope("background");
        write_color_scope(graphics_context.get_background_color());
        out_file.end_scope();
        out_file.start_scope("hilight");
        write_color_intensity(graphics_context.get_hilight_color_intensity());
        write_color_scope(graphics_context.get_hilight_color());
        out_file.end_scope();
        out_file.start_scope("incompletes");
        write_color_intensity(graphics_context.get_incomplete_color_intensity());
        write_color_scope(graphics_context.get_incomplete_color());
        out_file.end_scope();
        out_file.start_scope("outline");
        write_color_scope(graphics_context.get_outline_color());
        out_file.end_scope();
        out_file.start_scope("component_front");
        write_color_scope(graphics_context.get_component_color(true));
        out_file.end_scope();
        out_file.start_scope("component_back");
        write_color_scope(graphics_context.get_component_color(false));
        out_file.end_scope();
        out_file.start_scope("violations");
        write_color_scope(graphics_context.get_violations_color());
        out_file.end_scope();
        out_file.start_scope("length_matching");
        write_color_intensity(graphics_context.get_length_matching_area_color_intensity());
        write_color_scope(graphics_context.get_length_matching_area_color());
        out_file.end_scope();
        out_file.start_scope("traces");
        write_color_intensity(graphics_context.get_trace_color_intensity());
        write_color(graphics_context.get_trace_colors(false));
        out_file.end_scope();
        out_file.start_scope("fixed_traces");
        write_color_intensity(graphics_context.get_trace_color_intensity());
        write_color(graphics_context.get_trace_colors(true));
        out_file.end_scope();
        out_file.start_scope("vias");
        write_color_intensity(graphics_context.get_via_color_intensity());
        write_color(graphics_context.get_via_colors(false));
        out_file.end_scope();
        out_file.start_scope("fixed_vias");
        write_color_intensity(graphics_context.get_via_color_intensity());
        write_color(graphics_context.get_via_colors(true));
        out_file.end_scope();
        out_file.start_scope("pins");
        write_color_intensity(graphics_context.get_pin_color_intensity());
        write_color(graphics_context.get_pin_colors());
        out_file.end_scope();
        out_file.start_scope("conduction");
        write_color_intensity(graphics_context.get_conduction_color_intensity());
        write_color(graphics_context.get_conduction_colors());
        out_file.end_scope();
        out_file.start_scope("keepout");
        write_color_intensity(graphics_context.get_obstacle_color_intensity());
        write_color(graphics_context.get_obstacle_colors());
        out_file.end_scope();
        out_file.start_scope("via_keepout");
        write_color_intensity(graphics_context.get_via_obstacle_color_intensity());
        write_color(graphics_context.get_via_obstacle_colors());
        out_file.end_scope();

        out_file.end_scope();
    }

    private void write_color_intensity(double p_value) throws java.io.IOException {
        out_file.write(" ");
        Float value = (float) p_value;
        out_file.write(value.toString());
    }

    private void write_color_scope(java.awt.Color p_color) throws java.io.IOException {
        out_file.new_line();
        Integer red = p_color.getRed();
        out_file.write(red.toString());
        out_file.write(" ");
        Integer green = p_color.getGreen();
        out_file.write(green.toString());
        out_file.write(" ");
        Integer blue = p_color.getBlue();
        out_file.write(blue.toString());
    }

    private void write_color(java.awt.Color[] p_colors) throws java.io.IOException {
        for (int i = 0; i < p_colors.length; ++i) {
            write_color_scope(p_colors[i]);
        }
    }

    private void read_parameter_scope() throws java.io.IOException, GUIDefaultsFileException {
        // read the subscopes of the parameter scope
        Object next_token = null;
        for (;;) {
            Object prev_token = next_token;
            next_token = this.scanner.next_token();
            if (next_token == null) {
                throw new GUIDefaultsFileException("Unexpected end of file");
            }
            if (next_token == GUIDefaultsFileKeyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }

            if (prev_token == GUIDefaultsFileKeyword.OPEN_BRACKET) {
                if (next_token == GUIDefaultsFileKeyword.SELECTION_LAYERS) {
                    read_selection_layer_scope();
                } else if (next_token == GUIDefaultsFileKeyword.VIA_SNAP_TO_SMD_CENTER) {
                    read_via_snap_to_smd_center_scope();
                } else if (next_token == GUIDefaultsFileKeyword.SHOVE_ENABLED) {
                    read_shove_enabled_scope();
                } else if (next_token == GUIDefaultsFileKeyword.DRAG_COMPONENTS_ENABLED) {
                    read_drag_components_enabled_scope();
                } else if (next_token == GUIDefaultsFileKeyword.ROUTE_MODE) {
                    read_route_mode_scope();
                } else if (next_token == GUIDefaultsFileKeyword.PULL_TIGHT_REGION) {
                    read_pull_tight_region_scope();
                } else if (next_token == GUIDefaultsFileKeyword.PULL_TIGHT_ACCURACY) {
                    read_pull_tight_accuracy_scope();
                } else if (next_token == GUIDefaultsFileKeyword.IGNORE_CONDUCTION_AREAS) {
                    read_ignore_conduction_scope();
                } else if (next_token == GUIDefaultsFileKeyword.AUTOMATIC_LAYER_DIMMING) {
                    read_automatic_layer_dimming_scope();
                } else if (next_token == GUIDefaultsFileKeyword.CLEARANCE_COMPENSATION) {
                    read_clearance_compensation_scope();
                } else if (next_token == GUIDefaultsFileKeyword.HILIGHT_ROUTING_OBSTACLE) {
                    read_hilight_routing_obstacle_scope();
                } else if (next_token == GUIDefaultsFileKeyword.SELECTABLE_ITEMS) {
                    read_selectable_item_scope();
                } else if (next_token == GUIDefaultsFileKeyword.DESELECTED_SNAPSHOT_ATTRIBUTES) {
                    read_deselected_snapshot_attributes();
                } else {
                    // skip unknown scope
                    GUIDefaultsFileKeyword.skip_scope(scanner);
                }
            }
        }
    }

    private void write_parameter_scope() throws java.io.IOException {
        out_file.start_scope("parameter");
        write_selection_layer_scope();
        write_selectable_item_scope();
        write_via_snap_to_smd_center_scope();
        write_route_mode_scope();
        write_shove_enabled_scope();
        write_drag_components_enabled_scope();
        write_hilight_routing_obstacle_scope();
        write_pull_tight_region_scope();
        write_pull_tight_accuracy_scope();
        write_clearance_compensation_scope();
        write_ignore_conduction_scope();
        write_automatic_layer_dimming_scope();
        write_deselected_snapshot_attributes();
        out_file.end_scope();
    }

    private void read_selection_layer_scope() throws java.io.IOException, GUIDefaultsFileException {
        Object next_token = this.scanner.next_token();
        boolean select_on_all_layers;
        if (next_token == GUIDefaultsFileKeyword.ALL_VISIBLE) {
            select_on_all_layers = true;
        } else if (next_token == GUIDefaultsFileKeyword.CURRENT_ONLY) {
            select_on_all_layers = false;
        } else {
            throw new GUIDefaultsFileException("Unexpected token");
        }
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_select_on_all_visible_layers(select_on_all_layers);
    }

    private void read_shove_enabled_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean shove_enabled = GUIDefaultsFileKeyword.read_on_off(scanner);
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_push_enabled(shove_enabled);
    }

    private void read_drag_components_enabled_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean drag_components_enabled = GUIDefaultsFileKeyword.read_on_off(scanner);
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_drag_components_enabled(drag_components_enabled);
    }

    private void read_ignore_conduction_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean ignore_conduction = GUIDefaultsFileKeyword.read_on_off(scanner);
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.set_ignore_conduction(ignore_conduction);
    }

    private void write_shove_enabled_scope() throws java.io.IOException {
        out_file.start_scope("shove_enabled ");
        out_file.new_line();
        GUIDefaultsFileKeyword.write_on_off(out_file, board_handling.settings.get_push_enabled());
        out_file.end_scope();
    }

    private void write_drag_components_enabled_scope() throws java.io.IOException {
        out_file.start_scope("drag_components_enabled ");
        out_file.new_line();
        GUIDefaultsFileKeyword.write_on_off(out_file, board_handling.settings.get_drag_components_enabled());
        out_file.end_scope();
    }

    private void write_ignore_conduction_scope() throws java.io.IOException {
        out_file.start_scope("ignore_conduction_areas ");
        out_file.new_line();
        GUIDefaultsFileKeyword.write_on_off(out_file, board_handling.get_routing_board().rules.get_ignore_conduction());
        out_file.end_scope();
    }

    private void write_selection_layer_scope() throws java.io.IOException {
        out_file.start_scope("selection_layers ");
        out_file.new_line();
        if (this.board_handling.settings.get_select_on_all_visible_layers()) {
            out_file.write("all_visible");
        } else {
            out_file.write("current_only");
        }
        out_file.end_scope();
    }

    private void read_route_mode_scope() throws java.io.IOException, GUIDefaultsFileException {
        Object next_token = this.scanner.next_token();
        boolean is_stitch_mode;
        if (next_token == GUIDefaultsFileKeyword.STITCHING) {
            is_stitch_mode = true;
        } else if (next_token == GUIDefaultsFileKeyword.DYNAMIC) {
            is_stitch_mode = false;
        } else {
            throw new GUIDefaultsFileException("Unexpected token");
        }
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_stitch_route(is_stitch_mode);
    }

    private void write_route_mode_scope() throws java.io.IOException {
        out_file.start_scope("route_mode ");
        out_file.new_line();
        if (this.board_handling.settings.get_is_stitch_route()) {
            out_file.write("stitching");
        } else {
            out_file.write("dynamic");
        }
        out_file.end_scope();
    }

    private void read_pull_tight_region_scope() throws java.io.IOException, GUIDefaultsFileException {
        Object next_token = this.scanner.next_token();
        if (!(next_token instanceof Integer)) {
            throw new GUIDefaultsFileException("Integer expected");
        }
        int pull_tight_region = (Integer) next_token;
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_current_pull_tight_region_width(pull_tight_region);
    }

    private void write_pull_tight_region_scope() throws java.io.IOException {
        out_file.start_scope("pull_tight_region ");
        out_file.new_line();
        Integer pull_tight_region = this.board_handling.settings.get_trace_pull_tight_region_width();
        out_file.write(pull_tight_region.toString());
        out_file.end_scope();
    }

    private void read_pull_tight_accuracy_scope() throws java.io.IOException, GUIDefaultsFileException {
        Object next_token = this.scanner.next_token();
        if (!(next_token instanceof Integer)) {
            throw new GUIDefaultsFileException("Integer expected");
        }
        int pull_tight_accuracy = (Integer) next_token;
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_current_pull_tight_accuracy(pull_tight_accuracy);
    }

    private void write_pull_tight_accuracy_scope() throws java.io.IOException {
        out_file.start_scope("pull_tight_accuracy ");
        out_file.new_line();
        Integer pull_tight_accuracy = this.board_handling.settings.get_trace_pull_tight_accuracy();
        out_file.write(pull_tight_accuracy.toString());
        out_file.end_scope();
    }

    private void read_automatic_layer_dimming_scope() throws java.io.IOException, GUIDefaultsFileException {
        Object next_token = this.scanner.next_token();
        double intensity;
        if (next_token instanceof Double) {
            intensity = (Double) next_token;
        } else if (next_token instanceof Integer) {
            intensity = ((Number) next_token).doubleValue();
        } else {
            throw new GUIDefaultsFileException("Integer expected");
        }
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.graphics_context.set_auto_layer_dim_factor(intensity);
    }

    private void write_automatic_layer_dimming_scope() throws java.io.IOException {
        out_file.start_scope("automatic_layer_dimming ");
        out_file.new_line();
        Float layer_dimming = (float) this.board_handling.graphics_context.get_auto_layer_dim_factor();
        out_file.write(layer_dimming.toString());
        out_file.end_scope();
    }

    private void read_hilight_routing_obstacle_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean hilight_obstacle = GUIDefaultsFileKeyword.read_on_off(scanner);
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_hilight_routing_obstacle(hilight_obstacle);
    }

    private void write_hilight_routing_obstacle_scope() throws java.io.IOException {
        out_file.start_scope("hilight_routing_obstacle ");
        out_file.new_line();
        GUIDefaultsFileKeyword.write_on_off(out_file, board_handling.settings.get_hilight_routing_obstacle());
        out_file.end_scope();
    }

    private void read_clearance_compensation_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean clearance_compensation = GUIDefaultsFileKeyword.read_on_off(scanner);
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.set_clearance_compensation(clearance_compensation);
    }

    private void write_clearance_compensation_scope() throws java.io.IOException {
        out_file.start_scope("clearance_compensation ");
        out_file.new_line();
        GUIDefaultsFileKeyword.write_on_off(out_file, board_handling.get_routing_board().search_tree_manager.is_clearance_compensation_used());
        out_file.end_scope();
    }

    private void read_via_snap_to_smd_center_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean snap = GUIDefaultsFileKeyword.read_on_off(scanner);
        GUIDefaultsFileKeyword.read_keyword(scanner, GUIDefaultsFileKeyword.CLOSED_BRACKET);
        this.board_handling.settings.set_via_snap_to_smd_center(snap);
    }

    private void write_via_snap_to_smd_center_scope() throws java.io.IOException {
        out_file.start_scope("via_snap_to_smd_center ");
        out_file.new_line();
        GUIDefaultsFileKeyword.write_on_off(out_file, board_handling.settings.get_via_snap_to_smd_center());
        out_file.end_scope();
    }

    private void read_selectable_item_scope() throws java.io.IOException, GUIDefaultsFileException {
        ItemSelectionFilter item_selection_filter = this.board_handling.settings.get_item_selection_filter();
        item_selection_filter.deselect_all();
        for (;;) {
            Object next_token = this.scanner.next_token();
            if (next_token == GUIDefaultsFileKeyword.CLOSED_BRACKET) {
                break;
            }
            if (next_token == GUIDefaultsFileKeyword.TRACES) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.TRACES, true);
            } else if (next_token == GUIDefaultsFileKeyword.VIAS) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.VIAS, true);
            } else if (next_token == GUIDefaultsFileKeyword.PINS) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.PINS, true);
            } else if (next_token == GUIDefaultsFileKeyword.CONDUCTION) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.CONDUCTION, true);
            } else if (next_token == GUIDefaultsFileKeyword.KEEPOUT) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.KEEPOUT, true);
            } else if (next_token == GUIDefaultsFileKeyword.VIA_KEEPOUT) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.VIA_KEEPOUT, true);
            } else if (next_token == GUIDefaultsFileKeyword.FIXED) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.FIXED, true);
            } else if (next_token == GUIDefaultsFileKeyword.UNFIXED) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.UNFIXED, true);
            } else if (next_token == GUIDefaultsFileKeyword.VIAS) {
                item_selection_filter.set_selected(ItemSelectionFilter.SELECTABLE_CHOICES.VIAS, true);
            } else {
                throw new GUIDefaultsFileException("Unexpected token");
            }
        }
    }

    private void write_selectable_item_scope() throws java.io.IOException {
        out_file.start_scope("selectable_items ");
        out_file.new_line();
        ItemSelectionFilter item_selection_filter = this.board_handling.settings.get_item_selection_filter();
        ItemSelectionFilter.SELECTABLE_CHOICES[] selectable_choices
                = ItemSelectionFilter.SELECTABLE_CHOICES.values();
        for (int i = 0; i < selectable_choices.length; ++i) {
            if (item_selection_filter.is_selected(selectable_choices[i])) {
                out_file.write(selectable_choices[i].toString());
                out_file.write(" ");
            }
        }
        out_file.end_scope();
    }

    private void write_deselected_snapshot_attributes() throws java.io.IOException {
        SnapshotAttributes attributes = this.board_handling.settings.get_snapshot_attributes();
        out_file.start_scope("deselected_snapshot_attributes ");
        for (SnapshotAttributeKey key : SnapshotAttributeKey.values()) {
            if (key != SnapshotAttributeKey.INFO_LIST_SELECTIONS && !attributes.get(key)) {
                out_file.new_line();
                out_file.write(key.name().toLowerCase(Locale.ENGLISH) + " ");
            }
        }
        out_file.end_scope();
    }

    private void read_deselected_snapshot_attributes() throws java.io.IOException, GUIDefaultsFileException {
        SnapshotAttributes attributes = this.board_handling.settings.get_snapshot_attributes();
        for (;;) {
            Object next_token = this.scanner.next_token();
            if (next_token == GUIDefaultsFileKeyword.CLOSED_BRACKET) {
                break;
            }
            try {
                attributes.set(SnapshotAttributeKey.valueOf(next_token.toString()), Boolean.FALSE);
            } catch (IllegalArgumentException | NullPointerException ex) {
                throw new GUIDefaultsFileException("Unexpected token");
            }
        }
    }
}
