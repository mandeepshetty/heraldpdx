package droidia.com.heraldpdx.savedlocations;

/**
 * Created by mandeep on 26/6/16.
 */
public class HeraldLocation {

    public String locationID;
    public String locationName;

    public HeraldLocation(String locationID, String locationName) {
        this.locationID = locationID;
        this.locationName = locationName;
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

        if (!locationID.equals(that.locationID)) return false;
        return locationName.equals(that.locationName);

    }

    @Override
    public int hashCode() {
        int result = locationID.hashCode();
        result = 31 * result + locationName.hashCode();
        return result;
    }
}
