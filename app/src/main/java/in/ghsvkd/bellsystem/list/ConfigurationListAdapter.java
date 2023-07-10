package in.ghsvkd.bellsystem.list;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.strictmode.ImplicitDirectBootViolation;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.material.snackbar.Snackbar;
import in.ghsvkd.bellsystem.R;
import in.ghsvkd.bellsystem.databinding.AlertNewConfigurationBinding;
import in.ghsvkd.bellsystem.databinding.ListItemConfigurationBinding;
import in.ghsvkd.bellsystem.obj.Configuration;
import in.ghsvkd.bellsystem.ui.ConfigurationFragment;
import java.io.File;
import java.time.chrono.Era;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationListAdapter
    extends RecyclerView.Adapter<ConfigurationListAdapter.ViewHolder> {

  public static ConfigurationListAdapter lastUsed;
  public int lastCheck;
  public List<Configuration> configurationList;
  public Activity ctx;
  public ConfigurationFragment parent;

  public ConfigurationListAdapter(ConfigurationFragment cf) {

    this.ctx = cf.getActivity();
    parent = cf;
    if(lastUsed == null) configurationList = Configuration.getConfigurationList(new File(ctx.getFilesDir(), "Configs"));
    else configurationList = lastUsed.configurationList;
    Configuration.writeConfigurationList(new File(ctx.getFilesDir(), "Configs"),configurationList);
        
    new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
              @Override
              public int getMovementFlags(RecyclerView arg0, RecyclerView.ViewHolder arg1) {
                ViewHolder vh = (ViewHolder) arg1;
                return makeMovementFlags(0, ItemTouchHelper.LEFT);
              }

              @Override
              public boolean onMove(
                 RecyclerView arg0, RecyclerView.ViewHolder arg1, RecyclerView.ViewHolder arg2) {

                return false;
              }
              ViewHolder viewHolder;
              @Override
              public void onSwiped(RecyclerView.ViewHolder arg0, final int arg1) {
                viewHolder = (ViewHolder) arg0;
                if(!configurationList.get(viewHolder.itemId).label.contains("Default"))parent
                    .getActivity()
                    .runOnUiThread(
                        new Runnable() {
                            
                          boolean isCorrect = true;
                          public void run() {
                            Snackbar sb =
                                Snackbar.make(
                                    parent.binding.getRoot(),
                                    "Removed",
                                    Snackbar.LENGTH_SHORT);
                            
                                sb.setAction(
                                "Undo",
                                new View.OnClickListener() {
                                  @Override
                                  public void onClick(View arg0) {
                                      isCorrect = false;
                                      notifyDataSetChanged();      
                                  }
                                });
                            sb.setCallback(
                                new Snackbar.Callback() {

                                  @Override
                                  public void onDismissed(Snackbar arg0, int arg1) {
                                    if(isCorrect){ 
                                                String label = configurationList.get(viewHolder.itemId).label;
                                                configurationList.remove(viewHolder.itemId);
                                                notifyDataSetChanged();
                                                new File(parent.parent.getFilesDir(),"Configs/"+label+".obj").delete();
                                    }
                                    
                                    super.onDismissed(arg0, arg1);
                                  }
                                });
                            sb.show(); 
                          }
                        });
                    else notifyDataSetChanged();
                    
              }
            })
        .attachToRecyclerView(parent.binding.recyclerConf);
        
    lastUsed = this;
  }

  @Override
  public ConfigurationListAdapter.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
    return new ViewHolder(
        ListItemConfigurationBinding.inflate(LayoutInflater.from(arg0.getContext()), arg0, false),
        arg1);
  }

  @Override
  public void onBindViewHolder(final ConfigurationListAdapter.ViewHolder arg0, final int arg1) {
    arg0.binding.textConfiguration.setText(configurationList.get(arg1).label);
    arg0.binding.getRoot().setOnClickListener(openEvent(arg1));
    View v = arg0.binding.getRoot();
    v.setOnLongClickListener(new View.OnLongClickListener(){public boolean onLongClick(View v){
        final android.widget.TextView tv = arg0.binding.textConfiguration;
        final Configuration cfg = configurationList.get(arg1);
        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        alert.setItems(new CharSequence[]{"Duplicate","Rename"},new DialogInterface.OnClickListener(){public void onClick(DialogInterface di,int i){
            if(i == 1){
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                final AlertNewConfigurationBinding ancb = AlertNewConfigurationBinding.inflate(LayoutInflater.from(v.getContext()));
                adb.setTitle("Set label");
                                    
                adb.setView(ancb.getRoot());
                ancb.inputConfig.setText(cfg.label);                    
                adb.setPositiveButton("CREATE",new DialogInterface.OnClickListener(){public void onClick(DialogInterface di,int id){
                    if(!ancb.inputConfig.getText().toString().isEmpty()){
                        File oldf = new File(v.getContext().getFilesDir(),"Configs/"+cfg.label+".obj");                            
                        oldf.delete();
                                                    
                        cfg.label = ancb.inputConfig.getText().toString();
                        try{
                           cfg.writeToRoot(new File(v.getContext().getFilesDir(),"Configs"));                            
                        }catch(Throwable e){
                            
                        }
                        ConfigurationListAdapter.lastUsed.notifyItemChanged(arg1);                                                
                    }
                }});
                adb.create().show();        
                
            }else if(i == 0){
                Configuration dupe = new Configuration(cfg.label);
                dupe.active = cfg.active;
                dupe.listLength = cfg.listLength;
                dupe.label = cfg.label+" (Copy)";
                                    
                for(Configuration.TimeData t:cfg.timeDataList){
                    Configuration.TimeData n = new Configuration.TimeData();
                    n.active = t.active;
                    n.day = t.day;
                    n.sound = t.sound;
                    n.time = t.time;
                    dupe.timeDataList.add(n);                                                                                                      
                }
                try{
                    dupe.writeToRoot(new File(v.getContext().getFilesDir(),"Configs"));
                }catch(Throwable e){
                    
                }                    
                configurationList.add(dupe);
                notifyDataSetChanged();                                        
            }
        }});
        alert.create().show();
                                
        return true;
    }});     
    if(!configurationList.get(arg1).active)   arg0.binding.buttonActiveItem.setText(""); 
    else arg0.binding.buttonActiveItem.setText("{zmdi-check}");    
    arg0.binding.buttonActiveItem.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
         configurationList.get(arg1).active = ! configurationList.get(arg1).active;             
         Configuration.writeConfigurationList(new File(ctx.getFilesDir(), "Configs"),configurationList);
         notifyDataSetChanged();          
    }});
        arg0.itemId = arg1;    
  }

  public View.OnClickListener openEvent(final int current) {
    return new View.OnClickListener() {
      public void onClick(View v) {
        lastCheck = current;
        lastUsed = ConfigurationListAdapter.this;
        parent.parent.navController.navigate(R.id.screen_events);
      }
    };
  }
    

  @Override
  public int getItemCount() {
    Configuration.writeConfigurationList(new File(ctx.getFilesDir(), "Configs"),configurationList);
    return configurationList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ListItemConfigurationBinding binding;
    public int itemId;

    public ViewHolder(ListItemConfigurationBinding binding, int itemId) {
      super(binding.getRoot());
      this.itemId = itemId;

      this.binding = binding;
    }
  }
}
