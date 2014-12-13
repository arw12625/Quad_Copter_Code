/*
 * Vector.h
 *
 * Created: 12/7/2014 2:46:58 PM
 *  Author: Andy
 */ 


#ifndef VECTOR_H_
#define VECTOR_H_


float Vector_Dot_Product(float vector1[3],float vector2[3]);
void Vector_Cross_Product(float vectorOut[3], float v1[3],float v2[3]);
void Vector_Scale(float vectorOut[3],float vectorIn[3], float scale2);
void Vector_Add(float vectorOut[3],float vectorIn1[3], float vectorIn2[3]);


#endif /* VECTOR_H_ */