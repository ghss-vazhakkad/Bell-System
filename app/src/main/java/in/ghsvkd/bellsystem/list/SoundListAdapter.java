package in.ghsvkd.bellsystem.list;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    

    @Override
    public void onBindViewHolder(ViewHolder arg0, int arg1) {
        final String file = soundFiles.get(arg1);
        String label = file.substring(file.indexOf(":")+1,file.lastIndexOf(":"));
        final MediaPlayer mp = new MediaPlayer();
        
        if(file.startsWith("asset")){
            String path = "Audio/"+label+"."+file.substring(file.lastIndexOf(":")+1);
            try{
                mp.setDataSource(host.getAssets().openFd(path));
                mp.prepare();
            }catch(IOException e){
                label = e.getMessage();
            }
        }
        
        arg0.binding.textSoundName.setText(label);
        final IconButton playItem = arg0.binding.buttonPlayItem;
        playItem.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            if(!mp.isPlaying()){
               try{
                    mp.prepare();
               }catch(Throwable e){
                    
               }         
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
    
    
    public Activity host;
    public List<String> soundFiles;
    
    public SoundListAdapter(Activity host){
        this.host = host;
        this.soundFiles = retreiveSoundFiles(host);
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
