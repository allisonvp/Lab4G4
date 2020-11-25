package pe.pucp.dduu.tel306;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;

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

        for (String archivo : MainActivity.this.fileList()) {
            if (archivo.equals("login.json")) {
                Intent intent = new Intent(this, QuestionActivity.class);
                startActivity(intent);
                finish();
            }
        }
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

            EditText editTextLoginCorreo = findViewById(R.id.editTextLoginCorreo);
            EditText editTextLoginPassword = findViewById(R.id.editTextLoginPassword);

            String correo = editTextLoginCorreo.getText().toString();
            String password = editTextLoginPassword.getText().toString();

            Map<String, String> params = new HashMap<String, String>();
            params.put("email", correo);
            params.put("password", password);

            if (correo.isEmpty()) {
                editTextLoginCorreo.setError("No puede estar vacio");
            }
            if (password.isEmpty()) {
                editTextLoginPassword.setError("No puede estar vacio");
            }
            if (!correo.isEmpty() && !password.isEmpty()) {
                JSONObject jsonObj = new JSONObject(params);
                JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                Toast.makeText(MainActivity.this, "Inicio de sesion válido", Toast.LENGTH_SHORT).show();
                                String fileNameJson = "login.json";
                                try (FileOutputStream outputStream = MainActivity.this.openFileOutput(fileNameJson, MODE_PRIVATE);
                                     FileWriter fileWriter = new FileWriter(outputStream.getFD());) {
                                    fileWriter.write(String.valueOf(response));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Gson gson = new Gson();
                                String strResponse = String.valueOf(response);
                                Usuario usuario = gson.fromJson(strResponse, Usuario.class);


                                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                                intent.putExtra("id", usuario.getId());
                                String strId = String.valueOf(usuario.getId());
                                Log.d("infoApp", strId);
                                startActivity(intent);
                                finish();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        });

                requestQueue.add(jsonObjRequest);
            }
        }
    }

    public void registrarse(View view) {
        if (isInternetAvailable()) {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                String url = "http://34.236.191.118:3000/api/v1/users/new";

                EditText editTextRegistroNombre = findViewById(R.id.editTextRegistroNombre);
                EditText editTextRegistroCorreo = findViewById(R.id.editTextRegistroCorreo);
                EditText editTextRegistroPassword = findViewById(R.id.editTextRegistroPassword);

                String nombre = editTextRegistroNombre.getText().toString();
                String correo = editTextRegistroCorreo.getText().toString();
                String password = editTextRegistroPassword.getText().toString();

                if (nombre.isEmpty()) {
                    editTextRegistroNombre.setError("No puede estar vacio");
                }
                if (correo.isEmpty()) {
                    editTextRegistroCorreo.setError("No puede estar vacio");
                }
                if (password.isEmpty()) {
                    editTextRegistroPassword.setError("No puede estar vacio");
                }

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", nombre);
                jsonBody.put("email", correo);
                jsonBody.put("password", password);
                final String mRequestBody = jsonBody.toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !password.isEmpty()) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("infoApp", response);
                                    Toast.makeText(MainActivity.this, "Registro de usuario exitoso", Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("infoApp", error.toString());
                                    Toast.makeText(MainActivity.this, "El usuario ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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