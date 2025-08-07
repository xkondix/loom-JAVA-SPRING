#include <jni.h>
#include <stdio.h>
#include <windows.h>

JNIEXPORT void JNICALL Java_com_kowalczyk_konrad_loom_virtual_pinning_PinningJNI_nativeBlock(JNIEnv *env, jclass clazz, jobject lockObj) {
    printf("Entering nativeBlock 3\n");
    (*env)->MonitorEnter(env, lockObj);
    Sleep(5);
    (*env)->MonitorExit(env, lockObj);
    printf("Exiting nativeBlock 3\n");
}
