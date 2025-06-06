#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_esraa_nayel_movieapp_Constants_getApiKey(
        JNIEnv *env,
        jobject this
) {
    return (*env)->NewStringUTF(env, "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMGU4MjZiYWQ3ZTBiZTllM2QxYWMzYTc3OGI1MzA2OCIsIm5iZiI6MTczODk0MDQyNS4xMTEsInN1YiI6IjY3YTYyMDA5NDkyYjM3OTI4Nzg1OThjZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._ckz8jkwRnwLx04Agm9K9xm_1_KC8LRx5IfCI8NBwic");
}
