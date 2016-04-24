package givorenon.artists;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private ArrayList<Artist> artists;
    Context context;

    public ArtistsAdapter(ArrayList<Artist> artists, Context context) {
        this.artists = artists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Artist artist = artists.get(i);

        viewHolder.name.setText(artist.getName());
        viewHolder.genres.setText(artist.getGenres());
        String text = context.getString(R.string.albums_tracks, artist.getAlbums(), artist.getTracks());
        viewHolder.tracks.setText(text);
        String url = artist.getCover().split("\\|")[0];
        new SetImage(viewHolder.pic, context).execute(url);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView pic;
        private TextView name;
        private TextView genres;
        private TextView tracks;

        public ViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
            genres = (TextView) itemView.findViewById(R.id.genres);
            tracks = (TextView) itemView.findViewById(R.id.tracks);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Integer tag = getPosition();
            Intent intent = new Intent(context, DetailActivity.class);
            Integer artistId = artists.get(tag).getId();
            intent.putExtra("id", artistId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}