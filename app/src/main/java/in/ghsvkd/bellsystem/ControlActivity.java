package in.ghsvkd.bellsystem;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import in.ghsvkd.bellsystem.databinding.ActivityControlBinding;
import in.ghsvkd.bellsystem.databinding.AlertAddEventBinding;
import in.ghsvkd.bellsystem.databinding.ListItemSoundsBinding;
import in.ghsvkd.bellsystem.list.ControlListAdapter;
import in.ghsvkd.bellsystem.list.SoundListAdapter;
import in.ghsvkd.bellsystem.obj.Configuration;
import java.util.Date;
import java.util.List;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener{
  public ActivityControlBinding binding;
  public List<String> soundData; 
  public static ControlActivity current;  
     
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityControlBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
        
    setSupportActionBar(binding.toolbar);

    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
    binding.recyclerControl.setLayoutManager(new LinearLayoutManager(this));
    binding.recyclerControl.setAdapter(new ControlListAdapter());
    soundData = SoundListAdapter.retreiveSoundFiles(this);
        binding.fabControl.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
        AlertDialog.Builder adb = new AlertDialog.Builder(ControlActivity.this);
        adb.setTitle("Set event");
        final AlertAddEventBinding binding = AlertAddEventBinding.inflate(getLayoutInflater());
        adb.setView(binding.getRoot());
        adb.setCancelable(true);
                    
        adb.setPositiveButton("OK",new DialogInterface.OnClickListener(){public void onClick(DialogInterface p1,int p2){
            Configuration.TimeData ourTime = new Configuration.TimeData();
            ourTime.time = getDate(binding.timeSelected.getText()+"");
            int week = ourTime.time.getDay();
            if(week != 0) week -= 1;
            else week = 6;                                                            
            ourTime.day[week] = 1;
            ourTime.sound = SoundListAdapter.getSoundData(binding.soundSelected.getText()+"",soundData);
            AlarmService.timeDataList.add(ourTime);
            Date thisTime = new Date();
            int ctime = thisTime.getHours()*60+thisTime.getMinutes();
            int ttime = ourTime.time.getHours()*60+ourTime.time.getMinutes();
            if(ctime != ttime || ctime > ttime){
                Configuration.sortTimeData(AlarmService.timeDataList);                                                           
                ControlActivity.this.binding.recyclerControl.getAdapter().notifyDataSetChanged();
            }                                                                                                                    
        }});
                    
                    
        binding.timeSelected.setText(ControlListAdapter.parseTime(new java.util.Date()));
        binding.soundSelected.setText("Bell");           
        adb.create().show();
        binding.timeSelected.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            Date k = getDate(binding.timeSelected.getText()+"");
            TimePickerDialog tpd = new TimePickerDialog(ControlActivity.this,new TimePickerDialog.OnTimeSetListener(){public void onTimeSet(TimePicker tp,int h,int m){
                Date dt = new Date();
                dt.setHours(h);
                dt.setMinutes(m);
                String date = ControlListAdapter.parseTime(dt);
                binding.timeSelected.setText(date);                                                                                                                
            }},k.getHours(),k.getMinutes(),false);
            tpd.show();                    
        }});
        binding.soundSelected.setOnClickListener(new View.OnClickListener(){AlertDialog adi;public void onClick(final View v){
            AlertDialog.Builder adb = new AlertDialog.Builder(ControlActivity.this);
            adb.setTitle("Select sound");
            RecyclerView rv = new RecyclerView(ControlActivity.this); 
            SoundListAdapter adapter = new SoundListAdapter(ControlActivity.this);
            adapter.updateText = binding.soundSelected;
                                                                                        
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(ControlActivity.this));                    
            adb.setView(rv);
                                
            adi = adb.create();                    
            adapter.alertText = adi;            
            adi.show();                                                                                                  
        }});                                                                                                           
    }});
        current = this;    
  }
    @Override
    public void onClick(View v){
        
    }
    
    
    public static Date getDate(String time){
        Date date = new Date();
        String ntime = time+"";
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.substring(time.indexOf(":")+1,time.indexOf(" ")));
        if(time.contains("PM") && hour != 12) hour += 12;
        else if(time.contains("AM") && hour == 12){
            hour = 0;
        }
        date.setHours(hour);
        date.setMinutes(minute);
        return date;
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
