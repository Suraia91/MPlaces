package com.fragoso.admin.mplaces;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;


public class Geolocalizacao {

    private List<Address> findGeocoder(Double lat, Double lon , Geocoder geocoder){


        final int maxResults = 1;
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, maxResults);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return addresses;
    }


    public String localidade(Double lat , Double lon , Geocoder geocoder ){
        String stringThisAddress = "";
        List<Address> geoResult = findGeocoder(lat, lon, geocoder);
        if(geoResult != null){

            for(int i=0; i < geoResult.size(); i++){
                Address thisAddress = geoResult.get(i);

                for(int a=0; a < thisAddress.getMaxAddressLineIndex(); a++) {

                    stringThisAddress = thisAddress.getLocality()  + ", " + thisAddress.getFeatureName();


                    //categoryTextView.setText( thisAddress.getLocality()  + ", " + thisAddress.getFeatureName());
                }

            }

        }
        return stringThisAddress;
    }


}
