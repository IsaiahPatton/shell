package com.fungus_soft.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class ModernTextField extends JTextField {

    private static final long serialVersionUID = 1L;

    public ModernTextField(String text) {
        this();
        this.setText(text);
    }

    public ModernTextField() {
        super();
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 2), BorderFactory.createEmptyBorder(4,4,4,4)));
    }

}