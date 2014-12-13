int pushButton = 7;

void setup() {
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
  // make the pushbutton's pin an input:
  pinMode(pushButton, INPUT);
}

// the loop routine runs over and over again forever:
int modulus = 128;
int buttonState;
void loop() {
  // read the input pin:
  int inc = digitalRead(pushButton);
  buttonState += inc;
  buttonState %= modulus;
  // print out the state of the button:
  Serial.println(buttonState);
  delay(1);        // delay in between reads for stability
}
