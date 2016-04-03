/*
 * Copyright (C) 2016 Your Organisation
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

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.freerouting.freeroute.board.TestLevel;

/**
 * FXML Controller class
 *
 * @author robert
 */
public class MainAppController implements Initializable {

    @FXML
    private TextField message_field;

    @FXML
    private Button open_board_button;

    private Stage mainStage = null;
    private MainApp application = null;
    private static final Logger logger = Logger.getLogger(MainAppController.class.getName());

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public final void setStage(Stage stage) {
        this.mainStage = stage;
    }

    public final void setApp(MainApp application) {
        this.application = application;
    }

    @FXML
    private void open_board_design_action(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("All Design Files", "*.dsn", "*.bin"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            DesignFile design_file = new DesignFile(selectedFile);
            BoardFrame new_frame
                    = new BoardFrame(design_file,
                            BoardFrame.Option.FROM_START_MENU,
                            application.get_test_level(),
                            application.get_locale(),
                            application.is_test_version_option());
            if (new_frame.read(design_file.get_input_stream(),
                    design_file.is_created_from_text_file(),
                    message_field)) {
                // read_ok
                new_frame.menubar.add_design_dependent_items();
                if (design_file.is_created_from_text_file()) {
                    // Read the file  with the saved rules, if it is existing.
                    String file_name = design_file.get_name();
                    String[] name_parts = file_name.split("\\.");
                    java.util.ResourceBundle resources
                            = java.util.ResourceBundle.getBundle(
                                    "net.freerouting.freeroute.resources.MainApp",
                                    application.get_locale());
                    DesignFile.read_rules_file(name_parts[0], design_file.get_parent(),
                            new_frame.board_panel.board_handling,
                            resources.getString("confirm_import_rules"));
                    new_frame.refresh_windows();
                }
                new_frame.setVisible(true);
            } else {
                // read fail
                new_frame.dispose();
            }
        }
    }

    void create_board_frame() {
        BoardFrame.Option board_option;
        if (application.is_session_file_option()) {
            board_option = BoardFrame.Option.SESSION_FILE;
        } else {
            board_option = BoardFrame.Option.SINGLE_FRAME;
        }
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle(
                        "net.freerouting.freeroute.resources.MainApp",
                        application.get_locale());
        DesignFile design_file = new DesignFile(new File(application.get_design_file_name()));
        if (design_file == null) {
            logger.log(Level.SEVERE, "{0} {1} {2}", new Object[]{
                resources.getString("message_6"),
                application.get_design_file_name(),
                resources.getString("message_7")});
            throw new IllegalArgumentException();
        }
//        String message = resources.getString("loading_design") + " " + application.get_design_file_name();
//        WindowMessage welcome_window = WindowMessage.show(message);
        final BoardFrame new_frame
                = create_board_frame(board_option, design_file);
        design_file.finalize();
//        welcome_window.dispose();
        if (new_frame == null) {
            throw new IllegalArgumentException();
        }
    }

    public BoardFrame create_board_frame(BoardFrame.Option board_option, DesignFile design_file) {
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle(
                        "net.freerouting.freeroute.resources.MainApp",
                        application.get_locale());
        java.io.InputStream input_stream = design_file.get_input_stream();
        if (input_stream == null) {
            if (message_field != null) {
                message_field.setText(resources.getString("message_8") + " " + design_file.get_name());
            }
            return null;
        }

        BoardFrame new_frame = new BoardFrame(design_file, board_option, application.get_test_level(), application.get_locale(), !application.is_test_version_option());
        boolean read_ok= new_frame.read(input_stream, design_file.is_created_from_text_file(), message_field);
        if (!read_ok) {
            return null;
        }

        new_frame.menubar.add_design_dependent_items();
        if (design_file.is_created_from_text_file()) {
            // Read the file  with the saved rules, if it is existing.
            String file_name = design_file.get_name();
            String[] name_parts = file_name.split("\\.");
            DesignFile.read_rules_file(name_parts[0],
                    design_file.get_parent(),
                    new_frame.board_panel.board_handling,
                    resources.getString("confirm_import_rules"));
            new_frame.refresh_windows();
        }
        return new_frame;
    }
}
