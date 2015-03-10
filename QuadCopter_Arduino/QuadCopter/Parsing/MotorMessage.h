// MotorMessage.h

#ifndef _MOTORMESSAGE_h
#define _MOTORMESSAGE_h

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#include "message.h"

void process_motor_message(struct message_t*, byte size);

#endif

