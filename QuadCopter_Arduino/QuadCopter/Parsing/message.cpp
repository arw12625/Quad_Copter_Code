#include "message.h"
#include "Parsing.h"



void messageError(message_t *mes) {
	Serial.println("ERROR");
}

byte body_index;
byte body_length;

byte read_message(struct message_t *message) {

	while(Serial.available() && message->state != MESSAGE_READY) {
		
		char c = Serial.read();
		
		switch (message->state) {
			case WAITING_FOR_BEGIN: {
				if(c == BEGIN_CHAR) {
					message->state = WAITING_FOR_LENGTH;
				}
				break;
			}
			case WAITING_FOR_LENGTH: {
				if(0<c-'0' && c-'0'<MAX_MESSAGE_LENGTH) {
					message->data.header.length = c - '0';
					body_length = message->data.header.length - sizeof(message->data.header);
					body_index = 0;
					message->state = WAITING_FOR_ACTION;
				} else {
					message->state = ERROR;
				}
				break;
			}
			case WAITING_FOR_ACTION: {
				message->data.header.action = c;
				message->state = body_length == 0 ? WAITING_FOR_END : WAITING_FOR_BODY;
				break;
			}
			case WAITING_FOR_BODY: {
				if (body_index < body_length) {
					message->data.body[body_index++] = c;
				}
				if (body_index == body_length) {
					message->state = WAITING_FOR_END;
				}
				break;
			}
			case WAITING_FOR_END: {
				if(c == END_CHAR) {
					message->state = MESSAGE_READY;
				} else {
					message->state = ERROR;
				}
				break;
			}
			case MESSAGE_READY: {
				/** Do nothing if the message is ready */
				break;
			}
			case ERROR: {
				break;
			}
		}
		if(message->state == ERROR) {
			messageError(message);
			message->state = WAITING_FOR_BEGIN; 
		}
	}
	return message->state == MESSAGE_READY;
}

void init_messages(void) {
	Serial.begin(BAUD_RATE);
	delay(1000);
	for(int i = 0; i < 3; i++) {
		Serial.print("!");
		delay(1000);
	}
	Serial.println();
	 
}

message_t message;
void update_messages(void) {
	if (read_message(&message)) {
		Serial.println("Message Read");
		process_message(&message);
		message.state = WAITING_FOR_BEGIN;
	}
}