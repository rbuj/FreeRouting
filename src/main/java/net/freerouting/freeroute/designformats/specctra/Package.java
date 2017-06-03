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
 * Package.java
 *
 * Created on 21. Mai 2004, 09:31
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import net.freerouting.freeroute.board.Item;

/**
 * Class for reading and writing package scopes from dsn-files.
 *
 * @author alfons
 */
public class Package {

    public static Package read_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws DsnFileException, ReadScopeException {
        try {
            boolean is_front = true;
            Collection<Shape> outline = new LinkedList<>();
            Collection<Area> keepouts = new LinkedList<>();
            Collection<Area> via_keepouts = new LinkedList<>();
            Collection<Area> place_keepouts = new LinkedList<>();
            Object next_token = p_scanner.next_token();
            if (!(next_token instanceof String)) {
                throw new ReadScopeException("Package.read_scope: String expected");
            }
            String package_name = (String) next_token;
            Collection<PinInfo> pin_info_list = new LinkedList<>();
            for (;;) {
                Object prev_token = next_token;
                next_token = p_scanner.next_token();

                if (next_token == null) {
                    throw new ReadScopeException("Package.read_scope: unexpected end of file");
                }
                if (next_token == Keyword.CLOSED_BRACKET) {
                    // end of scope
                    break;
                }
                if (prev_token == Keyword.OPEN_BRACKET) {
                    if (next_token == Keyword.PIN) {
                        PinInfo next_pin = PinInfo.read_pin_info(p_scanner);
                        pin_info_list.add(next_pin);
                    } else if (next_token == Keyword.SIDE) {
                        is_front = read_placement_side(p_scanner);
                    } else if (next_token == Keyword.OUTLINE) {
                        Shape curr_shape = ShapeReadable.read_scope(p_scanner, p_layer_structure);
                        if (curr_shape != null) {
                            outline.add(curr_shape);
                        }
                        // overread closing bracket
                        next_token = p_scanner.next_token();
                        if (next_token != Keyword.CLOSED_BRACKET) {
                            throw new ReadScopeException("Package.read_scope: closed bracket expected");
                        }
                    } else if (next_token == Keyword.KEEPOUT) {
                        Area keepout_area = AreaReadable.read_area_scope(p_scanner, p_layer_structure, false);
                        if (keepout_area != null) {
                            keepouts.add(keepout_area);
                        }
                    } else if (next_token == Keyword.VIA_KEEPOUT) {
                        Area keepout_area = AreaReadable.read_area_scope(p_scanner, p_layer_structure, false);
                        if (keepout_area != null) {
                            via_keepouts.add(keepout_area);
                        }
                    } else if (next_token == Keyword.PLACE_KEEPOUT) {
                        Area keepout_area = AreaReadable.read_area_scope(p_scanner, p_layer_structure, false);
                        if (keepout_area != null) {
                            place_keepouts.add(keepout_area);
                        }
                    } else {
                        ScopeKeyword.skip_scope(p_scanner);
                    }
                }
            }
            PinInfo[] pin_info_arr = pin_info_list.stream().toArray(PinInfo[]::new);
            return new Package(package_name, pin_info_arr, outline, keepouts, via_keepouts, place_keepouts, is_front);
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Package.read_scope: IO error scanning file", e);
        }
    }

    public static void write_scope(WriteScopeParameter p_par, net.freerouting.freeroute.library.Package p_package) throws java.io.IOException {
        p_par.file.start_scope("image ");
        p_par.identifier_type.write(p_package.name, p_par.file);
        // write the placement side of the package
        p_par.file.new_line();
        p_par.file.write("(side ");
        if (p_package.is_front) {
            p_par.file.write("front)");
        } else {
            p_par.file.write("back)");
        }
        // write the pins of the package
        for (int i = 0; i < p_package.pin_count(); ++i) {
            PinInfo.write_scope(p_par, p_package.get_pin(i));
        }
        // write the keepouts belonging to  the package.
        for (int i = 0; i < p_package.keepout_arr.length; ++i) {
            write_package_keepout(p_package.keepout_arr[i], p_par, false);
        }
        for (int i = 0; i < p_package.via_keepout_arr.length; ++i) {
            write_package_keepout(p_package.via_keepout_arr[i], p_par, true);
        }
        // write the package outline.
        for (int i = 0; i < p_package.outline.length; ++i) {
            p_par.file.start_scope("outline");
            Shape curr_outline = p_par.coordinate_transform.board_to_dsn_rel(p_package.outline[i], Layer.SIGNAL);
            curr_outline.write_scope(p_par.file, p_par.identifier_type);
            p_par.file.end_scope();
        }
        p_par.file.end_scope();
    }

    private static void write_package_keepout(net.freerouting.freeroute.library.Package.Keepout p_keepout, WriteScopeParameter p_par,
            boolean p_is_via_keepout) throws java.io.IOException {
        Layer keepout_layer;
        if (p_keepout.layer >= 0) {
            net.freerouting.freeroute.board.Layer board_layer = p_par.board.layer_structure.arr[p_keepout.layer];
            keepout_layer = new Layer(board_layer.name, p_keepout.layer, board_layer.is_signal);
        } else {
            keepout_layer = Layer.SIGNAL;
        }
        net.freerouting.freeroute.geometry.planar.Shape boundary_shape;
        net.freerouting.freeroute.geometry.planar.Shape[] holes;
        if (p_keepout.area instanceof net.freerouting.freeroute.geometry.planar.Shape) {
            boundary_shape = (net.freerouting.freeroute.geometry.planar.Shape) p_keepout.area;
            holes = new net.freerouting.freeroute.geometry.planar.Shape[0];
        } else {
            boundary_shape = p_keepout.area.get_border();
            holes = p_keepout.area.get_holes();
        }
        if (p_is_via_keepout) {
            p_par.file.start_scope("via_keepout");
        } else {
            p_par.file.start_scope("keepout");
        }
        Shape dsn_shape = p_par.coordinate_transform.board_to_dsn(boundary_shape, keepout_layer);
        if (dsn_shape != null) {
            dsn_shape.write_scope(p_par.file, p_par.identifier_type);
        }
        for (int j = 0; j < holes.length; ++j) {
            Shape dsn_hole = p_par.coordinate_transform.board_to_dsn(holes[j], keepout_layer);
            dsn_hole.write_hole_scope(p_par.file, p_par.identifier_type);
        }
        p_par.file.end_scope();
    }

    /**
     * Writes the placements of p_package to a Specctra dsn-file.
     */
    public static void write_placement_scope(WriteScopeParameter p_par, net.freerouting.freeroute.library.Package p_package)
            throws java.io.IOException {
        Collection<Item> board_items = p_par.board.get_items();
        boolean component_found = false;
        for (net.freerouting.freeroute.board.Component curr_component : p_par.board.components) {
            if (curr_component.get_package() == p_package) {
                // check, if not all items of the component are deleted
                boolean undeleted_item_found = false;
                Iterator<Item> it = board_items.iterator();
                while (it.hasNext()) {
                    Item curr_item = it.next();
                    if (curr_item.get_component_no() == curr_component.no) {
                        undeleted_item_found = true;
                        break;
                    }
                }
                if (undeleted_item_found || !curr_component.is_placed()) {
                    if (!component_found) {
                        // write the scope header
                        p_par.file.start_scope("component ");
                        p_par.identifier_type.write(p_package.name, p_par.file);
                        component_found = true;
                    }
                    Component.write_scope(p_par, curr_component);
                }
            }
        }
        if (component_found) {
            p_par.file.end_scope();
        }
    }

    private static boolean read_placement_side(Scanner p_scanner) throws java.io.IOException {
        Object next_token = p_scanner.next_token();
        boolean result = (next_token != Keyword.BACK);

        next_token = p_scanner.next_token();
        if (next_token != Keyword.CLOSED_BRACKET) {
            System.out.println("Package.read_placement_side: closing bracket expected");
        }
        return result;
    }

    public final String name;
    /**
     * List of objects of type PinInfo.
     */
    public final PinInfo[] pin_info_arr;
    /**
     * The outline of the package.
     */
    public final Collection<Shape> outline;
    /**
     * Collection of keepoouts belonging to this package
     */
    public final Collection<Area> keepouts;
    /**
     * Collection of via keepoouts belonging to this package
     */
    public final Collection<Area> via_keepouts;
    /**
     * Collection of place keepoouts belonging to this package
     */
    public final Collection<Area> place_keepouts;
    /**
     * If false, the package is placed on the back side of the board
     */
    public final boolean is_front;

    /**
     * Creates a new instance of Package
     */
    public Package(String p_name, PinInfo[] p_pin_info_arr, Collection<Shape> p_outline, Collection<Area> p_keepouts,
            Collection<Area> p_via_keepouts, Collection<Area> p_place_keepouts, boolean p_is_front) {
        name = p_name;
        pin_info_arr = (p_pin_info_arr == null) ? null : p_pin_info_arr.clone();
        outline = p_outline;
        keepouts = p_keepouts;
        via_keepouts = p_via_keepouts;
        place_keepouts = p_place_keepouts;
        is_front = p_is_front;
    }
}
