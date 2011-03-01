package com.jivesoftware.debug;

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
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;


public class DebugDialog extends JDialog implements ActionListener {
    
    private JButton closeButton  = new JButton("Close");
    private JButton clearButton  = new JButton("Clear");
    private JLabel textLabel     = new JLabel("Set Priority ");
    private JTextField textField = new JTextField(3);
    private JTextArea textArea   = new JTextArea(30, 10);
    private static boolean dialogFlag   = false;
    private static DebugDialog instance = null;

    private DebugDialog() {
        initWindow();
    }

    public static DebugDialog getInstance() {
        if (instance == null)
            instance = new DebugDialog();

        return instance;
    }

    public void initWindow() {
        JPanel mainPane = new JPanel(new BorderLayout());

        clearButton.addActionListener(this);
        closeButton.addActionListener(this);

        JScrollPane scrollPane =
            new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textArea.setEditable(false);

        // Create and setup buttonPane.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(clearButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(closeButton);

        // Create and setup textPane.
        mainPane.add(scrollPane, BorderLayout.NORTH);
        mainPane.add(buttonPane, BorderLayout.SOUTH);
        add(mainPane);
 
        pack();
        setSize(400, 500);
        setBackground(Color.white);
        setLocationRelativeTo(null);
    }

    // Check if the dialog was already opened.
    public static boolean getFlag() {
        return dialogFlag;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            dialogFlag = false;
            setVisible(false);
        }
        else if (e.getSource() == clearButton) {
            textArea.setText("");   
        }
    }

    public void log(String text) {
        textArea.append(text+"\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
