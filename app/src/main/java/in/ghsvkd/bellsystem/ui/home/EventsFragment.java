package in.ghsvkd.bellsystem.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.malinskiy.materialicons.Iconify;
import in.ghsvkd.bellsystem.MainActivity;
import in.ghsvkd.bellsystem.databinding.FragmentEventsBinding;
import in.ghsvkd.bellsystem.list.ConfigurationListAdapter;
import static in.ghsvkd.bellsystem.list.ConfigurationListAdapter.*;
import in.ghsvkd.bellsystem.list.EventListAdapter;


public class EventsFragment extends Fragment{

    FragmentEventsBinding binding;
    
    @Override
    @MainThread
    @Nullable
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        binding = FragmentEventsBinding.inflate(arg0,arg1,false);
        MainActivity ma = (MainActivity) getActivity();
        ma.getSupportActionBar().setTitle(lastUsed.configurationList.get(lastUsed.lastCheck).label);
        
        binding.recyclerEvents.setAdapter(new EventListAdapter(this,lastUsed.lastCheck));
        binding.recyclerEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        
        return binding.getRoot();
    }
        
}
