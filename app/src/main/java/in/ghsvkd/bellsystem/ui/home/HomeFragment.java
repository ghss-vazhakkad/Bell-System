package in.ghsvkd.bellsystem.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import in.ghsvkd.bellsystem.AlarmService;
import in.ghsvkd.bellsystem.ControlActivity;
import in.ghsvkd.bellsystem.databinding.FragmentHomeBinding;
import in.ghsvkd.bellsystem.R;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        if(AlarmService.active) stageOn();
        else stageOff();
        
        binding.buttonStart.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            if(!AlarmService.active){
                Intent i = new Intent(getActivity(),AlarmService.class);
                getActivity().startService(i);
                stageOn();
            }else{
                Intent i = new Intent(getActivity(),AlarmService.class);
                getActivity().stopService(i);
                stageOff();         
            }
        }});
        
        return root;
    }
    
    public void stageOff(){
        binding.textStatus.setText(getString(R.string.service_stopped));
        binding.textProgress.setVisibility(View.INVISIBLE);
        binding.buttonStart.setText("Start");
        binding.layoutHome.setOnClickListener(new View.OnClickListener(){public void onClick(View v){}});
    }
    
    public void stageOn(){
        binding.textStatus.setText(getString(R.string.service_started));
        binding.textProgress.setVisibility(View.INVISIBLE);
        binding.buttonStart.setText("Stop");
        binding.layoutHome.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            Intent cpanel = new Intent(getActivity(),ControlActivity.class);
            startActivity(cpanel);        
        }});
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}