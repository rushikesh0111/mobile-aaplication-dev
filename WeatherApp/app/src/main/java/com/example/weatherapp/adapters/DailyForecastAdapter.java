package com.example.weatherapp.adapters;

import static com.example.weatherapp.static_config.StaticConfig.WEATHER_MODEL;

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
import com.example.weatherapp.models.DayModel;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder> {

    private final Context context;
    private final String unit;

    public DailyForecastAdapter(Context ctx, String unit) {
        this.context = ctx;
        this.unit = unit;
    }

    @NonNull
    @Override
    public DailyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_weather_details, parent, false);
        return new DailyForecastViewHolder(view);

    }

    //this method will return the count of items
    @Override
    public int getItemCount() {

        return WEATHER_MODEL.dayModelArrayList.size();
    }

    //this method will retuen the id of an icon
    private int getWeatherIcon(String iconName) {
        String i = iconName;
        i = i.replace("-", "_");

        int id = context.getResources().getIdentifier(i, "drawable", context.getPackageName());

        if (id == 0) {
            String TAG = "DailyForecastAdapter";
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + i);
            return 0;
        }
        return id;
    }

    //static class for daily activity specific variable definition and initialization
    static class DailyForecastViewHolder extends RecyclerView.ViewHolder {

        private final ImageView weatherIcon;

        private final TextView titleBar;
        private final TextView temp;

        private final TextView weatherDescr;
        private final TextView precipitation;
        private final TextView UVIndex;

        private final TextView morningTemp;
        private final TextView afternoonTemp;
        private final TextView eveningTemp;
        private final TextView nightTemp;

        public DailyForecastViewHolder(@NonNull View itemView) {

            super(itemView);

            weatherIcon = itemView.findViewById(R.id.view_weather_icon_image);
            titleBar = itemView.findViewById(R.id.title_bar);

            temp = itemView.findViewById(R.id.temparature_display);
            weatherDescr = itemView.findViewById(R.id.weather_description);

            precipitation = itemView.findViewById(R.id.textViewPrecipitation);
            UVIndex = itemView.findViewById(R.id.UV_index_view);

            morningTemp = itemView.findViewById(R.id.morning_temp_view);
            afternoonTemp = itemView.findViewById(R.id.afternoon_temp);

            eveningTemp = itemView.findViewById(R.id.evening_temp);
            nightTemp = itemView.findViewById(R.id.night_temp);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastViewHolder holder, int position) {

        String unitSymbol;

        if (unit != null) {
            if (unit.equals("metric"))
                unitSymbol = "C";
            else
                unitSymbol = "F";
        } else
            unitSymbol = "F";

        DayModel dm = WEATHER_MODEL.dayModelArrayList.get(position);

        SimpleDateFormat SDF = new SimpleDateFormat("EEEE dd/MM", Locale.ENGLISH);
        String date = SDF.format(new Date(dm.datetimeEpoch * 1000L));

        int iconID = getWeatherIcon(dm.weatherIcon);

        if (iconID != 0)
            holder.weatherIcon.setImageResource(iconID);

        holder.titleBar.setText(date);
        holder.temp.setText(MessageFormat.format("{0}°{1}/{2}°{3}", dm.tempmax, unitSymbol, dm.tempmin, unitSymbol));

        holder.weatherDescr.setText(dm.description);
        holder.precipitation.setText(MessageFormat.format("({0}% preci.)", dm.precipprob));
        holder.UVIndex.setText(MessageFormat.format("UV Index: {0}", dm.uvindex));

        holder.morningTemp.setText(MessageFormat.format("{0}", dm.hourModelArrayList.get(8).temp));
        holder.afternoonTemp.setText(MessageFormat.format("{0}", dm.hourModelArrayList.get(13).temp));
        holder.eveningTemp.setText(MessageFormat.format("{0}", dm.hourModelArrayList.get(17).temp));
        holder.nightTemp.setText(MessageFormat.format("{0}", dm.hourModelArrayList.get(23).temp));
    }
}
