package givorenon.artists;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ListActivity extends AppCompatActivity {

    ArrayList<Artist> artists = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.artists);
        String url = "http://download.cdn.yandex.net/mobilization-2016/artists.json";
        new GetArtists(recyclerView, getApplicationContext(), false).execute(url);
    }

    private class GetArtists extends AsyncTask<String, Void, ArrayList<Artist>> {
        Context context;
        RecyclerView recyclerView;
        boolean forceRefresh;

        public GetArtists(RecyclerView aRecyclerView, Context aContext, boolean aForceRefresh) {
            context = aContext;
            recyclerView = aRecyclerView;
            forceRefresh = aForceRefresh;
        }

        protected ArrayList<Artist> doInBackground(String... urls) {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
            Realm realm = Realm.getInstance(realmConfig);
            RealmResults<Artist> realmArtists = realm.allObjects(Artist.class);
            ArrayList<Artist> artists = new ArrayList<>();

            // if we have objects in database and it's not said that we need to refresh it
            // then get artists from db
            if ((realmArtists.size() != 0) && (!forceRefresh)) {

                for (int i = 0; i < realmArtists.size(); i++) {
                    Artist artist = new Artist(realmArtists.get(i));
                    artists.add(artist);
                }
                return artists;
            }

            StringBuffer response = new StringBuffer();
            try {
                URL obj = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");

                int responseCode = con.getResponseCode();
                if (responseCode >= 400)
                    throw new Exception("Failed to send request");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (Exception e) {
                Log.e("DATA", e.toString());
                Log.e("DATA", "Failed to send GET request");
                return artists;
            }

            try {
                realm.beginTransaction();
                JSONArray jArray = new JSONArray(response.toString());
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    Artist artist = jsonToArtist(jObject);
                    if (artist == null)
                        continue;
                    artists.add(artist);
                    if (realm.where(Artist.class)
                            .equalTo("id", artist.getId())
                            .findFirst() != null)
                        continue;
                    realm.copyToRealm(artist);
                }
                realm.commitTransaction();
            } catch (Exception e) {
                Log.e("DATA", e.toString());
                Log.e("DATA", "Failed to decode message");
            }
            return artists;
        }

        protected void onPostExecute(ArrayList<Artist> result) {
            artists = result;
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.artists);
            ArtistsAdapter adapter = new ArtistsAdapter(artists, context);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(itemAnimator);

        }
    }

    // some fields are optional so we don't do anything if they are empty
    private Artist jsonToArtist(JSONObject jObject) {
        Integer id;
        try {
            id = jObject.getInt("id");
        } catch (Exception e) {
            return null;
        }
        String name;
        try {
            name = jObject.getString("name");
        } catch (Exception e) {
            return null;
        }
        String genres = "";
        ArrayList<String> genresArray = new ArrayList<>();
        try {
            JSONArray jArrayGenres = jObject.getJSONArray("genres");
            for (int j = 0; j < jArrayGenres.length(); j++) {
                genresArray.add(jArrayGenres.getString(j));
            }
            genres = TextUtils.join(", ", genresArray);
        } catch (Exception e) {}
        Integer tracks = 0;
        try {
            tracks = jObject.getInt("tracks");
        } catch (Exception e) {}
        Integer albums = 0;
        try {
            albums = jObject.getInt("albums");
        } catch (Exception e) {}
        String link = "";
        try {
            link = jObject.getString("link");
        } catch (Exception e) {}
        String description = "";
        try {
            description = jObject.getString("description");
        } catch (Exception e) {}
        ArrayList<String> coverArray = new ArrayList<>();
        String cover;
        try {
            JSONObject jObjectCover = jObject.getJSONObject("cover");
            coverArray.add(jObjectCover.getString("small"));
            coverArray.add(jObjectCover.getString("big"));
            cover = TextUtils.join("|", coverArray);
        } catch (Exception e) {
            return null;
        }
        return new Artist(id, name, genres, tracks, albums, link, description, cover);
    }
}
