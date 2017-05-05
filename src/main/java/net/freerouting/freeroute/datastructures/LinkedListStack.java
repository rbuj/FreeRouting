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
 * LinkedListStack.java
 *
 * Created on 11. Maerz 2006, 06:52
 *
 */
package net.freerouting.freeroute.datastructures;

import java.util.LinkedList;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")

/**
 * Implementation of a LIFO stack.
 *
 * @author Alfons Wirtz
 */
public class LinkedListStack<T> {

    private LinkedList<T> list = new LinkedList<>();

    /**
     * Creates a new instance of ArrayStack.
     */
    public LinkedListStack() {
    }

    /**
     * Sets the stack to empty.
     */
    public void reset() {
        list.clear();
    }

    /**
     * Pushed p_element onto the stack.
     */
    public void push(T element) {
        list.addLast(element);
    }

    /**
     * Pops the next element from the top of the stack. Returns null, if the
     * stack is exhausted.
     */
    public T pop() {
        T result = null;
        try {
            result = list.removeLast();
        } catch (NoSuchElementException e) {
        }
        return result;
    }
}
