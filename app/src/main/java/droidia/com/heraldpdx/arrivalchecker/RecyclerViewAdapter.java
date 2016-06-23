package droidia.com.heraldpdx.arrivalchecker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.trimetapis.arrivals.Arrival;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;
import timber.log.Timber;

/**
 * Created by mandeep on 18/6/16.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ArrivalViewHolder> {

    private ArrivalResults arrivals;

    RecyclerViewAdapter(ArrivalResults arrivals) {
        this.arrivals = arrivals;
    }

    @Override
    public ArrivalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrival_item, parent, false);
        return new ArrivalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArrivalViewHolder holder, int position) {

        Arrival arrival = arrivals.resultSet.arrival.get(position);

        // todo Too much stuff maybe happening here. Move this shit off the view?
        if (arrival.status.equalsIgnoreCase(Arrival.STATUS_SCHEDULED)) {

            // a is AM/PM marker.
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            holder.estimatedArrivalIn.setText(sdf.format(new Date(arrival.scheduled)));

        } else if (arrival.status.equalsIgnoreCase(Arrival.STATUS_ESTIMATED)) {

            int arrivalIn = (int) ((arrival.estimated - System.currentTimeMillis()) / 1000 / 60);
            if (arrivalIn > 0)
                holder.estimatedArrivalIn.setText(String.format("%d min", arrivalIn));
            else
                holder.estimatedArrivalIn.setText("Due");

        } else {
            Timber.e("Not implemented handling of %s", arrival.status);
        }
    }

    @Override
    public int getItemCount() {
        // todo arrival could be null. eg. Location ID 5858.
        // todo This check should probably go in the Presenter and an interface
        // todo to the presenter needs to be added to handle the no arrivals found case.
        return arrivals.resultSet.arrival.size();
    }


    static class ArrivalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.estimatedArrival)
        TextView estimatedArrivalIn;

        ArrivalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

