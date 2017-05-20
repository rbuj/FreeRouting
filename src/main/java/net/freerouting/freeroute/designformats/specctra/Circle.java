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
public class Circle extends Shape {

    public final double[] coor;

    /**
     * Creates a new circle from the input parameters. p_coor is an array of
     * dimension 3. p_coor [0] is the radius of the circle, p_coor [1] is the x
     * coordinate of the circle, p_coor [2] is the y coordinate of the circle.
     */
    public Circle(Layer p_layer, double[] p_coor) {
        super(p_layer);
        coor = p_coor;
    }

    public Circle(Layer p_layer, double p_radius, double p_center_x, double p_center_y) {
        super(p_layer);
        coor = new double[3];
        coor[0] = p_radius;
        coor[1] = p_center_x;
        coor[2] = p_center_y;
    }

    @Override
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board(CoordinateTransform p_coordinate_transform) {
        double[] location = new double[2];
        location[0] = coor[1];
        location[1] = coor[2];
        IntPoint center = p_coordinate_transform.dsn_to_board(location).round();
        int radius = (int) Math.round(p_coordinate_transform.dsn_to_board(coor[0]) / 2);
        return new net.freerouting.freeroute.geometry.planar.Circle(center, radius);
    }

    @Override
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board_rel(CoordinateTransform p_coordinate_transform) {
        int[] new_coor = new int[3];
        new_coor[0] = (int) Math.round(p_coordinate_transform.dsn_to_board(coor[0]) / 2);
        for (int i = 1; i < 3; ++i) {
            new_coor[i] = (int) Math.round(p_coordinate_transform.dsn_to_board(coor[i]));
        }
        return new net.freerouting.freeroute.geometry.planar.Circle(new IntPoint(new_coor[1], new_coor[2]), new_coor[0]);
    }

    @Override
    public Rectangle bounding_box() {
        double[] bounds = new double[4];
        bounds[0] = coor[1] - coor[0];
        bounds[1] = coor[2] - coor[0];
        bounds[2] = coor[1] + coor[0];
        bounds[3] = coor[2] + coor[0];
        return new Rectangle(layer, bounds);
    }

    @Override
    public void write_scope(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
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
    public void write_scope_int(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
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

    /**
     * Reads a circle scope from a Specctra dsn file.
     */
    static Shape read_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws ReadScopeException {
        try {
            Layer circle_layer = null;
            boolean layer_ok = true;
            double circle_coor[] = new double[3];

            Object next_token = p_scanner.next_token();
            if (next_token == Keyword.PCB_SCOPE) {
                circle_layer = Layer.PCB;
            } else if (next_token == Keyword.SIGNAL) {
                circle_layer = Layer.SIGNAL;
            } else {
                if (p_layer_structure == null) {
                    throw new ReadScopeException("Shape.read_circle_scope: p_layer_structure != null expected");
                }
                if (!(next_token instanceof String)) {
                    throw new ReadScopeException("Shape.read_circle_scope: string for layer_name expected");
                }
                int layer_no = p_layer_structure.get_no((String) next_token);
                if (layer_no < 0 || layer_no >= p_layer_structure.arr.length) {
                    System.out.print("Shape.read_circle_scope: layer with name ");
                    System.out.print((String) next_token);
                    System.out.println(" not found in layer stracture ");
                    layer_ok = false;
                } else {
                    circle_layer = p_layer_structure.arr[layer_no];
                }
            }
            // fill the the the coordinates
            int curr_index = 0;
            for (;;) {
                next_token = p_scanner.next_token();
                if (next_token == Keyword.CLOSED_BRACKET) {
                    break;
                }
                if (curr_index > 2) {
                    throw new ReadScopeException("Shape.read_circle_scope: closed bracket expected");
                }
                if (next_token instanceof Double) {
                    circle_coor[curr_index] = (double) next_token;
                } else if (next_token instanceof Integer) {
                    circle_coor[curr_index] = ((Number) next_token).doubleValue();
                } else {
                    throw new ReadScopeException("Shape.read_circle_scope: number expected");
                }
                ++curr_index;
            }
            if (!layer_ok) {
                return null;
            }
            return new Circle(circle_layer, circle_coor);
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Shape.read_rectangle_scope: IO error scanning file", e);
        }
    }
}
