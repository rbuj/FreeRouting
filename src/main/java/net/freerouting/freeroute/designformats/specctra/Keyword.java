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
 * Keyword.java
 *
 * Created on 8. Mai 2004, 10:23
 */
package net.freerouting.freeroute.designformats.specctra;

/**
 * Enumeration class for keywords of the specctra dsn file format
 *
 * @author alfons
 */
public class Keyword {

    /**
     * The only instances of the internal classes:
     *
     * ScopeKeywords with an inividual read_scope method are defined in an extra
     * class,
     */
    static final Keyword ABSOLUTE = new Keyword("absolute");
    static final Keyword ACTIVE = new Keyword("active");
    static final Keyword AGAINST_PREFERRED_DIRECTION_TRACE_COSTS = new Keyword("against_preferred_direction_trace_costs");
    static final Keyword ATTACH = new Keyword("attach");
    static final Keyword AUTOROUTE = new Keyword("autoroute");
    static final Keyword AUTOROUTE_SETTINGS = new Keyword("autoroute_settings");
    static final Keyword BACK = new Keyword("back");
    static final Keyword BOUNDARY = new Keyword("boundary");
    static final Keyword CIRCUIT = new Keyword("circuit");
    static final Keyword CIRCLE = new Keyword("circle");
    static final Keyword CLASS = new Keyword("class");
    static final Keyword CLASS_CLASS = new Keyword("class_class");
    static final Keyword CLASSES = new Keyword("classes");
    static final ScopeKeyword COMPONENT_SCOPE = new Component();
    static final Keyword CONSTANT = new Keyword("constant");
    static final Keyword CONTROL = new Keyword("control");
    static final Keyword CLEARANCE = new Keyword("clearance");
    static final Keyword CLEARANCE_CLASS = new Keyword("clearance_class");
    static final Keyword CLOSED_BRACKET = new Keyword(")");
    static final Keyword FANOUT = new Keyword("fanout");
    static final Keyword FLIP_STYLE = new Keyword("flip_style");
    static final Keyword FIX = new Keyword("fix");
    static final Keyword FORTYFIVE_DEGREE = new Keyword("fortyfive_degree");
    static final Keyword FROMTO = new Keyword("fromto");
    static final Keyword FRONT = new Keyword("front");
    static final Keyword GENERATED_BY_FREEROUTE = new Keyword("generated_by_freeroute");
    static final Keyword HORIZONTAL = new Keyword("horizontal");
    static final Keyword HOST_CAD = new Keyword("host_cad");
    static final Keyword HOST_VERSION = new Keyword("host_version");
    static final Keyword IMAGE = new Keyword("image");
    static final Keyword KEEPOUT = new Keyword("keepout");
    static final Keyword LAYER = new Keyword("layer");
    static final Keyword LAYER_RULE = new Keyword("layer_rule");
    static final Keyword LENGTH = new Keyword("length");
    static final ScopeKeyword LIBRARY_SCOPE = new Library();
    static final Keyword LOCK_TYPE = new Keyword("lock_type");
    static final Keyword LOGICAL_PART = new Keyword("logical_part");
    static final Keyword LOGICAL_PART_MAPPING = new Keyword("logical_part_mapping");
    static final Keyword NET = new Keyword("net");
    static final Keyword NETWORK_OUT = new Keyword("network_out");
    static final ScopeKeyword NETWORK_SCOPE = new Network();
    static final Keyword NINETY_DEGREE = new Keyword("ninety_degree");
    static final Keyword NONE = new Keyword("none");
    static final Keyword NORMAL = new Keyword("normal");
    static final Keyword OFF = new Keyword("off");
    static final Keyword ON = new Keyword("on");
    static final Keyword OPEN_BRACKET = new Keyword("(");
    static final Keyword ORDER = new Keyword("order");
    static final Keyword OUTLINE = new Keyword("outline");
    static final Keyword PADSTACK = new Keyword("padstack");
    static final ScopeKeyword PART_LIBRARY_SCOPE = new PartLibrary();
    static final ScopeKeyword PARSER_SCOPE = new Parser();
    static final ScopeKeyword PCB_SCOPE = new ScopeKeyword("pcb");
    static final Keyword PIN = new Keyword("pin");
    static final Keyword PINS = new Keyword("pins");
    static final Keyword PLACE = new Keyword("place");
    static final ScopeKeyword PLACE_CONTROL = new PlaceControl();
    static final Keyword PLACE_KEEPOUT = new Keyword("place_keepout");
    static final ScopeKeyword PLACEMENT_SCOPE = new Placement();
    static final ScopeKeyword PLANE_SCOPE = new Plane();
    static final Keyword PLANE_VIA_COSTS = new Keyword("plane_via_costs");
    static final Keyword PREFERRED_DIRECTION = new Keyword("preferred_direction");
    static final Keyword PREFERRED_DIRECTION_TRACE_COSTS = new Keyword("preferred_direction_trace_costs");
    static final Keyword SNAP_ANGLE = new Keyword("snap_angle");
    static final Keyword POLYGON = new Keyword("polygon");
    static final Keyword POLYGON_PATH = new Keyword("polygon_path");
    static final Keyword POLYLINE_PATH = new Keyword("polyline_path");
    static final Keyword POSITION = new Keyword("position");
    static final Keyword POSTROUTE = new Keyword("postroute");
    static final Keyword POWER = new Keyword("power");
    static final Keyword PULL_TIGHT = new Keyword("pull_tight");
    static final Keyword RECTANGLE = new Keyword("rectangle");
    static final Keyword RESOLUTION_SCOPE = new Resolution();
    static final Keyword ROTATE = new Keyword("rotate");
    static final Keyword ROTATE_FIRST = new Keyword("rotate_first");
    static final Keyword ROUTES = new Keyword("routes");
    static final Keyword RULE = new Keyword("rule");
    static final Keyword RULES = new Keyword("rules");
    static final Keyword SESSION = new Keyword("session");
    static final Keyword SHAPE = new Keyword("shape");
    static final Keyword SHOVE_FIXED = new Keyword("shove_fixed");
    static final Keyword SIDE = new Keyword("side");
    static final Keyword SIGNAL = new Keyword("signal");
    static final Keyword SPARE = new Keyword("spare");
    static final Keyword START_PASS_NO = new Keyword("start_pass_no");
    static final Keyword START_RIPUP_COSTS = new Keyword("start_ripup_costs");
    static final Keyword STRING_QUOTE = new Keyword("string_quote");
    static final ScopeKeyword STRUCTURE_SCOPE = new Structure();
    static final Keyword TYPE = new Keyword("type");
    static final Keyword USE_LAYER = new Keyword("use_layer");
    static final Keyword USE_NET = new Keyword("use_net");
    static final Keyword USE_VIA = new Keyword("use_via");
    static final Keyword VERTICAL = new Keyword("vertical");
    static final Keyword VIA = new Keyword("via");
    static final Keyword VIAS = new Keyword("vias");
    static final Keyword VIA_AT_SMD = new Keyword("via_at_smd");
    static final Keyword VIA_COSTS = new Keyword("via_costs");
    static final Keyword VIA_KEEPOUT = new Keyword("via_keepout");
    static final Keyword VIA_RULE = new Keyword("via_rule");
    static final Keyword WIDTH = new Keyword("width");
    static final Keyword WINDOW = new Keyword("window");
    static final Keyword WIRE = new Keyword("wire");
    static final ScopeKeyword WIRING_SCOPE = new Wiring();
    static final Keyword WRITE_RESOLUTION = new Keyword("write_resolution");

    private final String name;

    /**
     * prevents creating more instances
     */
    protected Keyword(String p_name) {
        name = p_name;
    }

}
