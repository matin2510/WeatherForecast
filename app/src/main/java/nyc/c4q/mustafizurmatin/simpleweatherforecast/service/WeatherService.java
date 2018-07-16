package nyc.c4q.mustafizurmatin.simpleweatherforecast.service;

import nyc.c4q.mustafizurmatin.simpleweatherforecast.FiveDayForcastModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by c4q on 6/7/18.
 */

public interface WeatherService  {
    @GET("weather")
    Call<CurrentWeather> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longtitude,
            @Query("appid") String appID

    );
    @GET("forecast")
    Call<FiveDayForcastModel> getFiveDayForecast(
            @Query("lat") double latitude,
            @Query("lon") double longtitude,
            @Query("appid") String appID

    );

}
