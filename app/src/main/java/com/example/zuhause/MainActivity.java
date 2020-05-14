package com.example.zuhause;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ARTIST_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String ARTIST_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";
    public static final String DONE_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String DONE_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";
    EditText editTextName;
    ListView listViewArtists;


    String cMark = "\u2713";
    String sCart = "\uF6D2";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //our database reference object
    private DatabaseReference databaseArtists;
    //a list to store all the foods from firebase database
    List<Artist> artists;


    //our database reference object for all done things
    private DatabaseReference databaseDone;
    //a list to store all the foods from firebase database
    List<Artist> done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");
        //myRef.setValue("Hello, World!");

        //getting the reference of artists node
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        //list to store artists
        artists = new ArrayList<>();

        //getting the reference of artists node
        databaseDone = FirebaseDatabase.getInstance().getReference("doneItems");
        //list to store artists
        done = new ArrayList<>();

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

    /*
        // HINZUFÜGEN
        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addArtist();
            }
        });
     */
        editTextName.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addArtist();
                            Toast.makeText(getApplicationContext(),  "????", Toast.LENGTH_LONG).show();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        // LISTE
        //attaching listener to listview -- HIER DANN LOESCHEN??
        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Artist artist = artists.get(i);

                String artistId = artist.getArtistId();
                //creating an intent
                addDones(artistId);
                deleteArtist(artistId);
            }
        });

        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artists.get(i);
                //showUpdateDeleteDialog(artist.getArtistId(), artist.getArtistName());
                return true;
            }
        });

    }

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);

        //removing artist
        dR.removeValue();

        Toast.makeText(getApplicationContext(),  cMark, Toast.LENGTH_LONG).show();

        return true;
    }
    protected void onStart(){
            super.onStart();
            //attaching value event listener
            databaseArtists.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //clearing the previous artist list
                    artists.clear();
                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        Artist artist = postSnapshot.getValue(Artist.class);
                        //adding artist to the list
                        artists.add(artist);
                    }
                    //creating adapter
                    ArtistList artistAdapter = new ArtistList(MainActivity.this, artists);
                    //attaching adapter to the listview
                    listViewArtists.setAdapter((ListAdapter) artistAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /*
        databaseDone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                done.clear();
                //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        Artist artist = postSnapshot.getValue(Artist.class);
                        //adding artist to the list
                        done.add(artist);
                    }
                    //creating adapter
                    ArtistList artistAdapter = new ArtistList(MainActivity.this, done);
                    //attaching adapter to the listview
                    listViewDone.setAdapter((ListAdapter) artistAdapter);
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

             */
        }


            private void addArtist() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            //creating an Artist Object
            ArtistActivity.Artist artist = new ArtistActivity.Artist(id, name);

            //Saving the Artist
            databaseArtists.child(id).setValue(artist);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "hinzugefügt " + sCart, Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "bitte hinzufügen", Toast.LENGTH_LONG).show();
        }
    }
    private void addDones(String id) {
        //getting the values to save
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);
            // Attach a listener to read the data at our posts reference
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String did = databaseDone.push().getKey();

                Artist done = dataSnapshot.getValue(Artist.class);

                //Saving the Artist
                databaseDone.child(did).setValue(done);

                //displaying a success toast
                //Toast.makeText(this, "add dones! " + sCart, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist


        });
    }

}
