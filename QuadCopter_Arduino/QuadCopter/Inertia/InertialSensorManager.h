// InertialSensorManager.h

#ifndef _INERTIALSENSORMANAGER_h
#define _INERTIALSENSORMANAGER_h


#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

union Orientation {
	struct {
		float pitch;
		float yaw;
		float roll;
	} named;
	float pyr[3];
};

extern Orientation eulerOr;

void init_inertial_sensors(void);
void update_inertial_sensors(void);


#endif

