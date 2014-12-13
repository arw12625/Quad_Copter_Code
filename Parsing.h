// Parsing.h

#ifndef _PARSING_h
#define _PARSING_h

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif


void process_message(struct message_t *message);

#endif

