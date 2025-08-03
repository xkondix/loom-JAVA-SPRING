#include <jni.h>
#include <stdio.h>
#include <unistd.h>

JNIEXPORT void JNICALL Java_com_kowalczyk_konrad_loom_virtual_pinning_PinningJNI_nativeSleep2(JNIEnv *env, jclass clazz) {
    printf("In nativeSleep 2 \n");

    jbyteArray byteArray = (*env)->NewByteArray(env, 1024 * 1024);

    jboolean isCopy;
    jbyte* ptr = (*env)->GetPrimitiveArrayCritical(env, byteArray, &isCopy);

    for (int i = 0; i < 5; i++) {
        printf("Sleeping  %d\n", i);
        sleep(2);
    }

    (*env)->ReleasePrimitiveArrayCritical(env, byteArray, ptr, 0);

    printf("Leaving nativeSleep 2 \n");

}
