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
import java.util.LinkedList;
import static net.freerouting.freeroute.designformats.specctra.DsnFile.read_string_scope;
import static net.freerouting.freeroute.designformats.specctra.ScopeKeyword.skip_scope;
import static net.freerouting.freeroute.designformats.specctra.Shape.read_shape_scope;
import net.freerouting.freeroute.geometry.planar.PolylineShape;

/**
 *
 * @author Robert Buj
 */
class Area {

    /**
     * Reads a shape, which may contain holes from a specctra dsn-file. The
     * first shape in the shape_list of the result is the border of the area.
     * The other shapes in the shape_list are holes (windows).
     */
    static Area read_area_scope(Scanner p_scanner, LayerStructure p_layer_structure,
            boolean p_skip_window_scopes) throws DsnFileException, ReadScopeException {
        Collection<Shape> shape_list = new LinkedList<>();
        String clearance_class_name = null;
        String area_name = null;
        boolean result_ok = true;
        Object next_token;
        try {
            next_token = p_scanner.next_token();
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Area.read_area_scope: IO error scanning file", e);
        }
        if (next_token instanceof String) {
            String curr_name = (String) next_token;
            if (!curr_name.isEmpty()) {
                area_name = curr_name;
            }
        }
        Shape curr_shape = read_shape_scope(p_scanner, p_layer_structure);
        if (curr_shape == null) {
            result_ok = false;
        }
        shape_list.add(curr_shape);
        next_token = null;
        for (;;) {
            Object prev_token = next_token;
            try {
                next_token = p_scanner.next_token();
            } catch (java.io.IOException e) {
                throw new ReadScopeException("Area.read_area_scope: IO error scanning file");
            }
            if (next_token == null) {
                throw new ReadScopeException("Area.read_area_scope: unexpected end of file");
            }
            if (next_token == Keyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }

            if (prev_token == Keyword.OPEN_BRACKET) {
                // a new scope is expected
                if (next_token == Keyword.WINDOW && !p_skip_window_scopes) {
                    Shape hole_shape = read_shape_scope(p_scanner, p_layer_structure);
                    shape_list.add(hole_shape);
                    // overread closing bracket
                    try {
                        next_token = p_scanner.next_token();
                    } catch (java.io.IOException e) {
                        throw new ReadScopeException("Area.read_area_scope: IO error scanning file");
                    }
                    if (next_token != Keyword.CLOSED_BRACKET) {
                        throw new ReadScopeException("Area.read_area_scope: closed bracket expected");
                    }
                } else if (next_token == Keyword.CLEARANCE_CLASS) {
                    clearance_class_name = read_string_scope(p_scanner);
                } else {
                    // skip unknown scope
                    skip_scope(p_scanner);
                }
            }
        }
        if (!result_ok) {
            return null;
        }
        return new Area(area_name, shape_list, clearance_class_name);
    }

    /**
     * Transforms a shape with holes to the board coordinate system. The first
     * shape in the Collection p_area is the border, the other shapes are holes
     * of the area.
     */
    static net.freerouting.freeroute.geometry.planar.Area transform_area_to_board(Collection<Shape> p_area, CoordinateTransform p_coordinate_transform) {
        int hole_count = p_area.size() - 1;
        if (hole_count <= -1) {
            System.out.println("Area.transform_area_to_board: p_area.size() > 0 expected");
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
                System.out.println("Area.transform_area_to_board: PolylineShape expected");
                return null;
            }
            PolylineShape border = (PolylineShape) boundary_shape;
            PolylineShape[] holes = new PolylineShape[hole_count];
            for (int i = 0; i < holes.length; ++i) {
                net.freerouting.freeroute.geometry.planar.Shape hole_shape = it.next().transform_to_board(p_coordinate_transform);
                if (!(hole_shape instanceof PolylineShape)) {
                    System.out.println("Area.transform_area_to_board: PolylineShape expected");
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
    static net.freerouting.freeroute.geometry.planar.Area transform_area_to_board_rel(Collection<Shape> p_area, CoordinateTransform p_coordinate_transform) {
        int hole_count = p_area.size() - 1;
        if (hole_count <= -1) {
            System.out.println("Area.transform_area_to_board_rel: p_area.size() > 0 expected");
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
                System.out.println("Area.transform_area_to_board_rel: PolylineShape expected");
                return null;
            }
            PolylineShape border = (PolylineShape) boundary_shape;
            PolylineShape[] holes = new PolylineShape[hole_count];
            for (int i = 0; i < holes.length; ++i) {
                net.freerouting.freeroute.geometry.planar.Shape hole_shape = it.next().transform_to_board_rel(p_coordinate_transform);
                if (!(hole_shape instanceof PolylineShape)) {
                    System.out.println("Area.transform_area_to_board: PolylineShape expected");
                    return null;
                }
                holes[i] = (PolylineShape) hole_shape;
            }
            result = new net.freerouting.freeroute.geometry.planar.PolylineArea(border, holes);
        }
        return result;
    }

    String area_name; // may be generated later on, if area_name is null.
    final Collection<Shape> shape_list;
    final String clearance_class_name;

    private Area(String p_area_name, Collection<Shape> p_shape_list,
            String p_clearance_class_name) {
        area_name = p_area_name;
        shape_list = p_shape_list;
        clearance_class_name = p_clearance_class_name;
    }
}
