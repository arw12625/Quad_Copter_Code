/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Andy
 */
public class ConsoleSerial {
    public static void main(String[] args) throws Exception {

        final SerialStream serial = new SerialStream();
        serial.initialize();
        final AngleLogger at = new AngleLogger("res/angle1.txt");
        serial.addSerialAction(at);
        Thread t = new Thread() {

            @Override
            public void run() {
                boolean exit = false;
                //input from the console
                BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
                //output to the console
                BufferedOutputStream consoleOut = new BufferedOutputStream(System.out);
                byte newline = 10;
                while (!exit) {
                    try {
                        //Prompt for input
                        System.out.print("Input: ");
                        //Read a line from the console
                        String inputLine = consoleIn.readLine();
                        //convert the string to an array
                        byte[] byteArray = inputLine.getBytes();
                        //write the data to the serial stream
                        serial.sendBytes(byteArray);
                        //write the data to the console
                        //consoleOut.write(byteArray);
                        //consoleOut.flush();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ie) {
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        };
        t.start();
        System.out.println("Started");
    }
}
