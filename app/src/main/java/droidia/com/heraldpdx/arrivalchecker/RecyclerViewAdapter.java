package droidia.com.heraldpdx.arrivalchecker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.trimetapis.arrivals.Arrival;
import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;

/**
 * Created by mandeep on 18/6/16.
 */

class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ArrivalViewHolder>{

    private ArrivalResults arrivals;

    RecyclerViewAdapter(ArrivalResults arrivals){
        this.arrivals = arrivals;
    }

    @Override
    public ArrivalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrival_item, parent, false);
        return new ArrivalViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ArrivalViewHolder holder, int position) {

        holder.arrivalDesc.setText(arrivals.resultSet.location.get(0).desc);

        Arrival arrival = arrivals.resultSet.arrival.get(position);
        int arrivalIn = (int) ((arrival.estimated - System.currentTimeMillis()) / 1000 / 60);
        holder.estimatedArrivalIn.setText(String.format("Estimated arrival in %d minutes", arrivalIn));
    }

    @Override public int getItemCount() {
        return arrivals.resultSet.arrival.size();
    }


    class ArrivalViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.arrivalDesc) TextView arrivalDesc;
        @BindView(R.id.estimatedArrival) TextView estimatedArrivalIn;

        ArrivalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

