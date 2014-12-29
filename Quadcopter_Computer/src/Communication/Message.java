/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

/**
 *
 * @author Andy
 */
public class Message {
    
    static final int MAX_MESSAGE_LENGTH = 32;
    static final int HEADER_LENGTH = 2;
    static final int FOOTER_LENGTH = 0;
    
    static final char BEGIN_CHAR = '~';
    static final char END_CHAR = '~';
    
    int length;
    char action;
    char body[];
    
    Message() {
    }
    
    public Message(Message orig) {
        length = orig.length;
        action = orig.action;
        body = orig.body;
    }

    void reset() {
        length = 0;
        action = 0;
        body = null;
    }
    
    
}
