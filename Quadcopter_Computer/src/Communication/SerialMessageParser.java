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
public class SerialMessageParser implements SerialAction {

    MessageParser mp;
    
    
    public SerialMessageParser(MessageParser mp) {
        this.mp = mp;
    }
    
    @Override
    public void open() {
        currentMessage = new Message();
    }

    @Override
    public void run(BufferedReader input) throws IOException {
        processSerial(input);
    }

    @Override
    public void close() {
    }
    
    enum MessageState {
        WAITING_FOR_BEGIN,
        WAITING_FOR_LENGTH,
        WAITING_FOR_ACTION,
        WAITING_FOR_BODY,
        WAITING_FOR_END,
        MESSAGE_READY,
        ERROR,
    };
    
    MessageState state = MessageState.WAITING_FOR_LENGTH;
    private Message currentMessage;
    private int bodyIndex;

    
    //parse messages from serial in the form
    //(BEGIN_CHAR)(LENGTH)(ACTION)(BODY1)...(END_CHAR)
    private void processSerial(BufferedReader input) {
        try {
            while (input.ready()) {
                char c = (char) input.read();
                switch (state) {
                    case WAITING_FOR_BEGIN: {
                        if(c == Message.BEGIN_CHAR) {
                            state = MessageState.WAITING_FOR_LENGTH;
                        }
                        break;
                    }
                    case WAITING_FOR_LENGTH: {
                        currentMessage.length = c;
                        if(currentMessage.length > Message.MAX_MESSAGE_LENGTH) {
                            System.err.print("INVALID MESSAGE LENGTH");
                            state = MessageState.ERROR;
                        }
                        currentMessage.body = new char[currentMessage.length - Message.HEADER_LENGTH - Message.FOOTER_LENGTH];
                        state = MessageState.WAITING_FOR_ACTION;
                        break;
                    }
                    case WAITING_FOR_ACTION: {
                        currentMessage.action = c;
                        state = MessageState.WAITING_FOR_BODY;
                        break;
                    }
                    case WAITING_FOR_BODY: {
                        if (bodyIndex == currentMessage.length - Message.HEADER_LENGTH - Message.FOOTER_LENGTH) {
                            state = MessageState.WAITING_FOR_END;
                        } else {
                            currentMessage.body[bodyIndex++] = c;
                        }
                        break;
                    }
                    case WAITING_FOR_END: {
                        if(c == Message.END_CHAR) {
                            state = MessageState.MESSAGE_READY;
                        }
                        break;
                    }   
                    case ERROR: {
                        if(c == Message.END_CHAR) {
                            currentMessage.reset();
                            state = MessageState.WAITING_FOR_BEGIN;
                        }
                    }
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if(state == MessageState.MESSAGE_READY) {
            state = MessageState.WAITING_FOR_LENGTH;
            mp.parseMessage(new Message(currentMessage));
            currentMessage.reset();
        }
    }
    
    
    
}
