package in.ghsvkd.bellsystem.list;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import in.ghsvkd.bellsystem.AlarmService;
import in.ghsvkd.bellsystem.ControlActivity;
import in.ghsvkd.bellsystem.databinding.AlertAddEventBinding;
import in.ghsvkd.bellsystem.databinding.ListItemControlBinding;
import in.ghsvkd.bellsystem.obj.Configuration;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;

public class ControlListAdapter extends RecyclerView.Adapter<ControlListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemControlBinding binding;

        public ViewHolder(ListItemControlBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public ControlListAdapter.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        return new ViewHolder(ListItemControlBinding.inflate(LayoutInflater.from(arg0.getContext()),arg0,false));
    }

    int lastSize = 0;
    @Override
    public void onBindViewHolder( ControlListAdapter.ViewHolder arg0, final int arg1) {
        if(lastSize != AlarmService.timeDataList.size()) notifyDataSetChanged();
        arg0.binding.textTime.setText(parseTime(AlarmService.timeDataList.get(arg1).time));
        arg0.binding.textTime.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
        AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
        adb.setTitle("Set event");
                    
        final List soundData = SoundListAdapter.retreiveSoundFiles(parent);            
                    
        final AlertAddEventBinding binding = AlertAddEventBinding.inflate(LayoutInflater.from(v.getContext()));
        adb.setView(binding.getRoot());
        
        adb.setCancelable(true);
                    
        adb.setPositiveButton("OK",new DialogInterface.OnClickListener(){public void onClick(DialogInterface p1,int p2){
            Configuration.TimeData ourTime = new Configuration.TimeData();
            ourTime.time = ControlActivity.getDate(binding.timeSelected.getText()+"");
            int week = ourTime.time.getDay();
            if(week != 0) week -= 1;
            else week = 6;                                                            
            ourTime.day[week] = 1;
            ourTime.sound = SoundListAdapter.getSoundData(binding.soundSelected.getText()+"",soundData);
            AlarmService.timeDataList.set(arg1,ourTime);
            Date thisTime = new Date();
            int ctime = thisTime.getHours()*60+thisTime.getMinutes();
            int ttime = ourTime.time.getHours()*60+ourTime.time.getMinutes();
            if(ctime != ttime || ctime > ttime){
                Configuration.sortTimeData(AlarmService.timeDataList);                                                           
                notifyDataSetChanged();
            }                                                                                                                    
        }});
                    
                    
        binding.timeSelected.setText(ControlListAdapter.parseTime(AlarmService.timeDataList.get(arg1).time));
        binding.soundSelected.setText(SoundListAdapter.getLabel(AlarmService.timeDataList.get(arg1).sound));
                               
        adb.create().show();
        binding.timeSelected.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            Date k = ControlActivity.getDate(binding.timeSelected.getText()+"");
            
            TimePickerDialog tpd = new TimePickerDialog(v.getContext(),new TimePickerDialog.OnTimeSetListener(){public void onTimeSet(TimePicker tp,int h,int m){
                Date dt = new Date();
                dt.setHours(h);
                dt.setMinutes(m);
                String date = ControlListAdapter.parseTime(dt);
                binding.timeSelected.setText(date);                                                                                                                
            }},k.getHours(),k.getMinutes(),false);
            tpd.show();                    
        }});
        binding.soundSelected.setOnClickListener(new View.OnClickListener(){AlertDialog adi;public void onClick(final View v){
            AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
            adb.setTitle("Select sound");
            RecyclerView rv = new RecyclerView(v.getContext()); 
            SoundListAdapter adapter = new SoundListAdapter(parent);
                                
            adapter.updateText = binding.soundSelected;
                                                                                        
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(v.getContext()));                    
            adb.setView(rv);
                                
            adi = adb.create();                    
            adapter.alertText = adi;            
            adi.show();                                                                                                  
        }});                                                                                                           
           
        }});
        arg0.binding.btnClose.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            AlertDialog.Builder k = new AlertDialog.Builder(parent);
            k.setMessage("Do you want to delete this event ?");
            k.setPositiveButton("Yes",new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int id){
                AlarmService.timeDataList.remove(arg1);
                Configuration.sortTimeData(AlarmService.timeDataList);                    
                ControlListAdapter.this.notifyDataSetChanged();
            }});
            k.setNegativeButton("No",null);   
            k.create().show();                           
        }});
    }
    
    public static String parseTime(java.util.Date time){
        String out = "AM";
        int hour = time.getHours();
        int min = time.getMinutes();
        if(hour > 11) { out = "PM" ; hour -= 12; if(hour == 0) hour=12 ;}
        
        String ex = "";
        if(min < 10) ex = "0";
        
        return hour+":"+ex+min+" "+out;
    }

    @Override
    public int getItemCount() {
        lastSize = AlarmService.timeDataList.size();
        return lastSize;
    }
    
    public Activity parent;
    
    public ControlListAdapter(Activity parent){
        this.parent = parent;
    }
    
}
