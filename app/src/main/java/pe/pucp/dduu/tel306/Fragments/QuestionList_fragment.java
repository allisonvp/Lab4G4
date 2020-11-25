package pe.pucp.dduu.tel306.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.pucp.dduu.tel306.Classes.QuestionClass;
import pe.pucp.dduu.tel306.QListAdapter;
import pe.pucp.dduu.tel306.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionList_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionList_fragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String userid;
    private static QuestionClass[] listapreguntas;
    private static Context contexto;

    public QuestionList_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuestionList.
     */
    public static QuestionList_fragment newInstance(String uid, QuestionClass[] lista, Context c) {
        QuestionList_fragment fragment = new QuestionList_fragment();
        userid = uid;
        listapreguntas = lista;
        contexto = c;
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qlist, container, false);
        QListAdapter adapter = new QListAdapter(userid , listapreguntas, contexto);
        RecyclerView rView = view.findViewById(R.id.recyclerQuestion);
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(contexto));
        return view;
    }
}