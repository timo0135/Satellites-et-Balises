package model;

public class Position {
    private double longitude;
    private double altitude;

    public Position(double longitude, double altitude) {
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "(" + longitude + ", " + altitude + ")";
    }
}
