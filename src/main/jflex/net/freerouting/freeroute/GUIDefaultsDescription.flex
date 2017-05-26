package net.freerouting.freeroute;
@SuppressWarnings("all")
%%

%class GUIDefaultsScanner
%unicode
%ignorecase 
%function next_token
%type Object
/* %debug */

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]


/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "#" {InputCharacter}* {LineTerminator}

Letter=[A-Za-z]
Digit=[0-9]

DecIntegerLiteral =  ([+-]? (0 | [1-9][0-9]*))

Mantissa = ([+-]? [0-9]+ ("." [0-9]+)?)

Exponent = ([Ee] {DecIntegerLiteral})

DecFloatLiteral = {Mantissa} {Exponent}?

SpecChar = _


Identifier = ({Letter}|{SpecChar})({Letter}|{Digit}|{SpecChar})* 

%%

/* keywords */
<YYINITIAL> {
   "all_visible"               { return GUIDefaultsFileKeyword.ALL_VISIBLE; }
   "assign_net_rules"          { return GUIDefaultsFileKeyword.ASSIGN_NET_RULES; }
   "automatic_layer_dimming"   { return GUIDefaultsFileKeyword.AUTOMATIC_LAYER_DIMMING; }
   "background"                { return GUIDefaultsFileKeyword.BACKGROUND; }
   "board_frame"               { return GUIDefaultsFileKeyword.BOARD_FRAME; }
   "bounds"                    { return GUIDefaultsFileKeyword.BOUNDS; }
   "clearance_compensation"    { return GUIDefaultsFileKeyword.CLEARANCE_COMPENSATION; }
   "clearance_matrix"          { return GUIDefaultsFileKeyword.CLEARANCE_MATRIX; }
   "colors"                    { return GUIDefaultsFileKeyword.COLORS; }
   "color_manager"             { return GUIDefaultsFileKeyword.COLOR_MANAGER; }
   "component_back"            { return GUIDefaultsFileKeyword.COMPONENT_BACK; }
   "component_front"           { return GUIDefaultsFileKeyword.COMPONENT_FRONT; }
   "component_grid"            { return GUIDefaultsFileKeyword.COMPONENT_GRID; }
   "component_info"            { return GUIDefaultsFileKeyword.COMPONENT_INFO; }
   "conduction"                { return GUIDefaultsFileKeyword.CONDUCTION; }
   "current_layer"             { return GUIDefaultsFileKeyword.CURRENT_LAYER; }
   "current_only"              { return GUIDefaultsFileKeyword.CURRENT_ONLY; }
   "deselected_snapshot_attributes" { return GUIDefaultsFileKeyword.DESELECTED_SNAPSHOT_ATTRIBUTES; }
   "display_miscellanious"     { return GUIDefaultsFileKeyword.DISPLAY_MISCELLANIOUS; }
   "display_region"            { return GUIDefaultsFileKeyword.DISPLAY_REGION; }
   "drag_components_enabled"   { return GUIDefaultsFileKeyword.DRAG_COMPONENTS_ENABLED; }
   "dynamic"                   { return GUIDefaultsFileKeyword.DYNAMIC; }
   "edit_net_rules"            { return GUIDefaultsFileKeyword.EDIT_NET_RULES; }
   "edit_vias"                 { return GUIDefaultsFileKeyword.EDIT_VIAS; }
   "fixed"                     { return GUIDefaultsFileKeyword.FIXED; }
   "fixed_traces"              { return GUIDefaultsFileKeyword.FIXED_TRACES; }
   "fixed_vias"                { return GUIDefaultsFileKeyword.FIXED_VIAS; }
   "fortyfive_degree"          { return GUIDefaultsFileKeyword.FORTYFIVE_DEGREE; }
   "gui_defaults"              { return GUIDefaultsFileKeyword.GUI_DEFAULTS; }
   "hilight"                   { return GUIDefaultsFileKeyword.HILIGHT; }
   "hilight_routing_obstacle"  { return GUIDefaultsFileKeyword.HILIGHT_ROUTING_OBSTACLE; }
   "ignore_conduction_areas"   { return GUIDefaultsFileKeyword.IGNORE_CONDUCTION_AREAS; }
   "incompletes"               { return GUIDefaultsFileKeyword.INCOMPLETES; }
   "incompletes_info"          { return GUIDefaultsFileKeyword.INCOMPLETES_INFO; }
   "interactive_state"         { return GUIDefaultsFileKeyword.INTERACTIVE_STATE; }
   "keepout"                   { return GUIDefaultsFileKeyword.KEEPOUT; }
   "layer_visibility"          { return GUIDefaultsFileKeyword.LAYER_VISIBILITY; }
   "length_matching"           { return GUIDefaultsFileKeyword.LENGTH_MATCHING; }
   "manual_rules"              { return GUIDefaultsFileKeyword.MANUAL_RULES; }
   "manual_rule_settings"      { return GUIDefaultsFileKeyword.MANUAL_RULE_SETTINGS; }
   "move_parameter"            { return GUIDefaultsFileKeyword.MOVE_PARAMETER; }
   "net_info"                  { return GUIDefaultsFileKeyword.NET_INFO; }
   "ninety_degree"             { return GUIDefaultsFileKeyword.NINETY_DEGREE; }
   "none"                      { return GUIDefaultsFileKeyword.NONE; }
   "not_visible"               { return GUIDefaultsFileKeyword.NOT_VISIBLE; }
   "object_colors"             { return GUIDefaultsFileKeyword.OBJECT_COLORS; }
   "object_visibility"         { return GUIDefaultsFileKeyword.OBJECT_VISIBILITY; }
   "off"                       { return GUIDefaultsFileKeyword.OFF; }
   "on"                        { return GUIDefaultsFileKeyword.ON; }
   "outline"                   { return GUIDefaultsFileKeyword.OUTLINE; }
   "package_info"              { return GUIDefaultsFileKeyword.PACKAGE_INFO; }
   "padstack_info"             { return GUIDefaultsFileKeyword.PADSTACK_INFO; }
   "parameter"                 { return GUIDefaultsFileKeyword.PARAMETER; }
   "pins"                      { return GUIDefaultsFileKeyword.PINS; }
   "pull_tight_accuracy"       { return GUIDefaultsFileKeyword.PULL_TIGHT_ACCURACY; }
   "pull_tight_region"         { return GUIDefaultsFileKeyword.PULL_TIGHT_REGION; }
   "push_and_shove_enabled"    { return GUIDefaultsFileKeyword.PUSH_AND_SHOVE_ENABLED; }
   "route_details"             { return GUIDefaultsFileKeyword.ROUTE_DETAILS; }
   "route_mode"                { return GUIDefaultsFileKeyword.ROUTE_MODE; }
   "route_parameter"           { return GUIDefaultsFileKeyword.ROUTE_PARAMETER; }
   "rule_selection"            { return GUIDefaultsFileKeyword.RULE_SELECTION; }
   "select_parameter"          { return GUIDefaultsFileKeyword.SELECT_PARAMETER; }
   "selectable_items"          { return GUIDefaultsFileKeyword.SELECTABLE_ITEMS; }
   "selection_layers"          { return GUIDefaultsFileKeyword.SELECTION_LAYERS; }
   "snapshots"                 { return GUIDefaultsFileKeyword.SNAPSHOTS; }
   "shove_enabled"             { return GUIDefaultsFileKeyword.SHOVE_ENABLED; }
   "stitching"                 { return GUIDefaultsFileKeyword.STITCHING; }
   "traces"                    { return GUIDefaultsFileKeyword.TRACES; }
   "unfixed"                   { return GUIDefaultsFileKeyword.UNFIXED; }
   "via_keepout"               { return GUIDefaultsFileKeyword.VIA_KEEPOUT; }
   "vias"                      { return GUIDefaultsFileKeyword.VIAS; }
   "via_rules"                 { return GUIDefaultsFileKeyword.VIA_RULES; }
   "via_snap_to_smd_center"    { return GUIDefaultsFileKeyword.VIA_SNAP_TO_SMD_CENTER; }
   "violations"                { return GUIDefaultsFileKeyword.VIOLATIONS; }
   "violations_info"           { return GUIDefaultsFileKeyword.VIOLATIONS_INFO; }
   "visible"                   { return GUIDefaultsFileKeyword.VISIBLE; }
   "windows"                   { return GUIDefaultsFileKeyword.WINDOWS; }
   "("                         { return GUIDefaultsFileKeyword.OPEN_BRACKET; }
   ")"                         { return GUIDefaultsFileKeyword.CLOSED_BRACKET; }
}

<YYINITIAL> {
  /* identifiers */ 
  {Identifier}                   { return yytext(); }
 
  /* literals */
  {DecIntegerLiteral}            { return Integer.valueOf(yytext()); }
  {DecFloatLiteral}              { return Double.valueOf(yytext()); }

  /* comments */
  {Comment}                      { /* ignore */ }
 
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

/* error fallback */
[^]                             { throw new Error("Illegal character <"+
                                                    yytext()+">"); }
