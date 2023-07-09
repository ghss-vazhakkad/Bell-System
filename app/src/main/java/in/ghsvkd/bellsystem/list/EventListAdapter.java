package in.ghsvkd.bellsystem.list;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.malinskiy.materialicons.widget.IconButton;
import in.ghsvkd.bellsystem.ControlActivity;
import in.ghsvkd.bellsystem.databinding.AlertAddEventBinding;
import in.ghsvkd.bellsystem.databinding.ListItemEventsBinding;
import in.ghsvkd.bellsystem.databinding.ListItemWeekBinding;
import in.ghsvkd.bellsystem.obj.Configuration;
import in.ghsvkd.bellsystem.ui.ConfigurationFragment;
import in.ghsvkd.bellsystem.ui.home.EventsFragment;
import java.util.Date;
import java.util.List;
import static in.ghsvkd.bellsystem.list.ConfigurationListAdapter.*;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int itemId;
        public ListItemEventsBinding binding;
        public ViewHolder(ListItemEventsBinding binding,int current) {
            super(binding.getRoot());
            this.binding = binding;
            itemId = current;
            
        }
    }

    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        return new ViewHolder(ListItemEventsBinding.inflate(LayoutInflater.from(arg0.getContext()),arg0,false),arg1);
    }
    @Override
    public void onBindViewHolder(ViewHolder arg0, final int arg1) {
        arg0.itemId = arg1;
        LinearLayout weekList = arg0.binding.listWeek;
        final Configuration.TimeData tData = activeTimeList.get(arg1);
        TextView time = arg0.binding.textTime;
        
        time.setText(ControlListAdapter.parseTime(tData.time));
        IconButton icon = arg0.binding.btnEdit;
        icon.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            AlertDialog.Builder adb = new AlertDialog.Builder(parent.getActivity());
            final AlertAddEventBinding binding = AlertAddEventBinding.inflate(parent.getLayoutInflater());
            adb.setTitle("Set event");
            adb.setView(binding.getRoot());        
            adb.setPositiveButton("OK",new DialogInterface.OnClickListener(){public void onClick(DialogInterface p1,int p2){
                Configuration.TimeData ourTime = new Configuration.TimeData();
                ourTime.time = ControlActivity.getDate(binding.timeSelected.getText()+"");
                int week = ourTime.time.getDay();
                if(week != 0) week -= 1;
                else week = 6;                                                            
                ourTime.day[week] = 1;
                ourTime.sound = SoundListAdapter.getSoundData(binding.soundSelected.getText()+"",parent.soundData);
                lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList.set(arg0.itemId,ourTime);
                Configuration.sortTimeData(lastUsed.configurationList.get(lastUsed.lastCheck).timeDataList);
                notifyDataSetChanged();                                     
            }});
            binding.timeSelected.setText(ControlListAdapter.parseTime(tData.time));
            binding.soundSelected.setText(SoundListAdapter.getLabel(tData.sound));           
            adb.create().show();
            binding.timeSelected.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
                Date k = ControlActivity.getDate(binding.timeSelected.getText()+"");
                TimePickerDialog tpd = new TimePickerDialog(parent.getActivity(),new TimePickerDialog.OnTimeSetListener(){public void onTimeSet(TimePicker tp,int h,int m){
                Date dt = new Date();
                dt.setHours(h);
                dt.setMinutes(m);
                String date = ControlListAdapter.parseTime(dt);
                binding.timeSelected.setText(date);                                                                                                                
            }},k.getHours(),k.getMinutes(),false);
            tpd.show();                
            }});
            binding.soundSelected.setOnClickListener(new View.OnClickListener(){AlertDialog adi;public void onClick(final View v){
            AlertDialog.Builder adb = new AlertDialog.Builder(parent.getActivity());
            adb.setTitle("Select sound");
            RecyclerView rv = new RecyclerView(parent.getActivity()); 
            SoundListAdapter adapter = new SoundListAdapter(parent.getActivity());
            adapter.updateText = binding.soundSelected;
                                                                                        
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(parent.getActivity()));                    
            adb.setView(rv);
                                
            adi = adb.create();                    
            adapter.alertText = adi;            
            adi.show();                                                                                                  
        }});        
                
        }});
        
        String[] weeks = new String[]{"M","T","W","T","F","S","S"};
        for(int i = 0; i < 7;i++){
            TextView wk = ListItemWeekBinding.inflate(LayoutInflater.from(weekList.getContext()),weekList,false).getRoot();
            wk.setText(weeks[i]);
            final int it = i;
            if(tData.day[i] == 0) wk.setTextColor(0x55000000);
            else wk.setTextColor(0xFF000000);
            wk.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
                TextView wk = (TextView) v;        
                Configuration.TimeData tData = activeTimeList.get(arg1);
                if(tData.day[it] == 0){
                    tData.day[it] = 1;
                    wk.setTextColor(0xFF000000);                
                }else{
                    tData.day[it] = 0;
                    wk.setTextColor(0x55000000);                
                }
                                        
            }});
            weekList.addView(wk);
        }
    }
    
    

    @Override
    public int getItemCount() {
        activeTimeList = ConfigurationListAdapter.lastUsed.configurationList.get(current).timeDataList;
        return activeTimeList.size();
    }
    
    public EventsFragment parent;
    public List<Configuration.TimeData> activeTimeList;
    public int current;
    
    public EventListAdapter(EventsFragment parent,int current){
        this.parent = parent;
        this.current = current;
        
        activeTimeList = ConfigurationListAdapter.lastUsed.configurationList.get(current).timeDataList;
    }
}
