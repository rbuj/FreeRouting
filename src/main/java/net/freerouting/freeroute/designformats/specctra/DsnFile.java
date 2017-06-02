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
 * InputDsnFile.java
 *
 * Created on 10. Mai 2004, 07:43
 */
package net.freerouting.freeroute.designformats.specctra;

import java.io.OutputStream;
import java.io.Reader;
import net.freerouting.freeroute.board.BasicBoard;
import net.freerouting.freeroute.board.BoardObservers;
import net.freerouting.freeroute.board.TestLevel;
import net.freerouting.freeroute.datastructures.IdNoGenerator;
import net.freerouting.freeroute.datastructures.IndentFileWriter;
import net.freerouting.freeroute.interactive.BoardHandling;

/**
 * Class for reading and writing dsn-files.
 *
 * @author alfons
 */
public class DsnFile {

    static final String CLASS_CLEARANCE_SEPARATOR = "-";

    private DsnFile() {
        // not called
    }

    /**
     * Creates a routing board from a Specctra dns file. The parameters
     * p_item_observers and p_item_id_no_generator are used, in case the board
     * is embedded into a host system. Returns false, if an error occured.
     */
    public static void read(Reader p_reader, BoardHandling p_board_handling,
            BoardObservers p_observers, IdNoGenerator p_item_id_no_generator,
            TestLevel p_test_level) throws DsnFileException {
        Scanner scanner = new SpecctraFileScanner(p_reader);
        Object curr_token;
        for (int i = 0; i < 3; ++i) {
            try {
                curr_token = scanner.next_token();
            } catch (java.io.IOException exc) {
                throw new DsnFileException("DsnFile.read: IO error scanning file", exc);
            }
            boolean keyword_ok = true;
            if (i == 0) {
                keyword_ok = (curr_token == Keyword.OPEN_BRACKET);
            } else if (i == 1) {
                keyword_ok = (curr_token == Keyword.PCB_SCOPE);
                scanner.yybegin(SpecctraFileScanner.NAME); // to overread the name of the pcb for i = 2
            }
            if (!keyword_ok) {
                throw new DsnFileException("DsnFile.read: specctra dsn file format expected");
            }
        }
        ReadScopeParameter read_scope_par
                = new ReadScopeParameter(scanner, p_board_handling, p_observers, p_item_id_no_generator, p_test_level);
        boolean read_ok = Keyword.PCB_SCOPE.read_scope(read_scope_par);
        if (read_ok && read_scope_par.autoroute_settings == null) {
            // look for power planes with incorrect layer type and adjust autoroute parameters
            adjust_plane_autoroute_settings(p_board_handling);
        }
    }

    /**
     * Sets contains_plane to true for nets with a conduction_area covering a
     * large part of a signal layer, if that layer does not contain any traces
     * This is useful in case the layer type was not set correctly to plane in
     * the dsn-file. Returns true, if something was changed.
     */
    private static boolean adjust_plane_autoroute_settings(net.freerouting.freeroute.interactive.BoardHandling p_board_handling) {
        BasicBoard routing_board = p_board_handling.get_routing_board();
        net.freerouting.freeroute.board.LayerStructure board_layer_structure = routing_board.layer_structure;
        if (board_layer_structure.arr.length <= 2) {
            return false;
        }
        for (net.freerouting.freeroute.board.Layer curr_layer : board_layer_structure.arr) {
            if (!curr_layer.is_signal) {
                return false;
            }
        }
        boolean[] layer_contains_wires_arr = new boolean[board_layer_structure.arr.length];
        boolean[] changed_layer_arr = new boolean[board_layer_structure.arr.length];
        for (int i = 0; i < layer_contains_wires_arr.length; ++i) {
            layer_contains_wires_arr[i] = false;
            changed_layer_arr[i] = false;
        }
        java.util.Collection<net.freerouting.freeroute.board.ConductionArea> conduction_area_list = new java.util.LinkedList<>();
        java.util.Collection<net.freerouting.freeroute.board.Item> item_list = routing_board.get_items();
        for (net.freerouting.freeroute.board.Item curr_item : item_list) {
            if (curr_item instanceof net.freerouting.freeroute.board.Trace) {
                int curr_layer = ((net.freerouting.freeroute.board.Trace) curr_item).get_layer();
                layer_contains_wires_arr[curr_layer] = true;
            } else if (curr_item instanceof net.freerouting.freeroute.board.ConductionArea) {
                conduction_area_list.add((net.freerouting.freeroute.board.ConductionArea) curr_item);
            }
        }
        boolean nothing_changed = true;

        net.freerouting.freeroute.board.BoardOutline board_outline = routing_board.get_outline();
        double board_area = 0;
        for (int i = 0; i < board_outline.shape_count(); ++i) {
            net.freerouting.freeroute.geometry.planar.TileShape[] curr_piece_arr = board_outline.get_shape(i).split_to_convex();
            if (curr_piece_arr != null) {
                for (net.freerouting.freeroute.geometry.planar.TileShape curr_piece : curr_piece_arr) {
                    board_area += curr_piece.area();
                }
            }
        }
        for (net.freerouting.freeroute.board.ConductionArea curr_conduction_area : conduction_area_list) {
            int layer_no = curr_conduction_area.get_layer();
            if (layer_contains_wires_arr[layer_no]) {
                continue;
            }
            net.freerouting.freeroute.board.Layer curr_layer = routing_board.layer_structure.arr[layer_no];
            if (!curr_layer.is_signal || layer_no == 0 || layer_no == board_layer_structure.arr.length - 1) {
                continue;
            }
            net.freerouting.freeroute.geometry.planar.TileShape[] convex_pieces = curr_conduction_area.get_area().split_to_convex();
            double curr_area = 0;
            for (net.freerouting.freeroute.geometry.planar.TileShape curr_piece : convex_pieces) {
                curr_area += curr_piece.area();
            }
            if (curr_area < 0.5 * board_area) {
                // skip conduction areas not covering most of the board
                continue;
            }

            for (int i = 0; i < curr_conduction_area.net_count(); ++i) {
                net.freerouting.freeroute.rules.Net curr_net = routing_board.rules.nets.get(curr_conduction_area.get_net_no(i));
                curr_net.set_contains_plane(true);
                nothing_changed = false;
            }

            changed_layer_arr[layer_no] = true;
            if (curr_conduction_area.get_fixed_state().ordinal() < net.freerouting.freeroute.board.FixedState.USER_FIXED.ordinal()) {
                curr_conduction_area.set_fixed_state(net.freerouting.freeroute.board.FixedState.USER_FIXED);
            }
        }
        if (nothing_changed) {
            return false;
        }
        // Adjust the layer prefered directions in the autoroute settings.
        // and deactivate the changed layers.
        net.freerouting.freeroute.interactive.AutorouteSettings autoroute_settings = p_board_handling.settings.autoroute_settings;
        int layer_count = routing_board.get_layer_count();
        boolean curr_preferred_direction_is_horizontal
                = autoroute_settings.get_preferred_direction_is_horizontal(0);
        for (int i = 0; i < layer_count; ++i) {
            if (changed_layer_arr[i]) {
                autoroute_settings.set_layer_active(i, false);
            } else if (autoroute_settings.get_layer_active(i)) {
                autoroute_settings.set_preferred_direction_is_horizontal(i, curr_preferred_direction_is_horizontal);
                curr_preferred_direction_is_horizontal = !curr_preferred_direction_is_horizontal;
            }
        }
        return true;
    }

    /**
     * Writes p_board to a text file in the Specctra dsn format. Returns false,
     * if the write failed. If p_compat_mode is true, only standard speecctra
     * dsn scopes are written, so that any host system with an specctra
     * interface can read them.
     */
    public static void write(BoardHandling p_board_handling, OutputStream p_file,
            String p_design_name, boolean p_compat_mode) throws DsnFileException {
        //tests.Validate.check("before writing dsn", p_board);
        try (IndentFileWriter output_file = new IndentFileWriter(p_file)) {
            write_pcb_scope(p_board_handling, output_file, p_design_name, p_compat_mode);
        } catch (java.io.IOException exc) {
            throw new DsnFileException("unable to write dsn file", exc);
        }
    }

    private static void write_pcb_scope(net.freerouting.freeroute.interactive.BoardHandling p_board_handling, IndentFileWriter p_file, String p_design_name, boolean p_compat_mode)
            throws java.io.IOException {
        BasicBoard routing_board = p_board_handling.get_routing_board();
        WriteScopeParameter write_scope_parameter
                = new WriteScopeParameter(routing_board, p_board_handling.settings.autoroute_settings, p_file,
                        routing_board.communication.specctra_parser_info.string_quote,
                        routing_board.communication.coordinate_transform, p_compat_mode);

        p_file.start_scope("PCB ");
        write_scope_parameter.identifier_type.write(p_design_name, p_file);
        Parser.write_scope(write_scope_parameter.file,
                write_scope_parameter.board.communication.specctra_parser_info, write_scope_parameter.identifier_type, false);
        Resolution.write_scope(p_file, routing_board.communication);
        Structure.write_scope(write_scope_parameter);
        Placement.write_scope(write_scope_parameter);
        Library.write_scope(write_scope_parameter);
        PartLibrary.write_scope(write_scope_parameter);
        Network.write_scope(write_scope_parameter);
        Wiring.write_scope(write_scope_parameter);
        p_file.end_scope();
    }

    static boolean read_on_off_scope(Scanner p_scanner) throws DsnFileException {
        try {
            Object next_token = p_scanner.next_token();
            boolean result = false;
            if (next_token == Keyword.ON) {
                result = true;
            } else if (next_token != Keyword.OFF) {
                throw new DsnFileException("DsnFile.read_boolean: Keyword.OFF expected");
            }
            ScopeKeyword.skip_scope(p_scanner);
            return result;
        } catch (java.io.IOException exc) {
            throw new DsnFileException("DsnFile.read_boolean: IO error scanning file", exc);
        }
    }

    static int read_integer_scope(Scanner p_scanner) throws DsnFileException {
        try {
            int value;
            Object next_token = p_scanner.next_token();
            if (next_token instanceof Integer) {
                value = (int) next_token;
            } else {
                throw new DsnFileException("DsnFile.read_integer_scope: number expected");
            }
            next_token = p_scanner.next_token();
            if (next_token != Keyword.CLOSED_BRACKET) {
                throw new DsnFileException("DsnFile.read_integer_scope: closing bracket expected");
            }
            return value;
        } catch (java.io.IOException exc) {
            throw new DsnFileException("DsnFile.read_integer_scope: IO error scanning file", exc);
        }
    }

    static double read_float_scope(Scanner p_scanner) throws DsnFileException {
        try {
            double value;
            Object next_token = p_scanner.next_token();
            if (next_token instanceof Double) {
                value = (double) next_token;
            } else if (next_token instanceof Integer) {
                value = ((Number) next_token).doubleValue();
            } else {
                throw new DsnFileException("DsnFile.read_float_scope: number expected");
            }
            next_token = p_scanner.next_token();
            if (next_token != Keyword.CLOSED_BRACKET) {
                throw new DsnFileException("DsnFile.read_float_scope: closing bracket expected");
            }
            return value;
        } catch (java.io.IOException exc) {
            throw new DsnFileException("DsnFile.read_float_scope: IO error scanning file", exc);
        }
    }

    public static String read_string_scope(Scanner p_scanner) throws DsnFileException {
        try {
            p_scanner.yybegin(SpecctraFileScanner.NAME);
            Object next_token = p_scanner.next_token();
            if (!(next_token instanceof String)) {
                throw new DsnFileException("DsnFile:read_string_scope: String expected");
            }
            String result = (String) next_token;
            next_token = p_scanner.next_token();
            if (next_token != Keyword.CLOSED_BRACKET) {
                throw new DsnFileException("DsnFile.read_string_scope: closing bracket expected");
            }
            return result;
        } catch (java.io.IOException exc) {
            throw new DsnFileException("DsnFile.read_string_scope: IO error scanning file", exc);
        }
    }

    public static java.util.Collection<String> read_string_list_scope(Scanner p_scanner)
            throws DsnFileException {
        java.util.Collection<String> result = new java.util.LinkedList<>();
        try {
            for (;;) {
                p_scanner.yybegin(SpecctraFileScanner.NAME);
                Object next_token = p_scanner.next_token();
                if (next_token == Keyword.CLOSED_BRACKET) {
                    break;
                }
                if (!(next_token instanceof String)) {
                    throw new DsnFileException("DsnFileread_string_list_scope: string expected");
                }
                result.add((String) next_token);
            }
        } catch (java.io.IOException exc) {
            throw new DsnFileException("DsnFile.read_string_list_scope: IO error scanning file", exc);
        }
        return result;
    }
}
