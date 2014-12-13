/*
 * DCM.h
 *
 * Created: 12/7/2014 2:40:20 PM
 *  Author: Andy
 */ 


#ifndef DCM_H_
#define DCM_H_

#include "MinIMU9AHRS.h"
void Normalize(void);
void Drift_correction(void);
void Matrix_update(void);
void Euler_angles(void);

#endif /* DCM_H_ */