package com.example.zsoltvarga.hydrate;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    Bitmap bitmap = Bitmap.createBitmap(180, 520, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //sets toolbar to be the actionbar
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        //if app launched first time, user sent to set up his details
        SharedPreferences sharedPrefFirstTime = getSharedPreferences("firstLaunch",Context.MODE_PRIVATE );
        boolean launchedFirstTime = sharedPrefFirstTime.getBoolean("firstTime", true);
        if(launchedFirstTime){
            Intent setUp = new Intent (this, UserDetails.class);
            startActivity(setUp);
        }

        addEntryOnNewDay();

        ActionBar theActionBar =  getSupportActionBar();
        theActionBar.setDisplayHomeAsUpEnabled(false);
        theActionBar.setTitle("Hydrate");

        ImageView one = new ImageView(this);
        one.setImageResource(R.mipmap.p100ml);
        one.setId(R.id.measure1java);

        ImageView two = new ImageView(this);
        two.setImageResource(R.mipmap.p200ml);
        two.setId(R.id.measure2java);

        ImageView three = new ImageView(this);
        three.setImageResource(R.mipmap.p300ml);
        three.setId(R.id.measure3java);

        ImageView four = new ImageView(this);
        four.setImageResource(R.mipmap.p500ml);
        four.setId(R.id.measure4java);

        ImageView five = new ImageView(this);
        five.setImageResource(R.mipmap.p750ml);
        five.setId(R.id.measure5java);

        ImageView six = new ImageView(this);
        six.setImageResource(R.mipmap.p1liter);
        six.setId(R.id.measure6java);


        drawMeasures(one, R.id.iDrankText, "butOne", 100);

        drawMeasures(two, R.id.measure1java, "butTwo", 200);

        drawMeasures(three, R.id.measure2java, "butThree", 300);

        drawMeasures(four, R.id.measure3java, "butFour", 500);

        drawMeasures(five, R.id.measure4java, "butFive", 750);

        drawMeasures(six, R.id.measure5java, "butSix", 1000);

        refreshWater();




        //as the width of the drawn box references the width of an object that hasnt been rendered, it needs to be accessed
        //through a viewTree (was 0 before rendering)
        final TextView iDrankRef = (TextView) findViewById(R.id.iDrankText) ;
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rightMainPanelInside);
        ViewTreeObserver viewTree = iDrankRef.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int widthFinal = iDrankRef.getMeasuredWidth();
                int heightFinal = rl.getMeasuredHeight();

                //convert dpi values to pixels
                Resources r = getResources();
                float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());

                Bitmap bitmap = Bitmap.createBitmap(widthFinal+(int)px2,heightFinal+(int)px2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint waterBlue = new Paint();
                waterBlue.setColor(Color.argb(50,44,124,216));
                waterBlue.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),20,20, waterBlue);
                ImageView drinksBox = (ImageView) findViewById(R.id.drinksBoxMain);
                drinksBox.setImageBitmap(bitmap);
                return true;
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        addEntryOnNewDay();
    }


    /**
     * Creates a new entry in the database when the date changes
     */
    public void addEntryOnNewDay(){
        //this needs to be in the onCreate / onResume of Main activity as it is crucial this is called before measures are added
        //pushes yesterday's data up a row, so it wouldnt be deleted when adding a new measure
        if(newDay()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy");
            String date = sdf.format(new Date());

            SharedPreferences sharedPrefTarget = getSharedPreferences("targetSaved",Context.MODE_PRIVATE );
            double targetLitres = Double.longBitsToDouble(sharedPrefTarget.getLong("target", Double.doubleToLongBits(0)));
            double retainedFulness = ((double)(getWaterPreferences())/1000/targetLitres);
            int percentage = (int)(100*retainedFulness);
            saveWaterPreferences(0);
            DailyEntry entry = new DailyEntry(date, percentage);
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            dbHandler.addEntry(entry);
        }
    }


    /**
     * checks whether the day of the last entry in the database matches with the current date, returns true if match found
     */

    public boolean newDay(){
        boolean newday = false;
        String[] parts = dbHandler.getPastDay(0).split(":");
        int oldDay = Integer.parseInt(parts[0]);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        int currentDay = Integer.parseInt(sdf.format(new Date()));
        if (currentDay != oldDay){
            newday = true;
        }
        return newday;
    }

    /**
     *Draws the measure buttons
     */
    public void drawMeasures(ImageView x, int objectAbove, String preferenceName, int milimeters){
        SharedPreferences sharedPrefSelection = getSharedPreferences("settingButtons", Context.MODE_PRIVATE );
        RelativeLayout rightPanel = (RelativeLayout) findViewById(R.id.rightMainPanelInside);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, r.getDisplayMetrics());
        float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, r.getDisplayMetrics());
        final int milis = milimeters;
        final ImageView measure = x;


        if(sharedPrefSelection.getBoolean(preferenceName, false)==false){
            px = 0;
            px2 = 0;
        }

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                (int)px
        );
        imageParams.setMargins(0, (int)px2, 0, 0);
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageParams.addRule(RelativeLayout.BELOW, objectAbove);

        if(sharedPrefSelection.getBoolean(preferenceName, true)==false){
            measure.setVisibility(View.INVISIBLE);
        }


        rightPanel.addView(measure, imageParams);

        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementWater(milis);
                refreshWater();
                playDrop();
            }
        });
    }

    /**
     * plays the sound of water drop
     */
    public void playDrop(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.singlewaterdrop);

        if(mp!=null){
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mediaplayer) {
                    mp.stop();
                    mp.release();
                }
            });
            mp.start();
        }

    }

    /**
     * Raises the water level by calling the getWaterPreferences() method and incrementing it by 300ml
     */
    public void incrementWater(int incrementMililiters){
        saveWaterPreferences(getWaterPreferences()+incrementMililiters);
        Toast.makeText(getApplicationContext(), incrementMililiters + " mililiters drunk", Toast.LENGTH_SHORT).show();
    }


    /**
     * Draws the water inside the water bottle
     */
    public void refreshWater(){
        //null the water
      //  saveWaterPreferences(0);


        SharedPreferences sharedPrefTarget = getSharedPreferences("targetSaved",Context.MODE_PRIVATE );
        double targetLitres = Double.longBitsToDouble(sharedPrefTarget.getLong("target", Double.doubleToLongBits(0)));
        double retainedFulness = ((double)(getWaterPreferences())/1000/targetLitres);

        Paint waterBlue = new Paint();
        waterBlue.setColor(Color.argb(255,44,124,216));
        waterBlue.setStyle(Paint.Style.FILL);

        int waterLoaderSize = canvas.getHeight()-(int)(canvas.getHeight()*retainedFulness);

        canvas.drawRect(0, waterLoaderSize, canvas.getWidth(), canvas.getHeight(), waterBlue);
        ImageView waterLoad = (ImageView) findViewById(R.id.waterLoader);
        waterLoad.setImageBitmap(bitmap);

        TextView percentage = (TextView) findViewById(R.id.percentage);
        percentage.setText((int)(100*retainedFulness) + "%");

        //overwrite last entry in database
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy");
        String today = sdf.format(new Date());
        dbHandler.deleteEntry(today);
        DailyEntry entry = new DailyEntry(today, (int)(100*retainedFulness));
        dbHandler.addEntry(entry);
    }

    /**
     * Uses SharedPreferences to return the "fulness" value of the water bottle
     * @return [double] retained is a value > 0, where 1 = 100% fulness
     */
    public int getWaterPreferences(){
        SharedPreferences sharedPrefWater = getSharedPreferences("waterSaved",Context.MODE_PRIVATE );
        int retained = sharedPrefWater.getInt("fulness", 0);
        return retained;

    }

    /**
     * Uses SharedPreferences to store the water level value of the bottle
     * @param ab [double] is a value > 0, where 1 = 100% fulness
     */
    public void saveWaterPreferences(int ab){
        SharedPreferences sharedPrefWater = getSharedPreferences("waterSaved", Context.MODE_PRIVATE );
        SharedPreferences.Editor waterEditor = sharedPrefWater.edit();
        waterEditor.putInt("fulness", ab);
        waterEditor.commit();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //toolbar methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.user_details:
                Intent startUserDetails = new Intent(this, UserDetails.class);
                startActivity(startUserDetails);
                return true;
            case R.id.charts:
                Intent startCharts = new Intent(this, Charts.class);
                startActivity(startCharts);
                return true;
            case R.id.settings:
                Intent startSettings = new Intent(this, Settings.class);
                startActivity(startSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
