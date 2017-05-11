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
import java.util.LinkedList;

/**
 *
 * @author robert
 */
public interface ShapeReadable {

    static class ReadAreaScopeResult {

        String area_name; // may be generated later on, if area_name is null.
        final Collection<Shape> shape_list;
        final String clearance_class_name;

        private ReadAreaScopeResult(String p_area_name, Collection<Shape> p_shape_list, String p_clearance_class_name) {
            area_name = p_area_name;
            shape_list = p_shape_list;
            clearance_class_name = p_clearance_class_name;
        }
    }

    /**
     * Reads shape scope from a Specctra dsn file. If p_layer_structure == null,
     * only Layer.PCB and Layer.Signal are expected, no induvidual layers.
     */
    static Shape read_scope(Scanner p_scanner, LayerStructure p_layer_structure) {
        Shape result = null;
        try {
            Object next_token = p_scanner.next_token();
            if (next_token == Keyword.OPEN_BRACKET) {
                // overread the open bracket
                next_token = p_scanner.next_token();
            }

            if (next_token == Keyword.RECTANGLE) {
                result = Rectangle.read_scope(p_scanner, p_layer_structure);
            } else if (next_token == Keyword.POLYGON) {
                result = Polygon.read_scope(p_scanner, p_layer_structure);
            } else if (next_token == Keyword.CIRCLE) {
                result = Circle.read_scope(p_scanner, p_layer_structure);
            } else if (next_token == Keyword.POLYGON_PATH) {
                result = PolygonPath.read_scope(p_scanner, p_layer_structure);
            } else {
                // not a shape scope, skip it.
                ScopeKeyword.skip_scope(p_scanner);
            }
        } catch (java.io.IOException e) {
            System.out.println("Shape.read_scope: IO error scanning file");
            System.out.println(e);
            return result;
        }
        return result;
    }

    /**
     * Reads a shape , which may contain holes from a specctra dsn-file. The
     * first shape in the shape_list of the result is the border of the area.
     * The other shapes in the shape_list are holes (windows).
     */
    static ReadAreaScopeResult read_area_scope(Scanner p_scanner,
            LayerStructure p_layer_structure, boolean p_skip_window_scopes) throws DsnFileException {
        Collection<Shape> shape_list = new LinkedList<>();
        String clearance_class_name = null;
        String area_name = null;
        boolean result_ok = true;
        Object next_token;
        try {
            next_token = p_scanner.next_token();
        } catch (java.io.IOException e) {
            System.out.println("Shape.read_area_scope: IO error scanning file");
            return null;
        }
        if (next_token instanceof String) {
            String curr_name = (String) next_token;
            if (!curr_name.isEmpty()) {
                area_name = curr_name;
            }
        }
        Shape curr_shape = read_scope(p_scanner, p_layer_structure);
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
                System.out.println("Shape.read_area_scope: IO error scanning file");
                return null;
            }
            if (next_token == null) {
                System.out.println("Shape.read_area_scope: unexpected end of file");
                return null;
            }
            if (next_token == Keyword.CLOSED_BRACKET) {
                // end of scope
                break;
            }

            if (prev_token == Keyword.OPEN_BRACKET) {
                // a new scope is expected
                if (next_token == Keyword.WINDOW && !p_skip_window_scopes) {
                    Shape hole_shape = read_scope(p_scanner, p_layer_structure);
                    shape_list.add(hole_shape);
                    // overread closing bracket
                    try {
                        next_token = p_scanner.next_token();
                    } catch (java.io.IOException e) {
                        System.out.println("Shape.read_area_scope: IO error scanning file");
                        return null;
                    }
                    if (next_token != Keyword.CLOSED_BRACKET) {
                        System.out.println("Shape.read_area_scope: closed bracket expected");
                        return null;
                    }

                } else if (next_token == Keyword.CLEARANCE_CLASS) {
                    clearance_class_name = DsnFile.read_string_scope(p_scanner);
                } else {
                    // skip unknown scope
                    ScopeKeyword.skip_scope(p_scanner);
                }
            }
        }
        if (!result_ok) {
            return null;
        }
        return new ReadAreaScopeResult(area_name, shape_list, clearance_class_name);
    }
}
