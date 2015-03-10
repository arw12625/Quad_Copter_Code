/*
 * InertialMessage.h
 *
 * Created: 12/29/2014 9:48:40 PM
 *  Author: Andy
 */ 


#ifndef INERTIALMESSAGE_H_
#define INERTIALMESSAGE_H_

#include "Parsing/message.h"

extern void process_inertial_message(struct message_t* message, byte body_length);


#endif /* INERTIALMESSAGE_H_ */