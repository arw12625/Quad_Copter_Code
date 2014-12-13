//
//
//

#include "MotorManager.h"

motor_union motors;

void update_escs(void) {
	for(int i = 0; i < NUM_MOTORS; i++) {
		motors.numbered[i]->esc.write(motors.numbered[i]->value);
	}
}

void write_motor(motor_t* motor, int value) {
	motor->value = value;
	motor->esc.write(value);
}

void init_motors(void) {
	
	motors.back_left->pin = 6;
	motors.back_right->pin = 9;
	motors.front_left->pin = 11;
	motors.front_right->pin = 10;
	for(int i = 0; i < NUM_MOTORS; i++) {
		motors.numbered[i]->esc.attach(motors.numbered[i]->pin);  // attaches the esc on pin 9 to the servo object
		write_motor(motors.numbered[i], 20);
	}
}

int loop_num = 0;
void update_motors(void) {
	update_escs();
	if(loop_num++ >= 1<<12) {
		loop_num %= 8;
		for(int i = 0; i < NUM_MOTORS; i++) {
			Serial.print("\tMotor");
			Serial.print(i);
			Serial.print(": ");
			Serial.print(motors.numbered[i]->value);
		}
		Serial.println();
	}
}
