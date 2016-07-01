package droidia.com.heraldpdx.savedlocations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;

/**
 * Created by mandeep on 26/6/16.
 */

public class LocationListRecyclerViewAdapter extends
        RecyclerView.Adapter<LocationListRecyclerViewAdapter.LocationViewHolder>
        implements ILocationListClickListener {

    private final ILocationListClickListener listener;
    private final Context context;
    List<HeraldLocation> locationList;

    public LocationListRecyclerViewAdapter(List<HeraldLocation> locationList, ILocationListClickListener
            listener, Context context) {
        this.locationList = locationList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_location_item, parent, false);
        return new LocationViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        holder.locationID.setText(locationList.get(position).locationID);
        holder.locationDescription.setText(locationList.get(position).locationName);
        holder.locationSign.setText(locationList.get(position).locationName.substring(0, 1).toUpperCase());

        holder.locationSign.setBackground(context.getDrawable(R.drawable.circular_textview_accent));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void locationClicked(String locationID) {
        listener.locationClicked(locationID);
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ILocationListClickListener clickListener;

        @BindView(R.id.savedLocationID) TextView locationID;
        @BindView(R.id.savedLocationDescription) TextView locationDescription;
        @BindView(R.id.savedLocationCircularSign) TextView locationSign;
        LocationViewHolder(View itemView, ILocationListClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.locationClicked(locationID.getText().toString().trim());
        }
    }
}

