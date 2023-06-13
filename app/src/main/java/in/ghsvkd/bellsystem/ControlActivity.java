package in.ghsvkd.bellsystem;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;

public class ControlActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Window w = getWindow();
        w.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        w.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        w.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
        w.addFlags(LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }
}
