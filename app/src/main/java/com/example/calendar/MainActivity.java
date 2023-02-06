package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText input;
    private Button btn;
    private TextView view;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.input);
        btn = findViewById(R.id.button);
        view = findViewById(R.id.view);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = input.getText().toString().trim();

                if(city.equals("")){
                    Toast.makeText(MainActivity.this, R.string.for_empty_input,Toast.LENGTH_LONG).show();
                }else{
                    String key = "260125852511bbfff190fe3a3a562106";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city + "&appid=" + key + "&units=metric";

                    new getURL().execute(url);
                }
            }
        });

    }

    private class getURL extends AsyncTask<String,String,String>{

        protected void onPreExecute(){
            super.onPreExecute();
            view.setText("Ожидайте, спасибо  :)");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer res = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null){
                    res.append(line).append("\n");
                }

                return res.toString();

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    connection.disconnect();
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String res){
            super.onPostExecute(res);

            try {
                JSONObject jsonObject = new JSONObject(res);
                view.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}