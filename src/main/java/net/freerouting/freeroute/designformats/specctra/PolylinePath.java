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
import java.util.Iterator;
import java.util.LinkedList;
import net.freerouting.freeroute.datastructures.IdentifierType;
import net.freerouting.freeroute.datastructures.IndentFileWriter;

/**
 * Describes a path defined by a sequence of lines (instead of a sequence of
 * corners.
 *
 * @author alfons
 */
public class PolylinePath extends Path {

    /**
     * Reads an object of type PolylinePath from the dsn-file.
     */
    static Path read_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws ReadScopeException {
        try {
            LayerInfo layer;
            Object next_token = p_scanner.next_token();
            if (next_token == Keyword.PCB_SCOPE) {
                layer = LayerInfo.PCB;
            } else if (next_token == Keyword.SIGNAL) {
                layer = LayerInfo.SIGNAL;
            } else {
                if (p_layer_structure == null) {
                    throw new ReadScopeException("PolylinePath.read_scope: only layer types pcb or signal expected");
                }
                if (!(next_token instanceof String)) {
                    throw new ReadScopeException("PolylinePath.read_scope: layer name string expected");
                }
                int layer_no = p_layer_structure.get_no((String) next_token);
                if (layer_no < 0 || layer_no >= p_layer_structure.arr.length) {
                    throw new ReadScopeException("Shape.read_polyline_path_scope: layer name " + next_token.toString() + " not found in layer structure ");
                }
                layer = p_layer_structure.arr[layer_no];
            }
            Collection<Object> corner_list = new LinkedList<>();

            // read the width and the corners of the path
            for (;;) {
                next_token = p_scanner.next_token();
                if (next_token == Keyword.CLOSED_BRACKET) {
                    break;
                }
                corner_list.add(next_token);
            }
            if (corner_list.size() < 5) {
                throw new ReadScopeException("PolylinePath.read_scope: to few numbers in scope");
            }
            Iterator<Object> it = corner_list.iterator();
            double width;
            Object next_object = it.next();
            if (next_object instanceof Double) {
                width = (double) next_object;
            } else if (next_object instanceof Integer) {
                width = ((Number) next_object).doubleValue();
            } else {
                throw new ReadScopeException("PolylinePath.read_scope: number expected");
            }
            double[] corner_arr = new double[corner_list.size() - 1];
            for (int i = 0; i < corner_arr.length; ++i) {
                next_object = it.next();
                if (next_object instanceof Double) {
                    corner_arr[i] = (double) next_object;
                } else if (next_object instanceof Integer) {
                    corner_arr[i] = ((Number) next_object).doubleValue();
                } else {
                    throw new ReadScopeException("Shape.read_polygon_path_scope: number expected");
                }

            }
            return new PolylinePath(layer, width, corner_arr);
        } catch (java.io.IOException e) {
            throw new ReadScopeException("PolylinePath.read_scope: IO error scanning file", e);
        }
    }

    /**
     * Creates a new instance of PolylinePath
     */
    PolylinePath(LayerInfo p_layer, double p_width, double[] p_corner_arr) {
        super(p_layer, p_width, p_corner_arr);
    }

    /**
     * Writes this path as a scope to an output dsn-file.
     */
    @Override
    public void write_scope(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException {
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
    public void write_scope_int(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException {
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
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board_rel(CoordinateTransform p_coordinate_transform) {
        throw new UnsupportedOperationException("PolylinePath.transform_to_board_rel not implemented");
    }

    @Override
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board(CoordinateTransform p_coordinate_transform) {
        throw new UnsupportedOperationException("PolylinePath.transform_to_board_rel not implemented");
    }

    @Override
    public Rectangle bounding_box() {
        throw new UnsupportedOperationException("PolylinePath.boundingbox not implemented");
    }

}
