#include <jni.h>
#include <stdio.h>
#include <unistd.h>

JNIEXPORT void JNICALL Java_com_kowalczyk_konrad_loom_virtual_pinning_PinningJNI_nativeSleep(JNIEnv *, jclass) {
     printf("In nativeSleep 1\n");
     sleep(5);
     printf("Leaving nativeSleep 1\n");
}


