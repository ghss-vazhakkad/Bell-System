package in.ghsvkd.bellsystem;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import android.widget.Toast;
import in.ghsvkd.bellsystem.obj.Configuration;
import java.io.File;
import java.util.List;

public class AlarmService extends Service {

    public static boolean active;
    public static List<Configuration> configurations;
    
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
        active = true;
        configurations = Configuration.getConfigurationList(new File(getFilesDir(),"Configs"));
    }
    
    @Override
    public void onDestroy() {
        active = false;
    }
        
    
    
}
