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
package net.freerouting.freeroute;

/**
 * This class contains all the constants which are related with file names or
 * their extensions.
 *
 * @author Robert Antoni Buj Gelonch
 * {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 */
public class Filename {

    static final String[] ALL_FILE_EXTENSIONS = new String[]{"bin", "dsn"};
    static final String[] TEXT_FILE_EXTENSIONS = new String[]{"dsn"};
    static final String BINARY_FILE_EXTENSIONS = "bin";
    static final String RULES_FILE_EXTENSION = ".rules";
    static final String[] LOG_FILE_EXTENSIONS = {"log"};
    static final String GUI_DEFAULTS_FILE_NAME = "gui_defaults.par";
    static final String GUI_DEFAULTS_FILE_BACKUP_NAME = "gui_defaults.par.bak";
}
