/*
* InertialMessage.cpp
*
* Created: 12/29/2014 9:47:21 PM
*  Author: Andy
*/
#include "InertialMessage.h"
#include "InertialSensorManager.h"
#include "Control.h"


void printOrientation(void) {
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

void printPitch(void) {
	Serial.print("Pitch: ");
	Serial.println(eulerOr.named.pitch);
}
void printYaw(void) {
	Serial.print("Yaw: ");
	Serial.println(eulerOr.named.yaw);
}
void printRoll(void) {
	Serial.print("Roll: ");
	Serial.println(eulerOr.named.roll);
}

void process_inertial_message(struct message_t* message, byte body_length) {
	//Serial.print(message->data.body[1]);
	switch(message->data.body[0]) {
		case '*': {
			printOrientation();
			break;
		} case 'p': {
			printPitch();
			break;
		} case 'y': {
			printYaw();
			break;
		} case 'r': {
			printRoll();
			break;
		}	case 'm': {
			toggleMotorDebug();
			break;
		}	case 'e': {
			toggleEnabled();
			break;
		}	case 's': {
			message->data.body[message->data.header.length - MEMBER_SIZE(message_data_t, header)] = '\0';
			int kp = atoi((char *)&message->data.body[2]);
			Serial.print("kp\t");
			Serial.println(kp);
			switch(message->data.body[1]) {
				case 'e':{
					setEle_kp(kp);
					break;
				}	case 'p':{
					setPitch_kp(kp);
					break;
				}	case 'y':{
					setYaw_kp(kp);
					break;
				}	case 'r':{
					setRoll_kp(kp);
					break;
				}
			}
			break;
		}
	}
}
