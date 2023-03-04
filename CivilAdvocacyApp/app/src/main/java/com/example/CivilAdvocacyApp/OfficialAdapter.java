package com.example.CivilAdvocacyApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialAdapter.OfficialViewHolder> {
    private static final String TAG = OfficialAdapter.class.getName();

    private List<OfficialModel> officialModelList;
    private MainActivity mainActivity;

    public OfficialAdapter(List<OfficialModel> officialModelList, MainActivity mainActivity) {

        this.officialModelList = officialModelList;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getItemCount()
    {
        return officialModelList.size();
    }

    static class OfficialViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProfile;

        private TextView officialName;
        private TextView officeName;
        private TextView partyName;

        public OfficialViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProfile = itemView.findViewById(R.id.imageViewSenator);
            officeName = itemView.findViewById(R.id.officeName);
            officialName = itemView.findViewById(R.id.name);
            partyName = itemView.findViewById(R.id.party);
        }
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_entry,parent,false);
        itemView.setOnClickListener(mainActivity);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {

        OfficialModel temp = officialModelList.get(position);

        try {

            Picasso.get().load("" + temp.getPhotoURL()).placeholder(R.drawable.missing).error(R.drawable.missing).into(holder.imageViewProfile);

        } catch (IllegalArgumentException ae) {

            Picasso.get().load(R.drawable.missing).into(holder.imageViewProfile);
        }

        holder.officeName.setText(temp.getTitle());
        holder.officialName.setText(temp.getName());
        holder.partyName.setText(temp.getParty());
    }
}
