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
 * MainApp.java
 *
 * Created on 19. Oktober 2002, 17:58
 *
 */
package net.freerouting.freeroute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.freerouting.freeroute.BoardFrame.Option;
import net.freerouting.freeroute.board.TestLevel;

/**
 *
 * Main application for creating frames with new or existing board designs.
 *
 * @author Alfons Wirtz
 */
public class MainApp extends Application {

    /**
     * Change this string when creating a new version
     */
    protected static final String VERSION_NUMBER_STRING = "1.4-alpha";
    private static final Logger LOGGER = Logger.getLogger(MainAppController.class.getName());
    private static final TestLevel DEBUG_LEVEL = TestLevel.CRITICAL_DEBUGGING_OUTPUT;
    private static boolean single_design_option = false;
    private static boolean session_file_option = false;
    private static String design_file_name = "";
    private static String design_dir_name = "";
    private static Locale locale = null;
    private static Stage mainStage;
    private static TestLevel test_level;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.MainApp", locale);
        mainStage.setTitle(resources.getString("title"));
        mainStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/net/freerouting/freeroute/fxml/MainApp.fxml"));
        fxmlLoader.setResources(resources);
        Parent root = fxmlLoader.load();
        MainAppController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.centerOnScreen();

        Option board_option;
        if (single_design_option) {
            if (session_file_option) {
                board_option = BoardFrame.Option.SESSION_FILE;
            } else {
                board_option = BoardFrame.Option.SINGLE_FRAME;
            }
        } else {
            board_option = BoardFrame.Option.FROM_START_MENU;
        }

        controller.init_variables(
                new DesignFile(new File(design_file_name), design_dir_name),
                test_level,
                board_option,
                mainStage);

        if (single_design_option) {
            controller.create_board_frame();
        }

        mainStage.show();
        mainStage.setOnCloseRequest((WindowEvent we) -> {
            Runtime.getRuntime().exit(0);
        });
    }

    /**
     * Main function of the Application
     */
    public static void main(String[] args) {
        boolean test_version_option = false;
        /**
         * Parse arguments
         */
        try {
            for (int i = 0; i < args.length; ++i) {
                if (args[i].equals("-de")) // the design file is provided
                {
                    single_design_option = true;
                    design_file_name = args[i + 1];
                    ++i;
                } else if (args[i].equals("-di")) // the design directory is provided
                {
                    design_dir_name = args[i + 1];
                    ++i;
                } else if (args[i].equals("-l")) // the locale is provided
                {
                    String new_locale = args[i + 1].substring(0, 2);
                    try (InputStream in = MainApp.class.getClass().getResourceAsStream("/LOCALES"); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                        String line = reader.readLine();
                        while (line != null) {
                            if (line.equals(new_locale)) {
                                locale = new Locale(new_locale, "");
                                break;
                            }
                            line = reader.readLine();
                        }
                    }
                    if (locale == null) {
                        locale = new Locale("en", "");
                    }
                    ++i;
                } else if (args[i].equals("-s")) {
                    session_file_option = true;
                } else if (args[i].equals("-test")) {
                    test_version_option = true;
                } else {
                    throw new IllegalArgumentException("Argument: " + args[i] + " [not recognized]");
                }
            }
            if (locale == null) {
                String new_locale = java.util.Locale.getDefault().getLanguage();
                try (InputStream in = MainApp.class.getClass().getResourceAsStream("/LOCALES"); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    String line = reader.readLine();
                    while (line != null) {
                        if (line.equals(new_locale)) {
                            locale = new Locale(new_locale, "");
                            break;
                        }
                        line = reader.readLine();
                    }
                }
                if (locale == null) {
                    locale = new Locale("en", "");
                }
            }
            /**
             * Set data fiels
             */
            if (test_version_option) {
                test_level = DEBUG_LEVEL;
            } else {
                test_level = TestLevel.RELEASE_VERSION;
            }
            /**
             * call start
             */
            launch(args);
        } catch (IOException | IllegalArgumentException exc) {
            LOGGER.log(Level.SEVERE, exc.toString(), exc);
        }
    }
}
