package droidia.com.heraldpdx.savedlocations;

import android.os.Parcel;
import android.support.v7.widget.SearchView;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import droidia.com.heraldpdx.storage.RealmHeraldLocation;

/**
 * Created by mandeep on 26/6/16.
 */
public class HeraldLocation implements SearchSuggestion{

    public String locationID;

    public String locationName;
    public String transportType;
    public double lattitude;
    public double longitude;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    private String direction;
    private String routeDescription;

    public HeraldLocation(){}

    public HeraldLocation(String locationID, String locationName) {
        this.locationID = locationID;
        this.locationName = locationName;
    }

    public HeraldLocation(Parcel in) {
        this.locationID = in.readString();
        this.locationName = in.readString();
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public void setLatLong(double latittude, double longitude) {
        this.lattitude = latittude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("%s : %s", locationID, locationName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeraldLocation that = (HeraldLocation) o;

        return locationID.equals(that.locationID) && locationName.equals(that.locationName);

    }

    @Override
    public int hashCode() {
        int result = locationID.hashCode();
        result = 31 * result + locationName.hashCode();
        return result;
    }

    public static final Creator<HeraldLocation> CREATOR = new Creator<HeraldLocation>() {
        @Override
        public HeraldLocation createFromParcel(Parcel in) {
            return new HeraldLocation(in);
        }

        @Override
        public HeraldLocation[] newArray(int size) {
            return new HeraldLocation[size];
        }
    };

    @Override
    public String getBody() {
        return locationName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(locationID);
        parcel.writeString(locationName);
    }
}
