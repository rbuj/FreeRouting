/*
 * Copyright (C) 2017 Robert Antoni Buj Gelonch {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.Iterator;
import net.freerouting.freeroute.geometry.planar.PolylineShape;

/**
 *
 * @author robert
 */
public interface ShapeTransformable {
    /**
     * Transforms a specctra dsn shape to a geometry.planar.Shape.
     */
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board(CoordinateTransform p_coordinate_transform);

    /**
     * Transforms the relative (vector) coordinates of a specctra dsn shape to a
     * geometry.planar.Shape.
     */
    public net.freerouting.freeroute.geometry.planar.Shape transform_to_board_rel(CoordinateTransform p_coordinate_transform);

    /**
     * Transforms a shape with holes to the board coordinate system. The first
     * shape in the Collection p_area is the border, the other shapes are holes
     * of the area.
     */
    static public net.freerouting.freeroute.geometry.planar.Area transform_area_to_board(Collection<Shape> p_area, CoordinateTransform p_coordinate_transform) {
        int hole_count = p_area.size() - 1;
        if (hole_count <= -1) {
            System.out.println("Shape.transform_area_to_board: p_area.size() > 0 expected");
            return null;
        }
        Iterator<Shape> it = p_area.iterator();
        Shape boundary = it.next();
        net.freerouting.freeroute.geometry.planar.Shape boundary_shape = boundary.transform_to_board(p_coordinate_transform);
        net.freerouting.freeroute.geometry.planar.Area result;
        if (hole_count == 0) {
            result = boundary_shape;
        } else {
            // Area with holes
            if (!(boundary_shape instanceof net.freerouting.freeroute.geometry.planar.PolylineShape)) {
                System.out.println("Shape.transform_area_to_board: PolylineShape expected");
                return null;
            }
            PolylineShape border = (PolylineShape) boundary_shape;
            PolylineShape[] holes = new PolylineShape[hole_count];
            for (int i = 0; i < holes.length; ++i) {
                net.freerouting.freeroute.geometry.planar.Shape hole_shape = it.next().transform_to_board(p_coordinate_transform);
                if (!(hole_shape instanceof PolylineShape)) {
                    System.out.println("Shape.transform_area_to_board: PolylineShape expected");
                    return null;
                }
                holes[i] = (PolylineShape) hole_shape;
            }
            result = new net.freerouting.freeroute.geometry.planar.PolylineArea(border, holes);
        }
        return result;
    }

    /**
     * Transforms the relative coordinates of a shape with holes to the board
     * coordinate system. The first shape in the Collection p_area is the
     * border, the other shapes are holes of the area.
     */
    static public net.freerouting.freeroute.geometry.planar.Area transform_area_to_board_rel(Collection<Shape> p_area, CoordinateTransform p_coordinate_transform) {
        int hole_count = p_area.size() - 1;
        if (hole_count <= -1) {
            System.out.println("Shape.transform_area_to_board_rel: p_area.size() > 0 expected");
            return null;
        }
        Iterator<Shape> it = p_area.iterator();
        Shape boundary = it.next();
        net.freerouting.freeroute.geometry.planar.Shape boundary_shape = boundary.transform_to_board_rel(p_coordinate_transform);
        net.freerouting.freeroute.geometry.planar.Area result;
        if (hole_count == 0) {
            result = boundary_shape;
        } else {
            // Area with holes
            if (!(boundary_shape instanceof net.freerouting.freeroute.geometry.planar.PolylineShape)) {
                System.out.println("Shape.transform_area_to_board_rel: PolylineShape expected");
                return null;
            }
            PolylineShape border = (PolylineShape) boundary_shape;
            PolylineShape[] holes = new PolylineShape[hole_count];
            for (int i = 0; i < holes.length; ++i) {
                net.freerouting.freeroute.geometry.planar.Shape hole_shape = it.next().transform_to_board_rel(p_coordinate_transform);
                if (!(hole_shape instanceof PolylineShape)) {
                    System.out.println("Shape.transform_area_to_board: PolylineShape expected");
                    return null;
                }
                holes[i] = (PolylineShape) hole_shape;
            }
            result = new net.freerouting.freeroute.geometry.planar.PolylineArea(border, holes);
        }
        return result;
    }
}
