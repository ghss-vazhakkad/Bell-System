package in.ghsvkd.bellsystem.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import in.ghsvkd.bellsystem.MainActivity;
import in.ghsvkd.bellsystem.databinding.FragmentConfigurationBinding;
import in.ghsvkd.bellsystem.list.ConfigurationListAdapter;
import java.util.logging.Logger;

public class ConfigurationFragment extends Fragment{
    public FragmentConfigurationBinding binding;
    public MainActivity parent;
    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        binding = FragmentConfigurationBinding.inflate(arg0,arg1,false);
        View root = binding.getRoot();
        try{
            parent = (MainActivity) getActivity();
            binding.recyclerConf.setAdapter(new ConfigurationListAdapter(this));
        }catch(Throwable e){
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setMessage(Log.getStackTraceString(e)+"");
            adb.setTitle(e.getClass().getName());
            adb.create().show();
        }
        binding.recyclerConf.setLayoutManager(new LinearLayoutManager(getContext()));
        
        return root;
    }
        
}
