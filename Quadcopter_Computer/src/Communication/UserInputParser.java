/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

/**
 *
 * @author Andy
 */
public class UserInputParser {
    
    SerialStream s;
    
    public UserInputParser(SerialStream s) {
        this.s = s;
    }
    
    //process input into messages to serial
    public void parseMessage(String input) {
        int length = input.length() - 1 + Message.HEADER_LENGTH + Message.FOOTER_LENGTH;
        String message = "";
        message += Message.BEGIN_CHAR;
        message += ""+length;
        message += input;
        message += Message.END_CHAR;
        System.out.println(message);
        s.sendLine(message);
    }
}
