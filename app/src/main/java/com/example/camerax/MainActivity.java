package com.example.camerax;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;




public class MainActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private Preview preview;
    private ImageCapture imageCapture;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previewView = findViewById(R.id.viewFinder);


        if(allPermissionsGranted()){
            startCamera();
            Toast.makeText(this, "camera started", Toast.LENGTH_SHORT).show();


        }
        else{
            ActivityCompat.requestPermissions(this,REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            Toast.makeText(this, "request permission", Toast.LENGTH_SHORT).show();
        }
    }
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

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();
                preview = new Preview.Builder()
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
                b=(Button) findViewById(R.id.camera_capture_button);
                imageCapture=new ImageCapture.Builder().build();
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
                        File f = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");
                        ImageCapture.OutputFileOptions outputFileOptions =
                                new ImageCapture.OutputFileOptions.Builder(f).build();
                        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(MainActivity.this), new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Toast.makeText(MainActivity.this,"saved",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                Toast.makeText(MainActivity.this,String.valueOf(exception),Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                });



                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,imageCapture, preview);
                preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.getCameraInfo()));

            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));


    }

    private boolean allPermissionsGranted() {
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}

