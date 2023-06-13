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
    }
}
