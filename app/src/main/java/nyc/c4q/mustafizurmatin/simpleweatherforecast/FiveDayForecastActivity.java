package nyc.c4q.mustafizurmatin.simpleweatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import nyc.c4q.mustafizurmatin.simpleweatherforecast.adapters.FiveDayForeCastAdapter;
import nyc.c4q.mustafizurmatin.simpleweatherforecast.service.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiveDayForecastActivity extends AppCompatActivity {
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String APPID = "df896147c24aa8dbbc008ca80d9e966c";
    private static final String TAG = "FiveDayForecast";
    private RecyclerView fivedayRV;
    private FiveDayForeCastAdapter fiveDayForeCastAdapter;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_forecast);

        getLatLon();
        setRecyclerView();

        Log.d(TAG, "onConnected: lat: " + lat + " lon: " + lon);
        callForecast();
    }

    private void getLatLon() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("Lat", -1.0);
        lon = intent.getDoubleExtra("Lon", -1.0);
    }

    private void setRecyclerView() {
        fivedayRV = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        fivedayRV.setLayoutManager(layoutManager);
    }

    private void callForecast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<FiveDayForcastModel> call = weatherService.getFiveDayForecast(lat, lon, APPID);
        call.enqueue(new Callback<FiveDayForcastModel>() {
            @Override
            public void onResponse(Call<FiveDayForcastModel> call, Response<FiveDayForcastModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");
                    Log.d(TAG, "onResponse: ");
                    FiveDayForcastModel fiveDayForcastModel = response.body();
                    fiveDayForeCastAdapter = new FiveDayForeCastAdapter(fiveDayForcastModel.getList());
                    fivedayRV.setAdapter(fiveDayForeCastAdapter);


                }
            }

            @Override
            public void onFailure(Call<FiveDayForcastModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();

            }
        });
    }

}
