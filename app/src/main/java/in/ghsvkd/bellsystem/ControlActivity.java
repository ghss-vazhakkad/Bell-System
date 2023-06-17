package in.ghsvkd.bellsystem;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import in.ghsvkd.bellsystem.databinding.ActivityControlBinding;
import in.ghsvkd.bellsystem.list.ControlListAdapter;

public class ControlActivity extends AppCompatActivity {
  ActivityControlBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityControlBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);

    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    binding.recyclerControl.setLayoutManager(new LinearLayoutManager(this));
    binding.recyclerControl.setAdapter(new ControlListAdapter());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem arg0) {
        if(arg0.getItemId() == android.R.id.home) {
           finish(); 
           return true;
        }
            
    return super.onOptionsItemSelected(arg0);
  }
}
