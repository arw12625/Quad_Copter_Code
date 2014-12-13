/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Andy
 */
public class PrintSerial implements SerialAction {

    @Override
    public void run(BufferedReader input) throws IOException {
        while(input.ready()) {
            System.out.println(input.readLine());
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("Print Serial closing");
    }
    
}
