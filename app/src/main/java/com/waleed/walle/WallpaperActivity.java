package com.waleed.walle;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WallpaperActivity extends AppCompatActivity {
    Database db = new Database(this);
    boolean favourited = false;
    private InterstitialAd mInterstitialAd;
    private Entry received;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createAd();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        View view = (ImageView) findViewById(R.id.wallpaper_preview);
        received = (Entry) getIntent().getParcelableExtra("Details");
        Picasso.with(this).load(received.getLink()).resize(1080,1920).centerCrop().into((ImageView) view,  new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });
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
                if (mInterstitialAd!=null){
                    mInterstitialAd.show(WallpaperActivity.this);
                }else {

                    Intent intent = new Intent(getBaseContext(), FullWallpaperActivity.class);
                    intent.putExtra("Details", received);
                    startActivity(intent);
                }
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
                    registerForContextMenu(v);
                    openContextMenu(v);
                    unregisterForContextMenu(v);
                } catch (Exception e) {
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
                    Toast.makeText(getBaseContext(), "Wallpaper removed from Favourites!", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.insertWallpaper(received);
                    favButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    favourited = true;
                    Toast.makeText(getBaseContext(), "Wallpaper added to Favourites!", Toast.LENGTH_SHORT).show();
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

    private static void setWallpaper(Context context, String url, int mode_val) throws IOException {
        class Set extends AsyncTask<Void, Void, Void> {
            boolean isSet = false;
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (mode_val == 0) {
                                wallpaperManager.setBitmap(result, null, false, WallpaperManager.FLAG_LOCK);
                                isSet = true;
                            }
                            else if (mode_val == 1) {
                                wallpaperManager.setBitmap(result, null, false, WallpaperManager.FLAG_SYSTEM);
                                isSet = true;
                            }
                            else if (mode_val == 2) {
                                wallpaperManager.setBitmap(result, null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                                isSet = true;
                            }

                        }
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
                if(isSet == true) {
                    Toast.makeText(context, "Wallpaper Applied!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Wallpaper Not Applied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        Set set = new Set();
        set.execute();
    }

    private void downloadWallpaper(Context context, String title, String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("wallEWallpaper", "Wallpaper Download", importance);
            channel.setDescription("Notification for Wallpaper Download");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "wallEWallpaper");
        builder.setContentTitle("Fresh Wallpapers on the way..")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        class Save extends AsyncTask<Void, Integer, Void> {
            String savedPath;
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                builder.setProgress(100, 0, false);
                notificationManager.notify(m, builder.build());
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                builder.setProgress(100, values[0], false);
                notificationManager.notify(m, builder.build());
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {

                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "WallE");//context.getExternalFilesDir(null).getAbsolutePath()+"/wallE/Wallpapers");

                    String extension = url.substring(url.length()-4, url.length());
                    String fileName = title + extension;
                    if (title.contains("?")){
                        fileName = String.valueOf(m) + extension;
                    }

                    final File ImageFile = new File(dir + File.separator + fileName); // Create image file
                    savedPath = dir + File.separator + fileName;
                    FileOutputStream fos = null;
                    try {
                        publishProgress(50);
                        fos = new FileOutputStream(ImageFile);
                        Bitmap bitmap = Picasso.with(context).load(url).get();
                        if (extension.contains("png"))
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        else
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        publishProgress(100);

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
                //builder.setContentText("Download complete");
                // Removes the progress bar
                //builder.setProgress(0, 0, false);
                //notificationManager.notify(1, builder.build());

                notificationManager.cancel(m);
                Intent intent = new Intent(getBaseContext(), BroadcastNotification.class);
                intent.putExtra("path", savedPath);
                sendBroadcast(intent);
                Toast.makeText(context, "Wallpaper Downloaded Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        Save share = new Save();
        share.execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose where to apply");
        menu.add(0, v.getId(), 0, "Lock Screen");
        menu.add(0, v.getId(), 0, "Home Screen");
        menu.add(0, v.getId(), 0, "Both Home and Lock");
    }

    // menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == "Lock Screen") {
            Toast.makeText(getBaseContext(), "Applying Wallpaper...", Toast.LENGTH_SHORT).show();
            try {
                setWallpaper(getBaseContext(), received.getLink(), 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (item.getTitle() == "Home Screen") {
            Toast.makeText(getBaseContext(), "Applying Wallpaper...", Toast.LENGTH_SHORT).show();
            try {
                setWallpaper(getBaseContext(), received.getLink(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (item.getTitle() == "Both Home and Lock") {
            Toast.makeText(getBaseContext(), "Applying Wallpaper...", Toast.LENGTH_SHORT).show();
            try {
                setWallpaper(getBaseContext(), received.getLink(), 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    boolean isIn(ArrayList<Entry> list, Entry recv){
        for (Entry e:list){
            if(recv.getLink().equals( e.getLink())){
                return true;
            }
        }
        return false;
    }

    public void createAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-2960630260772033/1234962299", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });

    }

}






