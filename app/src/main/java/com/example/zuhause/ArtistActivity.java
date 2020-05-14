package com.example.zuhause;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class ArtistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        Intent intent = getIntent();

        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node track we are creating a new child with the artist id
         * and inside that node we will store all the tracks with unique ids
         * */

    }

    public static class Artist {
        private String artistId;
        private String artistName;


        public Artist() {
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
}
