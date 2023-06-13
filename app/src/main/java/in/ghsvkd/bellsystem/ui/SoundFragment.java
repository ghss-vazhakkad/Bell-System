package in.ghsvkd.bellsystem.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import in.ghsvkd.bellsystem.databinding.FragmentSoundBinding;

public class SoundFragment extends Fragment{
    FragmentSoundBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        binding = FragmentSoundBinding.inflate(arg0,arg1,false);
        View root = binding.getRoot();
        return root;
    }
        
}
