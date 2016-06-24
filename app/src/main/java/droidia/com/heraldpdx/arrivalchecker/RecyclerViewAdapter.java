package droidia.com.heraldpdx.arrivalchecker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    Context context;
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

        // todo Too much stuff maybe happening here. Move this shit off the view?

        Arrival arrival = arrivals.resultSet.arrival.get(position);

        holder.arrivalDescription.setText(arrival.shortSign);

        // a is AM/PM marker.
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        Date scheduledDate = new Date(Long.parseLong(String.valueOf(arrival.scheduled)));
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String scheduledTime = sdf.format(scheduledDate);
        holder.arrivalScheduledAt.setText("Scheduled at " + scheduledTime);

        if (arrival.status.equalsIgnoreCase(Arrival.STATUS_SCHEDULED)) {
            holder.estimatedArrivalIn.setText(scheduledTime);

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

    // todo make class static.
    class ArrivalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.estimatedArrival) TextView estimatedArrivalIn;
        @BindView(R.id.arrivalDescription) TextView arrivalDescription;
        @BindView(R.id.arrivalScheduledAt) TextView arrivalScheduledAt;
        ArrivalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }
    }
}

