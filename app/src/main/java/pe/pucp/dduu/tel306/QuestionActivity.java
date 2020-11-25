package pe.pucp.dduu.tel306;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pe.pucp.dduu.tel306.Classes.QuestionClass;
import pe.pucp.dduu.tel306.Fragments.QuestionList_fragment;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {
    private static String userid = "1" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        if (isInternetAvailable()) {
            try {
                getQuestions();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No tiene conexión a internet", Toast.LENGTH_SHORT).show();
        }


    }

    void getQuestions() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://34.236.191.118:3000/api/v1/questions")
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                QuestionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QuestionActivity.this, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("debugeo", "HUBO EXITO");
                final String responsestr = response.body().string();
                Log.d("debugeo", responsestr);

                QuestionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        QuestionClass[] lista = gson.fromJson(responsestr, QuestionClass[].class);
                        List<QuestionClass> listapreguntas = Arrays.asList(lista);
                        if (listapreguntas.isEmpty()) {
                            Toast.makeText(QuestionActivity.this, "LISTA DE PREGUNTAS VACIA", Toast.LENGTH_SHORT).show();
                        } else {
                            FragmentManager fm = getSupportFragmentManager();
                            Fragment fragment = fm.findFragmentById(R.id.idfragment_list);
                            if (fragment != null) {
                                fm.beginTransaction()
                                        .remove(fragment)
                                        .commit();
                            }

                            QuestionList_fragment listFragment = QuestionList_fragment.newInstance(userid, lista,QuestionActivity.this);
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.idfragment_list, listFragment)
                                    .commit();

                        }

                    }
                });

            }
        });
    }


    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
            return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network networks = connectivityManager.getActiveNetwork();
            if (networks == null)
                return false;

            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(networks);
            if (networkCapabilities == null)
                return false;

            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                return true;
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                return true;
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                return true;
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null)
                return false;
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)
                return true;

        }
        return false;
    }


}