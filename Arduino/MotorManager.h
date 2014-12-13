// MotorManager.h

#ifndef _MOTORMANAGER_h
#define _MOTORMANAGER_h

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#include <Servo.h>

#define NUM_MOTORS 4


struct motor_t {
	byte value;
	byte pin;
	Servo esc;
};

union motor_union{
	struct {
		motor_t* back_right;
		motor_t* front_right;
		motor_t* back_left;
		motor_t* front_left;
	};
	struct {
		motor_t* numbered[NUM_MOTORS];
	};
};

extern motor_union motors;

void init_motors(void);
void update_motors(void);
void write_motor(motor_t* motor, int value);

#endif

