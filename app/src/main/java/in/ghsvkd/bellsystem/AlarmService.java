package in.ghsvkd.bellsystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;
import android.os.IBinder;
import android.content.Intent;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import in.ghsvkd.bellsystem.obj.Configuration;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmService extends Service {

  public static boolean active;
    public static AlarmService current;
  public static List<Configuration> configurations;
  public static List<Configuration.TimeData> timeDataList;

  @Override
  public IBinder onBind(Intent arg0) {

    return null;
  }

  @Override
  public void onRebind(Intent arg0) {
    super.onRebind(arg0);
    // TODO: Implement this method
  }

  @Override
  public void onStart(Intent arg0, int arg1) {
    configurations = Configuration.getConfigurationList(new File(getFilesDir(), "Configs"));
    active = true;
    timeDataList = new ArrayList<Configuration.TimeData>();
        Date today = new Date();
        current = this;

    for (Configuration c : configurations) {
      if (c.active) {
        for (int x = 0; x < c.timeDataList.size(); x++) {
          Configuration.TimeData tdata = c.timeDataList.get(x);
          
          Date cdate = new Date();
          int ttime = tdata.time.getHours()*60+tdata.time.getMinutes();
          int ctime = cdate.getHours()*60+cdate.getMinutes();
                              
          int week = today.getDay();
          week = week-1;
          if(week == -1) week = 6;                              
          if (tdata.day[week] == 1 && ttime > ctime) timeDataList.add(tdata);
        }
      }
    }
    timeListener = new TimeListener();
    timeListener.start();    
  }
    
    TimeListener timeListener;

    public void kill(){
        active = false;
        if(timeListener != null) timeListener.isActive = false;
    
        stopSelf();
    }

  public class TimeListener extends Thread {
        public boolean isActive;
        @Override
        public void run() {
            
            isActive = true;
            
            Intent i = new Intent(AlarmService.this,RingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            while(isActive){try{
                try{
                    Thread.sleep(200);
                }catch(Throwable e){
                    
                }
                Date c = new Date();
                int oldSize = timeDataList.size();
                for(Configuration.TimeData td:timeDataList){
                    int ttime = td.time.getHours()*60+td.time.getMinutes();
                    int ctime = c.getHours()*60+c.getMinutes();
                    if(oldSize != timeDataList.size()) break; 
                    if(ttime < ctime ){
                        timeDataList.remove(td);
                        Configuration.sortTimeData(timeDataList);
                        if(ControlActivity.current != null){
                            ControlActivity.current.runOnUiThread(new Runnable(){public void run(){ControlActivity.current.binding.recyclerControl.getAdapter().notifyDataSetChanged();}});
                        }
                    }else if(ttime == ctime && td.active){
                        td.active = false;
                        nextData = td;
                        timeDataList.remove(td);
                        Configuration.sortTimeData(timeDataList);
                        if(ControlActivity.current != null){
                            ControlActivity.current.runOnUiThread(new Runnable(){public void run(){ControlActivity.current.binding.recyclerControl.getAdapter().notifyDataSetChanged();}});
                        }
                        try{Thread.sleep(100);}catch(Throwable e){}
                        startActivity(i);
                        
                        break;
                    }
                }    
            }catch(Throwable e){
                
            }}
            
        }
  }
    
    public static Configuration.TimeData nextData;
}
