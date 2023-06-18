package in.ghsvkd.bellsystem;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import in.ghsvkd.bellsystem.databinding.ActivityRingBinding;
import in.ghsvkd.bellsystem.obj.Configuration;
import java.io.IOException;
import java.sql.RowId;

public class RingActivity extends AppCompatActivity {
  public ActivityRingBinding binding;
  public MediaPlayer player;
  public boolean closed;  
  public Configuration.TimeData current;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityRingBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
        
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
          
    
    current = AlarmService.nextData;
    String file = current.sound;

    if (file.startsWith("asset")) {
      String path =
          "Audio/"
              + file.substring(file.indexOf(":") + 1, file.lastIndexOf(":"))
              + "."
              + file.substring(file.lastIndexOf(":") + 1);
      try {
        player = new MediaPlayer();
        player.setDataSource(getAssets().openFd(path));
        player.prepare();
      } catch (IOException e) {

      }
    }
        
    binding.buttonClose.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
        finish();
    }});
    

    binding.buttonPlayItem.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
        if(player.isPlaying()){
            binding.buttonPlayItem.setText("{zmdi-play}");
            player.pause();            
        }else{
            binding.buttonPlayItem.setText("{zmdi-pause}");
            player.start();
        }
    }});        
    
       task = new Task();
        player.start();
        binding.progressBarRing.setMax(player.getDuration());
        task.start(); 
  }

  public class Publisher implements Runnable{
        @Override
        public void run(){
            binding.progressBarRing.setProgress(player.getCurrentPosition());
            int s =  (player.getCurrentPosition()/1000);
            int m = 0;
            while(s >= 60){
                s -= 60;
                m++;
            }
            String sec = s+"";
            if(s < 10){
                sec = "0"+sec;
            }
            String min = m+"";
            if(m < 10){
                min = "0"+min;
            }
            binding.textProgressTime.setText(min+":"+sec);
            if(player.getCurrentPosition() == player.getDuration()) finish();
        }
        
  }
    
    
    public class Task extends Thread{
        public boolean isAlive;
        @Override
        public void run(){
            isAlive = true;
            while(isAlive){
                try{Thread.sleep(1000/30);}catch(Exception e){};
                runOnUiThread(new Publisher());
            }
        }
    }
    Task task;
    
    @Override
    protected void onDestroy() {
        if(player.isPlaying()) player.stop();
        if(task.isAlive) task.isAlive = false;
        super.onDestroy();
    }
    
    
}
