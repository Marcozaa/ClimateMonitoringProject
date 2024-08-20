package com.example.climatemonitoringapp;

/**
 * Classe che rappresenta le coordinate geografiche di un'area di interesse
 */
public class Coordinate {
    /**
     * Attributi
     */
    private double lat;
    private double lon;

    /**
     * Costruttore
     * @param lat
     * @param lon
     */
    public Coordinate(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat(){
        return lat;
    }

    public double getLon(){
        return lon;
    }

    /**
     * Metodo che calcola la distanza tra due coordinate
     * @param coords2 Coordinate con cui calcolare la distanza
     * @return Distanza tra le due coordinate
     */
    public double getDistance(Coordinate coords2){
        double lat1 = this.lat;
        double lon1 = this.lon;
        double lat2 = coords2.getLat();
        double lon2 = coords2.getLon();
        double R = 6371e3; // metres
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2-lat1);
        double Δλ = Math.toRadians(lon2-lon1);
        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }
}
