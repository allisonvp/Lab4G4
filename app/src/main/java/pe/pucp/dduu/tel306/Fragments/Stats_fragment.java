package pe.pucp.dduu.tel306.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pe.pucp.dduu.tel306.Classes.AnswerClass;
import pe.pucp.dduu.tel306.Classes.AnswerStatsClass;
import pe.pucp.dduu.tel306.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Stats_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Stats_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static  AnswerStatsClass[] answerStats;
    private static Context contexto;

    public Stats_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Stats_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Stats_fragment newInstance(AnswerStatsClass[] as,Context c) {
        Stats_fragment fragment = new Stats_fragment();
        answerStats=as;
        contexto = c;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_fragment, container, false);
        TextView tView = view.findViewById(R.id.idtext);

        int totalcount=getTotalCount();
        String texto = "";
        for (AnswerStatsClass as : answerStats){
            int count= Integer.parseInt(as.getCount());
            texto = texto + as.getAnswer().getAnswerText() + ": "+ String.valueOf(count*100/totalcount) + "%\n";
        }
        tView.setText(texto);
        Log.d("debugeo", texto);
        return view;
    }

    private int getTotalCount(){
        int count= 0;
        for (AnswerStatsClass as : answerStats){
            count= count + Integer.parseInt(as.getCount());
        }
        return count;
    }

}