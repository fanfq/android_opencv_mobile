package com.example.android_opencv_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_opencv_mobile.databinding.ActivityMainBinding;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'android_opencv_mobile' library on application startup.
    static {
        System.loadLibrary("android_opencv_mobile");
        //System.loadLibrary("OpenCV_LIBS");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        ImageView imageView = binding.sampleImg;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.tem);

        imageView.setImageBitmap(bitmap);

        ///灰度值
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixces = new int[w*h];
        bitmap.getPixels(pixces, 0, w, 0, 0, w, h);
        int[] rest = bitmap2Gray(pixces,w,h);
        Bitmap _b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        _b.setPixels(rest, 0, w, 0, 0, w, h);
        imageView.setImageBitmap(_b);
    }

    /**
     * A native method that is implemented by the 'android_opencv_mobile' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    public native int[] bitmap2Gray(int[] pixels,int w,int h);
}