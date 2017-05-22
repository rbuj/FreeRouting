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
 * Library.java
 *
 * Created on 21. Mai 2004, 08:09
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.freerouting.freeroute.geometry.planar.IntVector;
import net.freerouting.freeroute.geometry.planar.Vector;

/**
 * Class for reading and writing library scopes from dsn-files.
 *
 * @author Alfons Wirtz
 */
public class Library extends ScopeKeyword {

    public static void write_scope(WriteScopeParameter p_par) throws java.io.IOException {
        p_par.file.start_scope("library");
        for (net.freerouting.freeroute.library.Package curr_package : p_par.board.library.packages) {
            Package.write_scope(p_par, curr_package);
        }
        for (net.freerouting.freeroute.library.Padstack curr_padstack : p_par.board.library.padstacks) {
            Padstack.write_padstack_scope(p_par, curr_padstack);
        }
        p_par.file.end_scope();
    }

    /**
     * Creates a new instance of Library
     */
    public Library() {
        super("library");
    }

    @Override
    public boolean read_scope(ReadScopeParameter p_par) {
        net.freerouting.freeroute.board.RoutingBoard board = p_par.board_handling.get_routing_board();
        board.library.padstacks = new net.freerouting.freeroute.library.Padstacks(p_par.board_handling.get_routing_board().layer_structure);

        try {
            Collection<Package> package_list = new LinkedList<>();
            Object next_token = null;
            for (;;) {
                Object prev_token = next_token;
                try {
                    next_token = p_par.scanner.next_token();
                } catch (java.io.IOException e) {
                    throw new ReadScopeException("Library.read_scope: IO error scanning file", e);
                }
                if (next_token == null) {
                    throw new ReadScopeException("Library.read_scope: unexpected end of file");
                }
                if (next_token == CLOSED_BRACKET) {
                    // end of scope
                    break;
                }
                if (prev_token == OPEN_BRACKET) {
                    if (next_token == Keyword.PADSTACK) {
                        if (!Padstack.read_padstack_scope(p_par.scanner, p_par.layer_structure,
                                p_par.coordinate_transform, board.library.padstacks)) {
                            return false;
                        }
                    } else if (next_token == Keyword.IMAGE) {
                        Package curr_package = Package.read_scope(p_par.scanner, p_par.layer_structure);
                        if (curr_package == null) {
                            return false;
                        }
                        package_list.add(curr_package);
                    } else {
                        skip_scope(p_par.scanner);
                    }
                }
            }

            // Set the via padstacks.
            if (p_par.via_padstack_names != null) {
                int via_padstacks_size = p_par.via_padstack_names.size();
                net.freerouting.freeroute.library.Padstack[] via_padstacks = new net.freerouting.freeroute.library.Padstack[via_padstacks_size];
                int found_padstack_count = 0;
                for (String curr_padstack_name : p_par.via_padstack_names) {
                    net.freerouting.freeroute.library.Padstack curr_padstack = board.library.padstacks.get(curr_padstack_name);
                    if (curr_padstack != null) {
                        via_padstacks[found_padstack_count] = curr_padstack;
                        ++found_padstack_count;
                    } else {
                        System.out.print("Library.read_scope: via padstack with name ");
                        System.out.print(curr_padstack_name);
                        System.out.println(" not found");
                    }
                }
                if (found_padstack_count != via_padstacks_size) {
                    // Some via padstacks were not found in the padstacks scope of the dsn-file.
                    net.freerouting.freeroute.library.Padstack[] corrected_padstacks = new net.freerouting.freeroute.library.Padstack[found_padstack_count];
                    System.arraycopy(via_padstacks, 0, corrected_padstacks, 0, found_padstack_count);
                    via_padstacks = corrected_padstacks;
                }
                board.library.set_via_padstacks(via_padstacks);
            }

            // Create the library packages on the board
            board.library.packages = new net.freerouting.freeroute.library.Packages(board.library.padstacks);
            for (Package curr_package : package_list) {

                net.freerouting.freeroute.library.Package.Pin[] pin_arr = new net.freerouting.freeroute.library.Package.Pin[curr_package.pin_info_arr.length];
                for (int i = 0; i < pin_arr.length; ++i) {
                    PinInfo pin_info = curr_package.pin_info_arr[i];
                    int rel_x = (int) Math.round(p_par.coordinate_transform.dsn_to_board(pin_info.rel_coor[0]));
                    int rel_y = (int) Math.round(p_par.coordinate_transform.dsn_to_board(pin_info.rel_coor[1]));
                    Vector rel_coor = new IntVector(rel_x, rel_y);
                    net.freerouting.freeroute.library.Padstack board_padstack = board.library.padstacks.get(pin_info.padstack_name);
                    if (board_padstack == null) {
                        throw new ReadScopeException("Library.read_scope: board padstack not found");
                    }
                    pin_arr[i] = new net.freerouting.freeroute.library.Package.Pin(pin_info.pin_name, board_padstack.no, rel_coor, pin_info.rotation);
                }

                net.freerouting.freeroute.geometry.planar.Shape[] outline_arr = new net.freerouting.freeroute.geometry.planar.Shape[curr_package.outline.size()];
                Iterator<Shape> it3 = curr_package.outline.iterator();
                for (int i = 0; i < outline_arr.length; ++i) {
                    Shape curr_shape = it3.next();
                    if (curr_shape != null) {
                        outline_arr[i] = curr_shape.transform_to_board_rel(p_par.coordinate_transform);
                    } else {
                        System.out.println("Library.read_scope: outline shape is null");
                    }
                }

                generate_missing_keepout_names("keepout_", curr_package.keepouts);
                generate_missing_keepout_names("via_keepout_", curr_package.via_keepouts);
                generate_missing_keepout_names("place_keepout_", curr_package.place_keepouts);

                net.freerouting.freeroute.library.Package.Keepout[] keepout_arr = new net.freerouting.freeroute.library.Package.Keepout[curr_package.keepouts.size()];
                Iterator<Area> it2 = curr_package.keepouts.iterator();
                for (int i = 0; i < keepout_arr.length; ++i) {
                    Area curr_keepout = it2.next();
                    Layer curr_layer = curr_keepout.shape_list.iterator().next().layer;
                    net.freerouting.freeroute.geometry.planar.Area curr_area = AreaTransformable.transform_area_to_board_rel(curr_keepout.shape_list, p_par.coordinate_transform);
                    keepout_arr[i] = new net.freerouting.freeroute.library.Package.Keepout(curr_keepout.area_name, curr_area, curr_layer.no);
                }

                net.freerouting.freeroute.library.Package.Keepout[] via_keepout_arr = new net.freerouting.freeroute.library.Package.Keepout[curr_package.via_keepouts.size()];
                it2 = curr_package.via_keepouts.iterator();
                for (int i = 0; i < via_keepout_arr.length; ++i) {
                    Area curr_keepout = it2.next();
                    Layer curr_layer = (curr_keepout.shape_list.iterator().next()).layer;
                    net.freerouting.freeroute.geometry.planar.Area curr_area = AreaTransformable.transform_area_to_board_rel(curr_keepout.shape_list, p_par.coordinate_transform);
                    via_keepout_arr[i] = new net.freerouting.freeroute.library.Package.Keepout(curr_keepout.area_name, curr_area, curr_layer.no);
                }

                net.freerouting.freeroute.library.Package.Keepout[] place_keepout_arr = new net.freerouting.freeroute.library.Package.Keepout[curr_package.place_keepouts.size()];
                it2 = curr_package.place_keepouts.iterator();
                for (int i = 0; i < place_keepout_arr.length; ++i) {
                    Area curr_keepout = it2.next();
                    Layer curr_layer = (curr_keepout.shape_list.iterator().next()).layer;
                    net.freerouting.freeroute.geometry.planar.Area curr_area = AreaTransformable.transform_area_to_board_rel(curr_keepout.shape_list, p_par.coordinate_transform);
                    place_keepout_arr[i] = new net.freerouting.freeroute.library.Package.Keepout(curr_keepout.area_name, curr_area, curr_layer.no);
                }

                board.library.packages.add(curr_package.name, pin_arr, outline_arr,
                        keepout_arr, via_keepout_arr, place_keepout_arr, curr_package.is_front);
            }
            return true;
        } catch (DsnFileException | ReadScopeException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void generate_missing_keepout_names(String p_keepout_type, Collection<Area> p_keepout_list) {
        boolean all_names_existing = true;
        for (Area curr_keepout : p_keepout_list) {
            if (curr_keepout.area_name == null) {
                all_names_existing = false;
                break;
            }
        }
        if (all_names_existing) {
            return;
        }
        // generate names
        Integer curr_name_index = 1;
        for (Area curr_keepout : p_keepout_list) {
            curr_keepout.area_name = p_keepout_type + curr_name_index.toString();
            ++curr_name_index;
        }
    }
}
