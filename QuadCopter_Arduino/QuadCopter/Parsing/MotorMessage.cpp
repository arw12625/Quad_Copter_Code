//
//
//

#include "MotorMessage.h"
#include "Motor/MotorManager.h"

#define  NUM_MOTORS 4

struct motor_message_t {
	struct {
		char motor_number;
		char instruction;
	} header;
	char value[MAX_MESSAGE_LENGTH - 2];
};

enum motor_message_instruction_t {
	MOTOR_INSTRUCTION_SET = '=',
	MOTOR_INSTRUCTION_GET = '?',
};

void process_motor_message_helper(struct motor_message_t *motor_message, byte size) {
	byte motor_number = motor_message->header.motor_number - '0';
	byte all_motors = motor_message->header.motor_number == '*';
	int i;
	byte motor_value;

	if (!(all_motors || (0 <= motor_number && motor_number < NUM_MOTORS))) {
		return;
	}
	
	switch (motor_message->header.instruction) {
		case MOTOR_INSTRUCTION_SET: {
			if (size < sizeof(struct motor_message_t)) {
				byte index = size - MEMBER_SIZE(struct motor_message_t, header);
				motor_message->value[index] = '\0';
			}
			motor_value = atoi(motor_message->value);
			if (all_motors) {
				for (i=0; i<NUM_MOTORS; ++i) {
					write_motor(motors.numbered[i], motor_value);
				}
			} else {
				write_motor(motors.numbered[motor_number], motor_value);
			}
			break;
		}

		case MOTOR_INSTRUCTION_GET: {
			Serial.print("Motor ");
			Serial.print(motor_number);
			Serial.print(" = ");
			Serial.println(motors.numbered[motor_number]->value);
		}
		break;

		default:
		return;
	}
}

void process_motor_message(struct message_t* message, byte body_length) {
	process_motor_message_helper((struct motor_message_t *)&message->data.body, body_length);
}
