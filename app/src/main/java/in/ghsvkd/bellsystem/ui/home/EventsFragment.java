package in.ghsvkd.bellsystem.ui.home;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.malinskiy.materialicons.Iconify;
import in.ghsvkd.bellsystem.ControlActivity;
import in.ghsvkd.bellsystem.MainActivity;
import in.ghsvkd.bellsystem.databinding.AlertAddEventBinding;
import in.ghsvkd.bellsystem.databinding.FragmentEventsBinding;
import in.ghsvkd.bellsystem.databinding.ListItemSoundsBinding;
import in.ghsvkd.bellsystem.list.ConfigurationListAdapter;
import static in.ghsvkd.bellsystem.list.ConfigurationListAdapter.*;
import in.ghsvkd.bellsystem.list.ControlListAdapter;
import in.ghsvkd.bellsystem.list.EventListAdapter;
import in.ghsvkd.bellsystem.list.SoundListAdapter;
import in.ghsvkd.bellsystem.obj.Configuration;
import in.ghsvkd.bellsystem.ui.ConfigurationFragment;
import java.util.Date;
import java.util.List;
import in.ghsvkd.bellsystem.R;
import in.ghsvkd.bellsystem.list.EventListAdapter.ViewHolder;


public class EventsFragment extends Fragment{

    public FragmentEventsBinding binding;
    public List<String> soundData;
    
    
    @Override
    @MainThread
    @Nullable
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        binding = FragmentEventsBinding.inflate(arg0,arg1,false);
        MainActivity ma = (MainActivity) getActivity();
        ma.getSupportActionBar().setTitle(lastUsed.configurationList.get(lastUsed.lastCheck).label);
        soundData = SoundListAdapter.retreiveSoundFiles(getActivity());
        
        binding.recyclerEvents.setAdapter(new EventListAdapter(this,lastUsed.lastCheck));
        binding.recyclerEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fabAddEvent.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            final AlertAddEventBinding binding = AlertAddEventBinding.inflate(getLayoutInflater());
            adb.setTitle("Set event");
            adb.setView(binding.getRoot());        
            adb.setPositiveButton("OK",new DialogInterface.OnClickListener(){public void onClick(DialogInterface p1,int p2){
                Configuration.TimeData ourTime = new Configuration.TimeData();
                ourTime.time = ControlActivity.getDate(binding.timeSelected.getText()+"");
                int week = ourTime.time.getDay();
                if(week != 0) week -= 1;
                else week = 6;                                                            
                ourTime.day[week] = 1;
                ourTime.sound = SoundListAdapter.getSoundData(binding.soundSelected.getText()+"",soundData);
                lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList.add(ourTime);
                Configuration.sortTimeData(lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList);
                EventsFragment.this.binding.recyclerEvents.getAdapter().notifyDataSetChanged();
            }});
            binding.timeSelected.setText(ControlListAdapter.parseTime(new java.util.Date()));
            binding.soundSelected.setText("Bell");           
            adb.create().show();
            binding.timeSelected.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            Date k = ControlActivity.getDate(binding.timeSelected.getText()+"");
            TimePickerDialog tpd = new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener(){public void onTimeSet(TimePicker tp,int h,int m){
                Date dt = new Date();
                dt.setHours(h);
                dt.setMinutes(m);
                String date = ControlListAdapter.parseTime(dt);
                binding.timeSelected.setText(date);                                                                                                                
            }},k.getHours(),k.getMinutes(),false);
            tpd.show();                    
        }});            
        binding.soundSelected.setOnClickListener(new View.OnClickListener(){AlertDialog adi;public void onClick(final View v){
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle("Select sound");
            RecyclerView rv = new RecyclerView(getActivity()); 
            SoundListAdapter adapter = new SoundListAdapter(getActivity());
            adapter.updateText = binding.soundSelected;
                                                                                        
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));                    
            adb.setView(rv);
                                
            adi = adb.create();                    
            adapter.alertText = adi;            
            adi.show();                                                                                                  
        }});        
                
        }});
        
            new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
              @Override
              public int getMovementFlags(RecyclerView arg0, RecyclerView.ViewHolder arg1) {
                ViewHolder vh = (ViewHolder) arg1;
                return makeMovementFlags(0, ItemTouchHelper.LEFT);
              }

              @Override
              public boolean onMove(
                 RecyclerView arg0, RecyclerView.ViewHolder arg1, RecyclerView.ViewHolder arg2) {

                return false;
              }
              ViewHolder viewHolder;
              @Override
              public void onSwiped(RecyclerView.ViewHolder arg0, final int arg1) {
                viewHolder = (ViewHolder) arg0;
                    getActivity()
                    .runOnUiThread(
                        new Runnable() {
                            
                          boolean isCorrect = true;
                          public void run() {
                            final Configuration.TimeData tData = lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList.get(viewHolder.itemId);
                            lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList.remove(tData);
                            Snackbar sb =
                                Snackbar.make(
                                    binding.getRoot(),
                                    "Removed",
                                    Snackbar.LENGTH_SHORT);
                            
                                sb.setAction(
                                "Undo",
                                new View.OnClickListener() {
                                  @Override
                                  public void onClick(View arg0) {
                                      isCorrect = false;
                                      binding.recyclerEvents.getAdapter().notifyDataSetChanged();      
                                  }
                                });
                            sb.setCallback(
                                new Snackbar.Callback() {

                                  @Override
                                  public void onDismissed(Snackbar arg0, int arg1) {
                                            
                                    if(!isCorrect){ 
                                       lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList.add(tData);
                                       Configuration.sortTimeData(lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList);         
                                    }
                                    binding.recyclerEvents.getAdapter().notifyDataSetChanged();  
                                    
                                    super.onDismissed(arg0, arg1);
                                  }
                                });
                            sb.setDuration(800);
                            binding.recyclerEvents.getAdapter().notifyDataSetChanged();  
                            sb.show(); 
                          }
                        });
                    
                    
              }
            })
        .attachToRecyclerView(binding.recyclerEvents);
        
        
        return binding.getRoot();
    }
        
}
