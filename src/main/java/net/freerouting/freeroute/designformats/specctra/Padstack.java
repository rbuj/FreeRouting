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
import net.freerouting.freeroute.geometry.planar.PolygonShape;
import net.freerouting.freeroute.geometry.planar.Simplex;

/**
 *
 * @author robert
 */
public class Padstack {

    public static void write_padstack_scope(WriteScopeParameter p_par, net.freerouting.freeroute.library.Padstack p_padstack) throws java.io.IOException {
        // search the layer range of the padstack
        int first_layer_no = 0;
        while (first_layer_no < p_par.board.get_layer_count()) {
            if (p_padstack.get_shape(first_layer_no) != null) {
                break;
            }
            ++first_layer_no;
        }
        int last_layer_no = p_par.board.get_layer_count() - 1;
        while (last_layer_no >= 0) {
            if (p_padstack.get_shape(last_layer_no) != null) {
                break;
            }
            --last_layer_no;
        }
        if (first_layer_no >= p_par.board.get_layer_count() || last_layer_no < 0) {
            System.out.println("Library.write_padstack_scope: padstack shape not found");
            return;
        }

        p_par.file.start_scope("padstack ");
        p_par.identifier_type.write(p_padstack.name, p_par.file);
        for (int i = first_layer_no; i <= last_layer_no; ++i) {
            net.freerouting.freeroute.geometry.planar.Shape curr_board_shape = p_padstack.get_shape(i);
            if (curr_board_shape == null) {
                continue;
            }
            net.freerouting.freeroute.board.Layer board_layer = p_par.board.layer_structure.arr[i];
            Layer curr_layer = new Layer(board_layer.name, i, board_layer.is_signal);
            Shape curr_shape = p_par.coordinate_transform.board_to_dsn_rel(curr_board_shape, curr_layer);
            p_par.file.start_scope("shape");
            curr_shape.write_scope(p_par.file, p_par.identifier_type);
            p_par.file.end_scope();
        }
        if (!p_padstack.attach_allowed) {
            p_par.file.new_line();
            p_par.file.write("(attach off)");
        }
        if (p_padstack.placed_absolute) {
            p_par.file.new_line();
            p_par.file.write("(absolute on)");
        }
        p_par.file.end_scope();
    }

    static boolean read_padstack_scope(Scanner p_scanner, LayerStructure p_layer_structure,
            CoordinateTransform p_coordinate_transform, net.freerouting.freeroute.library.Padstacks p_board_padstacks) throws DsnFileException, ReadScopeException {
        String padstack_name;
        boolean is_drilllable = true;
        boolean placed_absolute = false;
        Collection<Shape> shape_list = new LinkedList<>();
        try {
            Object next_token = p_scanner.next_token();
            if (next_token instanceof String) {
                padstack_name = (String) next_token;
            } else {
                throw new ReadScopeException("Library.read_padstack_scope: unexpected padstack identifier");
            }

            while (next_token != Keyword.CLOSED_BRACKET) {
                Object prev_token = next_token;
                next_token = p_scanner.next_token();
                if (prev_token == Keyword.OPEN_BRACKET) {
                    if (next_token == Keyword.SHAPE) {
                        Shape curr_shape = ShapeReadable.read_scope(p_scanner, p_layer_structure);
                        if (curr_shape != null) {
                            shape_list.add(curr_shape);
                        }
                        // overread the closing bracket and unknown scopes.
                        Object curr_next_token = p_scanner.next_token();
                        while (curr_next_token == Keyword.OPEN_BRACKET) {
                            ScopeKeyword.skip_scope(p_scanner);
                            curr_next_token = p_scanner.next_token();
                        }
                        if (curr_next_token != Keyword.CLOSED_BRACKET) {
                            throw new ReadScopeException("Library.read_padstack_scope: closing bracket expected");
                        }
                    } else if (next_token == Keyword.ATTACH) {
                        is_drilllable = DsnFile.read_on_off_scope(p_scanner);
                    } else if (next_token == Keyword.ABSOLUTE) {
                        placed_absolute = DsnFile.read_on_off_scope(p_scanner);
                    } else {
                        ScopeKeyword.skip_scope(p_scanner);
                    }
                }

            }
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Library.read_padstack_scope: IO error scanning file", e);
        }
        if (p_board_padstacks.get(padstack_name) != null) {
            // Padstack exists already
            return true;
        }
        if (shape_list.isEmpty()) {
            System.out.print("Library.read_padstack_scope: shape not found for padstack with name ");
            System.out.println(padstack_name);
            return true;
        }
        net.freerouting.freeroute.geometry.planar.ConvexShape[] padstack_shapes = new net.freerouting.freeroute.geometry.planar.ConvexShape[p_layer_structure.arr.length];
        Iterator<Shape> it = shape_list.iterator();
        while (it.hasNext()) {
            Shape pad_shape = it.next();
            net.freerouting.freeroute.geometry.planar.Shape curr_shape = pad_shape.transform_to_board_rel(p_coordinate_transform);
            net.freerouting.freeroute.geometry.planar.ConvexShape convex_shape;
            if (curr_shape instanceof net.freerouting.freeroute.geometry.planar.ConvexShape) {
                convex_shape = (net.freerouting.freeroute.geometry.planar.ConvexShape) curr_shape;
            } else {
                if (curr_shape instanceof PolygonShape) {
                    curr_shape = ((PolygonShape) curr_shape).convex_hull();
                }
                net.freerouting.freeroute.geometry.planar.TileShape[] convex_shapes = curr_shape.split_to_convex();
                if (convex_shapes.length != 1) {
                    System.out.println("Library.read_padstack_scope: convex shape expected");
                }
                convex_shape = convex_shapes[0];
                if (convex_shape instanceof Simplex) {
                    convex_shape = ((Simplex) convex_shape).simplify();
                }
            }
            net.freerouting.freeroute.geometry.planar.ConvexShape padstack_shape = convex_shape;
            if (padstack_shape != null) {
                if (padstack_shape.dimension() < 2) {
                    System.out.print("Library.read_padstack_scope: shape is not an area ");
                    // enllarge the shape a little bit, so that it is an area
                    padstack_shape = padstack_shape.offset(1);
                    if (padstack_shape.dimension() < 2) {
                        padstack_shape = null;
                    }
                }
            }

            if (pad_shape.layer == Layer.PCB || pad_shape.layer == Layer.SIGNAL) {
                for (int i = 0; i < padstack_shapes.length; ++i) {
                    padstack_shapes[i] = padstack_shape;
                }
            } else {
                int shape_layer = p_layer_structure.get_no(pad_shape.layer.name);
                if (shape_layer < 0 || shape_layer >= padstack_shapes.length) {
                    throw new ReadScopeException("Library.read_padstack_scope: layer number found");
                }
                padstack_shapes[shape_layer] = padstack_shape;
            }
        }
        p_board_padstacks.add(padstack_name, padstack_shapes, is_drilllable, placed_absolute);
        return true;
    }
}
