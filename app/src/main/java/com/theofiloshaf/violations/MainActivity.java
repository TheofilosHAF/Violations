package com.theofiloshaf.violations;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import android.support.design.widget.Snackbar;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (!internet_connection()){
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Δεν υπάρχει σύνδεση στο Internet", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("Δοκιμή ξανά", new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (!internet_connection()){
                        Snackbar.make(findViewById(android.R.id.content), "Δεν έχετε συνδεθεί στο Internet",Snackbar.LENGTH_INDEFINITE).show();
                    }
                }
            }).show();
        }
        else {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Document site = null;
            try {
                site = Jsoup.connect("http://www.geetha.mil.gr/el/violations-gr/2015-01-22-11-43-23.html").timeout(6000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element table = site.select("table").get(1);
            Elements rows = table.select("tr");
            ArrayList<ViolationEvent> violations = new ArrayList<>();


            String curDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


            for (int i = 2; i < rows.size(); i++) { //first 2 rows are the table title names.
                Elements row = rows.get(i).select("td");

                ViolationEvent violation = getViolationEvent(row);
                violations.add(violation);

            }

            String date = violations.get(0).getDate();

            TextView status = (TextView) findViewById(R.id.newStatus);

            boolean violated = false;
            if (date.equals(curDate)) {
                status.setText("Σήμερα παρουσιάστηκε παραβίαση");
                violated = true;
            } else
                status.setText("Δεν παρουσιάστηκε παραβίαση σήμερα");

            if (violated) {
                TextView newViolationInfo = (TextView) findViewById(R.id.newViolationInfo);
                String all = violations.get(0).toString();
                newViolationInfo.setText("Στοιχεία παραβίασης:\n\n" + all);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static ViolationEvent getViolationEvent(Elements row) {
        int no = 0;
        if (!row.get(0).text().equals("-"))
            no = Integer.parseInt(row.get(0).text());

        String date = row.get(1).text();

        int formation = 0;
        if (!row.get(2).text().equals("-"))
            formation = Integer.parseInt(row.get(2).text());

        int jets = 0;
        if (!row.get(3).text().equals("-"))
            jets = Integer.parseInt(row.get(3).text());

        int armed = 0;
        if (!row.get(4).text().equals("-"))
            armed = Integer.parseInt(row.get(4).text());

        int cn235 = 0;
        if (!row.get(6).text().equals("-"))
            cn235 = Integer.parseInt(row.get(6).text());

        int helis = 0;
        if (!row.get(7).text().equals("-"))
            helis = Integer.parseInt(row.get(7).text());

        int totalPlanes = 0;
        if (!row.get(8).text().equals("-"))
            totalPlanes = Integer.parseInt(row.get(8).text());

        int atrViol = 0;
        if (!row.get(9).text().equals("-"))
            atrViol = Integer.parseInt(row.get(9).text());

        int naViol = 0;
        if (!row.get(10).text().equals("-"))
            naViol = Integer.parseInt(row.get(10).text());

        int dogfights = 0;
        if (!row.get(11).text().equals("-"))
            dogfights = Integer.parseInt(row.get(11).text());

        int overflight = 0;
        if (!row.get(12).text().equals("-"))
            overflight = Integer.parseInt(row.get(12).text());

        String location = row.get(13).text();

        return new ViolationEvent(no, date, formation, jets, armed, cn235, helis, totalPlanes, atrViol, naViol, dogfights, overflight, location);
    }


    boolean internet_connection(){
        //method to check if the device is connected to the network
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
