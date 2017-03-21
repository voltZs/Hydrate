package com.example.zsoltvarga.hydrate;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import android.widget.TextView;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class Charts extends AppCompatActivity {

    MyDBHandler dbHandler;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //sets toolbar to be the actionbar
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        ActionBar theActionBar =  getSupportActionBar();
        theActionBar.setDisplayHomeAsUpEnabled(true);
        theActionBar.setTitle("Charts");


        dbHandler = new MyDBHandler(this, null, null, 1);

        //SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:YYYY");
       // String currentDateandTime = sdf.format(new Date());
       // Random rnd = new Random();
       // String date = rnd.nextInt(31)+1 + ":" + (rnd.nextInt(12)+1) + ":" + 2017;
      //  DailyEntry entry = new DailyEntry(date, rnd.nextInt(150));

       // dbHandler.addEntry(entry);

        //dbHandler.deleteTable();


        TextView trialText = (TextView) findViewById(R.id.databaseTrialText);
        trialText.setText(dbHandler.databaseToString());
        trialText.setVisibility(View.GONE);



        //past week graph
        final GraphView graph = (GraphView) findViewById(R.id.graph1);
        LineGraphSeries<DataPoint> pastWeek = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, dbHandler.getPastPercentage(6)),
                new DataPoint(1, dbHandler.getPastPercentage(5)),
                new DataPoint(2, dbHandler.getPastPercentage(4)),
                new DataPoint(3, dbHandler.getPastPercentage(3)),
                new DataPoint(4, dbHandler.getPastPercentage(2)),
                new DataPoint(5, dbHandler.getPastPercentage(1)),
                new DataPoint(6, dbHandler.getPastPercentage(0))
        });
        LineGraphSeries<DataPoint> series100week = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 100),
                new DataPoint(6, 100),
        });
        pastWeek.setColor(Color.WHITE);
        pastWeek.setThickness(8);
        pastWeek.setBackgroundColor(Color.argb(50, 255,255,255));
        pastWeek.setDrawBackground(true);
        series100week.setThickness(4);
        graph.addSeries(series100week);
        graph.addSeries(pastWeek);

        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {
                Integer.toString(labelConditionsWeek(0)),
                Integer.toString(labelConditionsWeek(1)),
                Integer.toString(labelConditionsWeek(2)),
                Integer.toString(labelConditionsWeek(3)),
                Integer.toString(labelConditionsWeek(4)),
                Integer.toString(labelConditionsWeek(5)),
                Integer.toString(labelConditionsWeek(6))
        });
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);



        //second graph
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        LineGraphSeries<DataPoint> pastTwoWeeks = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, dbHandler.getPastPercentage(13)),
                new DataPoint(1, dbHandler.getPastPercentage(12)),
                new DataPoint(2, dbHandler.getPastPercentage(11)),
                new DataPoint(3, dbHandler.getPastPercentage(10)),
                new DataPoint(4, dbHandler.getPastPercentage(9)),
                new DataPoint(5, dbHandler.getPastPercentage(8)),
                new DataPoint(6, dbHandler.getPastPercentage(7)),
                new DataPoint(7, dbHandler.getPastPercentage(6)),
                new DataPoint(8, dbHandler.getPastPercentage(5)),
                new DataPoint(9, dbHandler.getPastPercentage(4)),
                new DataPoint(10, dbHandler.getPastPercentage(3)),
                new DataPoint(11, dbHandler.getPastPercentage(2)),
                new DataPoint(12, dbHandler.getPastPercentage(1)),
                new DataPoint(13, dbHandler.getPastPercentage(0)),
        });
        LineGraphSeries<DataPoint> graph2_100line = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 100),
                new DataPoint(13, 100),
        });
        pastTwoWeeks.setColor(Color.WHITE);
        pastTwoWeeks.setThickness(8);
        pastTwoWeeks.setBackgroundColor(Color.argb(50, 255,255,255));
        pastTwoWeeks.setDrawBackground(true);
        graph2_100line.setThickness(4);
        graph2.addSeries(graph2_100line);
        graph2.addSeries(pastTwoWeeks);

        graph2.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph2.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph2.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph2.getGridLabelRenderer().setNumHorizontalLabels(14);

        StaticLabelsFormatter staticLabelsFormatterTwo = new StaticLabelsFormatter(graph2);
        staticLabelsFormatterTwo.setHorizontalLabels(new String[] {
                Integer.toString(labelConditionsWeekTwo(0)),
                Integer.toString(labelConditionsWeekTwo(1)),
                Integer.toString(labelConditionsWeekTwo(2)),
                Integer.toString(labelConditionsWeekTwo(3)),
                Integer.toString(labelConditionsWeekTwo(4)),
                Integer.toString(labelConditionsWeekTwo(5)),
                Integer.toString(labelConditionsWeekTwo(6)),
                Integer.toString(labelConditionsWeekTwo(7)),
                Integer.toString(labelConditionsWeekTwo(8)),
                Integer.toString(labelConditionsWeekTwo(9)),
                Integer.toString(labelConditionsWeekTwo(10)),
                Integer.toString(labelConditionsWeekTwo(11)),
                Integer.toString(labelConditionsWeekTwo(12)),
                Integer.toString(labelConditionsWeekTwo(13))
        });
        staticLabelsFormatterTwo.setVerticalLabels(new String[] {"low", "middle", "high"});
        graph2.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatterTwo);



        //as the width of the drawn box references the width of the graph, it needs to be accessed
        //through a viewTree (was 0 before rendering)
        ViewTreeObserver viewTree = graph.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int widthOfGraph = graph.getMeasuredWidth();

                //convert dpi values to pixels
                Resources r = getResources();
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 210, r.getDisplayMetrics());
                float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());

                Bitmap bitmap = Bitmap.createBitmap(widthOfGraph+(int)px2,(int) px, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint waterBlue = new Paint();
                waterBlue.setColor(Color.argb(50,44,124,216));
                waterBlue.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),20,20, waterBlue);
                ImageView graphBox1 = (ImageView) findViewById(R.id.graphBackground1);
                graphBox1.setImageBitmap(bitmap);
                ImageView graphBox2 = (ImageView) findViewById(R.id.graphBackground2);
                graphBox2.setImageBitmap(bitmap);
                return true;
            }
        });




    }

    /**
     * Generates the label for the day of the past week - takes data from the existing database
     * @param value - [int} - the number of days to the past
     * @return - calendar date of that day
     */
    public int labelConditionsWeek(int value){
        dbHandler = new MyDBHandler(this, null, null, 1);
        int a = -value+6;
        String[] parts = dbHandler.getPastDay(a).split(":");
        //converting day value to an integer so zeros before one digit numbers are deleted
        int day = Integer.parseInt(parts[0]);
        return day;
    }

    /**
     * Generates the label for the day of the past two weeks - takes data from the existing database
     * @param value - [int} - the number of days to the past
     * @return - calendar date of that day
     */
    public int labelConditionsWeekTwo(int value){
        dbHandler = new MyDBHandler(this, null, null, 1);
        int a = -value+13;
        String[] parts = dbHandler.getPastDay(a).split(":");
        //converting day value to an integer so zeros before one digit numbers are deleted
        int day = Integer.parseInt(parts[0]);
        return day;
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
