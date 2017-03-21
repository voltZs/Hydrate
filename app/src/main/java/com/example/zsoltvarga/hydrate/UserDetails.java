package com.example.zsoltvarga.hydrate;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class UserDetails extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if app launches first time ever:
        SharedPreferences sharedPrefFirstTime = getSharedPreferences("firstLaunch",Context.MODE_PRIVATE );
        boolean launchedFirstTime = sharedPrefFirstTime.getBoolean("firstTime", true);
        if(launchedFirstTime){
            Toast.makeText(getApplicationContext(), "Please set up your details", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor firstLaunchEditor = sharedPrefFirstTime.edit();
            firstLaunchEditor.putBoolean("firstTime", false);
            firstLaunchEditor.commit();

            //sets up default main screen buttons
            SharedPreferences sharedPrefSelection = getSharedPreferences("settingButtons", Context.MODE_PRIVATE );
            SharedPreferences.Editor sharedPref = sharedPrefSelection.edit();
            sharedPref.putBoolean("butOne", true);
            sharedPref.putBoolean("butThree", true);
            sharedPref.putBoolean("butFour", true);
            sharedPref.putBoolean("butSix", true);
            sharedPref.commit();

            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            dbHandler.makeUpDatabase();

        }

        setContentView(R.layout.activity_user_details);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //sets toolbar to be the actionbar
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        ActionBar theActionBar =  getSupportActionBar();
        theActionBar.setDisplayHomeAsUpEnabled(true);
        theActionBar.setTitle("User");

        refreshTextViews();

        //as the width of the drawn box references the width of an object that hasnt been rendered, it needs to be accessed
        //through a viewTree (was 0 before rendering)
        final TextView shouldDrinkText = (TextView) findViewById(R.id.shouldDrink) ;

        ViewTreeObserver viewTree = shouldDrinkText.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int widthFinal = findViewById(R.id.shouldDrink).getMeasuredWidth();
              //  int heightFinal = rl.getMeasuredHeight();

                //convert dpi values to pixels
                Resources r = getResources();
                float px1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 165, r.getDisplayMetrics());
                float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());

                Bitmap bitmap = Bitmap.createBitmap(widthFinal+(int)px2, (int)(px1*1.5), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint waterBlue = new Paint();
                waterBlue.setColor(Color.argb(50,44,124,216));
                waterBlue.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),20,20, waterBlue);
                ImageView weightBox = (ImageView) findViewById(R.id.iWeightBox);
                weightBox.setImageBitmap(bitmap);


                float px3 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95, r.getDisplayMetrics());
                Bitmap bitmap2 = Bitmap.createBitmap(widthFinal+(int)px2, (int)px3, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmap2);
                Paint whiteTrans = new Paint();
                whiteTrans.setColor(Color.argb(90,255,255,255));
                whiteTrans.setStyle(Paint.Style.FILL);
                canvas2.drawRoundRect(new RectF(0, 0, canvas2.getWidth(), canvas2.getHeight()),20,20, waterBlue);
                ImageView targetBox = (ImageView) findViewById(R.id.targetBoxW);
                targetBox.setImageBitmap(bitmap2);

                return true;
            }
        });

    }

    public void refreshTextViews(){
        SharedPreferences sharedPrefWeight = getSharedPreferences("weightSaved",Context.MODE_PRIVATE );
        int userWeight = sharedPrefWeight.getInt("weight", (50));

        SharedPreferences sharedPrefWorkout = getSharedPreferences("workoutSaved",Context.MODE_PRIVATE );
        double userWorkout = sharedPrefWorkout.getInt("workout", (0));



        DecimalFormat formatter = new DecimalFormat("#.##");

        TextView textWeight = (TextView) findViewById(R.id.textMass);
        textWeight.setText(userWeight + " kg");
        textWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWeight();
            }
        });

        TextView textWorkout = (TextView) findViewById(R.id.numberOfHours);
        String hourForm = " hours";
        if(userWorkout==1){
            hourForm = " hour";
        }
        textWorkout.setText(formatter.format(userWorkout) + hourForm);
        textWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWorkout();
            }
        });


        SharedPreferences sharedPrefTarget = getSharedPreferences("targetSaved", Context.MODE_PRIVATE );
        SharedPreferences.Editor waterEditor = sharedPrefTarget.edit();
        double targetLitres = (userWeight*0.042)+(userWorkout*0.68);
        waterEditor.putLong("target", Double.doubleToRawLongBits(targetLitres));
        waterEditor.commit();

        TextView textLitres = (TextView) findViewById(R.id.shouldDrink);
        textLitres.setText(formatter.format(targetLitres) + " litres");
    }



    /**
     * creates dialog window for choosing weight
     */
    public void dialogWeight(){

        final Dialog dWeight = new Dialog(UserDetails.this);
        dWeight.setTitle(R.string.choose_weight);

        dWeight.setContentView(R.layout.layout_picker_dialog);
        Button setValue = (Button) dWeight.findViewById(R.id.setButton);
        Button cancel = (Button) dWeight.findViewById(R.id.cancelButton);
        final NumberPicker np = (NumberPicker) dWeight.findViewById(R.id.numberPicker);
        np.setMaxValue(200);
        np.setMinValue(40);
        np.setWrapSelectorWheel(false);


        setValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPrefWeight = getSharedPreferences("weightSaved", Context.MODE_PRIVATE );
                SharedPreferences.Editor waterEditor = sharedPrefWeight.edit();
                waterEditor.putInt("weight", np.getValue());
                waterEditor.commit();
                dWeight.dismiss();
                refreshTextViews();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dWeight.dismiss();
            }
        });
        dWeight.show();
    }

    /**
     * creates dialog window for choosing workout hours
     */
    public void dialogWorkout(){

        final Dialog dWorkout = new Dialog(UserDetails.this);
        dWorkout.setTitle(R.string.choose_workout);
        dWorkout.setContentView(R.layout.layout_picker_dialog);
        Button setValue = (Button) dWorkout.findViewById(R.id.setButton);
        Button cancel = (Button) dWorkout.findViewById(R.id.cancelButton);
        final NumberPicker np = (NumberPicker) dWorkout.findViewById(R.id.numberPicker);
        np.setMaxValue(6);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);


        setValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPrefWorkout = getSharedPreferences("workoutSaved", Context.MODE_PRIVATE );
                SharedPreferences.Editor waterEditor = sharedPrefWorkout.edit();
                waterEditor.putInt("workout", np.getValue());
                waterEditor.commit();
                dWorkout.dismiss();
                refreshTextViews();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dWorkout.dismiss();
            }
        });
        dWorkout.show();
        TextView unit = (TextView) dWorkout.findViewById(R.id.text_unit);
        unit.setText("hrs");
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
