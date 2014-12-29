#include <Servo.h>

#include "message.h"
#include "MotorManager.h"

void setup(void) {
	init_messages();
	init_motors();
}

void loop(void) {
	update_messages();
	update_motors();
}