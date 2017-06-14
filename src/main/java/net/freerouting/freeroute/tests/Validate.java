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
 * Validate.java
 *
 * Created on 7. Dezember 2002, 18:26
 */
package net.freerouting.freeroute.tests;

import java.util.Iterator;
import net.freerouting.freeroute.board.BasicBoard;
import net.freerouting.freeroute.board.Item;
import net.freerouting.freeroute.board.PolylineTrace;

/**
 * Some consistancy checking on a routing board.
 *
 * @author Alfons Wirtz
 */
public class Validate {

    /**
     * check, that all traces on p_board are multiples of 45 degree
     */
    static public void multiple_of_45_degree(String p_s, BasicBoard p_board) {
        int count = 0;
        Iterator<Item> it = p_board.get_items().iterator();
        while (it.hasNext()) {
            Item curr_ob = it.next();
            if (curr_ob instanceof PolylineTrace) {
                PolylineTrace curr_trace = (PolylineTrace) curr_ob;
                if (!curr_trace.polyline().is_multiple_of_45_degree()) {
                    ++count;
                }
            }
        }
        if (count > 1) {
            System.out.print(p_s);
            System.out.print(count);
            System.out.println(" traces not 45 degree");
        }
    }

    private Validate() {
        // not called
    }
}
