package me.isaiah.shell.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import me.isaiah.shell.api.JProgram;
import me.isaiah.shell.api.ProgramInfo;

@ProgramInfo(name = "Calculator", width=245, height=330)
public class Calc extends JProgram {

    private static final long serialVersionUID = 1L;

    private JLabel display;
    private float result = 0;
    private String operator = "=";
    private boolean calculating = true;

    public Calc() {
        display = new JLabel("0", SwingConstants.RIGHT);
        result = 0;
        operator = "=";
        calculating = true;

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(display, BorderLayout.NORTH);
        display.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(12,2,12,18)));
        display.setFont(display.getFont().deriveFont(16f));
        p.setBorder(BorderFactory.createEmptyBorder(14,10,10,10));
        display.setBackground(Color.WHITE);
        display.setOpaque(true);

        JPanel simple = new JPanel();
        simple.setBorder(BorderFactory.createEmptyBorder(14,0,0,0));
        simple.setLayout(new GridLayout(5, 4));

        String buttonLabels = "789/456*123-0.=+";

        ((JButton)simple.add(new JButton("C"))).addActionListener(l -> {
            operator = "C";
            calculate(0);
        });

        ((JButton)simple.add(new JButton("+-"))).addActionListener(l -> {
            operator = "neg";
            calculate(Float.parseFloat(display.getText()));
        });

        ((JButton)simple.add(new JButton("<"))).addActionListener(l -> {
            operator = "<-";
            calculate(Float.parseFloat(display.getText()));
        });

        simple.add(new JButton(""));

        for (int i = 0; i < buttonLabels.length(); i++) {
            int i_final = i;
            JButton btn = ((JButton)simple.add(new JButton(buttonLabels.substring(i, i + 1))));
            char ch = btn.getText().charAt(0);
            if ('0' <= ch && ch <= '9' || ch == '.') 
                btn.setBackground(Color.WHITE);

            btn.addActionListener(l -> actionPerformed(buttonLabels.substring(i_final, i_final + 1)));
        }

        for (Component c : simple.getComponents())
            c.setFont(c.getFont().deriveFont(14f));

        p.add(simple, BorderLayout.CENTER);
        setContentPane(p);
    }

    public void actionPerformed(String cmd) {
        if (('0' <= cmd.charAt(0) && cmd.charAt(0) <= '9') || cmd.equals(".") || cmd.equals("neg")) {
            if (calculating) display.setText(cmd);
            else display.setText(display.getText() + cmd);

            calculating = false;
        } else {
            if (calculating) {
                operator = cmd;
            } else {
                float x = Float.parseFloat(display.getText());
                calculate(x);
                operator = cmd;
                calculating = true;
            }
        }
    }

    private void calculate(float n) {
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
            case "C":
                this.calculating = true;
                result = n;
                break;
            case "neg": {
                this.calculating = true;
                if (Float.parseFloat(display.getText()) > 0) {
                    display.setText("-" + n);
                } else {
                    display.setText(("" + n).replace("-", ""));
                }
                result = Float.parseFloat(display.getText());
                n = result;
                break;
            }
        }
        if (String.valueOf(result).endsWith(".0"))
            display.setText("" + (int)result);
        else display.setText("" + result);
    }

 }