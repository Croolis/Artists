package givorenon.artists;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArtistsAdapter extends BaseAdapter {
    ArrayList<Artist> artists;
    LayoutInflater inflater;
    Context context;

    ArtistsAdapter(Context aContext, ArrayList<Artist> anArtists) {
        artists = anArtists;
        context = aContext;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public View getView(int aPosition, View aConvertView, ViewGroup aParent) {
        View resultView = aConvertView;
        if (resultView == null) {
            resultView = inflater.inflate(R.layout.artist, null, false);
        }

        Artist artist = artists.get(aPosition);
        ((TextView) resultView.findViewById(R.id.name)).setText(artist.getName());
        String genres = artist.getGenres();
        ((TextView) resultView.findViewById(R.id.genres)).setText(genres);
        String albums = artist.getAlbums().toString();
        String tracks = artist.getTracks().toString();
        ((TextView) resultView.findViewById(R.id.tracks)).setText(albums + " albums, " + tracks + " tracks");

        resultView.setTag(aPosition);
        resultView.setId(aPosition);

        return resultView;
    }

    @Override
    public long getItemId(int anId) {
        return anId;
    }

    @Override
    public Artist getItem(int anId) {
        return artists.get(anId);
    }
}