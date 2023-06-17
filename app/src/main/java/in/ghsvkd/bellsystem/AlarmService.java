package in.ghsvkd.bellsystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
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
        configurations = Configuration.getConfigurationList(new File(getFilesDir(),"Configs"));
        active = true;
        timeDataList = new ArrayList<Configuration.TimeData>();
        Date today = new Date();
        
        for(Configuration c: configurations){
            
            if(c.active){
                for(int x = 0; x < c.timeDataList.size();x++){
                    Configuration.TimeData tdata = c.timeDataList.get(x);
                    if(tdata.day[today.getDay()-1] == 1) timeDataList.add(tdata);
                }
            }
        }
    }
    
    @Override
    public void onDestroy() {
        active = false;
    }
        
    
    
}
