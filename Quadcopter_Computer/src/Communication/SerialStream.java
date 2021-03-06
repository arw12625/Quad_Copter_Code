/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class SerialStream implements SerialPortEventListener {

    SerialPort serialPort;
    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
        "COM4", // Windows
    };
    /**
     * A BufferedReader which will be fed by a InputStreamReader converting the
     * bytes into characters making the displayed results codepage independent
     */
    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private BufferedOutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private int DATA_RATE = 9600;
    //The maximum message size
    private static final int MAX_DATA_SIZE = 256;
    //A list of actions to perform on a serial event
    private ArrayList<SerialAction> actions;

    public boolean initialize(int baud) {
        DATA_RATE = baud;
        actions = new ArrayList<SerialAction>();


        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        System.setProperty("gnu.io.rxtx.SerialPorts", "COM4");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            System.out.println(currPortId.getName());
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return false;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = new BufferedOutputStream(serialPort.getOutputStream());

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }
        return true;
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
        for (SerialAction sa : actions) {
            sa.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                input.mark(MAX_DATA_SIZE);
                for (SerialAction sa : actions) {
                    input.reset();
                    sa.run(input);
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public synchronized void sendBytes(byte[] data) {
        try {
            output.write(data);
            output.flush();
        } catch (IOException ex) {
            System.err.println("Could not send data");
            ex.printStackTrace();
        }
    }

    public synchronized void sendLine(String line) {
        sendBytes(line.getBytes());
    }

    public synchronized void sendLines(String[] lines) {
        for (String s : lines) {
            sendLine(s);
        }
    }

    public synchronized byte[] readBytes() {
        byte[] byteArray = null;
        try {
            ArrayList<Byte> list = new ArrayList<Byte>();
            input.mark(MAX_DATA_SIZE);
            while (input.ready()) {
                list.add((byte) input.read());
            }
            byteArray = new byte[list.size()];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = list.get(i);
            }
            input.reset();
        } catch (IOException ex) {
            System.err.println("Could not read data");
            ex.printStackTrace();
        }
        return byteArray;
    }

    public synchronized String[] readLines() {
        String[] lineArray = null;
        try {
            ArrayList<String> list = new ArrayList<String>();
            input.mark(MAX_DATA_SIZE);
            while (input.ready()) {
                list.add(input.readLine());
            }
            lineArray = new String[list.size()];
            for (int i = 0; i < lineArray.length; i++) {
                lineArray[i] = list.get(i);
            }
            input.reset();
        } catch (IOException ex) {
            System.err.println("Could not read data");
            ex.printStackTrace();
        }
        return lineArray;
    }

    public void addSerialAction(SerialAction sa) {
        actions.add(sa);
        sa.open();
    }
}
