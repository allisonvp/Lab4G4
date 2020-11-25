package pe.pucp.dduu.tel306;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pe.pucp.dduu.tel306.Classes.DtoAnswers;
import pe.pucp.dduu.tel306.Classes.QuestionClass;
import pe.pucp.dduu.tel306.Fragments.QuestionDetails_fragment;
import pe.pucp.dduu.tel306.Fragments.QuestionList_fragment;
import pe.pucp.dduu.tel306.Fragments.Stats_fragment;

public class QListAdapter extends RecyclerView.Adapter<QListAdapter.QuestionViewHolder> {

    private static String userid;
    private static QuestionClass[] lista;
    private static Context contexto;


    public QListAdapter(String uid, QuestionClass[] l, Context c) {
        userid = uid;
        lista = l;
        contexto = c;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.item_question, parent, false);
        QuestionViewHolder questionViewHolder = new QuestionViewHolder(itemView);
        return questionViewHolder;
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        String qtext = lista[position].getQuestionText();
        String id = lista[position].getId();
        holder.textView.setText(qtext);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((QuestionActivity)contexto).checklogin();
                Toast.makeText(contexto, "Cargando...", Toast.LENGTH_SHORT).show();
                try {
                    checkAnswer(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.length;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView button;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView_Qitem);
            this.button = itemView.findViewById(R.id.idbutton);
        }
    }

    void checkAnswer(String questionid) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://34.236.191.118:3000/api/v1/answers/detail?questionid=" + questionid + "&userid=" + userid)
                .method("GET", null)
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
                Log.d("debugeo", "HUBO EXITO EN CHEQUEO");
                final String hasAnswered = response.body().string();
                ((QuestionActivity) contexto).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getQuestionStats(questionid, hasAnswered);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });
    }

    void getQuestionStats(String questionid, String hasAnswered) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://34.236.191.118:3000/api/v1/answers/stats?questionid=" + questionid)
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((QuestionActivity) contexto).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("debugeo", "HUBO ERROR en STATS");
                        Toast.makeText(contexto, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("debugeo", "HUBO EXITO en stats");
                final String responsestr = response.body().string();
                ((QuestionActivity)contexto).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        FragmentManager fm = ((QuestionActivity) contexto).getSupportFragmentManager();
                        Fragment fragment = fm.findFragmentById(R.id.idfragment_details);
                        if (fragment != null) {
                            fm.beginTransaction()
                                    .remove(fragment)
                                    .commit();
                        }
                        DtoAnswers dto = gson.fromJson(responsestr, DtoAnswers.class);
                        if (hasAnswered.equals("true")) {

                            Log.d("debugeo", "STATS: ES TRUE");
                            Stats_fragment stats = Stats_fragment.newInstance(dto, contexto);
                            fm.beginTransaction()
                                    .add(R.id.idfragment_details, stats)
                                    .commit();
                        } else {
                            Log.d("debugeo", "STATS: ES FALSE");
                            QuestionDetails_fragment details = QuestionDetails_fragment.newInstance(dto.getQuestion(),userid, contexto);
                            fm.beginTransaction()
                                    .add(R.id.idfragment_details, details)
                                    .commit();
                        }
                    }
                });

            }
        });
    }

}
