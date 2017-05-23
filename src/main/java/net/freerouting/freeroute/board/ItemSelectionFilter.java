/*
 *  Copyright (C) 2014  Alfons Wirtz  
 *   website www.freerouting.net
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License at <http://www.gnu.org/licenses/> 
 *   for more details.
 *
 * ItemSelectionFilter.java
 *
 * Created on 14. Dezember 2004, 10:57
 */
package net.freerouting.freeroute.board;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * Filter for selecting items on the board.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class ItemSelectionFilter implements java.io.Serializable {

    /**
     * The possible choices in the filter.
     */
    public enum SELECTABLE_CHOICES {
        TRACES, VIAS, PINS, CONDUCTION, KEEPOUT, VIA_KEEPOUT, COMPONENT_KEEPOUT, BOARD_OUTLINE, FIXED, UNFIXED
    }

    /**
     * the filter array of the item types
     */
    private final EnumMap<SELECTABLE_CHOICES, Boolean> values = new EnumMap<>(SELECTABLE_CHOICES.class);

    /**
     * Creates a new filter with all item types selected.
     */
    public ItemSelectionFilter() {
        for (SELECTABLE_CHOICES value : SELECTABLE_CHOICES.values()) {
            values.put(value, true);
        }
        values.replace(SELECTABLE_CHOICES.KEEPOUT, false);
        values.replace(SELECTABLE_CHOICES.VIA_KEEPOUT, false);
        values.replace(SELECTABLE_CHOICES.COMPONENT_KEEPOUT, false);
        values.replace(SELECTABLE_CHOICES.CONDUCTION, false);
        values.replace(SELECTABLE_CHOICES.BOARD_OUTLINE, false);
    }

    /**
     * Creates a new filter with only p_item_type selected.
     */
    public ItemSelectionFilter(SELECTABLE_CHOICES p_item_type) {
        for (SELECTABLE_CHOICES value : SELECTABLE_CHOICES.values()) {
            values.put(value, false);
        }
        values.replace(p_item_type, true);
        values.replace(SELECTABLE_CHOICES.FIXED, true);
        values.replace(SELECTABLE_CHOICES.UNFIXED, true);
    }

    /**
     * Creates a new filter with only p_item_types selected.
     */
    public ItemSelectionFilter(SELECTABLE_CHOICES[] p_item_types) {
        for (SELECTABLE_CHOICES value : SELECTABLE_CHOICES.values()) {
            values.put(value, false);
        }
        for (int i = 0; i < p_item_types.length; ++i) {
            values.replace(p_item_types[i], true);
        }
        values.replace(SELECTABLE_CHOICES.FIXED, true);
        values.replace(SELECTABLE_CHOICES.UNFIXED, true);
    }

    /**
     * Copy constructor
     */
    public ItemSelectionFilter(ItemSelectionFilter p_item_selection_filter) {
        values.putAll(p_item_selection_filter.values);
    }

    /**
     * Selects or deselects an item type
     */
    public void set_selected(SELECTABLE_CHOICES p_choice, boolean p_value) {
        values.replace(p_choice, p_value);
    }

    /**
     * Selects all item types.
     */
    public void select_all() {
        for (Entry<SELECTABLE_CHOICES, Boolean> entry : values.entrySet()) {
            entry.setValue(true);
        }
    }

    /**
     * Deselects all item types.
     */
    public void deselect_all() {
        for (Entry<SELECTABLE_CHOICES, Boolean> entry : values.entrySet()) {
            entry.setValue(false);
        }
    }

    /**
     * Filters a collection of items with this filter.
     */
    public Set<Item> filter(java.util.Set<net.freerouting.freeroute.board.Item> p_items) {
        Set<Item> result = new TreeSet<>();
        for (net.freerouting.freeroute.board.Item curr_item : p_items) {
            if (curr_item.is_selected_by_filter(this)) {
                result.add(curr_item);
            }
        }
        return result;
    }

    /**
     * Looks, if the input item type is selected.
     */
    public boolean is_selected(SELECTABLE_CHOICES p_choice) {
        return values.get(p_choice);
    }
}
