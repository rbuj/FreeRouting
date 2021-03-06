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

import java.util.EnumMap;
import java.util.Map.Entry;

/**
 * Used for storing the subwindow filters in a snapshot.
 *
 * @author Robert Buj
 */
@SuppressWarnings("serial")
public final class SubwindowSelections implements java.io.Serializable {

    private EnumMap<SnapshotSubwindowKey, WindowObjectListWithFilter.SnapshotInfo> window_selections;

    SubwindowSelections(EnumMap<SnapshotSubwindowKey, WindowObjectListWithFilter> snapshot_subwindows) {
        window_selections = new EnumMap<>(SnapshotSubwindowKey.class);
        for (Entry<SnapshotSubwindowKey, WindowObjectListWithFilter> entry : snapshot_subwindows.entrySet()) {
            window_selections.put(entry.getKey(), entry.getValue().get_snapshot_info());
        }
    }

    void set_snapshot_info(EnumMap<SnapshotSubwindowKey, WindowObjectListWithFilter> snapshot_subwindows) {
        for (Entry<SnapshotSubwindowKey, WindowObjectListWithFilter> entry : snapshot_subwindows.entrySet()) {
            entry.getValue().set_snapshot_info(window_selections.get(entry.getKey()));
        }
    }
}
