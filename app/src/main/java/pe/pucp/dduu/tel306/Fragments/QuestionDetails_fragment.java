package pe.pucp.dduu.tel306.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pe.pucp.dduu.tel306.Classes.AnswerStatsClass;
import pe.pucp.dduu.tel306.Classes.QuestionClass;
import pe.pucp.dduu.tel306.QuestionActivity;
import pe.pucp.dduu.tel306.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionDetails_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionDetails_fragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static QuestionClass question;
    private static Context contexto;
    private static String userid;
    private static int itemselected;


    public QuestionDetails_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuestionDetails.
     */
    public static QuestionDetails_fragment newInstance(QuestionClass q, String uid, Context c) {
        QuestionDetails_fragment fragment = new QuestionDetails_fragment();
        question = q;
        userid = uid;
        contexto = c;
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
        View view = inflater.inflate(R.layout.fragment_qdetails, container, false);
        TextView t = view.findViewById(R.id.idtitle);
        t.setText(question.getQuestionText());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexto, android.R.layout.simple_spinner_item, question.forSpinner());
        Spinner spinner = view.findViewById(R.id.idspinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemselected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button button = view.findViewById(R.id.idbuttonanswer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postAnswer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }


    void postAnswer() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String postBody = "{\n" +
                "    \"iduser\": \"" + userid + " \",\n" +
                "    \"idanswer\": \"" + question.getAnswers()[itemselected].getId() + "\"\n" +
                "}";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url("http://34.236.191.118:3000/api/v1/questions/" + question.getId() + "/answer")
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((QuestionActivity) contexto).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("debugeo", "HUBO EXITO EN RESPUESTA");
                final String responsesuccess = response.body().string();
                Log.d("debugeo", responsesuccess);
                ((QuestionActivity) contexto).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responsesuccess.equals("true")) {
                            Toast.makeText(contexto, "Respuesta guardada", Toast.LENGTH_SHORT).show();
                            ((QuestionActivity) contexto).deletedetailsfragment();

                        } else {
                            Toast.makeText(contexto, "No se pudo guardar su respuesta", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}