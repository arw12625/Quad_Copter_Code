/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Andrew_2
 */
public class GainSlider extends JPanel {

    JLabel name;
    JTextArea field;
    JSlider slide;
    int value;
    final int mins, maxs;

    public GainSlider(String name, int value, int min, int max) {
        super();
        this.name = new JLabel(name);
        this.name.setFont(new Font("Consolas", Font.PLAIN, 16));
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        this.mins = min;
        this.maxs = max;
        field = new JTextArea(1, 6);
        slide = new JSlider(mins, maxs);
        setValue(value);

        field.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                try {
                    if (e.getKeyChar() == '\n') {
                        int i = Integer.parseInt(field.getText().trim());
                        setValue(i);
                    }
                } catch (NumberFormatException ex) {
                    setValue(mins);
                }
            }
        });
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int i = Integer.parseInt(field.getText().trim());
                    setValue(i);
                } catch (NumberFormatException ex) {
                    setValue(mins);
                }
            }
        });

        slide.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                setValue(slide.getValue());
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        this.add(this.name, c);
        c.gridx = 1;
        this.add(field, c);
        c.gridx = 2;
        this.add(slide, c);

    }

    public void setValue(int value) {
        if (value > maxs) {
            value = maxs;
        }
        if (value < mins) {
            value = mins;
        }
        this.value = value;
        field.setText(" " + value);
        slide.setValue(value);
    }

    public int getValue() {
        return value;
    }
}
