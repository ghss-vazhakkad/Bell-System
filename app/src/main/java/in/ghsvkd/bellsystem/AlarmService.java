package in.ghsvkd.bellsystem;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import android.widget.Toast;

public class AlarmService extends Service {

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
        int time = 10;
        while(true){
            try{time -= 1;Thread.sleep(500);}catch(Throwable s){}
            Intent i = new Intent(this,ControlActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(time == 0) startActivity(i);
        }
        
        
    }
    
    
}
