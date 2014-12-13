// InertialSensorManager.h

#ifndef _INERTIALSENSORMANAGER_h
#define _INERTIALSENSORMANAGER_h

#include "MinIMU9AHRS.h"

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
	};
	float pyr[4];
};

extern Orientation eulerOr;

void init_inertial_sensors(void);
void update_inertial_sensors(void);


#endif

