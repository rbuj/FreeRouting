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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.freerouting.freeroute.board.TestLevel;

/**
 *
 * Main application for creating frames with new or existing board designs.
 *
 * @author Alfons Wirtz
 */
public class MainApp extends Application {

    private static final TestLevel DEBUG_LEVEL = TestLevel.CRITICAL_DEBUGGING_OUTPUT;
    /**
     * Change this string when creating a new version
     */
    private static final String VERSION_NUMBER_STRING = "1.2.43";
    private static DesignFile design_file = null;
    private static boolean single_design_option = false;
    private static boolean test_version_option = false;
    private static boolean session_file_option = false;
    private static String design_file_name = null;
    private static String design_dir_name = null;
    private static boolean is_test_version;
    private static Locale locale = null;
    private static TestLevel test_level;
    private static final Logger logger = Logger.getLogger(MainAppController.class.getName());
    private Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.MainApp", locale);
        stage.setTitle(resources.getString("title"));
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/net/freerouting/freeroute/fxml/MainApp.fxml"));
        fxmlLoader.setResources(resources);
        Parent root = fxmlLoader.load();
        MainAppController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setApp(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        if (single_design_option) {
            controller.create_board_frame();
        }
        stage.show();
        this.mainStage.setOnCloseRequest((WindowEvent we) -> {
            Alert closeConfirmation = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to exit?"
            );
            Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                    ButtonType.OK
            );
            exitButton.setText("Exit");
            closeConfirmation.setHeaderText("Confirm Exit");
            closeConfirmation.initModality(Modality.APPLICATION_MODAL);
            closeConfirmation.initOwner(mainStage);

            closeConfirmation.setX(mainStage.getX());
            closeConfirmation.setY(mainStage.getY());

            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get())) {
                we.consume();
            } else {
                Runtime.getRuntime().exit(0);
            }
        });
    }

    /**
     * Main function of the Application
     */
    public static void main(String[] args) {
        /**
         * Parse arguments
         */
        try {
            for (int i = 0; i < args.length; ++i) {
                if (args[i].startsWith("-de")) // the design file is provided
                {
                    if (!args[i + 1].startsWith("-")) {
                        single_design_option = true;
                        design_file_name = args[i + 1];
                        ++i;
                    } else {
                        throw new IllegalArgumentException("Argument: " + args[i] + " [not recognized]");
                    }
                } else if (args[i].startsWith("-di")) // the design directory is provided
                {
                    if (!args[i + 1].startsWith("-")) {
                        design_dir_name = args[i + 1];
                        ++i;
                    } else {
                        throw new IllegalArgumentException("Argument: " + args[i] + " [not recognized]");
                    }
                } else if (args[i].startsWith("-l")) // the locale is provided
                {
                    List<String> locale_list;
                    try (InputStream in = MainApp.class.getClass().getResourceAsStream("/LOCALES"); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                        locale_list = new ArrayList<>();
                        String line = reader.readLine();
                        while (line != null) {
                            locale_list.add(line);
                            line = reader.readLine();
                        }
                    }
                    String new_locale = args[i + 1].substring(0, 2);
                    if (locale_list.contains(new_locale)) {
                        locale = new Locale(new_locale, "");
                        ++i;
                    }
                } else if (args[i].startsWith("-s")) {
                    session_file_option = true;
                } else if (args[i].startsWith("-test")) {
                    test_version_option = true;
                } else {
                    throw new IllegalArgumentException("Argument: " + args[i] + " [not recognized]");
                }
            }
            if (locale == null) {
                List<String> locale_list;
                try (InputStream in = MainApp.class.getClass().getResourceAsStream("/LOCALES"); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    locale_list = new ArrayList<>();
                    String line = reader.readLine();
                    while (line != null) {
                        locale_list.add(line);
                        line = reader.readLine();
                    }
                }
                String new_locale = java.util.Locale.getDefault().getLanguage();
                if (locale_list.contains(new_locale)) {
                    locale = new Locale(new_locale, "");
                } else {
                    locale = new Locale("en", "");
                }
            }
        } catch (Exception exc) {
            logger.log(Level.SEVERE, exc.toString(), exc);
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
    }

    public boolean is_single_design_option() {
        return single_design_option;
    }

    public boolean is_session_file_option() {
        return session_file_option;
    }

    public boolean is_test_version_option() {
        return test_version_option;
    }

    public String get_design_file_name() {
        return design_file_name;
    }

    public Locale get_locale() {
        return locale;
    }

    public String get_design_dir_name() {
        return design_dir_name;
    }

    public TestLevel get_test_level() {
        return test_level;
    }
}
