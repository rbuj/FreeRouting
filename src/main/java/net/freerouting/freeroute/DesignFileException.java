/*
 * Copyright (C) 2016 Robert Antoni Buj Gelonch {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
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
package net.freerouting.freeroute;

/**
 *
 * @author Robert Antoni Buj Gelonch
 * {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 */
@SuppressWarnings("serial")
public final class DesignFileException extends Exception {

    /**
     * Creates a new instance of <code>DesignFileException</code> without detail
     * message.
     */
    public DesignFileException() {
    }

    /**
     * Constructs an instance of <code>DesignFileException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DesignFileException(String msg) {
        super(msg);
    }

    public DesignFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
