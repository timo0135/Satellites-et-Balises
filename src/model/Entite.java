package model;

public abstract class Entite {
    private final String id;
    private final Position position;

    public Entite(String id, double longitude, double altitude) {
        this.id = id;
        this.position = new Position(longitude, altitude);
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public abstract void mettreAJour(Simulation simulation);
}
