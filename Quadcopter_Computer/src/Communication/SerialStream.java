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
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialStream implements SerialPortEventListener {

    SerialPort serialPort;
    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
        "COM5", // Windows
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
    private static final int DATA_RATE = 9600;
    //The maximum message size
    private static final int DATA_SIZE = 128;
    //A list of actions to perform on a serial event
    private ArrayList<SerialAction> actions;

    public void initialize() {
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        System.setProperty("gnu.io.rxtx.SerialPorts", "COM5");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
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

            actions = new ArrayList<SerialAction>();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
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
        for(SerialAction sa : actions) {
            try {
                sa.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                input.mark(DATA_SIZE);
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
        for(String s : lines) {
            sendLine(s);
        }
    }
    
    public synchronized byte[] readBytes() {
        byte[] byteArray = null;
        try {
            ArrayList<Byte> list = new ArrayList<Byte>();
            input.mark(DATA_SIZE);
            while(input.ready()) {
                list.add((byte)input.read());
            }
            byteArray = new byte[list.size()];
            for(int i = 0; i < byteArray.length; i++) {
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
            input.mark(DATA_SIZE);
            while(input.ready()) {
                list.add(input.readLine());
            }
            lineArray = new String[list.size()];
            for(int i = 0; i < lineArray.length; i++) {
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
    }
}
