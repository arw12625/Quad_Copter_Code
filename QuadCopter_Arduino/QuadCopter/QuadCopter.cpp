#include "Arduino.h"

#include "Parsing/message.h"
#include "Motor/MotorManager.h"
#include "Inertia/InertialSensorManager.h"
#include "Inertia/Control.h"
#include "time_util.h"


void setup(void) {
	init_messages();
	init_motors();
	init_inertial_sensors();
	init_control();
	initTime();
}

int loop_counter = 0;
void loop(void) {
	if((millis()-timer)>=20)  // Main loop runs at 50Hz
	{
		if(loop_counter++ > 50) {
			loop_counter = 0;
			//Serial.println(millis() - timer);
		}
		updateTime();
		update_messages();
		update_motors();
		update_inertial_sensors();
		update_control();
		
	}
}
