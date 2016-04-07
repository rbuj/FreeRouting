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
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
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

    private Stage mainStage;
    private static final Logger logger = Logger.getLogger(MainAppController.class.getName());

    protected static SimpleStringProperty sp_message_field;
    private ResourceBundle resourceBundle;
    private DesignFile designFile;
    private TestLevel test_level = null;
    private BoardFrame.Option board_option;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        this.sp_message_field = new SimpleStringProperty();
        message_field.textProperty().bind(this.sp_message_field);
    }

    void init_variables(DesignFile p_designFile, TestLevel p_test_level, BoardFrame.Option p_board_option, Stage p_mainStage) {
        designFile = p_designFile;
        test_level = p_test_level;
        board_option = p_board_option;
        mainStage = p_mainStage;
    }

    @FXML
    private void open_board_design_action(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("All Design Files", "*.dsn", "*.bin"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        create_board_frame(selectedFile);
    }

    void create_board_frame() {
        create_board_frame(designFile.get_input_file());
    }

    private void create_board_frame(File selectedFile) {
        if (selectedFile != null) {
            java.util.ResourceBundle resources
                    = java.util.ResourceBundle.getBundle(
                            "net.freerouting.freeroute.resources.MainApp",
                            resourceBundle.getLocale());
            /**
             * Show loading dialog
             */
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.initOwner(mainStage);
            dialog.setTitle("Loading");
            dialog.setHeaderText("Loading...");
            dialog.setContentText(resources.getString("loading_design") + " " + selectedFile.toString());
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
                            test_level,
                            resourceBundle.getLocale(),
                            test_level == null); // true, if it's a test_version
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
