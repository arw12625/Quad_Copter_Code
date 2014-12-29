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
public interface SerialAction {

    public void open();
    public void run(BufferedReader input) throws IOException;
    public void close();
}
