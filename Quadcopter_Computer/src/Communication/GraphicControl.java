/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Andy
 */
public class GraphicControl extends JFrame {

    JTextArea outputText;
    JTextArea inputText;
    SerialStream serial;
    public boolean exit;

    public GraphicControl() {
        serial = new SerialStream();
        serial.initialize();
        AngleLogger at = new AngleLogger("res/angle1.txt");
        serial.addSerialAction(at);
        serial.addSerialAction(new SerialAction() {

            @Override
            public void run(BufferedReader input) throws IOException {
                while (input.ready()) {
                    processOutput(input.readLine());
                }

            }

            @Override
            public void close() throws IOException {
            }
        });

        setVisible(true);
        setTitle("Quadcontrol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container con = getContentPane();
        con.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 0, 10);
        JLabel title = new JLabel("Graphic Control");
        con.add(title, c);
        c.gridy = 1;
        outputText = new JTextArea(16, 64);
        outputText.setFont(new Font("Courier New", Font.PLAIN, 16));
        outputText.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        con.add(outputScroll, c);
        c.gridy = 2;
        c.insets = new Insets(0, 10, 0, 10);
        inputText = new JTextArea(1, 66);
        inputText.setFont(new Font("Courier New", Font.PLAIN, 16));
        inputText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    String text = inputText.getText();
                    outputText.append(text);
                    outputText.setCaretPosition(outputText.getDocument().getLength());
                    processInput(text);
                    inputText.setText("");
                }
            }
        });
        JScrollPane inputScroll = new JScrollPane(inputText, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        con.add(inputScroll, c);


        pack();
        setSize(800, 640);
    }
    public static final String[] exitWords = {"exit", "quit"};

    public void processInput(String text) {
        text = text.trim();
        for (String w : exitWords) {
            if (text.toLowerCase().equals(w)) {
                exit = true;
            }
        }
        serial.sendLine(text);
    }

    public void processOutput(String text) {
        outputText.append(text + "\n");
        //outputText.setCaretPosition(outputText.getDocument().getLength());
    }

    public static void main(String[] args) {
        final GraphicControl gc = new GraphicControl();
        Thread t = new Thread() {

            @Override
            public void run() {
                while (!gc.exit) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }
}
