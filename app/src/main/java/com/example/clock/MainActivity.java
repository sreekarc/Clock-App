package com.example.clock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Bitmap myImg;
    Matrix matrix;
    Bitmap rotated;
    int hoursdegree;
    int mindegree;
    int secdegree;
    ImageView secImage;
    ImageView minImage;
    ImageView hoursImage;
    public Handler updateUIHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secImage = findViewById(R.id.seconds);
        minImage = findViewById(R.id.minutes);
        hoursImage = findViewById(R.id.hours);

        new Thread(new handsThread()).start();

        createUpdateUiHandler();
    }

    public void createUpdateUiHandler() {
        if(updateUIHandler == null) {
            updateUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 1) {
                        myImg = BitmapFactory.decodeResource(getResources(), R.drawable.seconds);
                        matrix = new Matrix();
                        matrix.postRotate(secdegree);
                        rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(), matrix, true);
                        secImage.setImageBitmap(rotated);
                    }
                    else if(msg.what == 2) {
                        myImg = BitmapFactory.decodeResource(getResources(), R.drawable.minutes);
                        matrix = new Matrix();
                        matrix.postRotate(mindegree);
                        rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(), matrix, true);
                        minImage.setImageBitmap(rotated);
                    }
                    else if(msg.what == 3) {
                        myImg = BitmapFactory.decodeResource(getResources(), R.drawable.hours);
                        matrix = new Matrix();
                        matrix.postRotate(hoursdegree);
                        rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(), matrix, true);
                        hoursImage.setImageBitmap(rotated);
                    }
                }
            };
        }
    }

    class handsThread implements Runnable{
        public void run(){
            int oldSecDegree = 10000;
            int oldMinDegree = 10000;
            int oldHourDegree = 10000;
            Calendar cal;
            Date date;
            DateFormat dateFormat;
            String seconds;
            String minutes;
            String hours;

            while (true) {
                cal = Calendar.getInstance();
                date = cal.getTime();

                dateFormat = new SimpleDateFormat("ss");
                seconds = dateFormat.format(date);
                secdegree = (Integer.parseInt(seconds) + 1) * 6;

                dateFormat = new SimpleDateFormat("mm");
                minutes = dateFormat.format(date);
                mindegree = (Integer.parseInt(minutes) + 1) * 6;

                dateFormat = new SimpleDateFormat("hh");
                hours = dateFormat.format(date);
                hoursdegree = (Integer.parseInt(hours)) * 30;

                if (secdegree != oldSecDegree) {
                    Log.d("angle", Integer.toString(secdegree));
                    Message message = new Message();
                    message.what = 1;
                    updateUIHandler.sendMessage(message);
                }

                if (mindegree != oldMinDegree) {
                    Log.d("angle", Integer.toString(secdegree));
                    Message message = new Message();
                    message.what = 2;
                    updateUIHandler.sendMessage(message);
                }

                if (hoursdegree != oldHourDegree) {
                    Log.d("angle", Integer.toString(secdegree));
                    Message message = new Message();
                    message.what = 3;
                    updateUIHandler.sendMessage(message);
                }

                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                oldSecDegree = secdegree;
                oldMinDegree = mindegree;
                oldHourDegree = hoursdegree;
            }
        }
    }
}
