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

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.freerouting.freeroute.board.TestLevel;

/**
 *
 * Main application for creating frames with new or existing board designs.
 *
 * @author Alfons Wirtz
 */
public final class MainApp extends Application {

    /**
     * Change this string when creating a new version
     */
    private static final String VERSION_NUMBER_STRING = "1.4.6-alpha";
    private static final Logger LOGGER = Logger.getLogger(MainAppController.class.getName());
    private static final TestLevel DEBUG_LEVEL = TestLevel.CRITICAL_DEBUGGING_OUTPUT;

    public static String get_version() {
        return VERSION_NUMBER_STRING;
    }

    private boolean single_design_option = false;
    private boolean session_file_option = false;
    private String design_file_name = "";
    private String design_dir_name = "";
    private Stage mainStage;
    private TestLevel test_level;

    @Override
    public void init() throws Exception {
        List<String> locale_list = Arrays.asList("ca", "de", "es", "en");
        /**
         * Parse commandline arguments
         */
        try {
            boolean test_version_option = false;
            Locale locale = null;
            Parameters parameters = getParameters();
            List<String> list = parameters.getRaw();
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String string = iterator.next();
                switch (string) {
                    // the design file is provided
                    case "-de":
                        if (iterator.hasNext()) {
                            single_design_option = true;
                            design_file_name = iterator.next();
                        } else {
                            throw new IllegalArgumentException("Argument: " + string + " [missing design_file]");
                        }
                        break;
                    // the design directory is provided
                    case "-di":
                        if (iterator.hasNext()) {
                            design_dir_name = iterator.next();
                        } else {
                            throw new IllegalArgumentException("Argument: " + string + " [missing design_dir]");
                        }
                        break;
                    // the locale is provided
                    case "-l":
                        if (iterator.hasNext()) {
                            String new_locale = iterator.next();
                            if (locale_list.contains(new_locale)) {
                                locale = new Locale(new_locale, "");
                            } else {
                                LOGGER.log(Level.INFO, "locale: {0} not found [using default]", new_locale);
                                locale = new Locale("en", "");
                            }
                        } else {
                            throw new IllegalArgumentException("Argument: " + string + " [missing locale]");
                        }
                        break;
                    case "-s":
                        session_file_option = true;
                        break;
                    case "-test":
                        test_version_option = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Argument: " + string + " [not recognized]");
                }
            }

            if (locale == null) {
                String new_locale = java.util.Locale.getDefault().getLanguage();
                if (locale_list.contains(new_locale)) {
                    locale = new Locale(new_locale, "");
                } else {
                    locale = new Locale("en", "");
                }
            }
            Locale.setDefault(locale);

            /**
             * Set data fields
             */
            if (test_version_option) {
                test_level = DEBUG_LEVEL;
            } else {
                test_level = TestLevel.RELEASE_VERSION;
            }
        } catch (IllegalArgumentException exc) {
            LOGGER.log(Level.SEVERE, "{0}{1}Try -h, for available options.", new Object[]{exc.toString(), System.getProperty("line.separator")});
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.MainApp", Locale.getDefault());
        mainStage.setTitle(resources.getString("title"));
        mainStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/net/freerouting/freeroute/fxml/MainApp.fxml"));
        fxmlLoader.setResources(resources);
        Parent root = fxmlLoader.load();
        MainAppController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.centerOnScreen();

        LaunchMode launch_mode;
        if (single_design_option) {
            if (session_file_option) {
                launch_mode = LaunchMode.SESSION_FILE;
            } else {
                launch_mode = LaunchMode.SINGLE_FRAME;
            }
        } else {
            launch_mode = LaunchMode.FROM_START_MENU;
        }

        controller.init_variables(
                design_dir_name,
                design_file_name.isEmpty() ? null : new File(design_file_name),
                test_level,
                launch_mode,
                mainStage);

        if (single_design_option) {
            controller.create_board_frame();
        }

        mainStage.show();
        mainStage.setOnCloseRequest((WindowEvent we) -> {
            Runtime.getRuntime().exit(0);
        });
    }
}
