package cybertech.childguide;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 6/4/2013.
 */

public class LocationModel {
    String childLatitude;
    String childLongitude;
    String locationDate;

    public LocationModel() {
    }

    public LocationModel(String childLongitude, String childLatitude, String locationDate) {
        this.childLongitude = childLongitude;
        this.childLatitude = childLatitude;
        this.locationDate = locationDate;
    }

    public String getLocationDate() {
        return locationDate;
    }

    public void setLocationDate(String locationDate) {
        this.locationDate = locationDate;
    }

    public String getChildLatitude() {
        return childLatitude;
    }

    public void setChildLatitude(String childLatitude) {
        this.childLatitude = childLatitude;
    }

    public String getChildLongitude() {
        return childLongitude;
    }

    public void setChildLongitude(String childLongitude) {
        this.childLongitude = childLongitude;
    }
}
