package com.rgs.oes;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.oes.Auth.Login;
import com.rgs.oes.Event.Events;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navView;
    DrawerLayout drawerLayout;
    TextView nav_namec, nav_emailc, nav_rollno;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    static Home instance;
    CharSequence s;
    String role;
    BarChart barChart;
    FirebaseAuth firebaseAuth;
    PieChart pieChart ;
    PieDataSet pieDataSet ;
    List<PieEntry> entries;
    PieData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = this;
        firebaseAuth = FirebaseAuth.getInstance();


        {

            toolbar = findViewById(R.id.toolbar);
            navView = findViewById(R.id.nav_view);
            drawerLayout = findViewById(R.id.drawer_layout);
            setSupportActionBar(toolbar);
        }

        {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            navView.setNavigationItemSelectedListener(this);

        }

        {
            barChart = findViewById(R.id.barchart);
            //  barChart.setOnChartValueSelectedListener(DetailsDisplay.this);

            barChart.setDrawBarShadow(false);
            barChart.setTouchEnabled(false);
            barChart.setDragEnabled(false);
            barChart.setScaleEnabled(false);
            barChart.setScaleXEnabled(false);
            barChart.setScaleYEnabled(false);
            barChart.setDrawValueAboveBar(true);

            barChart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the barChart, no values will be
            // drawn
            barChart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(false);

            barChart.setDrawGridBackground(false);
            // barChart.setDrawYLabels(false);

            //    ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            //   xAxis.setValueFormatter(xAxisFormatter);

            //   ValueFormatter custom = new MyValueFormatter("$");

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setLabelCount(8, false);
            //  leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setLabelCount(8, false);
            //  rightAxis.setValueFormatter(custom);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

//            XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//            mv.setChartView(barChart); // For bounds control
//            barChart.setMarker(mv);
        }
            pieChart = (PieChart) findViewById(R.id.chart1);



            //PieEntryLabels = new ArrayList<String>();

            AddValuesToPIEENTRY();

            pieDataSet = new PieDataSet(entries, "n");

            pieData = new PieData(pieDataSet);

            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            pieChart.setData(pieData);

            pieChart.animateY(3000);

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);

        //TODO: Teahc diff

        role = sharedPreferences.getString("Role","0");
        if(role.equals("0")){
            navView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navView.getMenu();
            nav_Menu.findItem(R.id.nav_teacher).setVisible(false);
        } else if (role.equals("1")){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Teachers/" + firebaseAuth.getUid());
            databaseReference.child("Name").setValue(sharedPreferences.getString("name","0"));
        }

        if (sharedPreferences.getString("Admin","0").equals("1")){
            Toast.makeText(instance, "asdad", Toast.LENGTH_SHORT).show();
        }

        navviewdata();

        Date d = new Date();
        s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Last_used_on/" + sharedPreferences.getString("uid","Not aval"));
        databaseReference.child("Name").setValue(sharedPreferences.getString("name","Not aval"));
        databaseReference.child("Email").setValue(sharedPreferences.getString("email","Not aval"));
        databaseReference.child("Date").setValue(s);
        setDataBar();

    }
    public void AddValuesToPIEENTRY(){
        entries= new ArrayList<>();
        entries.add(new PieEntry(18.5f,"green"));
        entries.add(new PieEntry(26.7f, "yellow"));
        entries.add(new PieEntry(24.0f,"red"));
        entries.add(new PieEntry(30.8f,"blue"));

    }


    public static Home getInstance() {
        return instance;
    }

    public void navviewdata() {
        View nav_view = navView.getHeaderView(0);
        nav_emailc = nav_view.findViewById(R.id.nav_email);
        nav_namec = nav_view.findViewById(R.id.nav_name);
        nav_rollno = nav_view.findViewById(R.id.nav_rollno);
        String fb_name_main = sharedPreferences.getString("name", "NO data found");
        String fb_email_main = sharedPreferences.getString("email", "NO data found");
        nav_rollno.setText(sharedPreferences.getString("rollno", "NO data found"));
        nav_namec.setText(fb_name_main);
        nav_emailc.setText(fb_email_main);
        setTitle("Hi, " + fb_name_main);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();l
            showexitDialog();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(Home.this , Profile.class));
        } else if (id == R.id.nav_events){
            startActivity(new Intent(Home.this , Events.class));
        } else if (id == R.id.nav_Statistics){
            Toast.makeText(instance, "Stats", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_notification){
            Toast.makeText(instance, "Notifications", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_signout){
            showsignoutDialog();
        }  else if (id == R.id.nav_teacher){
            startActivity(new Intent(Home.this , Teacher.class));
        } else if (id == R.id.nav_about){
            Toast.makeText(instance, "About", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_help){
            Toast.makeText(instance, "Help", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static int i = 0;

    public void onClick_nav(View view) {
        i++;

        if (i == 2) {
            Toast.makeText(instance, "Your are one click away from entering Admin panel", Toast.LENGTH_SHORT).show();
        } else if (i == 3) {
            if(sharedPreferences.getString("Admin" , "0").equals("1")){
                startActivity(new Intent(Home.this , Adminactivity.class));
            } else {
                Toast.makeText(instance, "You do not have admin rights", Toast.LENGTH_SHORT).show(); }
            i = 0;
        }
    }

    //Exit Dialog
    private void showexitDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.exitdialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void showsignoutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.signout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, Login.class));
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void setDataBar() {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();


        values.add(new BarEntry(1f, 10));
        values.add(new BarEntry(2f, 20));
        values.add(new BarEntry(3f, 30));
        values.add(new BarEntry(4f, 40));
        values.add(new BarEntry(5f, 50));

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Temp");

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));


        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "Stats of student ");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            barChart.setData(data);
        }


    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }

}