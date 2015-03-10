//
//
//

#include "MotorManager.h"

motor_union motors;
bool motors_enabled = true;

void update_escs(void) {
	for(int i = 0; i < NUM_MOTORS; i++) {
		if(motors_enabled) {
			motors.numbered[i]->esc.write(motors.numbered[i]->value);
		} else {
			motors.numbered[i]->esc.write(20);
		}
	}
}

void write_motor(motor_t* motor, int value) {
		motor->value = value;
	/*Serial.print("Motor ");
	Serial.print(motor->pin);
	Serial.print(" value = ");
	Serial.println(value);
	motor->esc.write(value);*/
}

void init_motors(void) {
	Serial.println("INITIALIZING MOTORS");
	
	//Allocate memory for the motors
	for(int i = 0; i < NUM_MOTORS; i++) {
		motors.numbered[i] = (motor_t*)malloc(sizeof(motor_t));
		motors.numbered[i]->esc = Servo();
	}
	
	//Assign pin numbers
	motors.named.back_left->pin = 9;
	motors.named.back_right->pin = 10;
	motors.named.front_left->pin = 11;
	motors.named.front_right->pin = 6;
	
	for(int i = 0; i < NUM_MOTORS; i++) {
		//Attach servo objects for the speed controllers	
		motors.numbered[i]->esc.attach(motors.numbered[i]->pin); 
		
		Serial.print("Pin ");
		Serial.print(motors.numbered[i]->pin);
		Serial.println(" attached");
		
		//Write initial value to motor
		write_motor(motors.numbered[i], 20);

	}
}

int loop_num = 0;
void update_motors(void) {
	update_escs();
	if(0) {//loop_num++ >= 1<<12) {
		loop_num = 0;
		for(int i = 0; i < NUM_MOTORS; i++) {
			Serial.print("Motor");
			Serial.print(i);
			Serial.print(": ");
			Serial.print(motors.numbered[i]->value);
			Serial.print("\t");
		}
		Serial.println();
	}
}

void kill(void) {
	motors_enabled = false;
}
