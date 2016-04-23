package givorenon.artists;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ListActivity extends AppCompatActivity {

    ArrayList<Artist> artists = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //artists = getArtists();
        //

        new GetArtistsTask().execute();

    }

    private class GetArtistsTask extends AsyncTask<Context, Void, ArrayList<Artist>> {
        protected ArrayList<Artist> doInBackground(Context... contexts) {
            return getArtists();
        }

        protected void onPostExecute(ArrayList<Artist> result) {
            artists = result;
            ListView listView = ((ListView) findViewById(R.id.artists));
            listView.setAdapter(new ArtistsAdapter(getBaseContext(), artists));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    toDetail((Integer) view.getTag());
                }
            });
        }
    }

    private ArrayList<Artist> getArtists() {
        ArrayList<Artist> artists = new ArrayList<>();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this.getApplicationContext()).build();
        Realm realm = Realm.getInstance(realmConfig);
        StringBuffer response = new StringBuffer();
        try {
            String url = "http://download.cdn.yandex.net/mobilization-2016/artists.json";
            URL obj = new URL(url);
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

    private Artist jsonToArtist(JSONObject jObject) {
        Integer id = 0;
        try {
            id = jObject.getInt("id");
        } catch (Exception e) {}
        String name = "";
        try {
            name = jObject.getString("name");
        } catch (Exception e) {}
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
        String cover = "";
        try {
            JSONObject jObjectCover = jObject.getJSONObject("cover");
            coverArray.add(jObjectCover.getString("small"));
            coverArray.add(jObjectCover.getString("big"));
            cover = TextUtils.join("|", coverArray);
        } catch (Exception e) {}
        return new Artist(id, name, genres, tracks, albums, link, description, cover);
    }

    private void toDetail(Integer tag) {
        Intent intent = new Intent(this, DetailActivity.class);
        Integer artistId = artists.get(tag).getId();
        intent.putExtra("id", artistId);
        startActivity(intent);
    }
}
