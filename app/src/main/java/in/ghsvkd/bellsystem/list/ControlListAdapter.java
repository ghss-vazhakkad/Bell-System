package in.ghsvkd.bellsystem.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import in.ghsvkd.bellsystem.AlarmService;
import in.ghsvkd.bellsystem.databinding.ListItemControlBinding;
import in.ghsvkd.bellsystem.obj.Configuration;

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
    public void onBindViewHolder(ControlListAdapter.ViewHolder arg0, final int arg1) {
        if(lastSize != AlarmService.timeDataList.size()) notifyDataSetChanged();
        arg0.binding.textTime.setText(parseTime(AlarmService.timeDataList.get(arg1).time));
        arg0.binding.btnClose.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            AlarmService.timeDataList.remove(arg1);
            Configuration.sortTimeData(AlarmService.timeDataList);                    
            ControlListAdapter.this.notifyDataSetChanged();
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
}
