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
 * Layer.java
 *
 * Created on 26. Mai 2004, 06:31
 */
package net.freerouting.freeroute.board;

/**
 * Describes the structure of a board layer.
 *
 * @author alfons
 */
@SuppressWarnings("serial")
public abstract class Layer implements java.io.Serializable {

    /**
     * The name of the layer.
     */
    private final String name;

    /**
     * Creates a new instance of Layer
     */
    Layer(String name) {
        this.name = name;
    }

    public String get_name(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract String get_type();
}
