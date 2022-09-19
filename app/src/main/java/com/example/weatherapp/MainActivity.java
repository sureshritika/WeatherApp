package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    EditText zipcode;
    Button search;
    static TextView quote;
    static TextView date, city, temp, des;
    static TextView time2, time3, time4, time5;
    static TextView high2, high3, high4, high5;
    static ImageView image , image2, image3, image4, image5;
    static TextView low2, low3, low4, low5;
    static String zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipcode = findViewById(R.id.id_zipcode);
        search = findViewById(R.id.id_search);
        quote = findViewById(R.id.id_quote);
        date = findViewById(R.id.id_date);
        image = findViewById(R.id.id_image);
        city = findViewById(R.id.id_city);
        temp = findViewById(R.id.id_temp);
        des = findViewById(R.id.id_des);
        image2 = findViewById(R.id.id_image2);
        image3 = findViewById(R.id.id_image3);
        image4 = findViewById(R.id.id_image4);
        image5 = findViewById(R.id.id_image5);
        time2 = findViewById(R.id.id_time2);
        time3 = findViewById(R.id.id_time3);
        time4 = findViewById(R.id.id_time4);
        time5 = findViewById(R.id.id_time5);
        low2 = findViewById(R.id.id_low2);
        low3 = findViewById(R.id.id_low3);
        low4 = findViewById(R.id.id_low4);
        low5 = findViewById(R.id.id_low5);
        high2 = findViewById(R.id.id_high2);
        high3 = findViewById(R.id.id_high3);
        high4 = findViewById(R.id.id_high4);
        high5 = findViewById(R.id.id_high5);

        zip = "08824";
        new AsyncThread().execute(zip);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zip = zipcode.getText().toString();  //STRING USED
                new AsyncThread().execute(zip);
                zipcode.setText("");
            }
        });

    }

    public class AsyncThread extends AsyncTask<String, Void, String> {

        HttpURLConnection connection;
        BufferedReader reader;
        String result = "";

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL("https", "api.openweathermap.org", "/data/2.5/forecast?zip=" + zip + "&appid=f23386cecbeda7c2598fd2b5fdea93b9");
                Log.d("RITIKA" , "url: " + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String string = "";
                while (string != null) {
                    string = reader.readLine();
                    result = result + string;
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object1 = new JSONObject(s);
                mainWeather(object1);
                weather2(object1);
                weather3(object1);
                weather4(object1);
                weather5(object1);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void mainWeather (JSONObject object){
        try{
            String one = object.getString("list");
            JSONArray array1 = new JSONArray(one);

            JSONObject object2 = array1.getJSONObject(0);

            String two = object2.getString("weather");
            JSONArray array2 = new JSONArray(two);
            JSONObject object3 = array2.getJSONObject(0);
            String main = object3.getString("main");
            String description = object3.getString("description");

            JSONObject object4 = object2.getJSONObject("main");
            double tempK = Double.parseDouble(object4.getString("temp"));
            double tempF = tempK * 1.8 - 459.67;

            JSONObject object5 = object.getJSONObject("city");
            String place = object5.getString("name");
            String dt = object2.getString("dt_txt");
            int time = dt.indexOf(' ');
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
            df2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            Date date1 = df.parse(dt.substring(0, time));
            String output = df2.format(date1);

            DateFormat df3 = new SimpleDateFormat("HH:mm");
            df3.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date0 = df3.parse(dt.substring(time+1, dt.length()-3));
            DateFormat df4 = new SimpleDateFormat("hh:mm aa");
            df4.setTimeZone(TimeZone.getTimeZone("EST"));
            String output0 = df4.format(date0);

            des.setText(main + ": " + description);
            temp.setText(String.format("%.2f", tempF) + (char) 0x00B0);
            city.setText(place);
            date.setText(output);

            setImage((new JSONArray(object2.getString("weather")).getJSONObject(0)).getString("description"), image, output);

            setQuote(description, output0);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void weather2 (JSONObject object){
        try {
            String one = object.getString("list");
            JSONArray array1 = new JSONArray(one);

            JSONObject object2 = array1.getJSONObject(1);

            JSONObject object3 = object2.getJSONObject("main");
            double minK = Double.parseDouble(object3.getString("temp_min"));
            double minF = minK * 1.8 - 459.67;
            double maxK = Double.parseDouble(object3.getString("temp_max"));
            double maxF = maxK * 1.8 - 459.67;
            String dt = object2.getString("dt_txt");
            int time = dt.indexOf(' ');
            DateFormat df = new SimpleDateFormat("HH:mm");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dt.substring(time+1, dt.length()-3));
            DateFormat df2 = new SimpleDateFormat("hh:mm aa");
            df2.setTimeZone(TimeZone.getTimeZone("EST"));
            String output = df2.format(date);

            setImage((new JSONArray(object2.getString("weather")).getJSONObject(0)).getString("description"), image2, output);

            time2.setText(output);
            high2.setText(String.format("%.2f", maxF) + (char) 0x00B0);
            low2.setText(String.format("%.2f", minF) + (char) 0x00B0);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void weather3 (JSONObject object){
        try {
            String one = object.getString("list");
            JSONArray array1 = new JSONArray(one);

            JSONObject object2 = array1.getJSONObject(2);

            JSONObject object3 = object2.getJSONObject("main");
            double minK = Double.parseDouble(object3.getString("temp_min"));
            double minF = minK * 1.8 - 459.67;
            double maxK = Double.parseDouble(object3.getString("temp_max"));
            double maxF = maxK * 1.8 - 459.67;
            String dt = object2.getString("dt_txt");
            int time = dt.indexOf(' ');
            DateFormat df = new SimpleDateFormat("HH:mm");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dt.substring(time+1, dt.length()-3));
            DateFormat df2 = new SimpleDateFormat("hh:mm aa");
            df2.setTimeZone(TimeZone.getTimeZone("EST"));
            String output = df2.format(date);

            setImage((new JSONArray(object2.getString("weather")).getJSONObject(0)).getString("description"), image3, output);

            time3.setText(output);
            high3.setText(String.format("%.2f", maxF) + (char) 0x00B0);
            low3.setText(String.format("%.2f", minF) + (char) 0x00B0);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void weather4 (JSONObject object){
        try {
            String one = object.getString("list");
            JSONArray array1 = new JSONArray(one);

            JSONObject object2 = array1.getJSONObject(3);

            JSONObject object3 = object2.getJSONObject("main");
            double minK = Double.parseDouble(object3.getString("temp_min"));
            double minF = minK * 1.8 - 459.67;
            double maxK = Double.parseDouble(object3.getString("temp_max"));
            double maxF = maxK * 1.8 - 459.67;
            String dt = object2.getString("dt_txt");
            int time = dt.indexOf(' ');
            DateFormat df = new SimpleDateFormat("HH:mm");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dt.substring(time+1, dt.length()-3));
            DateFormat df2 = new SimpleDateFormat("hh:mm aa");
            df2.setTimeZone(TimeZone.getTimeZone("EST"));
            String output = df2.format(date);

            setImage((new JSONArray(object2.getString("weather")).getJSONObject(0)).getString("description"), image4, output);


            time4.setText(output);
            high4.setText(String.format("%.2f", maxF) + (char) 0x00B0);
            low4.setText(String.format("%.2f", minF) + (char) 0x00B0);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void weather5 (JSONObject object){
        try {
            String one = object.getString("list");
            JSONArray array1 = new JSONArray(one);

            JSONObject object2 = array1.getJSONObject(4);

            JSONObject object3 = object2.getJSONObject("main");
            double minK = Double.parseDouble(object3.getString("temp_min"));
            double minF = minK * 1.8 - 459.67;
            double maxK = Double.parseDouble(object3.getString("temp_max"));
            double maxF = maxK * 1.8 - 459.67;
            String dt = object2.getString("dt_txt");
            int time = dt.indexOf(' ');
            DateFormat df = new SimpleDateFormat("HH:mm");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dt.substring(time+1, dt.length()-3));
            DateFormat df2 = new SimpleDateFormat("hh:mm aa");
            df2.setTimeZone(TimeZone.getTimeZone("EST"));
            String output = df2.format(date);

            setImage((new JSONArray(object2.getString("weather")).getJSONObject(0)).getString("description"), image5, output);

            time5.setText(output);
            high5.setText(String.format("%.2f", maxF) + (char) 0x00B0);
            low5.setText(String.format("%.2f", minF) + (char) 0x00B0);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void setImage (String des, ImageView image, String time){
        if (des.equals("clear sky")) {
            if ((time.substring(time.length() - 2).equals("AM") && Integer.valueOf(time.substring(0, 2)) > 6) || (time.substring(time.length() - 2).equals("PM") && Integer.valueOf(time.substring(0, 2)) < 6)) {
                Glide.with(this).load(R.drawable.sun).into(image);
            }
            else {
                Glide.with(this).load(R.drawable.nightclear).into(image);
            }
        }

        if (des.equals("light rain") || des.equals("shower rain") || des.equals("mist")) {
            Glide.with(this).load(R.drawable.lightrain).into(image);
        }

        if (des.equals("moderate rain")) {
            Glide.with(this).load(R.drawable.moderaterain).into(image);
        }

        if (des.equals("heavy intensity rain")) {
            Glide.with(this).load(R.drawable.heavyrain).into(image);
        }

        if (des.contains("cloud")) {
            if ((time.substring(time.length() - 2).equals("AM") && Integer.valueOf(time.substring(0, 2)) > 6) || (time.substring(time.length() - 2).equals("PM") && Integer.valueOf(time.substring(0, 2)) < 6)) {
                Glide.with(this).load(R.drawable.daycloud).into(image);
            }
            else {
                Glide.with(this).load(R.drawable.nightcloud).into(image);
            }
        }

        if (des.contains("snow")) {
            Glide.with(this).load(R.drawable.snow).into(image);
        }

        if (des.contains("storm")) {
            if ((time.substring(time.length() - 2).equals("AM") && Integer.valueOf(time.substring(0, 2)) > 6) || (time.substring(time.length() - 2).equals("PM") && Integer.valueOf(time.substring(0, 2)) < 6)) {
                Glide.with(this).load(R.drawable.daystorm).into(image);
            }
            else {
                Glide.with(this).load(R.drawable.nightstorm).into(image);
            }
        }

    }

    public void setQuote(String des, String time){
        if (des.equals("clear sky")) {
            if ((time.substring(time.length() - 2).equals("AM") && Integer.valueOf(time.substring(0, 2)) > 6) || (time.substring(time.length() - 2).equals("PM") && Integer.valueOf(time.substring(0, 2)) <= 6)) {
                quote.setText("happiness can be found in the darkest of times, if one only remembers to turn on the light");
            }
            else {
                quote.setText("let us step out into the night and pursue that flighty temptress, adventure");
            }
        }
        if (des.equals("light rain") || des.equals("shower rain") || des.equals("mist")) {
            quote.setText("a few showers will Slytherin");
        }
        if (des.equals("moderate rain")) {
            quote.setText("we will see some some Cumulus Nimbus 2000 foot above us so hurry up, get your umbrellas, you don't want to be caught out");
        }
        if (des.equals("heavy intensity rain")) {
            quote.setText("we will see some some Cumulus Nimbus 2000 foot above us so hurry up, get your umbrellas, you don't want to be caught out");
        }
        if (des.equals("overcast clouds")) {
            quote.setText("we will see a few Sirius Black clouds rolling in");
        }
        if (des.equals("scattered clouds") || des.equals("few clouds") || des.equals("broken clouds")) {
            quote.setText("we will see a few Sirius Black clouds rolling in");
        }
        if (des.equals("light snow") || des.equals("snow")) {
            quote.setText("it's not quite summer yet, you know, so you just need to hang on if you Lovegood weather");
        }
        if (des.equals("thunderstorm")) {
            quote.setText("There’s a storm coming. And we all best be ready when she does.");

        }
    }



}