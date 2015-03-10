/*
 * Control.h
 *
 * Created: 1/12/2015 10:38:57 PM
 *  Author: Andy
 */ 


#ifndef CONTROL_H_
#define CONTROL_H_

void init_control(void);
void update_control(void);


typedef float mat4 [4][4];

void matrix4_multiply(float a[4][4], float b[4][4],float mat[4][4]);
void matrix4_scale(mat4 a, float scale ,mat4 mat);
void diagonal_matrix(float k1, float k2, float k3, float k4, mat4 mat);
float dot(float a[4], float b[4]);

void setEle_kp(int k);
void setPitch_kp(int k);
void setYaw_kp(int k);
void setRoll_kp(int k);

void toggleEnabled(void);
void toggleMotorDebug(void);

#endif /* CONTROL_H_ */