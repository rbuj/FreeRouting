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
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;
import net.freerouting.freeroute.board.TestLevel;
import net.freerouting.freeroute.designformats.specctra.DsnFileException;

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
    private TestLevel test_level = null;
    private BoardFrame.Option board_option;

    private File inputFile;
    private String design_dir_name;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        sp_message_field = new SimpleStringProperty();
        message_field.textProperty().bind(sp_message_field);
    }

    void init_variables(String p_design_dir_name, File p_inputFile, TestLevel p_test_level,
            BoardFrame.Option p_board_option, Stage p_mainStage) {
        design_dir_name = !p_design_dir_name.isEmpty()
                ? p_design_dir_name : (p_inputFile != null) ? p_inputFile.getParent() : "";
        inputFile = p_inputFile;
        test_level = p_test_level;
        board_option = p_board_option;
        mainStage = p_mainStage;
    }

    @FXML
    private void open_board_design_action(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        if (!design_dir_name.isEmpty()) {
            fileChooser.setInitialDirectory(new File(design_dir_name));
        }
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("All Design Files", "*.dsn", "*.bin"));
        inputFile = fileChooser.showOpenDialog(mainStage);
        create_board_frame();
    }

    protected void create_board_frame() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (inputFile != null) {
                    try {
                        design_dir_name = inputFile.getParent();
                        /**
                         * create board
                         */
                        BoardFrame new_frame = new BoardFrame(
                                new DesignFile(inputFile, design_dir_name),
                                board_option,
                                test_level,
                                resourceBundle.getLocale(),
                                test_level == null); // true, if it's a test_version
                    } catch (BoardFrameException | DsnFileException ex) {
                        Logger.getLogger(MainAppController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
