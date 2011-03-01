package com.jivesoftware.debug;

import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.smack.packet.Presence; 


import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Debug implements Plugin, ActionListener {
    
    /**
     * initialize this sparkplug.
     */
    public void initialize() {
        JMenuItem menuItem = new JMenuItem("Debugger");
        // Get JMenu instance.
        JMenu newMenu = SparkManager.getMainWindow().getMenuByName("Actions");
        newMenu.add(menuItem);
        menuItem.addActionListener(this);
    }

    public void shutdown() {
        // Write our current Priority
        // to the settings file.
    }

    public boolean canShutDown() {
        return true;
    }

    public void uninstall() {
        // Set older instance.
    }

    public void actionPerformed(ActionEvent e) {
        // create priority window.
        boolean dialogFlag = DebugDialog.getFlag();
        if (!dialogFlag) {
            DebugDialog debug = DebugDialog.getInstance();
            debug.setVisible(true);
        }
    }
}
