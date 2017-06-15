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

/**
 *
 * @author Robert Buj
 */
public final class SnapshotAttributes {

    EnumMap<SnapshotAttributeKey, Boolean> snapshot_attributes_map;

    public SnapshotAttributes() {
        snapshot_attributes_map = new EnumMap<>(SnapshotAttributeKey.class);
        for (SnapshotAttributeKey key : SnapshotAttributeKey.values()) {
            snapshot_attributes_map.put(key, Boolean.TRUE);
        }
    }

    public SnapshotAttributes(SnapshotAttributes p_snapshot_attributes) {
        snapshot_attributes_map = p_snapshot_attributes.clone_map();
    }

    public boolean get(SnapshotAttributeKey key) {
        return snapshot_attributes_map.get(key);
    }

    public void set(SnapshotAttributeKey key, boolean value) {
        snapshot_attributes_map.replace(key, value);
    }

    public EnumMap<SnapshotAttributeKey, Boolean> clone_map() {
        EnumMap<SnapshotAttributeKey, Boolean> p_snapshot_attributes_map = new EnumMap<>(snapshot_attributes_map);
        return p_snapshot_attributes_map;
    }
}
