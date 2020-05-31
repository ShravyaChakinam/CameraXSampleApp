package com.example.camerax;
import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
//import androidx.camera.core.CameraX;
//import androidx.camera.core.ImageCapture;

import androidx.camera.core.Camera;
//import androidx.camera.core.CameraXConfig;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.camera.view.PreviewView.ImplementationMode;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.LifecycleOwner;



import android.content.pm.PackageManager;
import android.os.Bundle;
//import android.os.Environment;
//import android.util.Rational;
//import android.os.Environment;
import android.os.Handler;
//import android.util.Size;
//import android.view.Surface;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.View;
//import android.view.ViewGroup;
import android.view.TextureView;
//import android.view.ViewGroup;
//import android.view.TextureView;
//import android.view.View;
//import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

//import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import java.io.File;


public class MainActivity extends AppCompatActivity  {
   private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
   TextureView textureView;
    private ExecutorService cameraExecutor;
    private Handler handler;

   // Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
   // Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen
    private Preview preview;
    private  PreviewView previewView;
    private ImageAnalysis imageAnalysis;
    private ImageCapture imageCapture;
    //Preview preview= new Preview.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previewView = findViewById(R.id.view_finder);
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);
        //textureView = findViewById(R.id.view_finder);
        cameraExecutor= Executors.newSingleThreadExecutor();//unused
        handler=new Handler();//unused
        //startCamera();
       if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }
    //@Override
  /*  public CameraXConfig getCameraXConfig(){
        return Camera2Config.defaultConfig();
    }*/


    private void startCamera()  {

        ListenableFuture<ProcessCameraProvider> cameraProviderFeature= ProcessCameraProvider.getInstance(this);
        cameraProviderFeature.addListener(new Runnable(){
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFeature.get();
                    cameraProvider.unbindAll();
                    bindPreview(cameraProvider);
                }
                catch (ExecutionException | InterruptedException e){
                    // should not be reached
                }
            }
                     }, ContextCompat.getMainExecutor(this));
       //Camera camera=cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }
    public void bindPreview(@NonNull ProcessCameraProvider cameraProvider)
    {
       // previewView.setImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);
     preview = new Preview.Builder().build();
       // imageAnalysis = new ImageAnalysis.Builder()
         //       .build();

        //imageCapture = new ImageCapture.Builder()
          //      .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            //    .build();
     CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);
        preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.getCameraInfo()));
    }
       /* preview.setOnPreviewOutputUpdateListener(
             new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we  have to destroy it first then re-add it
                   @Override
               public void onUpdated(Preview.PreviewOutput output){
                     ViewGroup parent = (ViewGroup) textureView.getParent();
                    parent.removeView(textureView);
                       parent.addView(textureView, 0);
//
                  textureView.setSurfaceTexture(output.getSurfaceTexture());
                      updateTransform();
                 }
        //        });*/


        /*ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);*/
     /*       Button cameracapturebutton=findViewById(R.id.camera_capture_button);
            cameracapturebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");
                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        String msg = "Pic captured at " + file.getAbsolutePath();
                        Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed : " + message;
                        Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();
                        if(cause != null){
                            cause.printStackTrace();
                        }
                    }
                });
            }
        });

        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner)this, preview, imgCap);
    }
*/
    /*private void updateTransform(){
       // Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int)textureView.getRotation();

        switch(rotation){
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        //Matrix.postRotate((float)rotationDgr, cX, cY);
        //textureView.setTransform(Matrix);
    }
*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){

                    startCamera();

            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}






