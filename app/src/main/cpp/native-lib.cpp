#include <jni.h>
#include <string>

#include<opencv2/opencv.hpp>
#include<iostream>
#include <opencv2/imgproc/types_c.h>
#include <unistd.h>

using namespace cv;
using namespace std;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_android_1opencv_1mobile_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jintArray JNICALL
Java_com_example_android_1opencv_1mobile_MainActivity_bitmap2Gray(JNIEnv *env, jobject instance, jintArray pixels, jint w, jint h) {
    jint *cur_array;

    jboolean isCopy = static_cast<jboolean> (false);

    cur_array = env-> GetIntArrayElements(pixels, &isCopy);
    if (cur_array == NULL) {
        return 0;
    }

    Mat img(h, w, CV_8UC4, (unsigned char *) cur_array);

    cvtColor(img, img, CV_BGRA2GRAY);
    cvtColor(img, img, CV_GRAY2BGRA);

    int size = w * h;
    jintArray result = env->NewIntArray(size);
    env-> SetIntArrayRegion(result, 0, size, (jint *) img.data);
    env-> ReleaseIntArrayElements(pixels, cur_array, 0);
    return result;
}

