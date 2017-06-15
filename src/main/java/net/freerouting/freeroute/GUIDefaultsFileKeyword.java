/*
 * Copyright (C) 2017 Robert Antoni Buj Gelonch {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.freerouting.freeroute;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.freerouting.freeroute.datastructures.IndentFileWriter;
import net.freerouting.freeroute.interactive.BoardHandling;

/**
 * Keywords in the gui defaults file.
 *
 * @author Robert Buj
 */
enum GUIDefaultsFileKeyword {
    BACKGROUND {
        @Override
        public void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
                throws java.io.IOException, GUIDefaultsFileException {
            java.awt.Color curr_color = read_color_not_null(scanner);
            board_handling.graphics_context.other_color_table.set_background_color(curr_color);
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
            return board_frame.savable_subwindows.get(SavableSubwindowKey.COLOR_MANAGER);
        }
    },
    OBJECT_VISIBILITY {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.OBJECT_VISIBILITY);
        }
    },
    LAYER_VISIBILITY {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.LAYER_VISIBILITY);
        }
    },
    DISPLAY_MISCELLANIOUS {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.DISPLAY_MISC);
        }
    },
    SNAPSHOTS {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.SNAPSHOT);
        }
    },
    SELECT_PARAMETER {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.SELECT_PARAMETER);
        }
    },
    ROUTE_PARAMETER {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.ROUTE_PARAMETER);
        }
    },
    MANUAL_RULES {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            WindowRouteParameter route_parameter_window = (WindowRouteParameter) board_frame.savable_subwindows.get(SavableSubwindowKey.ROUTE_PARAMETER);
            return route_parameter_window.manual_rule_window;
        }
    },
    ROUTE_DETAILS {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            WindowRouteParameter route_parameter_window = (WindowRouteParameter) board_frame.savable_subwindows.get(SavableSubwindowKey.ROUTE_PARAMETER);
            return route_parameter_window.detail_window;
        }
    },
    MOVE_PARAMETER {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.MOVE_PARAMETER);
        }
    },
    CLEARANCE_MATRIX {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.CLEARANCE_MATRIX);
        }
    },
    VIA_RULES {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.VIA);
        }
    },
    EDIT_VIAS {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.EDIT_VIAS);
        }
    },
    EDIT_NET_RULES {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.EDIT_NET_RULES);
        }
    },
    ASSIGN_NET_RULES {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.ASSIGN_NET_CLASSES);
        }
    },
    PADSTACK_INFO {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.PADSTACKS);
        }
    },
    PACKAGE_INFO {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.PACKAGES);
        }
    },
    COMPONENT_INFO {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.COMPONENTS);
        }
    },
    NET_INFO {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.NET_INFO);
        }
    },
    INCOMPLETES_INFO {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.INCOMPLETES);
        }
    },
    VIOLATIONS_INFO {
        @Override
        public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
            return board_frame.savable_subwindows.get(SavableSubwindowKey.CLEARANCE_VIOLATIONS);
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
     * reads a java.awt.Color from the defaults file. Returns null, if no valid
     * color was found.
     */
    private static java.awt.Color read_color(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
        int[] rgb_color_arr = new int[3];
        for (int i = 0; i < 3; ++i) {
            Object next_token = scanner.next_token();
            if (!(next_token instanceof Integer)) {
                if (next_token != GUIDefaultsFileKeyword.CLOSED_BRACKET) {
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
     * reads a n array java.awt.Color from the defaults file. Returns null, if
     * no valid colors were found.
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
        return color_list.stream().toArray(java.awt.Color[]::new);
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
    static void skip_scope(GUIDefaultsScanner p_scanner) throws GUIDefaultsFileException {
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
            if (curr_token == GUIDefaultsFileKeyword.OPEN_BRACKET) {
                ++open_bracked_count;
            } else if (curr_token == GUIDefaultsFileKeyword.CLOSED_BRACKET) {
                --open_bracked_count;
            }
        }
        Logger.getLogger(GUIDefaultsFile.class.getName()).log(Level.INFO, "Unknown scope skipped");
    }

    static void read_frame_scope(GUIDefaultsFileKeyword keyword, GUIDefaultsScanner scanner, BoardFrame board_frame)
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

    static void read_keyword(GUIDefaultsScanner scanner, GUIDefaultsFileKeyword keyword) throws GUIDefaultsFileException, IOException {
        Object next_token = scanner.next_token();
        if (next_token != keyword) {
            throw new GUIDefaultsFileException(keyword.name() + " expected");
        }
    }

    static boolean read_on_off(GUIDefaultsScanner scanner) throws java.io.IOException, GUIDefaultsFileException {
        Object next_token = scanner.next_token();
        boolean value;
        if (next_token == GUIDefaultsFileKeyword.ON) {
            value = true;
        } else if (next_token == GUIDefaultsFileKeyword.OFF) {
            value = false;
        } else {
            throw new GUIDefaultsFileException("Unexpected token");
        }
        return value;
    }

    static void write_on_off(IndentFileWriter out_file, boolean value) throws IOException {
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

    static void write_visible(IndentFileWriter out_file, boolean value) throws IOException {
        if (value) {
            out_file.write("visible");
        } else {
            out_file.write("not_visible");
        }
    }

    void read_color_setting(GUIDefaultsScanner scanner, BoardHandling board_handling)
            throws java.io.IOException, GUIDefaultsFileException {
        skip_scope(scanner);
    }

    public javax.swing.JFrame get_frame(BoardFrame board_frame) throws GUIDefaultsFileException {
        throw new GUIDefaultsFileException("Unknown frame");
    }
}
