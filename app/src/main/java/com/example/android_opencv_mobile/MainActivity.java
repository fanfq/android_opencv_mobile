package com.example.android_opencv_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_opencv_mobile.databinding.ActivityMainBinding;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'android_opencv_mobile' library on application startup.
    static {
        System.loadLibrary("android_opencv_mobile");
        //System.loadLibrary("OpenCV_LIBS");
    }

    private ActivityMainBinding binding;
    ImageView ivSrc;
    Bitmap bmpSrc;
    ImageView ivDst;
    Bitmap bmpDst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        TextView tvOutput = binding.txtOutput;


        ivSrc = binding.imgSrc;
        bmpSrc = BitmapFactory.decodeResource(getResources(),R.mipmap.tem);
        ivSrc.setImageBitmap(bmpSrc);

        bmpDst = bmpSrc.copy(bmpSrc.getConfig(), true);
        ivDst = binding.imgDst;


        ivSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launches photo picker in single-select mode.
                // This means that the user can select one photo or video.
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

        Button btnBitmap2Threshold = binding.bitmap2Threshold;
        Button btnBitmap2Gray = binding.bitmap2Gray;
        Button btnBitmap2GaussianBlur = binding.bitmap2GaussianBlur;
        btnBitmap2Threshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long st = System.currentTimeMillis();

                ///二值化
                bmpDst = bmpSrc.copy(bmpSrc.getConfig(),true);

                int w = bmpDst.getWidth();
                int h = bmpDst.getHeight();
                int[] pixels = new int[w*h];
                bmpDst.getPixels(pixels, 0, w, 0, 0, w, h);
                int[] retData = bitmap2Threshold(pixels,w,h);
                Bitmap retBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                retBitmap.setPixels(retData, 0, w, 0, 0, w, h);
                ivDst.setImageBitmap(retBitmap);


                String cost = "cost:"+ (System.currentTimeMillis() - st) /1000.0 +" s";
                tvOutput.setText(cost );
            }
        });

        btnBitmap2Gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long st = System.currentTimeMillis();
                ///灰度值
                bmpDst = bmpSrc.copy(bmpSrc.getConfig(),true);

                int w = bmpDst.getWidth();
                int h = bmpDst.getHeight();
                int[] pixels = new int[w*h];
                bmpDst.getPixels(pixels, 0, w, 0, 0, w, h);
                int[] retData = bitmap2Gray(pixels,w,h);
                Bitmap retBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                retBitmap.setPixels(retData, 0, w, 0, 0, w, h);
                ivDst.setImageBitmap(retBitmap);


                String cost = "cost:"+ (System.currentTimeMillis() - st) /1000.0 +" s";
                tvOutput.setText(cost );
            }
        });

        btnBitmap2GaussianBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long st = System.currentTimeMillis();
                bmpDst = bmpSrc.copy(bmpSrc.getConfig(),true);
                //高斯模糊
                bitmap2GaussianBlur(bmpDst);
                ivDst.setImageBitmap(bmpDst);

                String cost = "cost:"+ (System.currentTimeMillis() - st) /1000.0 +" s";
                tvOutput.setText(cost );
            }
        });


    }

    /**
     * A native method that is implemented by the 'android_opencv_mobile' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    /**
     * 灰度值
     * @param pixels
     * @param w
     * @param h
     * @return
     */
    public native int[] bitmap2Gray(int[] pixels,int w,int h);


    /**
     * 高斯模糊
     * @param bitmap
     * @return
     */
    public native void bitmap2GaussianBlur(Object bitmap);

    /**
     * 二值化
     * @param pixels
     * @param w
     * @param h
     * @return
     */
    public native int[] bitmap2Threshold(int[] pixels,int w,int h);












    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            Log.e(this.getClass().getName(), "Result:" + data.toString());
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                //ivSrc.setImageURI(uri);
                try {
                    bmpSrc = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivSrc.setImageBitmap(bmpSrc);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Log.e(this.getClass().getName(), "Uri:" + String.valueOf(uri));
            }
        }
    }


}