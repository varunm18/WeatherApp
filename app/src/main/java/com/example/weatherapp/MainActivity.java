package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    TextView t1, city, mainDesc, mainStatus, quote, t11, t12, t21, t22, t31, t32, t41, t42;
    Button submit;
    EditText code;
    ImageView main, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncThread t = new AsyncThread();

        t1 = findViewById(R.id.text);
        city = findViewById(R.id.city);
        submit = findViewById(R.id.button);
        code = findViewById(R.id.editTextTextPersonName);
        main = findViewById(R.id.imageView);
        title = findViewById(R.id.imageView7);
        mainDesc = findViewById(R.id.imageDesc);
        mainStatus = findViewById(R.id.mainStatus);
        quote = findViewById(R.id.quote);

        t11 = findViewById(R.id.textView2);
        t12 = findViewById(R.id.textView3);
        t21 = findViewById(R.id.textView4);
        t22 = findViewById(R.id.textView5);
        t31 = findViewById(R.id.textView6);
        t32 = findViewById(R.id.textView7);
        t41 = findViewById(R.id.textView8);
        t42 = findViewById(R.id.textView9);

        title.setImageResource(R.drawable.goodplace_title);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncThread().execute(code.getText().toString());
            }
        });

    }


    public class AsyncThread extends AsyncTask<String, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... strings) {
            try
            {
                JSONObject json;

                String zip = strings[0];
                URL url = new URL("https://api.openweathermap.org/geo/1.0/zip?zip="+zip+",US&appid=0b1af2ebb5270f0c7de5c3ed507e9816");
                URLConnection connect = url.openConnection();
                InputStream stream = connect.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String text = "";
                String line = "";
                while ((line = buffer.readLine()) != null) {
                    text += line;
                }
                json = new JSONObject(text);

                String lat = json.get("lat").toString();
                String lon = json.get("lon").toString();
                url = new URL("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid=0b1af2ebb5270f0c7de5c3ed507e9816&units=imperial");
                connect = url.openConnection();
                stream = connect.getInputStream();
                buffer = new BufferedReader(new InputStreamReader(stream));
                text = "";
                line = "";
                while ((line = buffer.readLine()) != null) {
                    text += line;
                }
                buffer.close();
                json = new JSONObject(text);

                return json;

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            JSONObject j = json;

            try {

                String lat = j.getJSONObject("city").getJSONObject("coord").get("lat").toString();
                String lon = j.getJSONObject("city").getJSONObject("coord").get("lon").toString();

                t1.setText("Lat: "+lat+"\nLon: "+lon);
                city.setText("City: "+j.getJSONObject("city").get("name"));

                JSONObject list;
                String time;
                String hour;
                String status;
                String picId;
                double high;
                double low;
                double curT;

                for(int i=0; i<5; i++)
                {
                    list = j.getJSONArray("list").getJSONObject(i);
                    time = list.get("dt_txt").toString();
                    hour = convertTime(time.substring(11, 13));
                    status = getStatus(list.getJSONArray("weather").getJSONObject(0).get("id").toString());
                    picId = list.getJSONArray("weather").getJSONObject(0).get("icon").toString();
                    high = list.getJSONObject("main").getDouble("temp_max");
                    low = list.getJSONObject("main").getDouble("temp_min");

                    if(i==0)
                    {
                        curT = list.getJSONObject("main").getInt("temp");
                        mainDesc.setText(time.substring(5, 7)+"/"+time.substring(8, 10)+"/"+time.substring(0, 4)+"\n"+hour+"\n\nH: "+high+"\u00B0F\nL: "+low+"\u00B0F");
                        mainStatus.setText(status+" "+curT+"\u00B0F");
                    }
                    else if(i==1)
                    {
                        setImage(R.id.imageView3, picId);
                        t11.setText(hour+"\n"+status);
                        t12.setText("H: "+high+"\u00B0F\nL: "+low+"\u00B0F");
                    }
                    else if(i==2)
                    {
                        setImage(R.id.imageView4, picId);
                        t21.setText(hour+"\n"+status);
                        t22.setText("H: "+high+"\u00B0F\nL: "+low+"\u00B0F");
                    }
                    else if(i==3)
                    {
                        setImage(R.id.imageView5, picId);
                        t31.setText(hour+"\n"+status);
                        t32.setText("H: "+high+"\u00B0F\nL: "+low+"\u00B0F");
                    }
                    else if(i==4)
                    {
                        setImage(R.id.imageView6, picId);
                        t41.setText(hour+"\n"+status);
                        t42.setText("H: "+high+"\u00B0F\nL: "+low+"\u00B0F");
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setImage(int n, String id)
        {
            ImageView img = findViewById(n);
            switch(id.substring(0, 2))
            {
                case "01":
                    img.setImageResource(R.drawable.clear_sky);
                    break;
                case "02":
                    img.setImageResource(R.drawable.few_clouds);
                    break;
                case "03":
                    img.setImageResource(R.drawable.scattered_clouds);
                    break;
                case "04":
                    img.setImageResource(R.drawable.broken_clouds);
                    break;
                case "09":
                    img.setImageResource(R.drawable.shower_rain);
                    break;
                case "10":
                    img.setImageResource(R.drawable.rain);
                    break;
                case "11":
                    img.setImageResource(R.drawable.thunderstorm);
                    break;
                case "13":
                    img.setImageResource(R.drawable.snow);
                    break;
                case "50":
                    img.setImageResource(R.drawable.mist);
                    break;
            }
        }

        public String getStatus(String id)
        {
            String first = id.substring(0,1);
            switch(first){
                case "2":
                    main.setImageResource(R.drawable.main_thunderstorm);
                    quote.setText("\"I am pretty good at turning every place I go into my personal hell\" - Chidi");
                    return "Thunderstorms";
                case "3":
                    main.setImageResource(R.drawable.main_rain);
                    quote.setText("\"Well fork you, too\" - Eleanor");
                    return "Drizzle";
                case "5":
                    main.setImageResource(R.drawable.main_rain);
                    quote.setText("\"I’m going to…start crying\" - Chidi");
                    return "Rainy";
                case "6":
                    main.setImageResource(R.drawable.main_snow);
                    quote.setText("\"Claustrophobic? Who would ever be afraid of Santa Clause?\" - Jason");
                    return "Snowy";
                case "7":
                    if(id=="781")
                    {
                        main.setImageResource(R.drawable.main_tornado);
                        quote.setText("\"Birth is a curse and existence is a prison\" - Michael");
                        return "Tornados";
                    }
                    if(id=="721")
                    {
                        main.setImageResource(R.drawable.main_dust);
                        quote.setText("\"Oh, he is from Florida? Yeah, he belongs in The Bad Place\" - Shawn");
                        return "Dust";
                    }
                    if(id=="771")
                    {
                        main.setImageResource(R.drawable.main_squal);
                        quote.setText("\"Hi, guys! I’m broken\" - Janet");
                        return "Squal";
                    }
                    main.setImageResource(R.drawable.main_fog);
                    quote.setText("\"Why do bad things always happen to mediocre people who are lying about their identities\" - Eleanor");
                    return "Fog";
                case "8":
                    if(id=="800")
                    {
                        quote.setText("\"I haven’t encountered this much resistance since I tried to get Timothée Chalamet to go out into the sun\" - Tahini");
                        main.setImageResource(R.drawable.main_sunny);
                        return "Sunny";
                    }
                    main.setImageResource(R.drawable.main_cloudy);
                    quote.setText("\"Pobody’s nerfect\" - Eleanor");
                    return "Cloudy";
                default:
                    return "Not Found";
            }
        }

        public String convertTime(String hr)
        {
            int hour = Integer.parseInt(hr)-5;
            Log.d("hour", ""+hour);
            if(hour>12)
            {
                return String.valueOf(hour%12)+":00 PM";
            }
            else if(hour==12)
            {
                return "12:00 PM";
            }
            else if(hour>0)
            {
                return String.valueOf(hour)+":00 AM";
            }
            else if(hour==0)
            {
                return "12:00 AM";
            }
            return String.valueOf(hour+12)+":00 PM";
        }
    }
}