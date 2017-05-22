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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        Keyword.read_keyword(scanner, Keyword.OPEN_BRACKET);
        Keyword.read_keyword(scanner, Keyword.GUI_DEFAULTS);
        // read the direct subscopes of the gui_defaults scope
        Object next_token = null;
        for (;;) {
            Object prev_token = next_token;
            next_token = this.scanner.next_token();
            if (next_token == null) {
                // end of file
                return;
            }
            if (next_token == Keyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }
            if (prev_token == Keyword.OPEN_BRACKET) {
                if (next_token == Keyword.COLORS) {
                    read_colors_scope();
                } else if (next_token == Keyword.WINDOWS) {
                    read_windows_scope();
                } else if (next_token == Keyword.PARAMETER) {
                    read_parameter_scope();
                } else {
                    // overread all scopes except the routes scope for the time being
                    Keyword.skip_scope(scanner);
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
            if (next_token == Keyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }
            if (prev_token == Keyword.OPEN_BRACKET) {
                if (next_token instanceof Keyword) {
                    Keyword.read_frame_scope((Keyword) next_token, scanner, board_frame);
                } else {
                    throw new GUIDefaultsFileException("Keyword expected");
                }
            }
        }
    }

    private void write_windows_scope() throws java.io.IOException {
        out_file.start_scope("windows");
        write_frame_scope(this.board_frame, "board_frame");
        write_frame_scope(this.board_frame.color_manager, "color_manager");
        write_frame_scope(this.board_frame.layer_visibility_window, "layer_visibility");
        write_frame_scope(this.board_frame.object_visibility_window, "object_visibility");
        write_frame_scope(this.board_frame.display_misc_window, "display_miscellanious");
        write_frame_scope(this.board_frame.snapshot_window, "snapshots");
        write_frame_scope(this.board_frame.select_parameter_window, "select_parameter");
        write_frame_scope(this.board_frame.route_parameter_window, "route_parameter");
        write_frame_scope(this.board_frame.route_parameter_window.manual_rule_window, "manual_rules");
        write_frame_scope(this.board_frame.route_parameter_window.detail_window, "route_details");
        write_frame_scope(this.board_frame.move_parameter_window, "move_parameter");
        write_frame_scope(this.board_frame.clearance_matrix_window, "clearance_matrix");
        write_frame_scope(this.board_frame.via_window, "via_rules");
        write_frame_scope(this.board_frame.edit_vias_window, "edit_vias");
        write_frame_scope(this.board_frame.edit_net_rules_window, "edit_net_rules");
        write_frame_scope(this.board_frame.assign_net_classes_window, "assign_net_rules");
        write_frame_scope(this.board_frame.padstacks_window, "padstack_info");
        write_frame_scope(this.board_frame.packages_window, "package_info");
        write_frame_scope(this.board_frame.components_window, "component_info");
        write_frame_scope(this.board_frame.net_info_window, "net_info");
        write_frame_scope(this.board_frame.incompletes_window, "incompletes_info");
        write_frame_scope(this.board_frame.clearance_violations_window, "violations_info");
        out_file.end_scope();
    }

    private void write_frame_scope(javax.swing.JFrame p_frame, String p_frame_name)
            throws java.io.IOException {
        out_file.start_scope(p_frame_name);
        out_file.new_line();
        Keyword.write_visible(out_file, p_frame.isVisible());
        write_bounds(p_frame.getBounds());
        out_file.end_scope();
    }

    private void write_bounds(Rectangle2D p_bounds) throws java.io.IOException {
        out_file.start_scope("bounds");
        out_file.new_line();
        Integer x = (int) p_bounds.getX();
        out_file.write(x.toString());
        Integer y = (int) p_bounds.getY();
        out_file.write(" ");
        out_file.write(y.toString());
        Integer width = (int) p_bounds.getWidth();
        out_file.write(" ");
        out_file.write(width.toString());
        Integer height = (int) p_bounds.getHeight();
        out_file.write(" ");
        out_file.write(height.toString());
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
            if (next_token == Keyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }

            if (prev_token == Keyword.OPEN_BRACKET) {
                if (next_token instanceof Keyword) {
                    ((Keyword) next_token).read_color_setting(scanner, board_handling);
                } else {
                    // skip unknown scope
                    Keyword.skip_scope(scanner);
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
            if (next_token == Keyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }

            if (prev_token == Keyword.OPEN_BRACKET) {
                if (next_token == Keyword.SELECTION_LAYERS) {
                    read_selection_layer_scope();
                } else if (next_token == Keyword.VIA_SNAP_TO_SMD_CENTER) {
                    read_via_snap_to_smd_center_scope();
                } else if (next_token == Keyword.SHOVE_ENABLED) {
                    read_shove_enabled_scope();
                } else if (next_token == Keyword.DRAG_COMPONENTS_ENABLED) {
                    read_drag_components_enabled_scope();
                } else if (next_token == Keyword.ROUTE_MODE) {
                    read_route_mode_scope();
                } else if (next_token == Keyword.PULL_TIGHT_REGION) {
                    read_pull_tight_region_scope();
                } else if (next_token == Keyword.PULL_TIGHT_ACCURACY) {
                    read_pull_tight_accuracy_scope();
                } else if (next_token == Keyword.IGNORE_CONDUCTION_AREAS) {
                    read_ignore_conduction_scope();
                } else if (next_token == Keyword.AUTOMATIC_LAYER_DIMMING) {
                    read_automatic_layer_dimming_scope();
                } else if (next_token == Keyword.CLEARANCE_COMPENSATION) {
                    read_clearance_compensation_scope();
                } else if (next_token == Keyword.HILIGHT_ROUTING_OBSTACLE) {
                    read_hilight_routing_obstacle_scope();
                } else if (next_token == Keyword.SELECTABLE_ITEMS) {
                    read_selectable_item_scope();
                } else if (next_token == Keyword.DESELECTED_SNAPSHOT_ATTRIBUTES) {
                    read_deselected_snapshot_attributes();
                } else {
                    // skip unknown scope
                    Keyword.skip_scope(scanner);
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
        if (next_token == Keyword.ALL_VISIBLE) {
            select_on_all_layers = true;
        } else if (next_token == Keyword.CURRENT_ONLY) {
            select_on_all_layers = false;
        } else {
            throw new GUIDefaultsFileException("Unexpected token");
        }
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
        this.board_handling.settings.set_select_on_all_visible_layers(select_on_all_layers);
    }

    private void read_shove_enabled_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean shove_enabled = Keyword.read_on_off(scanner);
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
        this.board_handling.settings.set_push_enabled(shove_enabled);
    }

    private void read_drag_components_enabled_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean drag_components_enabled = Keyword.read_on_off(scanner);
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
        this.board_handling.settings.set_drag_components_enabled(drag_components_enabled);
    }

    private void read_ignore_conduction_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean ignore_conduction = Keyword.read_on_off(scanner);
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
        this.board_handling.set_ignore_conduction(ignore_conduction);
    }

    private void write_shove_enabled_scope() throws java.io.IOException {
        out_file.start_scope("shove_enabled ");
        out_file.new_line();
        Keyword.write_on_off(out_file, board_handling.settings.get_push_enabled());
        out_file.end_scope();
    }

    private void write_drag_components_enabled_scope() throws java.io.IOException {
        out_file.start_scope("drag_components_enabled ");
        out_file.new_line();
        Keyword.write_on_off(out_file, board_handling.settings.get_drag_components_enabled());
        out_file.end_scope();
    }

    private void write_ignore_conduction_scope() throws java.io.IOException {
        out_file.start_scope("ignore_conduction_areas ");
        out_file.new_line();
        Keyword.write_on_off(out_file, board_handling.get_routing_board().rules.get_ignore_conduction());
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
        if (next_token == Keyword.STITCHING) {
            is_stitch_mode = true;
        } else if (next_token == Keyword.DYNAMIC) {
            is_stitch_mode = false;
        } else {
            throw new GUIDefaultsFileException("Unexpected token");
        }
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
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
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
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
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
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
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
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
        boolean hilight_obstacle = Keyword.read_on_off(scanner);
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
        this.board_handling.settings.set_hilight_routing_obstacle(hilight_obstacle);
    }

    private void write_hilight_routing_obstacle_scope() throws java.io.IOException {
        out_file.start_scope("hilight_routing_obstacle ");
        out_file.new_line();
        Keyword.write_on_off(out_file, board_handling.settings.get_hilight_routing_obstacle());
        out_file.end_scope();
    }

    private void read_clearance_compensation_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean clearance_compensation = Keyword.read_on_off(scanner);
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
        this.board_handling.set_clearance_compensation(clearance_compensation);
    }

    private void write_clearance_compensation_scope() throws java.io.IOException {
        out_file.start_scope("clearance_compensation ");
        out_file.new_line();
        Keyword.write_on_off(out_file, board_handling.get_routing_board().search_tree_manager.is_clearance_compensation_used());
        out_file.end_scope();
    }

    private void read_via_snap_to_smd_center_scope() throws java.io.IOException, GUIDefaultsFileException {
        boolean snap = Keyword.read_on_off(scanner);
        Keyword.read_keyword(scanner, Keyword.CLOSED_BRACKET);
        this.board_handling.settings.set_via_snap_to_smd_center(snap);
    }

    private void write_via_snap_to_smd_center_scope() throws java.io.IOException {
        out_file.start_scope("via_snap_to_smd_center ");
        out_file.new_line();
        Keyword.write_on_off(out_file, board_handling.settings.get_via_snap_to_smd_center());
        out_file.end_scope();
    }

    private void read_selectable_item_scope() throws java.io.IOException, GUIDefaultsFileException {
        ItemSelectionFilter item_selection_filter = this.board_handling.settings.get_item_selection_filter();
        item_selection_filter.deselect_all();
        for (;;) {
            Object next_token = this.scanner.next_token();
            if (next_token == Keyword.CLOSED_BRACKET) {
                break;
            }
            if (next_token == Keyword.TRACES) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.TRACES, true);
            } else if (next_token == Keyword.VIAS) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.VIAS, true);
            } else if (next_token == Keyword.PINS) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.PINS, true);
            } else if (next_token == Keyword.CONDUCTION) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.CONDUCTION, true);
            } else if (next_token == Keyword.KEEPOUT) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.KEEPOUT, true);
            } else if (next_token == Keyword.VIA_KEEPOUT) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.VIA_KEEPOUT, true);
            } else if (next_token == Keyword.FIXED) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.FIXED, true);
            } else if (next_token == Keyword.UNFIXED) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.UNFIXED, true);
            } else if (next_token == Keyword.VIAS) {
                item_selection_filter.set_selected(ItemSelectionFilter.SelectableChoices.VIAS, true);
            } else {
                throw new GUIDefaultsFileException("Unexpected token");
            }
        }
    }

    private void write_selectable_item_scope() throws java.io.IOException {
        out_file.start_scope("selectable_items ");
        out_file.new_line();
        ItemSelectionFilter item_selection_filter = this.board_handling.settings.get_item_selection_filter();
        ItemSelectionFilter.SelectableChoices[] selectable_choices
                = ItemSelectionFilter.SelectableChoices.values();
        for (int i = 0; i < selectable_choices.length; ++i) {
            if (item_selection_filter.is_selected(selectable_choices[i])) {
                out_file.write(selectable_choices[i].toString());
                out_file.write(" ");
            }
        }
        out_file.end_scope();
    }

    private void write_deselected_snapshot_attributes() throws java.io.IOException {
        net.freerouting.freeroute.interactive.SnapShot.Attributes attributes = this.board_handling.settings.get_snapshot_attributes();
        out_file.start_scope("deselected_snapshot_attributes ");
        if (!attributes.object_colors) {
            out_file.new_line();
            out_file.write("object_colors ");
        }
        if (!attributes.object_visibility) {
            out_file.new_line();
            out_file.write("object_visibility ");
        }
        if (!attributes.layer_visibility) {
            out_file.new_line();
            out_file.write("layer_visibility ");
        }
        if (!attributes.display_region) {
            out_file.new_line();
            out_file.write("display_region ");
        }
        if (!attributes.interactive_state) {
            out_file.new_line();
            out_file.write("interactive_state ");
        }
        if (!attributes.selection_layers) {
            out_file.new_line();
            out_file.write("selection_layers ");
        }
        if (!attributes.selectable_items) {
            out_file.new_line();
            out_file.write("selectable_items ");
        }
        if (!attributes.current_layer) {
            out_file.new_line();
            out_file.write("current_layer ");
        }
        if (!attributes.rule_selection) {
            out_file.new_line();
            out_file.write("rule_selection ");
        }
        if (!attributes.manual_rule_settings) {
            out_file.new_line();
            out_file.write("manual_rule_settings ");
        }
        if (!attributes.push_and_shove_enabled) {
            out_file.new_line();
            out_file.write("push_and_shove_enabled ");
        }
        if (!attributes.drag_components_enabled) {
            out_file.new_line();
            out_file.write("drag_components_enabled ");
        }
        if (!attributes.pull_tight_region) {
            out_file.new_line();
            out_file.write("pull_tight_region ");
        }
        if (!attributes.component_grid) {
            out_file.new_line();
            out_file.write("component_grid ");
        }
        out_file.end_scope();
    }

    private void read_deselected_snapshot_attributes() throws java.io.IOException, GUIDefaultsFileException {
        net.freerouting.freeroute.interactive.SnapShot.Attributes attributes = this.board_handling.settings.get_snapshot_attributes();
        for (;;) {
            Object next_token = this.scanner.next_token();
            if (next_token == Keyword.CLOSED_BRACKET) {
                break;
            }
            if (next_token == Keyword.OBJECT_COLORS) {
                attributes.object_colors = false;
            } else if (next_token == Keyword.OBJECT_VISIBILITY) {
                attributes.object_visibility = false;
            } else if (next_token == Keyword.LAYER_VISIBILITY) {
                attributes.layer_visibility = false;
            } else if (next_token == Keyword.DISPLAY_REGION) {
                attributes.display_region = false;
            } else if (next_token == Keyword.INTERACTIVE_STATE) {
                attributes.interactive_state = false;
            } else if (next_token == Keyword.SELECTION_LAYERS) {
                attributes.selection_layers = false;
            } else if (next_token == Keyword.SELECTABLE_ITEMS) {
                attributes.selectable_items = false;
            } else if (next_token == Keyword.CURRENT_LAYER) {
                attributes.current_layer = false;
            } else if (next_token == Keyword.RULE_SELECTION) {
                attributes.rule_selection = false;
            } else if (next_token == Keyword.MANUAL_RULE_SETTINGS) {
                attributes.manual_rule_settings = false;
            } else if (next_token == Keyword.PUSH_AND_SHOVE_ENABLED) {
                attributes.push_and_shove_enabled = false;
            } else if (next_token == Keyword.DRAG_COMPONENTS_ENABLED) {
                attributes.drag_components_enabled = false;
            } else if (next_token == Keyword.PULL_TIGHT_REGION) {
                attributes.pull_tight_region = false;
            } else if (next_token == Keyword.COMPONENT_GRID) {
                attributes.component_grid = false;
            } else {
                throw new GUIDefaultsFileException("Unexpected token");
            }
        }
    }

    /**
     * Keywords in the gui defaults file.
     */
    enum Keyword {
        BACKGROUND {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_background_color(curr_color);
//                board_frame.set_board_background(curr_color);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        CONDUCTION {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_conduction_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_conduction_colors(curr_colors);
            }
        },
        HILIGHT {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_hilight_color_intensity(intensity);
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_hilight_color(curr_color);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        INCOMPLETES {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_incomplete_color_intensity(intensity);
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_incomplete_color(curr_color);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        KEEPOUT {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_obstacle_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_keepout_colors(curr_colors);
            }
        },
        OUTLINE {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_outline_color(curr_color);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        COMPONENT_BACK {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_component_color(curr_color, false);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        COMPONENT_FRONT {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_component_color(curr_color, true);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        LENGTH_MATCHING {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_length_matching_area_color_intensity(intensity);
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_length_matching_area_color(curr_color);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        PINS {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_pin_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_pin_colors(curr_colors);
            }
        },
        TRACES {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_trace_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_trace_colors(curr_colors, false);
            }
        },
        FIXED_TRACES {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_trace_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_trace_colors(curr_colors, true);
            }
        },
        VIA_KEEPOUT {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_via_obstacle_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_via_keepout_colors(curr_colors);
            }
        },
        VIAS {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_via_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_via_colors(curr_colors, false);
            }
        },
        FIXED_VIAS {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                double intensity = read_color_intensity(scanner);
                board_handling.graphics_context.set_via_color_intensity(intensity);
                java.awt.Color[] curr_colors = read_color_array_not_null(scanner);
                board_handling.graphics_context.item_color_table.set_via_colors(curr_colors, true);
            }
        },
        VIOLATIONS {
            @Override
            public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                    throws java.io.IOException, GUIDefaultsFileException {
                java.awt.Color curr_color = read_color_not_null(scanner);
                board_handling.graphics_context.other_color_table.set_violations_color(curr_color);
                read_keyword(scanner, CLOSED_BRACKET);
            }
        },
        ALL_VISIBLE, AUTOMATIC_LAYER_DIMMING, BOUNDS, CLEARANCE_COMPENSATION, CLOSED_BRACKET, COLORS,
        COMPONENT_GRID, CURRENT_LAYER, CURRENT_ONLY, DESELECTED_SNAPSHOT_ATTRIBUTES, DISPLAY_REGION,
        DRAG_COMPONENTS_ENABLED, DYNAMIC, FIXED, FORTYFIVE_DEGREE, GUI_DEFAULTS, HILIGHT_ROUTING_OBSTACLE,
        IGNORE_CONDUCTION_AREAS, INTERACTIVE_STATE, MANUAL_RULE_SETTINGS, NINETY_DEGREE, NONE,
        NOT_VISIBLE, OBJECT_COLORS, OPEN_BRACKET, OFF, ON, PARAMETER, PULL_TIGHT_ACCURACY,
        PULL_TIGHT_REGION, PUSH_AND_SHOVE_ENABLED, ROUTE_MODE, RULE_SELECTION, SELECTABLE_ITEMS,
        SELECTION_LAYERS, SHOVE_ENABLED, STITCHING, UNFIXED, VISIBLE, VIA_SNAP_TO_SMD_CENTER, WINDOWS,
        BOARD_FRAME {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame;
            }
        },
        COLOR_MANAGER {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.color_manager;
            }
        },
        OBJECT_VISIBILITY {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.object_visibility_window;
            }
        },
        LAYER_VISIBILITY {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.layer_visibility_window;
            }
        },
        DISPLAY_MISCELLANIOUS {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.display_misc_window;
            }
        },
        SNAPSHOTS {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.snapshot_window;
            }
        },
        SELECT_PARAMETER {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.select_parameter_window;
            }
        },
        ROUTE_PARAMETER {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.route_parameter_window;
            }
        },
        MANUAL_RULES {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.route_parameter_window.manual_rule_window;
            }
        },
        ROUTE_DETAILS {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.route_parameter_window.detail_window;
            }
        },
        MOVE_PARAMETER {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.move_parameter_window;
            }
        },
        CLEARANCE_MATRIX {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.clearance_matrix_window;
            }
        },
        VIA_RULES {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.via_window;
            }
        },
        EDIT_VIAS {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.edit_vias_window;
            }
        },
        EDIT_NET_RULES {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.edit_net_rules_window;
            }
        },
        ASSIGN_NET_RULES {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.assign_net_classes_window;
            }
        },
        PADSTACK_INFO {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.padstacks_window;
            }
        },
        PACKAGE_INFO {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.packages_window;
            }
        },
        COMPONENT_INFO {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.components_window;
            }
        },
        NET_INFO {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.net_info_window;
            }
        },
        INCOMPLETES_INFO {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.incompletes_window;
            }
        },
        VIOLATIONS_INFO {
            @Override
            public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
                return board_frame.clearance_violations_window;
            }
        };

        private static java.awt.Color read_color_not_null(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
            java.awt.Color color = read_color(scanner);
            if (color == null) {
                throw new GUIDefaultsFileException("color == null");
            }
            return color;
        }

        /**
         * reads a java.awt.Color from the defaults file. Returns null, if no
         * valid color was found.
         */
        private static java.awt.Color read_color(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
            int[] rgb_color_arr = new int[3];
            for (int i = 0; i < 3; ++i) {
                Object next_token = scanner.next_token();
                if (!(next_token instanceof Integer)) {
                    if (next_token != Keyword.CLOSED_BRACKET) {
                        throw new GUIDefaultsFileException("CLOSED_BRACKET expected");
                    }
                    return null;
                }
                rgb_color_arr[i] = (Integer) next_token;
            }
            return new java.awt.Color(rgb_color_arr[0], rgb_color_arr[1], rgb_color_arr[2]);
        }

        private static java.awt.Color[] read_color_array_not_null(GUIDefaultsScanner scanner)
                throws java.io.IOException, GUIDefaultsFileException {
            java.awt.Color[] colors = read_color_array(scanner);
            if (colors.length < 1) {
                throw new GUIDefaultsFileException("colors.length < 1");
            }
            return colors;
        }

        /**
         * reads a n array java.awt.Color from the defaults file. Returns null,
         * if no valid colors were found.
         */
        private static java.awt.Color[] read_color_array(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
            java.util.Collection<java.awt.Color> color_list = new java.util.LinkedList<>();
            for (;;) {
                java.awt.Color curr_color = read_color(scanner);
                if (curr_color == null) {
                    break;
                }
                color_list.add(curr_color);
            }
            java.awt.Color[] result = new java.awt.Color[color_list.size()];
            java.util.Iterator<java.awt.Color> it = color_list.iterator();
            for (int i = 0; i < result.length; ++i) {
                result[i] = it.next();
            }
            return result;
        }

        private static double read_color_intensity(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
            double result;
            Object next_token = scanner.next_token();
            if (next_token instanceof Double) {
                result = (Double) next_token;
            } else if (next_token instanceof Integer) {
                result = ((Number) next_token).doubleValue();
            } else {
                throw new GUIDefaultsFileException("Number expected");
            }
            if (result < 0) {
                throw new GUIDefaultsFileException("intensity < 0");
            }
            return result;
        }

        /**
         * Skips the current scope. Returns false, if no legal scope was found.
         */
        private static void skip_scope(GUIDefaultsScanner p_scanner) throws GUIDefaultsFileException {
            int open_bracked_count = 1;
            while (open_bracked_count > 0) {
                Object curr_token;
                try {
                    curr_token = p_scanner.next_token();
                } catch (IOException ex) {
                    throw new GUIDefaultsFileException("Error while scanning file", ex);
                }
                if (curr_token == null) {
                    return; // end of file
                }
                if (curr_token == Keyword.OPEN_BRACKET) {
                    ++open_bracked_count;
                } else if (curr_token == Keyword.CLOSED_BRACKET) {
                    --open_bracked_count;
                }
            }
            Logger.getLogger(GUIDefaultsFile.class.getName()).log(Level.INFO, "Unknown scope skipped");
        }

        private static void read_frame_scope(Keyword keyword, GUIDefaultsScanner scanner, BoardFrame board_frame)
                throws java.io.IOException, GUIDefaultsFileException {
            boolean is_visible = read_visible(scanner);
            read_keyword(scanner, OPEN_BRACKET);
            read_keyword(scanner, BOUNDS);
            Rectangle2D bounds = read_rectangle(scanner);
            read_keyword(scanner, CLOSED_BRACKET);
            read_keyword(scanner, CLOSED_BRACKET);
            javax.swing.JFrame curr_frame = keyword.get_frame(board_frame);
            if (null != curr_frame) {
                curr_frame.setVisible(is_visible);
                if (keyword == BOARD_FRAME) {
                    curr_frame.setBounds(bounds.getBounds());
                } else {
                    // Set only the location.
                    // Do not change the size of the frame because it depends on the layer count.
                    curr_frame.setLocation(bounds.getBounds().getLocation());
                }
            } else {
                throw new GUIDefaultsFileException("Null frame");
            }
        }

        private static Rectangle2D read_rectangle(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
            int[] coor = new int[4];
            for (int i = 0; i < 4; ++i) {
                Object next_token = scanner.next_token();
                if (next_token instanceof Integer) {
                    coor[i] = (Integer) next_token;
                } else {
                    throw new GUIDefaultsFileException("Integer expected");
                }
            }
            return new Rectangle2D.Double(coor[0], coor[1], coor[2], coor[3]);
        }

        private static void read_keyword(GUIDefaultsScanner scanner, Keyword keyword) throws GUIDefaultsFileException, IOException {
            Object next_token = scanner.next_token();
            if (next_token != keyword) {
                throw new GUIDefaultsFileException(keyword.name() + " expected");
            }
        }

        public static boolean read_on_off(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
            Object next_token = scanner.next_token();
            boolean value;
            if (next_token == Keyword.ON) {
                value = true;
            } else if (next_token == Keyword.OFF) {
                value = false;
            } else {
                throw new GUIDefaultsFileException("Unexpected token");
            }
            return value;
        }

        public static void write_on_off(IndentFileWriter out_file, boolean value) throws IOException {
            if (value) {
                out_file.write("on");
            } else {
                out_file.write("off");
            }
        }

        private static boolean read_visible(GUIDefaultsScanner scanner) throws GUIDefaultsFileException, IOException {
            Object next_token = scanner.next_token();
            boolean is_visible;
            if (next_token == VISIBLE) {
                is_visible = true;
            } else if (next_token == NOT_VISIBLE) {
                is_visible = false;
            } else {
                throw new GUIDefaultsFileException("VISIBLE or NOT_VISIBLE expected");
            }
            return is_visible;
        }

        public static void write_visible(IndentFileWriter out_file, boolean value) throws IOException {
            if (value) {
                out_file.write("visible");
            } else {
                out_file.write("not_visible");
            }
        }

        public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                throws java.io.IOException, GUIDefaultsFileException {
            skip_scope(scanner);
        }

        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            throw new GUIDefaultsFileException("Unknown frame");
        }
    }
}
