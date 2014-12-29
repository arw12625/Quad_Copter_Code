/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

/**
 *
 * @author Andy
 */
public class MessageParser {

    public MessageParser() {
    }

    void parseMessage(Message m) {
        switch (m.action) {
            case 'm': {
                for(int i = 0; i < m.body.length; i++) {
                    System.out.print(m.body[i]);
                }
                System.out.println();
                break;
            }
        }
    }
}
