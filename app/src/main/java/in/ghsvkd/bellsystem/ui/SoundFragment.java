package in.ghsvkd.bellsystem.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.malinskiy.materialicons.Iconify;
import in.ghsvkd.bellsystem.databinding.FragmentSoundBinding;
import in.ghsvkd.bellsystem.list.SoundListAdapter;

public class SoundFragment extends Fragment{
    FragmentSoundBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        binding = FragmentSoundBinding.inflate(arg0,arg1,false);
        View root = binding.getRoot();
        binding.recyclerSounds.setAdapter(new SoundListAdapter(getActivity()));
        binding.recyclerSounds.setLayoutManager(new LinearLayoutManager(getContext()));
        
        return root;
    }
        
}
