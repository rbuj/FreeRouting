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
 * ReadScopeParameter.java
 *
 * Created on 21. Juni 2004, 08:28
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Default parameter type used while reading a Specctra dsn-file.
 *
 * @author alfons
 */
class ReadScopeParameter {

    final Scanner scanner;
    final net.freerouting.freeroute.interactive.BoardHandling board_handling;
    final NetList netlist = new NetList();

    final net.freerouting.freeroute.board.BoardObservers observers;
    final net.freerouting.freeroute.datastructures.IdNoGenerator item_id_no_generator;
    final net.freerouting.freeroute.board.TestLevel test_level;

    /**
     * Collection of elements of class PlaneInfo. The plane cannot be inserted
     * directly into the boards, because the layers may not be read completely.
     */
    final Collection<PlaneInfo> plane_list = new LinkedList<>();

    /**
     * Component placement information. It is filled while reading the placement
     * scope and can be evaluated after reading the library and network scope.
     */
    final Collection<ComponentPlacement> placement_list = new LinkedList<>();

    /**
     * The names of the via padstacks filled while reading the structure scope
     * and evaluated after reading the library scope.
     */
    Collection<String> via_padstack_names = null;

    boolean via_at_smd_allowed = false;
    net.freerouting.freeroute.board.AngleRestriction snap_angle = net.freerouting.freeroute.board.AngleRestriction.FORTYFIVE_DEGREE;

    /**
     * The logical parts are used for pin and gate swaw
     */
    java.util.Collection<PartLibrary.LogicalPartMapping> logical_part_mappings
            = new java.util.LinkedList<>();
    java.util.Collection<PartLibrary.LogicalPart> logical_parts = new java.util.LinkedList<>();

    /**
     * The following objects are from the parser scope.
     */
    String string_quote = "\"";
    String host_cad = null;
    String host_version = null;

    boolean dsn_file_generated_by_host = true;

    final Collection<String[]> constants = new LinkedList<>();
    net.freerouting.freeroute.board.Communication.SpecctraParserInfo.WriteResolution write_resolution = null;

    /**
     * The following objects will be initialised when the structure scope is
     * read.
     */
    CoordinateTransform coordinate_transform = null;
    LayerStructure layer_structure = null;
    net.freerouting.freeroute.interactive.AutorouteSettings autoroute_settings = null;

    net.freerouting.freeroute.board.Unit unit = net.freerouting.freeroute.board.Unit.MIL;
    int resolution = 100; // default resulution

    /**
     * Creates a new instance of ReadScopeParameter
     */
    ReadScopeParameter(Scanner p_scanner, net.freerouting.freeroute.interactive.BoardHandling p_board_handling,
            net.freerouting.freeroute.board.BoardObservers p_observers,
            net.freerouting.freeroute.datastructures.IdNoGenerator p_item_id_no_generator, net.freerouting.freeroute.board.TestLevel p_test_level) {
        scanner = p_scanner;
        board_handling = p_board_handling;
        observers = p_observers;
        item_id_no_generator = p_item_id_no_generator;
        test_level = p_test_level;
    }

    static class PlaneInfo {

        final Area area;
        final String net_name;

        PlaneInfo(Area p_area, String p_net_name) {
            area = p_area;
            net_name = p_net_name;
        }
    }
}
