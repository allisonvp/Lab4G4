package pe.pucp.dduu.tel306;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient;*/
import pe.pucp.dduu.tel306.entity.Usuario;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentoLogin();
        fragmentoRegistro();
        findViewById(R.id.fragmentRegistro).setVisibility(View.GONE);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegistro = findViewById(R.id.buttonRegistro);
        Button buttonRegistrase = findViewById(R.id.buttonRegistrase);
        Button buttonIniciaSesion = findViewById(R.id.buttonIniciaSesion);

        buttonRegistrase.setVisibility(View.GONE);
        buttonIniciaSesion.setVisibility(View.GONE);

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.fragmentLogin).setVisibility(View.GONE);
                findViewById(R.id.fragmentRegistro).setVisibility(View.VISIBLE);

                buttonLogin.setVisibility(View.GONE);
                buttonRegistro.setVisibility(View.GONE);
                buttonRegistrase.setVisibility(View.VISIBLE);
                buttonIniciaSesion.setVisibility(View.VISIBLE);
            }
        });

        buttonIniciaSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.fragmentRegistro).setVisibility(View.GONE);
                findViewById(R.id.fragmentLogin).setVisibility(View.VISIBLE);

                buttonRegistrase.setVisibility(View.GONE);
                buttonIniciaSesion.setVisibility(View.GONE);
                buttonLogin.setVisibility(View.VISIBLE);
                buttonRegistro.setVisibility(View.VISIBLE);
            }
        });

    }

    public void fragmentoLogin() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentLogin, loginFragment);
        fragmentTransaction.commit();
    }

    public void fragmentoRegistro() {
        RegistroFragment registroFragment = RegistroFragment.newInstance();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentRegistro, registroFragment);
        fragmentTransaction.commit();
    }


    public void iniciarSesion(View view) {
        if (isInternetAvailable()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://34.236.191.118:3000/api/v1/users/login";
            /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Usuario usuario = gson.fromJson(response, Usuario.class);

                            EditText editTextLoginCorreo = findViewById(R.id.editTextLoginCorreo);
                            EditText editTextLoginPassword = findViewById(R.id.editTextLoginPassword);

                            String correo = editTextLoginCorreo.getText().toString();
                            String password = editTextLoginPassword.getText().toString();

                            if(usuario.getEmail().equals(correo) && usuario.getPassword().equals(password)) {
                                Toast.makeText(MainActivity.this,"se logro", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(MainActivity.this,"se logro", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this,"ggwp", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", "a20160555@pucp.pe");
                    params.put("password", "1234");
                    return params;
                }
            };
            requestQueue.add(stringRequest);*/

            EditText editTextLoginCorreo = findViewById(R.id.editTextLoginCorreo);
            EditText editTextLoginPassword = findViewById(R.id.editTextLoginPassword);

            String correo = editTextLoginCorreo.getText().toString();
            String password = editTextLoginPassword.getText().toString();

            Map<String, String> params = new HashMap<String, String>();
            params.put("email", correo);
            params.put("password", password);

            JSONObject jsonObj = new JSONObject(params);
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(MainActivity.this, "se logro", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "ggwp", Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(jsonObjRequest);

            /*OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"email\":\"a20160555@pucp.pe\",\r\n    \"password\":\"1234\"\r\n}\r\n");
            Request request = new Request.Builder()
                    .url("http://34.236.191.118:3000/api/v1/users/login")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    public void registrarse(View view) {
        if(isInternetAvailable()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://34.236.191.118:3000/api/v1/users/new";

            EditText editTextRegistroNombre = findViewById(R.id.editTextRegistroNombre);
            EditText editTextRegistroCorreo = findViewById(R.id.editTextRegistroCorreo);
            EditText editTextRegistroPassword = findViewById(R.id.editTextRegistroPassword);

            String nombre = editTextRegistroNombre.getText().toString();
            String correo = editTextRegistroCorreo.getText().toString();
            String password = editTextRegistroPassword.getText().toString();

            if(nombre.isEmpty()){
                editTextRegistroNombre.setError("No puede estar vacio");
            }
            if(correo.isEmpty()){
                editTextRegistroCorreo.setError("No puede estar vacio");
            }
            if(password.isEmpty()){
                editTextRegistroPassword.setError("No puede estar vacio");
            }

            Map<String, String> params = new HashMap<String, String>();
            params.put("name", nombre);
            params.put("email", correo);
            params.put("password", password);

            JSONObject jsonObj = new JSONObject(params);
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(MainActivity.this, "se logro", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "ggwp", Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(jsonObjRequest);
        }
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
            return false;
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
            return false;

        }
    }
}