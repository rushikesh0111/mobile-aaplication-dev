package com.example.weatherapp.adapters;

import static com.example.weatherapp.static_config.StaticConfig.WEATHER_MODEL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.models.HourModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder> {
    private final Context context;

    private String unit;

    public HourlyWeatherAdapter(Context ctx, String unit) {
        this.context = ctx;
        this.unit = unit;
    }

    @NonNull
    @Override
    public HourlyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_weather, parent, false);
        return new HourlyWeatherViewHolder(view);
    }

    //this method will return the count of items
    @Override
    public int getItemCount() {
        if (WEATHER_MODEL == null)
            return 0;

        return WEATHER_MODEL.dayModelArrayList.get(0).hourModelArrayList.size();
    }

    //this method will return the icons for hourly module
    private int getWeatherIcon(String iconName) {

        String i = iconName;
        i = i.replace("-", "_");

        int id = context.getResources().getIdentifier(i, "drawable", context.getPackageName());

        if (id == 0) {

            String TAG = "HourlyWeatherAdapter";
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + i);

            return 0;
        }
        return id;
    }

    //this method will update the data for hourly module
    @SuppressLint("NotifyDataSetChanged")
    public void updateHourlyData(String u) {

        this.unit = u;
        notifyDataSetChanged();

    }

    //static class for Hourly activity specific variable definition and initialization
    static class HourlyWeatherViewHolder extends RecyclerView.ViewHolder {

        private final ImageView weatherIcon;
        private final TextView time;

        private final TextView temp;
        private final TextView weatherDescr;

        public HourlyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            weatherIcon = itemView.findViewById(R.id.view_weather_icon_image);
            time = itemView.findViewById(R.id.textViewTime);

            temp = itemView.findViewById(R.id.temparature_display);
            weatherDescr = itemView.findViewById(R.id.weather_description);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherViewHolder hv, int pos) {

        HourModel hm = WEATHER_MODEL.dayModelArrayList.get(0).hourModelArrayList.get(pos);

        SimpleDateFormat rawTime = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String time = rawTime.format(new Date(hm.datetimeEpoch * 1000L));

        int id = getWeatherIcon(WEATHER_MODEL.currentConditionModel.icon);

        if (id != 0)
            hv.weatherIcon.setImageResource(id);

        String unitSymbol;

        if (unit != null) {

            if (unit.equals("metric"))
                unitSymbol = "C";
            else
                unitSymbol = "F";
        }
        else
            unitSymbol = "F";

        hv.time.setText("" + time);
        hv.temp.setText("" + hm.temp + "°" + unitSymbol);
        hv.weatherDescr.setText(hm.conditions);
    }
}
