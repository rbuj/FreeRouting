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
 */
package net.freerouting.freeroute.interactive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.freerouting.freeroute.geometry.planar.FloatPoint;

/**
 * Logfile to track the actions in the interactive board handling for automatic
 * replay.
 *
 * @author Alfons Wirtz
 *
 */
public class Logfile {

    private LogfileScanner scanner = null;
    private Writer file_writer = null;
    private boolean write_enabled = false;
    private Object pending_token = null;

    /**
     * opens the logfile for reading
     */
    public void start_read(Reader p_reader) {
        this.scanner = new LogfileScanner(p_reader);
    }

    /**
     * Reads the next corner from the logfile. Return null, if no valid corner
     * is found.
     */
    public FloatPoint read_corner() {

        double x = 0;
        double y = 0;
        for (int i = 0; i < 2; ++i) {
            Object curr_ob = this.next_token();
            if (!(curr_ob instanceof Double)) {
                this.pending_token = curr_ob;
                return null;
            }
            double f = (double) curr_ob;
            if (i == 0) {
                x = f;
            } else {
                y = f;
            }
        }
        return new FloatPoint(x, y);
    }

    /**
     * closes the logfile after writing
     */
    public void close_output() {
        if (this.file_writer != null) {
            try {
                this.file_writer.close();
            } catch (IOException e) {
                Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, "unable to close logfile", e);
            }
        }
        this.write_enabled = false;
    }

    /**
     * opens a logfile for writing
     */
    public boolean start_write(File p_file) {
        try {
            this.file_writer = new OutputStreamWriter(new FileOutputStream(p_file), StandardCharsets.UTF_8);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, "unable to create logfile", ex);
            return false;
        }
        write_enabled = true;
        return true;
    }

    /**
     * Marks the beginning of a new item in the olutput stream
     */
    public void start_scope(LogfileScope p_logfile_scope) {
        if (write_enabled) {
            try {
                this.file_writer.write(p_logfile_scope.name);
                this.file_writer.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, "Logfile.start_scope: write failed", ex);
            }
        }
    }

    /**
     * Marks the beginning of a new scope in the olutput stream Writes also an
     * integer value.
     */
    public void start_scope(LogfileScope p_logfile_scope, int p_int_value) {
        start_scope(p_logfile_scope);
        add_int(p_int_value);
    }

    /**
     * Marks the beginning of a new scope in the olutput stream Writes also 1,
     * if p_boolean_value is true, or 0, if p_boolean_value is false;
     */
    public void start_scope(LogfileScope p_logfile_scope, boolean p_boolean_value) {
        start_scope(p_logfile_scope);
        int int_value;
        if (p_boolean_value) {
            int_value = 1;
        } else {
            int_value = 0;
        }
        add_int(int_value);
    }

    /**
     * Marks the beginning of a new item in the olutput stream Writes also the
     * start corner.
     */
    public void start_scope(LogfileScope p_logfile_scope, FloatPoint p_start_corner) {
        start_scope(p_logfile_scope);
        add_corner(p_start_corner);
    }

    /**
     * Reads the next scope iidentifier from the logfile. Returns null if no
     * more item scope was found.
     */
    public LogfileScope start_read_scope() {
        Object curr_ob = this.next_token();
        if (curr_ob == null) {
            return null;
        }
        if (!(curr_ob instanceof String)) {
            Logger.getLogger(Logfile.class.getName()).log(Level.INFO, "Logfile.start_read_scope: String expected");
            this.pending_token = curr_ob;
            return null;
        }
        LogfileScope result = LogfileScope.get_scope((String) curr_ob);
        return result;
    }

    /**
     * adds an int to the logfile
     */
    public void add_int(int p_int) {

        if (write_enabled) {
            try {
                this.file_writer.write(Integer.toString(p_int));
                this.file_writer.write("\n");
            } catch (IOException e) {
                Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, "unable to write integer to logfile", e);
            }
        }
    }

    /**
     * Reads the next int from the logfile. Returns -1, if no valid integer was
     * found.
     */
    public int read_int() {
        Object curr_ob = this.next_token();
        if (!(curr_ob instanceof Integer)) {
            Logger.getLogger(Logfile.class.getName()).log(Level.INFO, "Logfile.read_int: Integer expected");
            this.pending_token = curr_ob;
            return -1;
        }
        return ((int) curr_ob);
    }

    /**
     * adds a FloatPoint to the logfile
     */
    public void add_corner(FloatPoint p_corner) {
        if (write_enabled) {
            if (p_corner == null) {
                Logger.getLogger(Logfile.class.getName()).log(Level.INFO, "logfile.add_corner: p_corner is null");
                return;
            }
            try {
                this.file_writer.write(Double.toString(p_corner.x));
                this.file_writer.write(" ");
                this.file_writer.write(Double.toString(p_corner.y));
                this.file_writer.write("\n");
            } catch (IOException e) {
                Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, "unable to write to logfile while adding corner", e);
            }
        }
    }

    private Object next_token() {
        if (this.pending_token != null) {
            Object result = this.pending_token;
            this.pending_token = null;
            return result;
        }
        try {
            Object result = this.scanner.next_token();
            return result;
        } catch (IOException e) {
            Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, "Logfile.next_token: IO error scanning file", e);
            return null;
        }
    }

}
