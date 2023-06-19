package in.ghsvkd.bellsystem.list;
import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.malinskiy.materialicons.Iconify;
import com.malinskiy.materialicons.widget.IconButton;
import in.ghsvkd.bellsystem.databinding.ListItemSoundsBinding;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundListAdapter extends RecyclerView.Adapter<SoundListAdapter.ViewHolder>{
    
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public int itemId;
        public ListItemSoundsBinding binding;
        public ViewHolder(ListItemSoundsBinding binding,int itemId){
            super(binding.getRoot());
            this.itemId = itemId;
            this.binding = binding;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        return new ViewHolder(ListItemSoundsBinding.inflate(LayoutInflater.from(arg0.getContext()),arg0,false),arg1);
    }
    
    public AlertDialog alertText;

    @Override
    public void onBindViewHolder(ViewHolder arg0, int arg1) {
        final String file = soundFiles.get(arg1);
        final String label = file.substring(file.indexOf(":")+1,file.lastIndexOf(":"));
        
        arg0.binding.textSoundName.setText(label);
        if(updateText != null){
            arg0.binding.layoutSound.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
                
                host.runOnUiThread(new Runnable(){public void run(){updateText.setText(label);alertText.dismiss();}});        
            }});
        }
        final IconButton playItem = arg0.binding.buttonPlayItem;
        playItem.setOnClickListener(new View.OnClickListener(){MediaPlayer mp;public void onClick(View v){
            
            if(mp == null){
                 mp = new MediaPlayer();       
                 if(file.startsWith("asset")){
                    String path = "Audio/"+label+"."+file.substring(file.lastIndexOf(":")+1);
                    try{
                        mp.setDataSource(host.getAssets().openFd(path));        
                        mp.prepare();
                    }catch(IOException e){
                                
                    }
                 }
            }
            if(!mp.isPlaying()){
                        
                        
               mp.start();
               class OnSeekComplete implements MediaPlayer.OnCompletionListener{
                   public void onCompletion(MediaPlayer player){
                       playItem.setText("{zmdi-play}");
                   }
               }         
               mp.setOnCompletionListener(new OnSeekComplete());                  
               playItem.setText("{zmdi-pause}");
            }else{
               mp.pause();         
               playItem.setText("{zmdi-play}");
            }
                                
        }});
    }
    

    @Override
    public int getItemCount() {
        return soundFiles.size();
    }
    
    public TextView updateText;
    
    public Activity host;
    public static List<String> soundFiles;
    
    
    public SoundListAdapter(Activity host){
        this.host = host;
        if(soundFiles == null) soundFiles = retreiveSoundFiles(host);
    }
    
    public static List<String> retreiveSoundFiles(Activity host){
        File soundsDir = new File(host.getFilesDir(),"Sounds");
        List<String> soundFiles = new ArrayList<String>();
        
        if(soundsDir.exists()){
            for(File f:soundsDir.listFiles()){
                String s = f.getName();
                soundFiles.add("file:"+f.getName().substring(0,f.getName().lastIndexOf("."))+":"+f.getAbsolutePath());
            }
        }
        
        try{
            String[] inbuilt = host.getAssets().list("Audio");
            for(String s:inbuilt){
                soundFiles.add("asset:"+s.substring(0,s.lastIndexOf("."))+":"+s.substring(s.lastIndexOf(".")+1,s.length()));
            }
        }catch(IOException e){
            
        }
        return soundFiles;
    }
    
    public static String getSoundData(String label,List<String> soundDataList){
        String rt = null;
        for(String s:soundDataList){
            if(s.contains(label)){
                return s;
            }
        }
        
        return rt;
    }
    
    public static String getLabel(String str){
        return str.substring(str.indexOf(":")+1,str.lastIndexOf(":"));
    }
}
