/*
* Control.cpp
*
* Created: 1/12/2015 10:38:26 PM
*  Author: Andy
*/
#include "Control.h"
#include "InertialSensorManager.h"
#include "Motor/MotorManager.h"
#include "Constants.h"

//mat4 const_matrix;
mat4 transform_matrix;

mat4 solution_matrix  = {{1,-1,1,-1},
{1,1,-1,-1},
{1,-1,-1,1},
{1,1,1,1}};
//the proportional constants
float ele_kp = 1;
float pitch_kp = 1;
float yaw_kp = 1;
float roll_kp = 1;

bool enabled = false;
bool motorDebug = false;

Orientation setPoint;

void updateTransform(void) {
	
	//The matrix specifying the gains for control
	mat4 gain_matrix;
	diagonal_matrix(ele_kp, pitch_kp, yaw_kp, roll_kp, gain_matrix);
	matrix4_multiply(solution_matrix, gain_matrix, transform_matrix);
	
	Serial.println("Transform Matrix");
	for(int i = 0; i < 4; i++) {
		for(int j = 0; j < 4; j++) {
			Serial.print(transform_matrix[i][j]);
			Serial.print(" ");
		}
		Serial.println();
	}
}

void init_control(void) {
	
	//diagonal_matrix(mass, Ix / x, Iy / y, Iz, const_matrix);
	//matrix4_multiply(const_matrix, transform_matrix, transform_matrix);
	
	updateTransform();
	
	setPoint.named.pitch = 0;
	setPoint.named.yaw = 0;
	setPoint.named.roll = 0;
	
}


int counnnnnt = 0;
void update_control(void) {
	
	float err_pitch = setPoint.named.pitch - eulerOr.named.pitch;
	float err_yaw = setPoint.named.yaw - eulerOr.named.yaw;
	float err_roll = setPoint.named.roll - eulerOr.named.roll;
	float err_ele = 1;
	
	float err[4] = {err_ele, err_pitch, err_yaw, err_roll};
	
	float thrust[4];
	for(int i = 0; i < 4; i++) {
		thrust[i] = dot(transform_matrix[i],err);
		
		thrust[i] += 20;
		if(thrust[i] > 60) {
			thrust[i] = 60;
		}
		if(thrust[i] < 20) {
			thrust[i] = 20;
		}
		
		if(counnnnnt == 0) {
			if(motorDebug) {
				for(int i = 0; i < NUM_MOTORS; i++) {
					Serial.print("Motor");
					Serial.print(i);
					Serial.print(": ");
					Serial.print(motors.numbered[i]->value);
					Serial.print("\t");
				}
				Serial.println();
			}
		}

		if(counnnnnt++ > 50) {
			counnnnnt = 0;
		}
		if(enabled) {
			for(int i = 0; i < 4; i++) {
				write_motor(motors.numbered[i], (int)thrust[i]);
			}

		}
	}
}

void matrix4_multiply(mat4 a, mat4 b,mat4 mat)
{
	float op[4];
	for(int i=0; i<4; i++)
	{
		for(int j=0; j<4; j++)
		{
			for(int k=0; k<4; k++)
			{
				op[k]=a[i][k]*b[k][j];
			}
			mat[i][j]=op[0]+op[1]+op[2]+op[3];
			
		}
	}
}

void matrix4_scale(mat4 a, float scale ,mat4 mat)
{
	for(int i=0; i<4; i++)
	{
		for(int j=0; j<4; j++)
		{
			mat[i][j] = scale * a[i][j];
		}
	}
}

void diagonal_matrix(float k1, float k2, float k3, float k4, mat4 mat)
{
	float constants[] = {k1, k2, k3, k4};
	int constant_index = 0;
	for (int i=0; i<4; i++) {
		for (int j = 0; j<4; j++)
		if (i == j) {
			mat[i][j] = constants[constant_index++];
			} else {
			mat[i][j] = 0;
		}

	}
	
}

float dot(float a[4], float b[4])  {
	float val = 0;
	for(int i=0; i<4; i++)
	{
		val += a[i] * b[i];
	}
	return val;
}

void setEle_kp(int k) {
	ele_kp = k / 5.0;
	updateTransform();
}
void setPitch_kp(int k) {
	pitch_kp = k / 100.0;
	updateTransform();
}
void setYaw_kp(int k) {
	yaw_kp = k / 25.0;
	updateTransform();
}
void setRoll_kp(int k) {
	roll_kp = k / 100.0;
	updateTransform();
}

void toggleMotorDebug() {
	motorDebug = !motorDebug;
	
	if(motorDebug) {
		Serial.println("Motor Debug Enabled");
		} else {
		Serial.println("Motor Debug Disabled");
	}
}

void toggleEnabled() {
	enabled = !enabled;
	if(enabled) {
		Serial.println("Control Enabled");
		} else {
		Serial.println("Control Disabled");
	}
}