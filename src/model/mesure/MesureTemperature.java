package model.mesure;

import model.Position;

/** Temperature de l'eau, en degres Celsius. */
public class MesureTemperature extends Mesure {
    private final double temperature;

    public MesureTemperature(double instant, Position lieu, double temperature) {
        super(instant, lieu);
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return String.format("Temperature %.2f C (t=%.0f)", temperature, getInstant());
    }
}
