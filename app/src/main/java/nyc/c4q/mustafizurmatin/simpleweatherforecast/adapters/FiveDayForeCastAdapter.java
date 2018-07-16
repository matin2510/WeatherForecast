package nyc.c4q.mustafizurmatin.simpleweatherforecast.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nyc.c4q.mustafizurmatin.simpleweatherforecast.FiveDayForcastModel.ListBean;
import nyc.c4q.mustafizurmatin.simpleweatherforecast.R;
import nyc.c4q.mustafizurmatin.simpleweatherforecast.views.FiveDayForecastViewHolder;

/**
 * Created by c4q on 6/7/18.
 */

public class FiveDayForeCastAdapter extends RecyclerView.Adapter<FiveDayForecastViewHolder> {

    private List<ListBean> listBeans;

    public FiveDayForeCastAdapter(List<ListBean> listBeans) {
        this.listBeans = listBeans;
    }

    @NonNull
    @Override
    public FiveDayForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fiveday_item_view, parent, false);
        return new FiveDayForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiveDayForecastViewHolder holder, int position) {
        holder.onBind(listBeans.get(position));

    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }
}
