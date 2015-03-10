/*
 * Time.h
 *
 * Created: 2/17/2015 11:47:26 PM
 *  Author: Andrew_2
 */ 


#ifndef TIME_H_
#define TIME_H_

extern long timer;
extern long timer_old;
extern float G_Dt;
void initTime(void);
void updateTime(void);


#endif /* TIME_H_ */