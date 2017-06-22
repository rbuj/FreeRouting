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
enum Keyword {

    /**
     * The only instances of the internal classes:
     *
     * ScopeKeywords with an inividual read_scope method are defined in an extra
     * class,
     */
    ABSOLUTE("absolute"),
    ACTIVE("active"),
    AGAINST_PREFERRED_DIRECTION_TRACE_COSTS("against_preferred_direction_trace_costs"),
    ATTACH("attach"),
    AUTOROUTE("autoroute"),
    AUTOROUTE_SETTINGS("autoroute_settings"),
    BACK("back"),
    BOUNDARY("boundary"),
    CIRCUIT("circuit"),
    CIRCLE("circle"),
    CLASS("class"),
    CLASS_CLASS("class_class"),
    CLASSES("classes"),
    CONSTANT("constant"),
    CONTROL("control"),
    CLEARANCE("clearance"),
    CLEARANCE_CLASS("clearance_class"),
    CLOSED_BRACKET(")"),
    FANOUT("fanout"),
    FLIP_STYLE("flip_style"),
    FIX("fix"),
    FORTYFIVE_DEGREE("fortyfive_degree"),
    FROMTO("fromto"),
    FRONT("front"),
    GENERATED_BY_FREEROUTE("generated_by_freeroute"),
    HORIZONTAL("horizontal"),
    HOST_CAD("host_cad"),
    HOST_VERSION("host_version"),
    IMAGE("image"),
    KEEPOUT("keepout"),
    LAYER("layer"),
    LAYER_RULE("layer_rule"),
    LENGTH("length"),
    LOCK_TYPE("lock_type"),
    LOGICAL_PART("logical_part"),
    LOGICAL_PART_MAPPING("logical_part_mapping"),
    NET("net"),
    NETWORK_OUT("network_out"),
    NINETY_DEGREE("ninety_degree"),
    NONE("none"),
    NORMAL("normal"),
    OFF("off"),
    ON("on"),
    OPEN_BRACKET("("),
    ORDER("order"),
    OUTLINE("outline"),
    PADSTACK("padstack"),
    PIN("pin"),
    PINS("pins"),
    PLACE("place"),
    PLACE_KEEPOUT("place_keepout"),
    PLANE_VIA_COSTS("plane_via_costs"),
    PREFERRED_DIRECTION("preferred_direction"),
    PREFERRED_DIRECTION_TRACE_COSTS("preferred_direction_trace_costs"),
    SNAP_ANGLE("snap_angle"),
    POLYGON("polygon"),
    POLYGON_PATH("polygon_path"),
    POLYLINE_PATH("polyline_path"),
    POSITION("position"),
    POSTROUTE("postroute"),
    POWER("power"),
    PULL_TIGHT("pull_tight"),
    RECTANGLE("rectangle"),
    ROTATE("rotate"),
    ROTATE_FIRST("rotate_first"),
    ROUTES("routes"),
    RULE("rule"),
    RULES("rules"),
    SESSION("session"),
    SHAPE("shape"),
    SHOVE_FIXED("shove_fixed"),
    SIDE("side"),
    SIGNAL("signal"),
    SPARE("spare"),
    START_PASS_NO("start_pass_no"),
    START_RIPUP_COSTS("start_ripup_costs"),
    STRING_QUOTE("string_quote"),
    TYPE("type"),
    USE_LAYER("use_layer"),
    USE_NET("use_net"),
    USE_VIA("use_via"),
    VERTICAL("vertical"),
    VIA("via"),
    VIAS("vias"),
    VIA_AT_SMD("via_at_smd"),
    VIA_COSTS("via_costs"),
    VIA_KEEPOUT("via_keepout"),
    VIA_RULE("via_rule"),
    WIDTH("width"),
    WINDOW("window"),
    WIRE("wire"),
    WRITE_RESOLUTION("write_resolution");

    private final String name;

    /**
     * prevents creating more instances
     */
    private Keyword(String p_name) {
        name = p_name;
    }
}
