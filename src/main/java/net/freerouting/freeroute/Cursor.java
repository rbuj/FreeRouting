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
 * Cursor.java
 *
 * Created on 17. Maerz 2006, 06:52
 *
 */
package net.freerouting.freeroute;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Map;
import static java.util.Map.entry;

/**
 *
 * @author Alfons Wirtz
 */
public abstract class Cursor {

    private static final double MAX_COOR = 1_000;

    private static final Line2D VERTICAL_LINE = new Line2D.Double(0, -MAX_COOR, 0, MAX_COOR);
    private static final Line2D HORIZONTAL_LINE = new Line2D.Double(-MAX_COOR, 0, MAX_COOR, 0);
    private static final Line2D RIGHT_DIAGONAL_LINE = new Line2D.Double(-MAX_COOR, -MAX_COOR, MAX_COOR, MAX_COOR);
    private static final Line2D LEFT_DIAGONAL_LINE = new Line2D.Double(-MAX_COOR, MAX_COOR, MAX_COOR, -MAX_COOR);

    private static final Map<java.awt.RenderingHints.Key, Object> RENDERING_HINTS_MAP = Map.ofEntries(
            entry(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON),
            entry(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED),
            entry(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED),
            entry(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED)
    );

    public static Cursor get_45_degree_cross_hair_cursor() {
        return new FortyfiveDegreeCrossHairCursor();
    }

    protected static void init_graphics(Graphics2D p_graphics) {
        BasicStroke bs = new BasicStroke(0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        p_graphics.setStroke(bs);
        p_graphics.addRenderingHints(RENDERING_HINTS_MAP);
        p_graphics.setColor(Color.WHITE);
        p_graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    double x_coor;
    double y_coor;
    boolean location_initialized = false;

    public abstract void draw(Graphics p_graphics);

    public void set_location(Point2D p_location) {
        this.x_coor = p_location.getX();
        this.y_coor = p_location.getY();
        location_initialized = true;
    }

    private final static class FortyfiveDegreeCrossHairCursor extends Cursor {

        static final GeneralPath DRAW_PATH = new GeneralPath(Path2D.WIND_EVEN_ODD);
        static {
            DRAW_PATH.append(VERTICAL_LINE, false);
            DRAW_PATH.append(HORIZONTAL_LINE, false);
            DRAW_PATH.append(RIGHT_DIAGONAL_LINE, false);
            DRAW_PATH.append(LEFT_DIAGONAL_LINE, false);
        }

        @Override
        public void draw(Graphics p_graphics) {
            if (!location_initialized) {
                return;
            }
            if (p_graphics instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) p_graphics;
                init_graphics(g2);
                g2.translate(this.x_coor, this.y_coor);
                g2.draw(DRAW_PATH);
            }
        }
    }

}
