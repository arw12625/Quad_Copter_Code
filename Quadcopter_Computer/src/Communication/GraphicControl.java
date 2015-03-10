/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Andy
 */
public class GraphicControl extends JFrame {

    public static final int baud = 115200;
    boolean connected;
    SerialAction simpleRecieverSerial;

    JTextArea outputText;
    DefaultCaret caret;

    JTextArea inputText;
    SerialStream serial;
    UserInputParser inputParser;
    public boolean exit;

    public GraphicControl() {
        serial = new SerialStream();
        inputParser = new UserInputParser(serial);

        simpleRecieverSerial = new SerialAction() {
            @Override
            public void open() {
            }

            @Override
            public void run(BufferedReader input) throws IOException {
                while (input.ready()) {
                    processOutput(input.readLine());
                }
            }

            @Override
            public void close() {
            }
        };

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
        c.gridwidth = 3;
        outputText = new JTextArea(16, 64);
        outputText.setFont(new Font("Courier New", Font.PLAIN, 16));
        outputText.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        new SmartScroller(outputScroll);
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
                    outputText.append("Message Sent: " + text);
                    processInput(text);
                    inputText.setText("");
                }
            }
        });

        JScrollPane inputScroll = new JScrollPane(inputText, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        con.add(inputScroll, c);
        c.gridwidth = 1;

        c.gridy = 3;
        c.gridheight = 5;
        JButton connect = new JButton("Connect");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connected) {
                    disconnect();
                    connect.setText("Connect");
                } else {
                    connect();
                    if (connected) {
                        connect.setText("Disconnect");
                    }
                }
            }
        });
        con.add(connect, c);

        GainSlider ele = new GainSlider("Elev ", 10, 0, 255);
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 3;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.WEST;
        con.add(ele, c);
        GainSlider pitch = new GainSlider("Pitch", 10, 0, 255);
        c.gridy = 4;
        con.add(pitch, c);
        GainSlider yaw = new GainSlider("Yaw  ", 10, 0, 255);
        c.gridy = 5;
        con.add(yaw, c);
        GainSlider roll = new GainSlider("Roll ", 10, 0, 255);
        c.gridy = 6;
        con.add(roll, c);

        JButton set = new JButton("Set Gains");
        set.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processInput("ise" + ele.getValue());
                processInput("isp" + pitch.getValue());
                processInput("isy" + yaw.getValue());
                processInput("isr" + roll.getValue());
            }
        });
        c.gridx = 2;
        c.gridy = 3;
        c.gridheight = 4;
        con.add(set, c);

        pack();
        setSize(800, 640);

    }

    public void processInput(String text) {
        text = text.trim();
        if(!text.equals("")) {
            inputParser.parseMessage(text);
        }
    }

    public void processOutput(String text) {
        if(text.contains("Pitch:")) {
            orientationGraph(text);
        }
            outputText.append(text + "\n");
    }

    public void connect() {
        connected = serial.initialize(baud);
        serial.addSerialAction(simpleRecieverSerial);
    }

    public void disconnect() {
        connected = false;
        serial.close();
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

    private void orientationGraph(String data) {
        String[] split = data.replace('\t', ' ').split(" ");
        float pitch = Float.parseFloat(split[1]);
        float yaw = Float.parseFloat(split[3]);
        float roll = Float.parseFloat(split[5]);
        hackyGraph(pitch);
    }

    private void hackyGraph(float pitch) {
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < (pitch + 45) / 3; i++) {
            line.append("X");
        }
        System.out.println(line);
    }
}
