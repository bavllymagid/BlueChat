package project.java4.bluechat.preferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import project.java4.bluechat.R;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    Intent starterIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        starterIntent = getIntent();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
    //TODO:
//    private float getFontSizeFromPref(SharedPreferences sharedPreferences){
//        String fontSize = sharedPreferences.getString(getString(R.string.pref_font_size_key), "medium");
//        switch (fontSize){
//            case "small":
//                return 9;
//            case "medium":
//                return 13;
//            case "large":
//                return 15;
//            default:
//                return 12;
//        }
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_theme_color_key))){
            finish();
            startActivity(starterIntent);
        }
    }

    private int getCurrTheme(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themeString = sharedPreferences.getString(getString(R.string.pref_theme_color_key), getString(R.string.pref_theme_color_purple_value));
        if(themeString.equals(getString(R.string.pref_theme_color_purple_value))){
            return R.style.AppTheme;
        }
        else if (themeString.equals(getString(R.string.pref_theme_color_dark_blue_value))){
            return R.style.AppThemeDarkBlue;
        }
        else if (themeString.equals(getString(R.string.pref_theme_color_vibrant_blue_value))){
            return R.style.AppThemeVibrantBlue;
        }
        else if (themeString.equals(getString(R.string.pref_theme_color_grey_value))){
            return R.style.AppThemeGrey;
        }
        return R.style.AppTheme;
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme =  super.getTheme();
        theme.applyStyle(getCurrTheme(), true);
        return theme;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}