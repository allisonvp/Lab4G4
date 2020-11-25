package pe.pucp.dduu.tel306;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;



public class LoginFragment extends Fragment implements OnClickListener {


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        /*Button buttonLogin1 = (Button) view.findViewById(R.id.buttonLogin1);
        //buttonLogin1.setOnClickListener(this);
        buttonLogin1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        return view;
    }


    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
            case R.id.buttonLogin1:
                MainActivity mainActivity = new MainActivity();
                mainActivity.iniciarSesion(view);
                break;

        }*/
    }
}