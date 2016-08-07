package droidia.com.heraldpdx.arrivalchecker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

class ArrivalsRecyclerViewAdapter extends RecyclerView.Adapter<ArrivalsRecyclerViewAdapter.ArrivalViewHolder> {

    private ArrivalResults arrivals;
    private Context context;

    ArrivalsRecyclerViewAdapter(Context context, ArrivalResults arrivals) {
        this.context = context;
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

        int index = arrival.fullSign.toLowerCase().indexOf(" ");
        String fullsign = arrival.fullSign.substring(index).trim();

        // space in "to " is necessary to not accidentally catch words like downtown
        int toIndex = fullsign.toLowerCase().indexOf("to ");

        if (toIndex >= 0) {
            fullsign = fullsign.substring(toIndex + "to ".length()).trim();
        }
        fullsign = "to " + fullsign;
        holder.arrivalDescription.setText(fullsign);

        // a is AM/PM marker.
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        Date scheduledDate = new Date(Long.parseLong(String.valueOf(arrival.scheduled)));
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String scheduledTime = sdf.format(scheduledDate);
        holder.arrivalScheduledAt.setText("Scheduled at " + scheduledTime);

        String signText = getSignText(arrival);
        holder.arrivalCircularSign.setText(signText);

        holder.arrivalCircularSign.setBackground(getSignDrawable(signText));

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


    private Drawable getSignDrawable(String signText) {

        int id;
        if (signText.equalsIgnoreCase("b")) id = R.drawable.circular_textview_blue;
        else if (signText.equalsIgnoreCase("g")) id = R.drawable.circular_textview_green;
        else if (signText.equalsIgnoreCase("o")) id = R.drawable.circular_textview_orange;
        else if (signText.equalsIgnoreCase("y")) id = R.drawable.circular_textview_yellow;
        else if (signText.equalsIgnoreCase("r")) id = R.drawable.circular_textview_red;
        else id = R.drawable.circular_textview_accent;

        return context.getDrawable(id);
    }

    private String getSignText(Arrival arrival) {

        List<String> MAXLines = Arrays.asList("Orange", "Blue", "Green", "Yellow", "Red");

        String[] split = arrival.shortSign.split(" ");

        for (String lines : MAXLines) {

            // It's a MAX train.
            if (split[0].equalsIgnoreCase(lines))
                return lines.substring(0, 1).toUpperCase();
        }

        // It's a bus. Returns the bus number.
        return String.valueOf(arrival.route);

    }

    @Override
    public int getItemCount() {
        // todo arrival could be null. eg. Location ID 5858.
        // todo This check should probably go in the Presenter and an interface
        // todo to the presenter needs to be added to handle the no arrivals found case.
        return arrivals.resultSet.arrival.size();
    }

    // todo make class static.
    class ArrivalViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.estimatedArrival)
        TextView estimatedArrivalIn;
        @BindView(R.id.arrivalDescription)
        TextView arrivalDescription;
        @BindView(R.id.arrivalScheduledAt)
        TextView arrivalScheduledAt;
        @BindView(R.id.arrivalCircularSign)
        TextView arrivalCircularSign;

        ArrivalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

