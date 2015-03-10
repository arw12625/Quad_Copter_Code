// 
// 
// 

#include "InertialSensorManager.h"

#include "MinIMU9AHRS.h"

Orientation eulerOr;

void init_inertial_sensors(void) {
	init_MinIMU9();
}

long convert_to_deci(float x)
{
	return x*10000000;
}

int loop_num1 = 0;
void update_inertial_sensors(void) {
	update_MinIMU9();
	eulerOr.named.pitch = ToDeg(getPitch());
	eulerOr.named.yaw = ToDeg(getYaw());
	eulerOr.named.roll = -ToDeg(getRoll());
	if(loop_num1++ >= 10) {
		loop_num1 = 0;
		Serial.print("Pitch: ");
		Serial.print(eulerOr.named.pitch);
		Serial.print("\t");
		Serial.print("Yaw: ");
		Serial.print(eulerOr.named.yaw);
		Serial.print("\t");
		Serial.print("Roll: ");
		Serial.print(eulerOr.named.roll);
		Serial.println();
	}
}