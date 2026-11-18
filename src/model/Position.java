package model;

/**
 * Position dans le monde 2D (vue de cote).
 * <p>
 * La longitude est l'axe horizontal. L'altitude est l'axe vertical :
 * 0 a la surface de la mer, negative sous l'eau (profondeur), positive
 * pour les satellites.
 */
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

    public Position copie() {
        return new Position(longitude, altitude);
    }

    public double distanceHorizontale(Position autre) {
        return Math.abs(longitude - autre.longitude);
    }

    @Override
    public String toString() {
        return "(" + longitude + ", " + altitude + ")";
    }
}
