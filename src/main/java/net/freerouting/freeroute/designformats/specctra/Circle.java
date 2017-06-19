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
 * Circle.java
 *
 * Created on 20. Mai 2004, 09:22
 */
package net.freerouting.freeroute.designformats.specctra;

import net.freerouting.freeroute.datastructures.IdentifierType;
import net.freerouting.freeroute.datastructures.IndentFileWriter;
import net.freerouting.freeroute.geometry.planar.IntPoint;

/**
 * Class for reading and writing circle scopes from dsn-files.
 *
 * @author alfons
 */
class Circle extends Shape {

    /**
     * Reads a circle scope from a Specctra dsn file.
     */
    static Shape read_circle_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws ReadScopeException {
        try {
            LayerInfo circle_layer = read_layer_shape_scope(p_scanner, p_layer_structure);
            double circle_coor[] = new double[3];
            Object next_token;

            // fill the the the coordinates
            int curr_index = 0;
            for (;;) {
                next_token = p_scanner.next_token();
                if (next_token == Keyword.CLOSED_BRACKET) {
                    break;
                }
                if (curr_index > 2) {
                    throw new ReadScopeException("Circle.read_circle_scope: closed bracket expected");
                }
                if (next_token instanceof Double) {
                    circle_coor[curr_index] = (double) next_token;
                } else if (next_token instanceof Integer) {
                    circle_coor[curr_index] = ((Number) next_token).doubleValue();
                } else {
                    throw new ReadScopeException("Circle.read_circle_scope: number expected");
                }
                ++curr_index;
            }
            return new Circle(circle_layer, circle_coor);
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Circle.read_circle_scope: IO error scanning file", e);
        }
    }

    final double[] coor;

    /**
     * Creates a new circle from the input parameters. p_coor is an array of
     * dimension 3. p_coor [0] is the radius of the circle, p_coor [1] is the x
     * coordinate of the circle, p_coor [2] is the y coordinate of the circle.
     */
    private Circle(LayerInfo p_layer, double[] p_coor) {
        super(p_layer);
        coor = (p_coor == null) ? null : p_coor.clone();
    }

    Circle(LayerInfo p_layer, double p_radius, double p_center_x, double p_center_y) {
        super(p_layer);
        coor = new double[3];
        coor[0] = p_radius;
        coor[1] = p_center_x;
        coor[2] = p_center_y;
    }

    @Override
    net.freerouting.freeroute.geometry.planar.Shape transform_to_board(CoordinateTransform p_coordinate_transform) {
        double[] location = new double[2];
        location[0] = coor[1];
        location[1] = coor[2];
        IntPoint center = p_coordinate_transform.dsn_to_board(location).round();
        int radius = (int) Math.round(p_coordinate_transform.dsn_to_board(coor[0]) / 2);
        return new net.freerouting.freeroute.geometry.planar.Circle(center, radius);
    }

    @Override
    net.freerouting.freeroute.geometry.planar.Shape transform_to_board_rel(CoordinateTransform p_coordinate_transform) {
        int[] new_coor = new int[3];
        new_coor[0] = (int) Math.round(p_coordinate_transform.dsn_to_board(coor[0]) / 2);
        for (int i = 1; i < 3; ++i) {
            new_coor[i] = (int) Math.round(p_coordinate_transform.dsn_to_board(coor[i]));
        }
        return new net.freerouting.freeroute.geometry.planar.Circle(new IntPoint(new_coor[1], new_coor[2]), new_coor[0]);
    }

    @Override
    Rectangle bounding_box() {
        double[] bounds = new double[4];
        bounds[0] = coor[1] - coor[0];
        bounds[1] = coor[2] - coor[0];
        bounds[2] = coor[1] + coor[0];
        bounds[3] = coor[2] + coor[0];
        return new Rectangle(layer, bounds);
    }

    @Override
    void write_scope(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
        p_file.new_line();
        p_file.write("(circle ");
        p_identifier_type.write(this.layer.name, p_file);
        for (int i = 0; i < coor.length; ++i) {
            p_file.write(" ");
            p_file.write(Double.toString(coor[i]));
        }
        p_file.write(")");
    }

    @Override
    void write_scope_int(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
        p_file.new_line();
        p_file.write("(circle ");
        p_identifier_type.write(this.layer.name, p_file);
        for (int i = 0; i < coor.length; ++i) {
            p_file.write(" ");
            Integer curr_coor = (int) Math.round(coor[i]);
            p_file.write(curr_coor.toString());
        }
        p_file.write(")");
    }
}
