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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;

/**
 *
 * @author robert
 */
public class SavableSubwindows {

    enum SAVABLE_SUBWINDOW_KEY {
        ABOUT, ROUTE_PARAMETER, AUTOROUTE_PARAMETER, SELECT_PARAMETER, MOVE_PARAMETER,
        CLEARANCE_MATRIX, VIA, EDIT_VIAS, EDIT_NET_RULES, ASSIGN_NET_CLASSES,
        PADSTACKS, PACKAGES, INCOMPLETES, NET_INFO, CLEARANCE_VIOLATIONS,
        LENGHT_VIOLATIONS, UNCONNECTED_ROUTE, ROUTE_STUBS, COMPONENTS, LAYER_VISIBILITY,
        OBJECT_VISIBILITY, DISPLAY_MISC, SNAPSHOT, COLOR_MANAGER
    }

    EnumMap<SAVABLE_SUBWINDOW_KEY, BoardSavableSubWindow> savable_subwindows_map;

    /**
     * The contructor method.
     */
    public SavableSubwindows() {
        savable_subwindows_map = new EnumMap<>(SAVABLE_SUBWINDOW_KEY.class);
    }

    /**
     * Associates the specified board_savable_subwindow with the specified
     * savable_subwindow_key in savable_subwindows_map. If
     * savable_subwindows_map previously contained a mapping for the key, the
     * old value is replaced.
     *
     * @param savable_subwindow_key key with which the specified
     * BoardSavableSubWindow is to be associated
     * @param board_savable_subwindow BoardSavableSubWindow to be associated
     * with the specified key
     */
    void put(SAVABLE_SUBWINDOW_KEY savable_subwindow_key, BoardSavableSubWindow board_savable_subwindow) {
        savable_subwindows_map.put(savable_subwindow_key, board_savable_subwindow);
    }

    /**
     * Returns the BoardSavableSubWindow to which the specified
     * savable_subwindow_key is mapped, or null if the savable_subwindows_map
     * contains no mapping for the savable_subwindow_key.
     *
     * @param savable_subwindow_key
     * @return the BoardSavableSubWindow to which the specified key is mapped,
     * or null if savable_subwindows_map contains no mapping for the key
     */
    BoardSavableSubWindow get(SAVABLE_SUBWINDOW_KEY savable_subwindow_key) {
        return savable_subwindows_map.get(savable_subwindow_key);
    }

    /**
     * Save all subwindows.
     *
     * @param object_stream the ObjectInputStream.
     */
    void read_all(ObjectInputStream object_stream) {
        for (BoardSavableSubWindow window : savable_subwindows_map.values()) {
            window.read(object_stream);
        }
    }

    /**
     * Save all subwindows.
     *
     * @param object_stream the ObjectOutputStream.
     */
    void save_all(ObjectOutputStream object_stream) {
        for (BoardSavableSubWindow window : savable_subwindows_map.values()) {
            window.save(object_stream);
        }
    }

    /**
     * Dispose all subwindows.
     */
    void dispose_all() {
        for (BoardSavableSubWindow window : savable_subwindows_map.values()) {
            if (window != null) {
                window.dispose();
                window = null;
            }
        }
    }

    /**
     * Restores the visible state of all subwindows.
     */
    void deiconifed_all() {
        for (BoardSavableSubWindow window : savable_subwindows_map.values()) {
            if (window != null) {
                window.parent_deiconified();
            }
        }
    }

    /**
     * Saves the visible state of all subwindows all subwindows before hiding
     * them.
     */
    void iconifed_all() {
        for (BoardSavableSubWindow window : savable_subwindows_map.values()) {
            window.parent_iconified();
        }
    }

    /**
     * Refresh all subwindows.
     */
    void refresh_all() {
        for (BoardSavableSubWindow window : savable_subwindows_map.values()) {
            if (window != null) {
                window.refresh();
            }
        }
    }

    /**
     * Repaint all subwindows.
     */
    void repaint_all() {
        for (BoardSavableSubWindow window : savable_subwindows_map.values()) {
            window.repaint();
        }
    }
}
