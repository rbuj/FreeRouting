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
 * Path.java
 *
 * Created on 24. Mai 2004, 08:10
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import net.freerouting.freeroute.datastructures.IdentifierType;
import net.freerouting.freeroute.datastructures.IndentFileWriter;
import net.freerouting.freeroute.geometry.planar.FloatPoint;
import net.freerouting.freeroute.geometry.planar.IntOctagon;
import net.freerouting.freeroute.geometry.planar.IntPoint;

/**
 * Class for reading and writing path scopes consisting of a polygon from
 * dsn-files.
 *
 * @author Alfons Wirtz
 */
public class PolygonPath extends Path {

    /**
     * Creates a new instance of PolygonPath
     */
    PolygonPath(Layer p_layer, double p_width, double[] p_coordinate_arr) {
        super(p_layer, p_width, p_coordinate_arr);
    }

    /**
     * Writes this path as a scope to an output dsn-file.
     */
    @Override
    public void write_scope(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
        p_file.start_scope("path ");
        p_identifier_type.write(this.layer.name, p_file);
        p_file.write(" ");
        p_file.write(Double.toString(this.width));
        int corner_count = coordinate_arr.length / 2;
        for (int i = 0; i < corner_count; ++i) {
            p_file.new_line();
            p_file.write(Double.toString(coordinate_arr[2 * i]));
            p_file.write(" ");
            p_file.write(Double.toString(coordinate_arr[2 * i + 1]));
        }
        p_file.end_scope();
    }

    @Override
    public void write_scope_int(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
        p_file.start_scope("path ");
        p_identifier_type.write(this.layer.name, p_file);
        p_file.write(" ");
        p_file.write(Double.toString(this.width));
        int corner_count = coordinate_arr.length / 2;
        for (int i = 0; i < corner_count; ++i) {
            p_file.new_line();
            Integer curr_coor = (int) Math.round(coordinate_arr[2 * i]);
            p_file.write(curr_coor.toString());
            p_file.write(" ");
            curr_coor = (int) Math.round(coordinate_arr[2 * i + 1]);
            p_file.write(curr_coor.toString());
        }
        p_file.end_scope();
    }

    @Override
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board(CoordinateTransform p_coordinate_transform) {
        FloatPoint[] corner_arr = new FloatPoint[this.coordinate_arr.length / 2];
        double[] curr_point = new double[2];
        for (int i = 0; i < corner_arr.length; ++i) {
            curr_point[0] = this.coordinate_arr[2 * i];
            curr_point[1] = this.coordinate_arr[2 * i + 1];
            corner_arr[i] = p_coordinate_transform.dsn_to_board(curr_point);
        }
        double offset = p_coordinate_transform.dsn_to_board(this.width) / 2;
        if (corner_arr.length <= 2) {
            IntOctagon bounding_oct = FloatPoint.bounding_octagon(corner_arr);
            return bounding_oct.enlarge(offset);
        }
        IntPoint[] rounded_corner_arr = new IntPoint[corner_arr.length];
        for (int i = 0; i < corner_arr.length; ++i) {
            rounded_corner_arr[i] = corner_arr[i].round();
        }
        net.freerouting.freeroute.geometry.planar.Shape result = new net.freerouting.freeroute.geometry.planar.PolygonShape(rounded_corner_arr);
        if (offset > 0) {
            result = result.bounding_tile().enlarge(offset);
        }
        return result;
    }

    @Override
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board_rel(CoordinateTransform p_coordinate_transform) {
        FloatPoint[] corner_arr = new FloatPoint[this.coordinate_arr.length / 2];
        double[] curr_point = new double[2];
        for (int i = 0; i < corner_arr.length; ++i) {
            curr_point[0] = this.coordinate_arr[2 * i];
            curr_point[1] = this.coordinate_arr[2 * i + 1];
            corner_arr[i] = p_coordinate_transform.dsn_to_board_rel(curr_point);
        }
        double offset = p_coordinate_transform.dsn_to_board(this.width) / 2;
        if (corner_arr.length <= 2) {
            IntOctagon bounding_oct = FloatPoint.bounding_octagon(corner_arr);
            return bounding_oct.enlarge(offset);
        }
        IntPoint[] rounded_corner_arr = new IntPoint[corner_arr.length];
        for (int i = 0; i < corner_arr.length; ++i) {
            rounded_corner_arr[i] = corner_arr[i].round();
        }
        net.freerouting.freeroute.geometry.planar.Shape result = new net.freerouting.freeroute.geometry.planar.PolygonShape(rounded_corner_arr);
        if (offset > 0) {
            result = result.bounding_tile().enlarge(offset);
        }
        return result;
    }

    @Override
    public Rectangle bounding_box() {
        double offset = this.width / 2;
        double[] bounds = new double[4];
        bounds[0] = Integer.MAX_VALUE;
        bounds[1] = Integer.MAX_VALUE;
        bounds[2] = Integer.MIN_VALUE;
        bounds[3] = Integer.MIN_VALUE;
        for (int i = 0; i < coordinate_arr.length; ++i) {
            if (i % 2 == 0) {
                // x coordinate
                bounds[0] = Math.min(bounds[0], coordinate_arr[i] - offset);
                bounds[2] = Math.max(bounds[2], coordinate_arr[i]) + offset;
            } else {
                // x coordinate
                bounds[1] = Math.min(bounds[1], coordinate_arr[i] - offset);
                bounds[3] = Math.max(bounds[3], coordinate_arr[i] + offset);
            }
        }
        return new Rectangle(layer, bounds);
    }

    /**
     * Reads an object of type Path from the dsn-file.
     */
    static Path read_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws ReadScopeException {
        try {
            Layer layer = null;
            Object next_token = p_scanner.next_token();
            if (next_token == Keyword.PCB_SCOPE) {
                layer = Layer.PCB;
            } else if (next_token == Keyword.SIGNAL) {
                layer = Layer.SIGNAL;
            } else {
                if (p_layer_structure == null) {
                    throw new ReadScopeException("Shape.read_polygon_path_scope: only layer types pcb or signal expected");
                }
                if (!(next_token instanceof String)) {
                    throw new ReadScopeException("Path.read_scope: layer name string expected");
                }
                int layer_no = p_layer_structure.get_no((String) next_token);
                if (layer_no < 0 || layer_no >= p_layer_structure.arr.length) {
                    throw new ReadScopeException("Shape.read_polygon_path_scope: layer with name " +  next_token.toString() + " not found in layer structure ");
                } else {
                    layer = p_layer_structure.arr[layer_no];
                }
            }
            Collection<Object> corner_list = new LinkedList<>();

            // read the width and the corners of the path
            for (;;) {
                next_token = p_scanner.next_token();
                if (next_token == Keyword.OPEN_BRACKET) {
                    // unknown scope
                    ScopeKeyword.skip_scope(p_scanner);
                    next_token = p_scanner.next_token();
                }
                if (next_token == Keyword.CLOSED_BRACKET) {
                    break;
                }
                corner_list.add(next_token);
            }
            if (corner_list.size() < 5) {
                throw new ReadScopeException("Shape.read_polygon_path_scope: to few numbers in scope");
            }
            Iterator<Object> it = corner_list.iterator();
            double width;
            Object next_object = it.next();
            if (next_object instanceof Double) {
                width = (double) next_object;
            } else if (next_object instanceof Integer) {
                width = ((Number) next_object).doubleValue();
            } else {
                throw new ReadScopeException("Shape.read_polygon_path_scope: number expected");
            }
            double[] coordinate_arr = new double[corner_list.size() - 1];
            for (int i = 0; i < coordinate_arr.length; ++i) {
                next_object = it.next();
                if (next_object instanceof Double) {
                    coordinate_arr[i] = (double) next_object;
                } else if (next_object instanceof Integer) {
                    coordinate_arr[i] = ((Number) next_object).doubleValue();
                } else {
                    throw new ReadScopeException("Shape.read_polygon_path_scope: number expected");
                }

            }
            return new PolygonPath(layer, width, coordinate_arr);
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Shape.read_polygon_path_scope: IO error scanning file", e);
        }
    }
}
