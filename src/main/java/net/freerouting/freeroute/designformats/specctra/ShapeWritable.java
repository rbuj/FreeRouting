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

import net.freerouting.freeroute.datastructures.IdentifierType;
import net.freerouting.freeroute.datastructures.IndentFileWriter;

/**
 *
 * @author robert
 */
public interface ShapeWritable {
    
    /**
     * Writes a shape scope to a Specctra dsn file.
     */
    void write_scope(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException;

    /**
     * Writes a shape scope to a Specctra session file. In a session file all
     * coordinates must be integer.
     */
    void write_scope_int(IndentFileWriter p_file, IdentifierType p_identifier) throws java.io.IOException;

    default void write_hole_scope(IndentFileWriter p_file, IdentifierType p_identifier_type) throws java.io.IOException {
        p_file.start_scope("window");
        write_scope(p_file, p_identifier_type);
        p_file.end_scope();
    }

}
