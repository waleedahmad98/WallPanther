package com.waleed.walle;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WallpaperActivity extends AppCompatActivity {
    Database db = new Database(this);
    boolean favourited = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        //getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        View view = (ImageView) findViewById(R.id.wallpaper_preview);
        Entry received = (Entry) getIntent().getParcelableExtra("Details");
        Picasso.with(this).load(received.getLink()).resize(1080,1920).centerCrop().into((ImageView) view);
        ArrayList<Entry> favouriteBackgrounds = db.getAllWallpapers();
        if (isIn(favouriteBackgrounds, received)){
            favourited = true;
        }
        TextView name = (TextView)findViewById(R.id.name_details);
        TextView author = (TextView)findViewById(R.id.author_details);
        name.setText(received.getTitle());
        author.setText(received.getAuthor());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FullWallpaperActivity.class);
                intent.putExtra("Details", received);
                startActivity(intent);
            }
        });

        ImageButton downloadButton = (ImageButton) findViewById(R.id.download_btn);
        ImageButton setButton = (ImageButton) findViewById(R.id.set_btn);
        ImageButton favButton = (ImageButton) findViewById(R.id.fav_btn);
        if (favourited == true){
            favButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        }

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getBaseContext(), "Applying Wallpaper...", Toast.LENGTH_SHORT).show();
                    setWallpaper(getBaseContext(), received.getLink());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Downloading Wallpaper...", Toast.LENGTH_SHORT).show();
                downloadWallpaper(getBaseContext(), received.getTitle(), received.getLink());
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourited == true){
                    db.deleteWallpaper(received.getLink());
                    favButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    favourited = false;
                }
                else {
                    db.insertWallpaper(received);
                    favButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    favourited = true;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private static void setWallpaper(Context context, String url) throws IOException {
        class Set extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    Bitmap result = Picasso.with(context).load(url).get();
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                    try {
                        wallpaperManager.setBitmap(result);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                Toast.makeText(context, "Wallpaper Applied!", Toast.LENGTH_SHORT).show();
            }
        }
        Set set = new Set();
        set.execute();
    }

    private static void downloadWallpaper(Context context, String title, String url) {
        final ProgressDialog progress = new ProgressDialog(context);
        class Save extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*progress.setTitle("Downloading...");
                progress.setMessage("Please Wait...");
                progress.setCancelable(false);
                progress.show();*/
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {

                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "WallE");//context.getExternalFilesDir(null).getAbsolutePath()+"/wallE/Wallpapers");
                    String fileName = title + ".jpg";
                    dir.mkdirs();
                    final File ImageFile = new File(dir + File.separator + fileName); // Create image file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(ImageFile);
                        Bitmap bitmap = Picasso.with(context).load(url).get();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(ImageFile));
                        context.sendBroadcast(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                Toast.makeText(context, "Wallpaper Downloaded Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        Save share = new Save();
        share.execute();
    }

    boolean isIn(ArrayList<Entry> list, Entry recv){
        for (Entry e:list){
            if(recv.getLink().equals( e.getLink())){
                return true;
            }
        }
        return false;
    }
}




