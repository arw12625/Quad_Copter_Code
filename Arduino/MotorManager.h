// MotorManager.h

#ifndef _MOTORMANAGER_h
#define _MOTORMANAGER_h

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#define NUM_MOTORS 4

#include <Servo.h>

struct motor_t {
	int value;
	int pin;
	Servo esc;
};

union motor_union {
	struct {
		motor_t* back_right;
		motor_t* front_right;
		motor_t* back_left;
		motor_t* front_left;
	};
	motor_t* numbered[NUM_MOTORS];
};

extern motor_union motors;

void init_motors(void);
void update_motors(void);
void write_motor(motor_t* motor, int value);

#endif

