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

/**
 *
 * @author Robert Buj
 */
public interface ShapeReadable {

    /**
     * Reads shape scope from a Specctra dsn file. If p_layer_structure == null,
     * only Layer.PCB and Layer.Signal are expected, no induvidual layers.
     */
    static Shape read_scope(Scanner p_scanner, LayerStructure p_layer_structure) throws ReadScopeException {
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
            throw new ReadScopeException("Shape.read_scope: IO error scanning file", e);
        }
        return result;
    }

}
