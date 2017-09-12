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
enum SavableSubwindowKey {
    ABOUT {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowAbout();
        }
    }, ROUTE_PARAMETER {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowRouteParameter(board_frame);
        }
    }, AUTOROUTE_PARAMETER {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowAutorouteParameter(board_frame);
        }
    }, SELECT_PARAMETER {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowSelectParameter(board_frame);
        }
    }, MOVE_PARAMETER {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowMoveParameter(board_frame);
        }
    },
    CLEARANCE_MATRIX {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowClearanceMatrix(board_frame);
        }
    }, VIA {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowVia(board_frame);
        }
    }, EDIT_VIAS {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowEditVias(board_frame);
        }
    }, EDIT_NET_RULES {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowNetClasses(board_frame);
        }
    }, ASSIGN_NET_CLASSES {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowAssignNetClass(board_frame);
        }
    },
    PADSTACKS {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowPadstacks(board_frame);
        }
    }, PACKAGES {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowPackages(board_frame);
        }
    }, INCOMPLETES {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowIncompletes(board_frame);
        }
    }, NET_INFO {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowNets(board_frame);
        }
    }, CLEARANCE_VIOLATIONS {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowClearanceViolations(board_frame);
        }
    },
    LENGHT_VIOLATIONS {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowLengthViolations(board_frame);
        }
    }, UNCONNECTED_ROUTE {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowUnconnectedRoute(board_frame);
        }
    }, ROUTE_STUBS {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowRouteStubs(board_frame);
        }
    }, COMPONENTS {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowComponents(board_frame);
        }
    }, LAYER_VISIBILITY {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return WindowLayerVisibility.get_instance(board_frame);
        }
    },
    OBJECT_VISIBILITY {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return WindowObjectVisibility.get_instance(board_frame);
        }
    }, DISPLAY_MISC {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowDisplayMisc(board_frame);
        }
    }, SNAPSHOT {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new WindowSnapshot(board_frame);
        }
    }, COLOR_MANAGER {
        @Override
        BoardSavableSubWindow create(BoardFrame board_frame) {
            return new ColorManager(board_frame);
        }
    };

    abstract BoardSavableSubWindow create(BoardFrame board_frame);
}
