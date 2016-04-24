package givorenon.artists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DetailActivity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this.getApplicationContext()).build();
        realm = Realm.getInstance(realmConfig);

        Integer id = getIntent().getIntExtra("id", 0);
        Artist artist = realm.where(Artist.class)
                .equalTo("id", id)
                .findFirst();
        if (artist != null) {
            String genres = artist.getGenres();
            ((TextView) findViewById(R.id.detail_genres)).setText(genres);
            String albums = artist.getAlbums().toString();
            String tracks = artist.getTracks().toString();
            ((TextView) findViewById(R.id.detail_albums)).setText(albums + " albums • " + tracks + " tracks");
            ((TextView) findViewById(R.id.detail_info)).setText(artist.getDescription());

            try {
                String urlStr = artist.getCover().split("\\|")[1];
                URL url = new URL(urlStr);
                new GetPic().execute(url);
            } catch (Exception e) {
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.stub);
                ((ImageView) findViewById(R.id.big_pic)).setImageBitmap(bitmap);
            }
        }
    }

    private class GetPic extends AsyncTask<URL, Void, Bitmap> {
        protected Bitmap doInBackground(URL... urls) {
            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.stub);
            try {
                bitmap = BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("DATA", e.toString());
                Log.e("DATA", "Failed to load pic");
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            ((ImageView) findViewById(R.id.big_pic)).setImageBitmap(bitmap);
        }
    }
}
