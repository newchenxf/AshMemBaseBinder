#include <jni.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <stdlib.h>
#include <stdio.h>
#include <cstring>
#include <android/log.h>
#include <errno.h>

/**
 * ref from https://github.com/dev-area/ashmem/blob/master/TestAshmem/app/src/main/cpp/native-lib.cpp
 */

const char *LOG_TAG = "chenxf#ShmLib";

char *shmWriteAddr;
bool hasDoMap = false;


extern "C"
JNIEXPORT jint JNICALL
Java_com_chenxf_ashmemclient_ShmLib_sendNativeBuf(JNIEnv *env, jclass clazz, jint shmFd, jint shmSize,
                                                  jbyteArray bytearray) {

    jbyte *bytes;
    bytes = env->GetByteArrayElements(bytearray, 0);

    int bufferLen = env->GetArrayLength(bytearray);

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "buffer bufferLen %d, shmFd %d, shmSize %d", bufferLen, shmFd, shmSize);

    if(!hasDoMap) {
        shmWriteAddr = (char *) mmap(0, shmSize, PROT_READ | PROT_WRITE, MAP_SHARED, shmFd, 0);
        if (shmWriteAddr == (void *) -1) {
            __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "do mmap fail %s", strerror(errno));
            return -1;
        }
    }
    hasDoMap = true;

    int maxCpySize = bufferLen > shmSize ? shmSize : bufferLen;

    memcpy(shmWriteAddr, bytes, maxCpySize);
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "finish memcpy");

    env->ReleaseByteArrayElements(bytearray, bytes, 0);
    return 0;
}