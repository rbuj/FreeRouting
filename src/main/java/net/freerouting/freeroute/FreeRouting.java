/*
 * Copyright (C) 2017 Robert Antoni Buj Gelonch {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.freerouting.freeroute;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Robert Buj
 */
public class FreeRouting {

    public static void main(String[] args) {
        // -h: shows usage
        if (Arrays.stream(args).anyMatch("-h"::equals)) {
            show_usage();
            System.exit(0);
        }
        // Enable OpenGL pipeline
        System.setProperty("sun.java2d.opengl", "true");
        // System look & feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.INFO, null, ex);
        }
        // launch MainApp
        Application.launch(MainApp.class, args);
    }

    private static void show_usage() {
        System.out.println("Available options:");
        System.out.println("\t-de DSN_FILE   opens the specified design file");
        System.out.println("\t-di DSN_DIR    specifies the design directory");
        System.out.println("\t-l LOCALE      specifies the LOCALE");
        System.out.println("\t-s             enable session mode");
        System.out.println("\t-test          enable test mode");
        System.out.println("\t-h             shows this help");
        System.out.println("Support: https://github.com/rbuj/FreeRouting/issues");
    }
}
