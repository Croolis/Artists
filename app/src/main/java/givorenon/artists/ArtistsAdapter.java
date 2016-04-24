package givorenon.artists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
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

        String url = artist.getCover().split("\\|")[0];

        ImageView imageView = (ImageView) resultView.findViewById(R.id.pic);
        new GetPic(imageView, context).execute(url);

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

    private class GetPic extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        Context context;

        GetPic(ImageView anImageView, Context aContext) {
            imageView = anImageView;
            context = aContext;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.stub);
            try {
                URL url = new URL(urls[0]);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("DATA", e.toString());
                Log.e("DATA", "Failed to load pic");
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}