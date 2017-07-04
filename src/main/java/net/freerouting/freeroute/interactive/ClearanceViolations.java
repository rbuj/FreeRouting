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
 * ClearanceViolations.java
 *
 * Created on 3. Oktober 2004, 09:13
 */
package net.freerouting.freeroute.interactive;

import com.google.common.collect.ImmutableList;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.freerouting.freeroute.board.ClearanceViolation;
import net.freerouting.freeroute.board.Item;
import net.freerouting.freeroute.boardgraphics.GraphicsContext;

/**
 * To display the clearance violations between items on the screen.
 *
 * @author alfons
 */
public class ClearanceViolations implements Iterable<ClearanceViolation>{

    /**
     * The list of clearance violations.
     */
    private final ImmutableList<ClearanceViolation> immutable_list;

    /**
     * Creates a new instance of ClearanceViolations
     */
    public ClearanceViolations(Collection<Item> p_item_list) {
        List<ClearanceViolation> list = p_item_list.stream()
                .map(item -> item.clearance_violations())
                .flatMap(cv -> cv.stream())
                .collect(Collectors.toCollection(ArrayList<ClearanceViolation>::new));
        immutable_list = ImmutableList.copyOf(list);
    }

    public void draw(Graphics p_graphics, GraphicsContext p_graphics_context) {
        java.awt.Color draw_color = p_graphics_context.get_violations_color();
        for (ClearanceViolation curr_violation : immutable_list) {
            double intensity = p_graphics_context.get_layer_visibility(curr_violation.layer_no);
            p_graphics_context.fill_area(curr_violation.shape, p_graphics, draw_color, intensity);
            // draw a circle around the violation.
            double draw_radius = curr_violation.first_item.board.rules.get_min_trace_half_width() * 5;
            p_graphics_context.draw_circle(curr_violation.shape.centre_of_gravity(), draw_radius, 0.1 * draw_radius, draw_color,
                    p_graphics, intensity);
        }
    }

    @Override
    public Iterator<ClearanceViolation> iterator() {
        return immutable_list.iterator();
    }

    int size() {
        return immutable_list.size();
    }
}
