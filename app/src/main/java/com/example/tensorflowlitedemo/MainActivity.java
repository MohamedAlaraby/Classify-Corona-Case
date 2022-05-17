package com.example.tensorflowlitedemo;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tensorflowlitedemo.ml.Tfmodel;
import com.google.android.material.button.MaterialButton;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
   MaterialButton camera,gallery;
    TextView result;
    ImageView imageView;
    int imageSize=224;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera=findViewById(R.id.button);
        gallery=findViewById(R.id.button2);
        imageView=findViewById(R.id.imageView);
        result=findViewById(R.id.result);
        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                  if (checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
                      Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                      startActivityForResult(cameraIntent, 3);
                  }else
                      requestPermissions(new String[]{Manifest.permission.CAMERA},100);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode==3){
                //to get the image from the camera
                Bitmap image= (Bitmap) data.getExtras().get("data");
                //to resize the bitmap
                int dimension=Math.min(image.getWidth(),image.getHeight());
                image= ThumbnailUtils.extractThumbnail(image,dimension,dimension);
                imageView.setImageBitmap(image);
                image=Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
                classifyImage(image);
            }else{
                //to get the image from the gallery
               Uri dat = data.getData();
               Bitmap image=null;
                try {
                    image=MediaStore.Images.Media.getBitmap(this.getContentResolver(),dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                image=Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void classifyImage(Bitmap image) {
        try {
            Tfmodel model = Tfmodel.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            //4>>no. of bytes that the float takes
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4*3*imageSize*imageSize);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues=new int[imageSize*imageSize];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            //iterate over each pixel and extract R,G ,and B values .add those values individually to thr byte buffer
            int pixel=0;
            for (int i=0;i<imageSize;i++){
                for (int j =0;j<imageSize;j++) {
                  int val =intValues[pixel++];//RGB
                    //USING bitwise operator to handle extracting the values
                  byteBuffer.putFloat(((val>>16)&0xFF) *(1.f/1));
                  byteBuffer.putFloat(((val>>8)&0xFF) *(1.f/1));
                  byteBuffer.putFloat((val&0xFF) *(1.f/1));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Tfmodel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float [] confidences=outputFeature0.getFloatArray();
            //find the index of the class with the biggest confidence
            int maxPos=0;
            float maxConfidence=0;
            for (int i=0;i<confidences.length;i++){
                if (confidences[i]>maxConfidence){
                    maxConfidence=confidences[i];
                    maxPos=i;
                 }
            }
            String [] classes={"COVID","NORMAl"};
            result.setText(confidences[0]*100+"\n"+confidences[1]*100+"\n"+classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}