package com.sensors.florinrotaru.checksensors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MenuItem darkTheme;
    MenuItem lightTheme;
    int counter = 0;
    Fragment baseFragment;
    FragmentTransaction transaction;
    Vibrator v;
    String prefTheme;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        retrieveSharedPrefs();

        if (!"dark".equals(prefTheme)) {
            setSharedPrefs("light");
            setTheme(R.style.AppThemeLight);


        } else {
            setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        baseFragment = new BaseFragment(prefTheme, context);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, baseFragment);
        transaction.addToBackStack(null);
        transaction.commit();



    }
    private void vibrate () {
        v.vibrate(1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.useDarkTheme) {
            showAlertDialog("dark");

        }
        if (id == R.id.useLightTheme) {
            showAlertDialog("light");

        }
        return super.onOptionsItemSelected(item);
    }

    //Creates the three dot menu in top right corner, that has the option to log out
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        darkTheme = menu.findItem(R.id.useDarkTheme);
        lightTheme = menu.findItem(R.id.useLightTheme);
        if ("light".equals(prefTheme))
        {
        darkTheme.setVisible(true);
        lightTheme.setVisible(false);

        }
        if ("dark".equals(prefTheme))
        {
            lightTheme.setVisible(true);
            darkTheme.setVisible(false);
        }
        return true;
    }

    private String getPrefTheme (){
        return prefTheme;
    }

    private void showAlertDialog (final String theme){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Change Theme")
                .setMessage("Change the current theme? (Restart required)")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (theme.equals("dark"))
                        {
                            Toast.makeText(context, "dark theme reg", Toast.LENGTH_SHORT).show();
                            setSharedPrefs("dark");
                            System.exit(0);
                        }
                        if (theme.equals("light"))
                        {
                            Toast.makeText(context, "light theme reg", Toast.LENGTH_SHORT).show();
                            setSharedPrefs("light");
                            System.exit(0);
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void retrieveSharedPrefs() {
        SharedPreferences pref = context.getSharedPreferences("Theme_Preference", MODE_PRIVATE);
        prefTheme = pref.getString("theme", null);   // get the preferred theme
    }

    private void setSharedPrefs(String theme){
        SharedPreferences pref = context.getSharedPreferences("Theme_Preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("theme", theme);
        editor.commit();
    }
}
