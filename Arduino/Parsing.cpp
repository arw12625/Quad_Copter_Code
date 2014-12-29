// 
// 
// 

#include "Parsing.h"
#include "message.h"
#include "MotorMessage.h"


void process_message(struct message_t *message) {
	byte body_length = message->data.header.length - sizeof(message->data.header);


	switch (message->data.header.action) {
		case 'm': {
			process_motor_message(message, body_length);
			break;
		}
	}

}
