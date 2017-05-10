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
 * Shape.java
 *
 * Created on 16. Mai 2004, 11:09
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Describes a shape in a Specctra dsn file.
 *
 * @author alfons
 */
public abstract class Shape implements ShapeReadable, ShapeWritable, ShapeTransformable {

    public final Layer layer;

    protected Shape(Layer p_layer) {
        layer = p_layer;
    }

    /**
     * Returns the smallest axis parallel rectangle containing this shape.
     */
    public abstract Rectangle bounding_box();

}
