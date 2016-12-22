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
 * WindowAbout.java
 *
 * Created on 7. Juli 2005, 07:24
 *
 */
package net.freerouting.freeroute;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Locale;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Displays general information about the freeroute software.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class WindowAbout extends BoardSavableSubWindow {

    public WindowAbout(Locale p_locale) {
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.WindowAbout", p_locale);
        this.setTitle(resources.getString("title"));
        this.setLayout(new BorderLayout());
        this.setSize(520, 200);
        final JFXPanel jfxPanel = new JFXPanel();
        this.add(jfxPanel, BorderLayout.CENTER);
        this.setResizable(false);
        Platform.runLater(() -> initFX(jfxPanel, p_locale));
    }

    private void initFX(JFXPanel jfxPanel, Locale p_locale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/freerouting/freeroute/fxml/WindowAbout.fxml"));
            java.util.ResourceBundle rb
                    = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.WindowAbout", p_locale);
            loader.setResources(rb);
            Parent root = (Parent) loader.load();
            WindowAboutController controller = loader.getController();
            controller.initialize(null, rb);
            Scene scene = new Scene(root);
            jfxPanel.setScene(scene);
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
