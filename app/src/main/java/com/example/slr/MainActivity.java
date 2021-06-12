package com.example.slr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_PERMISSION_CODE = 100;

    Camera camera;
    ShowCamera showCamera;
    TextView textView;
    FrameLayout frameLayout;
    Button capture;
    Bitmap bitmap;
    Model model;
    ImageView sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        //capture = (Button) findViewById(R.id.capture);
        textView =(TextView) findViewById(R.id.textView2);
        textView.setText("");
        sign = (ImageView) findViewById(R.id.imageView3);
        model = new Model();
        //Open Camera
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        camera = camera.open();
        showCamera=new ShowCamera(this,camera);
        frameLayout.addView(showCamera);
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    Camera.PictureCallback nPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

               // for(int i=0;i<data.length;i++)
                 //   System.out.println(data[i]);
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                bitmap = model.rotateImage(bitmap,90);
               // sign.setImageBitmap(bitmap);
                textView.setText(model.predict(bitmap,getApplicationContext()));
                camera.startPreview();

        }
    };

    public void captureImage(View v){
        if(camera!=null){
            camera.takePicture(null,null,nPictureCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopPreview();
    }


}
