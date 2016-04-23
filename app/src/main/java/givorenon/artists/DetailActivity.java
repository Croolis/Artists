package givorenon.artists;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

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
            ((TextView) findViewById(R.id.detail_albums)).setText(albums + " albums, " + tracks + " tracks");
            ((TextView) findViewById(R.id.detail_info)).setText(artist.getDescription());
        }
    }
}
