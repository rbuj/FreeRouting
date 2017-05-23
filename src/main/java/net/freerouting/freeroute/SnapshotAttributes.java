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
 * @author robert
 */
public class SnapshotAttributes {

    public enum SNAPSHOT_ATTRIBUTE_KEY {
        OBJECT_COLORS, OBJECT_VISIBILITY, LAYER_VISIBILITY, DISPLAY_REGION,
        INTERACTIVE_STATE, SELECTION_LAYERS, SELECTABLE_ITEMS, CURRENT_LAYER,
        RULE_SELECTION, MANUAL_RULE_SETTINGS, PUSH_AND_SHOVE_ENABLED,
        DRAG_COMPONENTS_ENABLED, PULL_TIGHT_REGION, COMPONENT_GRID,
        INFO_LIST_SELECTIONS
    }

    EnumMap<SNAPSHOT_ATTRIBUTE_KEY, Boolean> snapshot_attributes_map;

    public SnapshotAttributes() {
        snapshot_attributes_map = new EnumMap<>(SNAPSHOT_ATTRIBUTE_KEY.class);
        for (SNAPSHOT_ATTRIBUTE_KEY key : SNAPSHOT_ATTRIBUTE_KEY.values()) {
            snapshot_attributes_map.put(key, Boolean.TRUE);
        }
    }

    public SnapshotAttributes(SnapshotAttributes p_snapshot_attributes) {
        snapshot_attributes_map = p_snapshot_attributes.clone_map();
    }

    public boolean get(SNAPSHOT_ATTRIBUTE_KEY key) {
        return snapshot_attributes_map.get(key);
    }

    public void set(SNAPSHOT_ATTRIBUTE_KEY key, boolean value) {
        snapshot_attributes_map.replace(key, value);
    }

    public EnumMap<SNAPSHOT_ATTRIBUTE_KEY, Boolean> clone_map() {
        EnumMap<SNAPSHOT_ATTRIBUTE_KEY, Boolean> p_snapshot_attributes_map = new EnumMap<>(SNAPSHOT_ATTRIBUTE_KEY.class);
        p_snapshot_attributes_map = snapshot_attributes_map.clone();
        return p_snapshot_attributes_map;
    }
}
