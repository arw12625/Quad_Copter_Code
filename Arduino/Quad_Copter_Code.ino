#include <Vector.h>
#include <Output.h>
#include <matrix.h>
#include <I2C.h>
#include <DCM.h>
#include <Compass.h>
#include <Wire.h>
#include <Servo.h>
#include <MinIMU9AHRS.h>
#include <LSM303.h>
#include <L3G.h>

#include "MotorManager.h"
#include "message.h"
#include "InertialSensorManager.h"


void setup(void) {
	init_messages();
	init_inertial_sensors();
	init_motors();
}

void loop(void) {
	update_messages();
	update_inertial_sensors();
	update_motors();
	
}