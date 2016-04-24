package givorenon.artists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.net.URL;

public class SetImage extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Context context;

    SetImage(ImageView anImageView, Context aContext) {
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