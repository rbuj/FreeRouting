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
 * Shape.java
 *
 * Created on 16. Mai 2004, 11:09
 */
package net.freerouting.freeroute.designformats.specctra;

import java.io.IOException;
import net.freerouting.freeroute.datastructures.IdentifierType;
import net.freerouting.freeroute.datastructures.IndentFileWriter;
import static net.freerouting.freeroute.designformats.specctra.Circle.read_circle_scope;
import static net.freerouting.freeroute.designformats.specctra.Polygon.read_polygon_scope;
import static net.freerouting.freeroute.designformats.specctra.PolygonPath.read_polygon_path_scope;
import static net.freerouting.freeroute.designformats.specctra.Rectangle.read_rectangle_scope;
import static net.freerouting.freeroute.designformats.specctra.ScopeKeyword.skip_scope;

/**
 * Describes a shape in a Specctra dsn file.
 *
 * @author alfons
 */
abstract class Shape {

    /**
     * Reads shape scope from a Specctra dsn file. If p_layer_structure == null,
     * only Layer.PCB and Layer.Signal are expected, no induvidual layers.
     */
    static Shape read_shape_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws ReadScopeException {
        Shape result = null;
        try {
            Object next_token = p_scanner.next_token();
            if (next_token == Keyword.OPEN_BRACKET) {
                // overread the open bracket
                next_token = p_scanner.next_token();
            }
            if (next_token == Keyword.RECTANGLE) {
                result = read_rectangle_scope(p_scanner, p_layer_structure);
            } else if (next_token == Keyword.POLYGON) {
                result = read_polygon_scope(p_scanner, p_layer_structure);
            } else if (next_token == Keyword.CIRCLE) {
                result = read_circle_scope(p_scanner, p_layer_structure);
            } else if (next_token == Keyword.POLYGON_PATH) {
                result = read_polygon_path_scope(p_scanner, p_layer_structure);
            } else {
                // not a shape scope, skip it.
                skip_scope(p_scanner);
            }
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Shape.read_scope: IO error scanning file", e);
        }
        return result;
    }

    static LayerInfo read_layer_shape_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws IOException, ReadScopeException {
        LayerInfo layer_info;
        Object next_token = p_scanner.next_token();
        if (next_token == Keyword.PCB_SCOPE) {
            layer_info = LayerInfo.PCB;
        } else if (next_token == Keyword.SIGNAL) {
            layer_info = LayerInfo.SIGNAL;
        } else if (p_layer_structure != null) {
            if (!(next_token instanceof String)) {
                throw new ReadScopeException("Layer name string expected");
            }
            String layer_name = (String) next_token;
            int layer_no = p_layer_structure.get_no(layer_name);
            if (layer_no < 0 || layer_no >= p_layer_structure.arr.length) {
                throw new ReadScopeException("Layer name " + layer_name
                        + " not found in layer structure ");
            } else {
                layer_info = p_layer_structure.arr[layer_no];
            }
        } else {
            layer_info = LayerInfo.SIGNAL;
        }
        return layer_info;
    }

    final LayerInfo layer;

    Shape(LayerInfo p_layer) {
        layer = p_layer;
    }

    /**
     * Returns the smallest axis parallel rectangle containing this shape.
     */
    abstract Rectangle bounding_box();

    /**
     * Transforms a specctra dsn shape to a geometry.planar.Shape.
     */
    abstract net.freerouting.freeroute.geometry.planar.Shape transform_to_board(CoordinateTransform p_coordinate_transform);

    /**
     * Transforms the relative (vector) coordinates of a specctra dsn shape to a
     * geometry.planar.Shape.
     */
    abstract net.freerouting.freeroute.geometry.planar.Shape transform_to_board_rel(CoordinateTransform p_coordinate_transform);

    /**
     * Writes a shape scope to a Specctra dsn file.
     */
    abstract void write_scope(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException;

    /**
     * Writes a shape scope to a Specctra session file. In a session file all
     * coordinates must be integer.
     */
    abstract void write_scope_int(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException;

    void write_hole_scope(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
        p_file.start_scope("window");
        write_scope(p_file, p_identifier_type);
        p_file.end_scope();
    }
}
