package com.fragoso.admin.mplaces;

/**
 * Created by admin on 12/14/17.
 */

public class Postagens {

    private String Fotos;
    private String comentarios;
    private double latitude;
    private double longitude;
    private long data;


    public Postagens() {
      /*Blank default constructor essential for Firebase*/
    }

    public String getFotos() {
        return Fotos;
    }

    //
    public void setImage(String fotos) {
        this.Fotos = fotos;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}