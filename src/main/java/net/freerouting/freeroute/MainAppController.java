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
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    protected static SimpleStringProperty sp_message_field;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.sp_message_field = new SimpleStringProperty();
        message_field.textProperty().bind(this.sp_message_field);
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
        create_board_frame(BoardFrame.Option.FROM_START_MENU, selectedFile);
    }

    void create_board_frame() {
        BoardFrame.Option board_option;
        if (application.is_session_file_option()) {
            board_option = BoardFrame.Option.SESSION_FILE;
        } else {
            board_option = BoardFrame.Option.SINGLE_FRAME;
        }
        create_board_frame(board_option, new File(application.get_design_file_name()));
    }

    private void create_board_frame(BoardFrame.Option board_option, File selectedFile) {
        if (selectedFile != null) {
            java.util.ResourceBundle resources
                    = java.util.ResourceBundle.getBundle(
                            "net.freerouting.freeroute.resources.MainApp",
                            application.get_locale());
            /**
             * Show loading dialog
             */
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.initOwner(mainStage);
            dialog.setTitle("Loading");
            dialog.setHeaderText("Loading...");
            dialog.setContentText(resources.getString("loading_design") + " " + application.get_design_file_name());
            dialog.setResizable(false);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.show();
            /**
             * create board
             */
            DesignFile design_file = new DesignFile(selectedFile);
            BoardFrame new_frame
                    = new BoardFrame(design_file,
                            board_option,
                            application.get_test_level(),
                            application.get_locale(),
                            application.is_test_version_option());
            if (new_frame.read(design_file.get_input_stream(),
                    design_file.is_created_from_text_file(),
                    this.sp_message_field)) {
                // read_ok
                new_frame.menubar.add_design_dependent_items();
                if (design_file.is_created_from_text_file()) {
                    // Read the file  with the saved rules, if it is existing.
                    String file_name = design_file.get_name();
                    String[] name_parts = file_name.split("\\.");
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
            /**
             * close loading dialog
             */
            try {
                dialog.close();
            } catch (Exception exc) {
            }
        }
    }
}
