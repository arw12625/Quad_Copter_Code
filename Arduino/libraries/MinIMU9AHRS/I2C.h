/*
 * I2C.h
 *
 * Created: 12/7/2014 2:43:05 PM
 *  Author: Andy
 */ 


#ifndef I2C_H_
#define I2C_H_

#include <MinIMU9AHRS.h>
#include <Wire.h>
#include <L3G.h>
#include <LSM303.h>


void I2C_Init(void);
void Gyro_Init(void);
void Read_Gyro(void);
void Accel_Init(void);
void Read_Accel(void);
void Compass_Init(void);
void Read_Compass(void);


#endif /* I2C_H_ */