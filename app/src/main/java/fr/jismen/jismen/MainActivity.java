package fr.jismen.jismen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText tfId, tfPass;
    private Button btnLogin;
    private Context currentContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentContext  = this;
        tfId            = (EditText) findViewById(R.id.idId);
        tfPass          = (EditText) findViewById(R.id.idPass);
        btnLogin        = (Button) findViewById(R.id.idConnexion);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = tfId.getText().toString().trim();
        String password = tfPass.getText().toString().trim();
        if (username.length() > 0 && password.length() > 0) {
            RequestParams params = new RequestParams();
            params.put("username", username);
            params.put("password", password);

            RestClient.post("check_pass", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success")){
                            SessionManager session  = new SessionManager(currentContext);
                            JSONObject jsonUser     = response.getJSONObject("user");
                            session.createUser(jsonUser);
                            Toast.makeText(currentContext, "Bienvenue " + jsonUser.getString("username") + " !", Toast.LENGTH_LONG).show();
                            Intent intent           = new Intent(currentContext, Menu_activity.class);
                            currentContext.startActivity(intent);
                        } else {
                            Toast.makeText(currentContext, "Identifiants invalides", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
