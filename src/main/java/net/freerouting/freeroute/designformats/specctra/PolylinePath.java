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
 * PolylinePath.java
 *
 * Created on 30. Juni 2004, 08:24
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.LinkedList;
import net.freerouting.freeroute.datastructures.IdentifierType;
import net.freerouting.freeroute.datastructures.IndentFileWriter;

/**
 * Describes a path defined by a sequence of lines (instead of a sequence of
 * corners.
 *
 * @author alfons
 */
class PolylinePath extends Path {

    /**
     * Reads an object of type PolylinePath from the dsn-file.
     */
    static Path read_polyline_path_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws ReadScopeException {
        try {
            LayerInfo layer = read_layer_shape_scope(p_scanner, p_layer_structure);
            Object next_token;
            Collection<Object> corner_list = new LinkedList<>();

            // read the width and the corners of the path
            for (;;) {
                next_token = p_scanner.next_token();
                if (next_token == Keyword.CLOSED_BRACKET) {
                    break;
                }
                corner_list.add(next_token);
            }
            return new PolylinePath(layer, corner_list);
        } catch (java.io.IOException e) {
            throw new ReadScopeException("PolylinePath.read_polyline_path_scope: IO error scanning file", e);
        }
    }

    /**
     * Creates a new instance of PolylinePath
     */
    PolylinePath(LayerInfo p_layer, double p_width, double[] p_corner_arr) {
        super(p_layer, p_width, p_corner_arr);
    }

    private PolylinePath(LayerInfo layer, Collection<Object> corner_list) throws ReadScopeException {
        super(layer, corner_list);
    }

    /**
     * Writes this path as a scope to an output dsn-file.
     */
    @Override
    void write_scope(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException {
        p_file.start_scope("polyline_path ");
        p_identifier.write(this.layer.name, p_file);
        p_file.write(" ");
        p_file.write(Double.toString(this.width));
        int line_count = coordinate_arr.length / 4;
        for (int i = 0; i < line_count; ++i) {
            p_file.new_line();
            for (int j = 0; j < 4; ++j) {
                p_file.write(Double.toString(coordinate_arr[4 * i + j]));
                p_file.write(" ");
            }
        }
        p_file.end_scope();
    }

    @Override
    void write_scope_int(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException {
        p_file.start_scope("polyline_path ");
        p_identifier.write(this.layer.name, p_file);
        p_file.write(" ");
        p_file.write(Double.toString(this.width));
        int line_count = coordinate_arr.length / 4;
        for (int i = 0; i < line_count; ++i) {
            p_file.new_line();
            for (int j = 0; j < 4; ++j) {
                Integer curr_coor = (int) Math.round(coordinate_arr[4 * i + j]);
                p_file.write(curr_coor.toString());
                p_file.write(" ");
            }
        }
        p_file.end_scope();
    }

    @Override
    net.freerouting.freeroute.geometry.planar.Shape transform_to_board_rel(CoordinateTransform p_coordinate_transform) {
        throw new UnsupportedOperationException("PolylinePath.transform_to_board_rel not implemented");
    }

    @Override
    net.freerouting.freeroute.geometry.planar.Shape transform_to_board(CoordinateTransform p_coordinate_transform) {
        throw new UnsupportedOperationException("PolylinePath.transform_to_board_rel not implemented");
    }

    @Override
    Rectangle bounding_box() {
        throw new UnsupportedOperationException("PolylinePath.boundingbox not implemented");
    }
}
