package in.ghsvkd.bellsystem.list;

import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.recyclerview.widget.RecyclerView;
import in.ghsvkd.bellsystem.databinding.ListItemEventsBinding;
import in.ghsvkd.bellsystem.databinding.ListItemWeekBinding;
import in.ghsvkd.bellsystem.obj.Configuration;
import in.ghsvkd.bellsystem.ui.ConfigurationFragment;
import in.ghsvkd.bellsystem.ui.home.EventsFragment;
import java.util.Date;
import java.util.List;

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
        Configuration.TimeData tData = activeTimeList.get(arg1);
        TextView time = arg0.binding.textTime;
        time.setText(ControlListAdapter.parseTime(tData.time));
        
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
