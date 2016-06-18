package droidia.com.heraldpdx.arrivalchecker;

import java.util.List;

import droidia.com.heraldpdx.trimetapis.arrivals.ArrivalResults;


/**
 * Created by mandeep on 18/6/16.
 */

interface ArrivalListingView {

    void displayArrivals(List<ArrivalResults> arrivals);

    void fetchingArrivalsFailed(String message);
}
