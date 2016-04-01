/*
 * Copyright (C) 2016 Robert Antoni Buj Gelonch <rbuj@fedoraproject.org>
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Robert Antoni Buj Gelonch <rbuj@fedoraproject.org>
 */
public class WindowAboutController implements Initializable {

    @FXML
    private Label version;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try (InputStream in = MainApp.class.getClass().getResourceAsStream("/net/freerouting/freeroute/VERSION"); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = reader.readLine();
            version.setText(new StringBuilder().append(rb.getString("version")).append(" ").append(line).toString());
        }
        catch (Exception ex) {
            Logger.getLogger(WindowAboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
