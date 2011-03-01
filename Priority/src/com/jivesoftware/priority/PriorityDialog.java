package com.jivesoftware.priority;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JMenuItem;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;

import org.jivesoftware.spark.Workspace;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.SparkManager;

public class PriorityDialog extends JDialog implements ActionListener {
    
    private JButton setButton    = new JButton("Set");
    private JButton closeButton  = new JButton("Close");
    private JLabel textLabel     = new JLabel("Set Priority ");
    private JTextField textField = new JTextField(3);
    private static boolean dialogFlag   = false;

    public PriorityDialog() {
        initWindow();
    }

    public void initWindow() {
        JPanel mainPane = new JPanel(new BorderLayout());

        setButton.addActionListener(this);
        closeButton.addActionListener(this);

        // Create and setup buttonPane.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(setButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(closeButton);

        // Create and setup textPane.
        JPanel fieldsPane = new JPanel();
        fieldsPane.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
        fieldsPane.setLayout(new GridBagLayout());
        GridBagLayout gridbag = (GridBagLayout)fieldsPane.getLayout();
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 1;
        c.gridy = 2;
        textField.setActionCommand("Set Priority");
        gridbag.setConstraints(textField, c);
        fieldsPane.add(textField);

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(textLabel, c);
        fieldsPane.add(textLabel);

        mainPane.add(fieldsPane, BorderLayout.NORTH);
        mainPane.add(buttonPane, BorderLayout.SOUTH);
        add(mainPane);
        Integer currentPresence = new Integer(SparkManager.getWorkspace().
            getStatusBar().getPresence().getPriority());
        textField.setText(currentPresence.toString());       
 
        pack();
        setSize(200, 120);
        setBackground(Color.white);
        setLocationRelativeTo(null);
    }

    // Check if the dialog was already opened.
    public static boolean getFlag() {
        return dialogFlag;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == setButton) {
            // Check and confirm input.
            dialogFlag = true;
            int priority = Integer.parseInt(textField.getText());
            if (priority < 0) {
                // Illegal. Set back to default.
                priority = 5;
            }
            //Set the priority 
            Presence presence = SparkManager.getWorkspace().getStatusBar().getPresence();
            presence.setPriority(priority);
            SparkManager.getConnection().sendPacket(presence);

            setVisible(false);
            dialogFlag = false;
        }
        else if (e.getSource() == closeButton) {
            dialogFlag = false;
            setVisible(false);
            dispose();
        }
    }
}
