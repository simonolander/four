package se.olander.android.four;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, SelectLevelFragmentItem.newInstance())
            .addToBackStack(null)
            .commit();
    }
}
