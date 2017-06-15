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
 *
 * @author Robert Buj
 */
public final class TemporarySubwindows {

    java.util.Collection<BoardTemporarySubWindow> temporary_subwindows_list;

    /**
     * The constructor method.
     */
    public TemporarySubwindows() {
        temporary_subwindows_list = new java.util.LinkedList<>();
    }

    /**
     * Dispose all subwindows.
     */
    void dispose_all() {
        for (BoardTemporarySubWindow curr_subwindow : temporary_subwindows_list) {
            if (curr_subwindow != null) {
                curr_subwindow.board_frame_disposed();
            }
        }
    }

    /**
     * Saves the visible state of all subwindows all subwindows before hiding
     * them.
     */
    void iconifed_all() {
        for (BoardSubWindow curr_subwindow : temporary_subwindows_list) {
            if (curr_subwindow != null) {
                curr_subwindow.parent_iconified();
            }
        }
    }

    /**
     * Restores the visible state of all subwindows.
     */
    void deiconifed_all() {
        for (BoardSubWindow curr_subwindow : temporary_subwindows_list) {
            if (curr_subwindow != null) {
                curr_subwindow.parent_deiconified();
            }
        }
    }

    /**
     * Appends a temporary window to temporary subwindow list.
     *
     * @param window temporary window
     */
    void add(BoardTemporarySubWindow window) {
        temporary_subwindows_list.add(window);
    }

    /**
     * Removes the specified temporary window from temporary subwindow list.
     *
     * @param window temporary window
     */
    void remove(BoardTemporarySubWindow window) {
        temporary_subwindows_list.remove(window);
    }
}
