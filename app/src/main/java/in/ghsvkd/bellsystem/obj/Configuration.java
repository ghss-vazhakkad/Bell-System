package in.ghsvkd.bellsystem.obj;

import android.text.format.Time;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Configuration implements Serializable{
    public String label;
    public boolean active;
    public int listLength;
    public List<TimeData> timeDataList;
    
    public Configuration(String label){
        active = true;
        this.label = label;
        this.timeDataList = new ArrayList<TimeData>();
    }
    
    public static class TimeData implements Serializable{
        public Date time;
        public int[] day;
        public String sound;
        public boolean active;
        
        public TimeData(){
            active = true;
            day = new int[]{0,0,0,0,0,0,0};
            time = null;
            sound = null;
        }
    }
    
    
    public void writeToRoot(File root)throws IOException{
        File f = new File(root,this.label+".obj");
        if(f.exists()) f.delete();
        f.createNewFile();
        
        this.listLength = timeDataList.size();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(this);
        oos.flush();
        oos.close();
        
    }
    
    public static Configuration readFromFile(File f)throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Configuration cf = (Configuration) ois.readObject();
        ois.close();
        return cf;
    }
    
    public static List<Configuration> getConfigurationList(File root){
        File[] files = root.listFiles();
        List<Configuration> confs = new ArrayList<Configuration>();
        for(File f:files){
            try{
                Configuration conf = readFromFile(f);
                confs.add(conf);
            }catch(Throwable e){
                
            }
        }
        return confs;
    }
    
    public static void writeConfigurationList(File root, List<Configuration> lst){
        for(Configuration c:lst){
            try{
                c.writeToRoot(root);
            }catch(Throwable e){
                
            }
        }
    }
    
    public static void sortTimeData(List<TimeData> lst){
        try{
            int k = 0;
        int[] timlst = new int[lst.size()];
        for(TimeData t:lst){
            timlst[k] = t.time.getHours()*60+t.time.getMinutes();
            k++;
        }
        
        for(int z = 0;z < timlst.length;z++){
            for(int m = 0; m < timlst.length; m++){
                if(m != timlst.length-1){
                    int now = timlst[m];
                    int next = timlst[m+1];
                    if(next < now){
                        timlst[m] = next;
                        timlst[m+1] = now;
                        TimeData nextT = lst.get(m+1);
                        TimeData nowT = lst.get(m);
                        lst.set(m,nextT);
                        lst.set(m+1,nowT);
                    }
                }
            }
        }
        }catch(Throwable e){
            
        }
    }
}
 