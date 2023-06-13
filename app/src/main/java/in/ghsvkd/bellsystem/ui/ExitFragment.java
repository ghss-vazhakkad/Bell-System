package in.ghsvkd.bellsystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.Nullable;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import in.ghsvkd.bellsystem.AlarmService;


public class ExitFragment extends Fragment{
    @Override
    @MainThread
    @Nullable
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        getActivity().startService(new Intent(getActivity(),AlarmService.class));
        getActivity().finish();
        
        return super.onCreateView(arg0, arg1, arg2);
    }
        
}
