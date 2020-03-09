package me.isaiah.shell.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Calculator", width=200, height=300)
public class Calc extends JProgram implements ActionListener{

    private static final long serialVersionUID = 1L;
    private JTextField display = new JTextField("0");
    private double result = 0;
    private String operator = "=";
    private boolean calculating = true;

    public Calc() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        display.setEditable(false);
        p.add(display, "North");

        JPanel panel = new JPanel(new GridLayout(1,1));
        JPanel simple = new JPanel();
        simple.setLayout(new GridLayout(5, 4));

        String buttonLabels = "789/456*123-0.=+";
        ((JButton)simple.add(new JButton("C"))).addActionListener(a -> {
            operator = "C";
            calculate(Double.parseDouble(display.getText()));
        });
        ((JButton)simple.add(new JButton("\u221A"))).addActionListener(a -> {
            operator = "\u221A";
            calculate(Double.parseDouble(display.getText()));
        });
        simple.add(new JSeparator(SwingConstants.VERTICAL));
        JButton off = ((JButton)simple.add(new JButton("X")));
        off.addActionListener(l -> {
            operator = "X";
            calculate(Double.parseDouble(display.getText()));
        });
        off.setBackground(Color.RED);
        for (int i = 0; i < buttonLabels.length(); i++)
            ((JButton)simple.add(new JButton(buttonLabels.substring(i, i + 1)))).addActionListener(this);

        panel.add(simple);
        p.add(panel, "Center");
        setContentPane(p);
    }

    public void actionPerformed(ActionEvent evt) {
        String cmd = evt.getActionCommand();
        if ('0' <= cmd.charAt(0) && cmd.charAt(0) <= '9' || cmd.equals(".")) {
            if (calculating) display.setText(cmd);
            else display.setText(display.getText() + cmd);

            calculating = false;
        } else {
            if (calculating) {
                if (cmd.equals("-")) {
                    display.setText(cmd);
                    calculating = false;
                } else operator = cmd;
            } else {
                double x = Double.parseDouble(display.getText());
                calculate(x);
                operator = cmd;
                calculating = true;
            }
        }
    }

    private void calculate(double n) {
        switch (operator) {
            case "+":
                result += n;
                break;
            case "-":
                result -= n;
                break;
            case "*":
                result *= n;
                break;
            case "/":
                result /= n;
                  break;
            case "=":
                result = n;
                break;
            case "\u221A":
                result = Math.sqrt(n);
                break;
            case "C":
                result = 0;
                break;
            case "X":
                try { this.setClosed(true); } catch (PropertyVetoException e) {}
                break;
        }
        display.setText("" + result);
    }

 }