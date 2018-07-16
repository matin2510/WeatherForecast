package nyc.c4q.mustafizurmatin.simpleweatherforecast;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import nyc.c4q.mustafizurmatin.simpleweatherforecast.service.CurrentWeather;
import nyc.c4q.mustafizurmatin.simpleweatherforecast.service.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "Main Activity";
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String APPID = "df896147c24aa8dbbc008ca80d9e966c";
    private double lat;
    private double lon;
    private TextView description;
    private TextView highTemp;
    private TextView lowTemp;
    private TextView actualTemp;
    private ImageView weatherIcon;
    private Button fiveDayForcast;


    private GoogleApiClient googleApiClient;
    Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleApiClient = new GoogleApiClient.Builder(this,this,this).addApi(LocationServices.API).build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationServices();

        if (lastLocation != null) {
            callWeatherService();
        }

    }

    private void callWeatherService() {
        lat = lastLocation.getLatitude();
        lon = lastLocation.getLongitude();
        Log.d(TAG, "onConnected: lat: " + lat + " lon: " + lon);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<CurrentWeather> call = weatherService.getCurrentWeather(lat,lon,APPID);
        call.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");
                    CurrentWeather currentWeather = response.body();
                    Log.d(TAG, "onResponse: " + currentWeather.getWeather().get(0).getDescription());
                    settingUI(currentWeather);
                }
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {

            }
        });
    }

    private void settingUI(CurrentWeather currentWeather) {
         description = (TextView) findViewById(R.id.description);
         highTemp = (TextView) findViewById(R.id.high_temp);
         lowTemp = (TextView) findViewById(R.id.low_temp);
         actualTemp = (TextView) findViewById(R.id.actual_temp);
         weatherIcon = (ImageView) findViewById(R.id.weather_image);
         fiveDayForcast = (Button) findViewById(R.id.five_day_forcast);
         fiveDayForcast.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, FiveDayForecastActivity.class);
                 intent.putExtra("Lat", lat);
                 intent.putExtra("Lon", lon);
                 startActivity(intent);



             }
         });

         description.setText(currentWeather.getWeather().get(0).getDescription());
         String temp = String.valueOf(kelvinToFahrenheit(currentWeather.getMain().getTemp())).substring(0,2) + (char) 0x00B0;
         actualTemp.setText(temp);
         highTemp.setText((char) 0x25B2 + String.valueOf(kelvinToFahrenheit(currentWeather.getMain().getTemp_max())).substring(0,2));
         lowTemp.setText((char) 0x25BC  + String.valueOf(kelvinToFahrenheit(currentWeather.getMain().getTemp_min())).substring(0,2));
        Picasso.get().load("https://openweathermap.org/img/w/" + currentWeather.getWeather().get(0).getIcon() + ".png")
        .into(weatherIcon);






    }

    private double kelvinToFahrenheit(double kelvin) {
        return kelvin * (9.0/5.0) - 459.67;
    }

    private void initLocationServices() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1020);
        }

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null){
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}