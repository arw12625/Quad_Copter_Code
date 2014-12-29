#ifndef COMMUNICATE_MESSAGE_H_
#define COMMUNICATE_MESSAGE_H_


#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#define MAX_MESSAGE_LENGTH 32
#define BAUD_RATE 115200
#define MEMBER_SIZE(type, member) sizeof(((type *)0)->member)
#define BEGIN_CHAR '~'
#define END_CHAR '~'

enum message_state_t {
  WAITING_FOR_BEGIN,
  WAITING_FOR_LENGTH,
  WAITING_FOR_ACTION,
  WAITING_FOR_BODY,
  WAITING_FOR_END,
  MESSAGE_READY,
  ERROR
};

struct message_data_t {
  struct {
    byte length;
    byte action;
  } header;
  byte body[MAX_MESSAGE_LENGTH];
};

struct message_t {
  enum message_state_t state;
  struct message_data_t data;
};

void init_messages(void);
void update_messages(void);

#endif // COMMUNICATE_MESSAGE_H_
