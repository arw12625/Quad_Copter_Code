
#include "time_util.h"
#include "Arduino.h"

long timer, timer_old;
float G_Dt;

void initTime(void) {
	
	timer=millis();
}


void updateTime(void) {
	timer_old = timer;
	timer=millis();
	if (timer>timer_old) {
		G_Dt = (timer-timer_old)/1000.0;    // Real time of loop run. We use this on the DCM algorithm (gyro integration time)
	} else {
		G_Dt = 0;
	}
}