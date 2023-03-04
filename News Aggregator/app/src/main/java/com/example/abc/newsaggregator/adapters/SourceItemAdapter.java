package com.example.abc.newsaggregator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.abc.newsaggregator.activities.MainActivity;
import com.example.abc.newsaggregator.models.SourceModel;
import com.example.abc.newsaggregator.R;

public class SourceItemAdapter extends ArrayAdapter<SourceModel> {

    private final MainActivity mainActivity;
    private final SourceModel[] objects;

    public SourceItemAdapter(@NonNull Context context, int resource, @NonNull SourceModel[] objects) {
        super(context, resource, objects);
        this.mainActivity = (MainActivity) context;
        this.objects = objects;
    }

    //
    //
    //
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SourceItemViewHolder vh;

        if (convertView == null) {

            LayoutInflater inflater = mainActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            vh = new SourceItemViewHolder(convertView);
            convertView.setTag(vh);

        } else {

            vh = (SourceItemViewHolder) convertView.getTag();
        }

        SourceModel source = objects[position];
        vh.name.setText(source.getName());

        vh.name.setTextColor(Color.parseColor(
                mainActivity.colorCategories.getOrDefault(source.getCategory(), "#FFFFFF")));

        return convertView;
    }

    //
    //
    //
    static class SourceItemViewHolder {

        TextView name;

        public SourceItemViewHolder(@NonNull View itemView) {
            name = itemView.findViewById(R.id.sourceItemTextView);
        }
    }

}
