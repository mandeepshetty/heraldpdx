package droidia.com.heraldpdx.autocompleter;

import android.os.Parcel;
import android.support.v7.util.SortedList;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import droidia.com.heraldpdx.savedlocations.HeraldLocation;
import droidia.com.heraldpdx.storage.RealmHeraldLocation;
import droidia.com.heraldpdx.trimetapis.geospatialdata.GeoSpatialKMLDownloader;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AutoCompleter {

    private static final List<RealmHeraldLocation> allStops = new ArrayList<>(6000);
    private static Date localDataSetTimeStamp = new Date(-1);
    private static Date tempTimeStamp = new Date(-1);
    private static AllStopsStore allStopsStore = null;

    public static void initialize() {

        GeoSpatialKMLDownloader
                .getLastModifiedDateForDataSet()
                .subscribeOn(Schedulers.newThread())
                .filter(AutoCompleter::isDataSetUpdated)

                .doOnNext(AutoCompleter::cacheDate)

                .flatMap(date -> GeoSpatialKMLDownloader.download())
                .flatMap(AutoCompleter::parseKMLForStops)

                .doOnCompleted(() -> {

                    Timber.e("Fetched %d locations.", allStops.size());
                    updateLocalDataSetTimeStamp();

                    allStopsStore = AllStopsStore.getInstance();
                    allStopsStore.save(allStops);
                })
                .doOnError(throwable -> System.out.println(throwable.getMessage()))
                .subscribe(heraldLocation -> allStops.add(heraldLocation));

    }

    public static Observable<HeraldLocation> getFuzzyMatches(String pattern, int count){
        Timber.i("Fetching matches for : %s", pattern);
        return Observable.defer(() -> fetchFuzzyMatches(pattern, count));
    }

    private static  Observable<HeraldLocation> fetchFuzzyMatches(String pattern, int count) {

        // todo Replace this with a heap maybe.
        Timber.i("Getting fuzzy matches in %d", Thread.currentThread().getId());
        SortedList<RealmHeraldLocation> fuzzyMatches = new SortedList<>(RealmHeraldLocation.class, callback);
        AllStopsStore instance = AllStopsStore.getInstance();
        List<RealmHeraldLocation> allStops = instance.getAll();
        Observable.from(allStops)
                .filter(rhl ->
                        FuzzyMatcher.isPatternSequentiallyPresentInString(pattern, rhl.getLocationName()))
                .doOnNext(rhl -> Timber.i("Evaluating %s", rhl.getLocationName()))
                .doOnNext(rhl -> rhl.fuzzyScore = FuzzyMatcher.fuzzyMatch(pattern, rhl.getLocationName()))
                .doOnNext(rhl -> fuzzyMatches.add(rhl))
                .doOnCompleted(() -> {
                    Timber.i("Completed ! size : %d", fuzzyMatches.size());
                })
                .subscribe();

        Timber.i("Now it is size : %d", fuzzyMatches.size());
        // Do this because Realm objects can only be accessed from thread they
        // are created in wtf !
        List<HeraldLocation> listFuzzyMatches = new ArrayList<>(count);
        for (int i = 0; i < fuzzyMatches.size() && i < count; i++) {

            RealmHeraldLocation rhl = fuzzyMatches.get(i);
            HeraldLocation hl = new HeraldLocation(rhl.getLocationID(), rhl.getLocationName());
            hl.setTransportType(rhl.getTransportType());
            hl.setRouteDescription(rhl.getRouteDescription());
            hl.setDirection(rhl.getDirection());
            listFuzzyMatches.add(hl);
        }
        instance.destroyInstance();
        return Observable.from(listFuzzyMatches);

    }

    static  SortedList.Callback<RealmHeraldLocation> callback = new SortedList.Callback<RealmHeraldLocation>() {
        @Override
        public int compare(RealmHeraldLocation o1, RealmHeraldLocation o2) {
            return -1 *Double.compare(o1.fuzzyScore, o2.fuzzyScore);
        }

        @Override public void onInserted(int position, int count) { }
        @Override public void onRemoved(int position, int count) { }
        @Override public void onMoved(int fromPosition, int toPosition) { }
        @Override public void onChanged(int position, int count) { }
        @Override public boolean areContentsTheSame(RealmHeraldLocation oldItem, RealmHeraldLocation newItem) { return false; }
        @Override public boolean areItemsTheSame(RealmHeraldLocation item1, RealmHeraldLocation item2) { return false; }
    };

    private static void cacheDate(Date remoteDataSetDate) {
        tempTimeStamp = remoteDataSetDate;
    }

    private static boolean isDataSetUpdated(Date remoteDataSetTimeStamp) {
        return remoteDataSetTimeStamp.after(localDataSetTimeStamp);
    }

    private static void updateLocalDataSetTimeStamp() {
        localDataSetTimeStamp = tempTimeStamp;
    }


    // Parse XML/KML and return a list of stops.
    private static Observable<RealmHeraldLocation> parseKMLForStops(String kmlAsString) {

        try {
            String kmlDataSetAsJson = XML.toJSONObject(kmlAsString).toString();
            JsonToKMLMapper jsonToKMLMapper = new ObjectMapper().readValue(kmlDataSetAsJson, JsonToKMLMapper.class);
            return Observable.from(jsonToKMLMapper.kml.Document.Placemark)
                    .map(AutoCompleter::getHeraldLocationFromPlacemark);

        } catch (JSONException e) {
            return Observable.error(e);
        } catch (IOException e) {
            return Observable.error(e);
        }
    }

    private static RealmHeraldLocation getHeraldLocationFromPlacemark(Placemark placemark) {

        String locationID = null;
        String locationName = null;
        String transportType = null;
        String direction = null;
        String routeDescription = null;
        for (Datum d : placemark.ExtendedData.Data) {

            switch (d.name) {
                case "stop_id":
                    locationID = d.value;
                    break;
                case "stop_name":
                    locationName = d.value;
                    break;
                case "type":
                    transportType = d.value;
                    break;
                case "route_description":
                    routeDescription = d.value;
                    break;
                case "direction_description":
                    direction = d.value;
                    break;
            }
        }
        RealmHeraldLocation rhl = new RealmHeraldLocation();
        rhl.setLocationID(locationID);
        rhl.setLocationName(locationName);
        rhl.setDirection(direction);
        rhl.setTransportType(transportType);
        rhl.setRouteDescription(routeDescription);
        rhl.setPrimaryKey();
        return rhl;
    }


}
