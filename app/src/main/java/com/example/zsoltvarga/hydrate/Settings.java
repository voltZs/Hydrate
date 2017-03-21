package com.example.zsoltvarga.hydrate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences sharedPrefSelection = getSharedPreferences("settingButtons", Context.MODE_PRIVATE );
        final SharedPreferences.Editor sharedPref = sharedPrefSelection.edit();



        final ImageView firstRec = (ImageView) findViewById(R.id.settingsOneWhite);
        final ImageView firstImg =(ImageView) findViewById(R.id.settingsOne);
        handleButtonTasks(firstRec, firstImg, "butOne");

        final ImageView secondRec = (ImageView) findViewById(R.id.settingsTwoWhite);
        final ImageView secondImg =(ImageView) findViewById(R.id.settingsTwo);
        handleButtonTasks(secondRec, secondImg, "butTwo");

        final ImageView thirdRec = (ImageView) findViewById(R.id.settingsThreeWhite);
        final ImageView thirdImg =(ImageView) findViewById(R.id.settingsThree);
        handleButtonTasks(thirdRec, thirdImg, "butThree");

        final ImageView fourthRec = (ImageView) findViewById(R.id.settingsFourWhite);
        final ImageView fourthImg =(ImageView) findViewById(R.id.settingsFour);
        handleButtonTasks(fourthRec, fourthImg, "butFour");

        final ImageView fifthRec = (ImageView) findViewById(R.id.settingsFiveWhite);
        final ImageView fifthImg =(ImageView) findViewById(R.id.settingsFive);
        handleButtonTasks(fifthRec, fifthImg, "butFive");

        final ImageView sixthRec = (ImageView) findViewById(R.id.settingsSixWhite);
        final ImageView sixthImg =(ImageView) findViewById(R.id.settingsSix);
        handleButtonTasks(sixthRec, sixthImg, "butSix");




        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //sets toolbar to be the actionbar
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        ActionBar theActionBar =  getSupportActionBar();
        theActionBar.setDisplayHomeAsUpEnabled(true);
        theActionBar.setTitle("Settings");


        //as the width of the drawn box references the width of an object that hasnt been rendered, it needs to be accessed
        //through a viewTree (was 0 before rendering)
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.contain_buttons_settings);
        final TextView topText = (TextView) findViewById(R.id.textChooseButtons);
        ViewTreeObserver viewTree = rl.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int widthFinal = rl.getMeasuredWidth();
                int heightFinal = rl.getMeasuredHeight();

                //convert dpi values to pixels
                Resources r = getResources();
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());

                Bitmap bitmap = Bitmap.createBitmap(widthFinal+(int)px, heightFinal+(int)px, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint waterBlue = new Paint();
                waterBlue.setColor(Color.argb(50,44,124,216));
                waterBlue.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),20,20, waterBlue);
                ImageView settingsBox = (ImageView) findViewById(R.id.settingsBox);
                settingsBox.setImageBitmap(bitmap);


                heightFinal = topText.getMeasuredHeight();

                float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());

                bitmap = Bitmap.createBitmap(widthFinal+(int)px, heightFinal+(int)px2, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                canvas.drawRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),20,20, waterBlue);
                ImageView settingsBoxTop = (ImageView) findViewById(R.id.settingsTopBox);
                settingsBoxTop.setImageBitmap(bitmap);

                return true;
            }
        });



    }


    /**
     * Handles all tasks for the button
     * @param imageViewWhite [ImageView] - background image view that containes a white rectangle
     * @param imageViewButton [ImageView] - foreground, contains buttom inage itself
     * @param prefString [String] - name of the SharedPreference file where the button's selection is stored as boolean
     */
    public void handleButtonTasks(ImageView imageViewWhite, ImageView imageViewButton, String prefString){
        final SharedPreferences sharedPrefSelection = getSharedPreferences("settingButtons", Context.MODE_PRIVATE );
        final SharedPreferences.Editor sharedPref = sharedPrefSelection.edit();
        final ImageView tapper = imageViewWhite;
        final String prefFile = prefString;

        drawBackgroundTappers(tapper);
        checkVisibilityTapper(sharedPrefSelection, tapper, prefFile);

        imageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefSelection.getBoolean(prefFile, false) == true && countActiveMeasures()==1){
                    Toast.makeText(getApplicationContext(), "At least one measure needs to be chosen!", Toast.LENGTH_SHORT).show();
                }else {
                    sharedPref.putBoolean(prefFile, !(sharedPrefSelection.getBoolean(prefFile, false)));
                    sharedPref.commit();
                }

                drawBackgroundTappers(tapper);
                checkVisibilityTapper(sharedPrefSelection, tapper, prefFile);

            }
        });
    }

    /**
     * Counts the number of buttons currently selected
     */

    public int countActiveMeasures(){
        String[] buttons = {"butOne", "butTwo", "butThree", "butFour", "butFive", "butSix"};
        int actives = 0;
        final SharedPreferences sharedPrefSelection = getSharedPreferences("settingButtons", Context.MODE_PRIVATE );
        for(int i=0; i<buttons.length; i++){
            if(sharedPrefSelection.getBoolean(buttons[i], false)){
                actives++;
            }
        }
        return actives;
    }
    /**
     * Check whether array contains less than five "ones"
     * @param array
     * @return [boolean]
     */
    public boolean checkIfLessThanFive(int[] array){
        int count = 0;
        for(int number:array){
            if(number==1){
                count++;
            }
        }
        if(count<=4){
            return true;
        } else {
            return false;
        }
    }


    /**
     * checks if background tapper should be visible
     * @param a [SharedPrefernces] where visibility value is stored
     * @param b [ImageView] of background tapper
     * @param c [String] name of the SharedPreference file
     */
    public void checkVisibilityTapper(SharedPreferences a, ImageView b, String c){
        if((a.getBoolean(c, false))==true){
            b.setVisibility(View.VISIBLE);
        }
        if((a.getBoolean(c, false))==false){
            b.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * draws a single transparent white rectangle a time
     * @param passedIn [ImageView} for where to draw this rectangle
     */
    public void drawBackgroundTappers(ImageView passedIn){
        Bitmap bitmap = Bitmap.createBitmap(120, 42, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint transWhite = new Paint();

        transWhite.setColor(Color.argb(90,255,255,255));
        transWhite.setStyle(Paint.Style.FILL);

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), transWhite);
        passedIn.setImageBitmap(bitmap);
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
