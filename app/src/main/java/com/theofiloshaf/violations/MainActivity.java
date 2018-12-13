package com.theofiloshaf.violations;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.design.widget.Snackbar;


public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /*
       Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        */

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);

        violationCheck();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            violationCheck();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void violationCheck(){
        if (!internetConnection()){
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            Snackbar.make(findViewById(android.R.id.content), "Δεν έχετε συνδεθεί στο Internet",Snackbar.LENGTH_LONG).show();
        }
        else{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Document site = null;
            try {
                site = Jsoup.connect("http://www.geetha.mil.gr/el/violations-gr/2015-01-22-11-43-23.html").timeout(6000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element table = Objects.requireNonNull(site).select("table").get(1);
            Elements rows = table.select("tr");
            ArrayList<ViolationEvent> violations = new ArrayList<>();


            String curDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


            for (int i = 2; i < rows.size(); i++) { //first 2 rows are the table title names.
                Elements row = rows.get(i).select("td");

                ViolationEvent violation = ViolationEvent.getViolationEvent(row);
                violations.add(violation);

            }

            String date = violations.get(0).getDate();

            TextView status = findViewById(R.id.newStatus);
            TextView newViolationInfo = findViewById(R.id.newViolationInfo);

            boolean violated = false;
            if (date.equals(curDate)) {
                status.setText("Σήμερα παρουσιάστηκε παραβίαση");
                notificationCreate();
                violated = true;
            } else
                status.setText("Δεν παρουσιάστηκε παραβίαση σήμερα");
            newViolationInfo.setText("Τελευταία παραβίαση: " + violations.get(0).getDate());

            if (violated) {
                String all = violations.get(0).toString();
                newViolationInfo.setText("Στοιχεία παραβίασης:\n\n" + all);
            }
        }

    }

    boolean internetConnection(){
        //method to check if the device is connected to the network
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void notificationCreate(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker("test")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Νέο συμβάν παραβίασης")
                .setContentText("Περισσότερες λεπτομέρειες")
                .setContentInfo("Info");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
        vibrationGenerate();
    }

    private void vibrationGenerate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}
