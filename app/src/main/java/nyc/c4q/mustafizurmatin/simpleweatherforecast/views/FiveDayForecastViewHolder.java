package nyc.c4q.mustafizurmatin.simpleweatherforecast.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import nyc.c4q.mustafizurmatin.simpleweatherforecast.FiveDayForcastModel.ListBean;
import nyc.c4q.mustafizurmatin.simpleweatherforecast.R;

/**
 * Created by c4q on 6/7/18.
 */

public class FiveDayForecastViewHolder extends RecyclerView.ViewHolder {
    private TextView description;
    private TextView time;
    private TextView highTemp;
    private TextView lowTemp;
    private TextView actualTemp;
    private ImageView weatherIcon;
    private Button fiveDayForcast;
    public FiveDayForecastViewHolder(View itemView) {
        super(itemView);
        description= itemView.findViewById(R.id.description2);
        time= itemView.findViewById(R.id.time_forecast);
        highTemp= itemView.findViewById(R.id.high_temp2);
        lowTemp= itemView.findViewById(R.id.low_temp2);
        actualTemp= itemView.findViewById(R.id.actual_temp2);
        weatherIcon= itemView.findViewById(R.id.weather_image2);
    }

    public void onBind(ListBean listBean) {
        if (listBean != null) {
            time.setText(listBean.getDt_txt());
            description.setText(listBean.getWeather().get(0).getDescription());
            String temp = String.valueOf(kelvinToFahrenheit(listBean.getMain().getTemp())).substring(0, 2) + (char) 0x00B0;
            actualTemp.setText(temp);
            highTemp.setText((char) 0x25B2 + String.valueOf(kelvinToFahrenheit(listBean.getMain().getTemp_max())).substring(0, 2));
            lowTemp.setText((char) 0x25BC + String.valueOf(kelvinToFahrenheit(listBean.getMain().getTemp_min())).substring(0, 2));
            Picasso.get().load("https://openweathermap.org/img/w/" + listBean.getWeather().get(0).getIcon() + ".png")
                    .into(weatherIcon);
        } else {
            Toast.makeText(itemView.getContext(), "null response", Toast.LENGTH_LONG).show();
        }


    }
    private double kelvinToFahrenheit(double kelvin) {
        return kelvin * (9.0/5.0) - 459.67;
    }
}
