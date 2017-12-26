package se.olander.android.four;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate theme: " + getTheme());
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, new LevelFragment())
            .addToBackStack(null)
            .commit();
    }
}
