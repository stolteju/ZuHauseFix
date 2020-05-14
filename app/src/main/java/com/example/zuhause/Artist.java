package com.example.zuhause;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Artist {
    private String artistId;
    private String artistName;


    public Artist(){
        //this constructor is required
    }

    public Artist(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

}
