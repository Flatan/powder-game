package powder;

//Example:

//ParticleGate[][] O2 = {
    //{TRUE,  null,  null},
	//{TRUE,  ____,  null},
	//{null,  null,  null},
//};


enum GateType {
  ALL, ANY
}

enum ParticleGate {
  // Use an enum instead of primitives to allow "null"
  TRUE, FALSE, ____
}

