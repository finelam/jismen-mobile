package fr.jismen.jismen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class Account_activity extends AppCompatActivity implements View.OnClickListener {
    private Context currentContext;
    private SessionManager session;
    private HashMap<String, Object> user;
    private EditText accName;
    private EditText accFirstname;
    private EditText accEmail;
    private EditText accAddress;
    private EditText accPc;
    private EditText accCity;
    private EditText accBirthday;
    private CheckBox accNewletter;
    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activity);
        currentContext = this;

        accName         = (EditText) findViewById(R.id.accName);
        accFirstname    = (EditText) findViewById(R.id.accFisrtname);
        accEmail        = (EditText) findViewById(R.id.accEmail);
        accAddress      = (EditText) findViewById(R.id.accAddress);
        accPc           = (EditText) findViewById(R.id.accPc);
        accCity         = (EditText) findViewById(R.id.accCity);
        accBirthday     = (EditText) findViewById(R.id.accBirthday);
        accNewletter    = (CheckBox) findViewById(R.id.accNewsletter);
        btnEdit         = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);

        session = new SessionManager(this);
        user = session.getUser();
        updateFields();
    }

    @Override
    public void onClick(View v) {
        RequestParams params = new RequestParams();
        params.put("name", accName.getText());
        params.put("firstname", accFirstname.getText());
        params.put("email", accEmail.getText());
        params.put("address", accAddress.getText());
        params.put("pc", accPc.getText());
        params.put("city", accCity.getText());
        params.put("birthday", accBirthday.getText());
        params.put("newsletter", accNewletter.isChecked() ? 1 : 0);
        RestClient.post(user.get(session.KEY_ID) + "/edit_user", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")){
                        JSONObject jsonUser = response.getJSONObject("user");
                        System.out.println(jsonUser);
                        session.createUser(jsonUser);
                        Toast.makeText(currentContext, "Modifications effectuées !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(currentContext, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable exception){
                //Toast.makeText(currentContext, statusCode, Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }
        });
    }

    public void updateFields(){
        accName.setText((String)user.get(session.KEY_NAME));
        accFirstname.setText((String)user.get(session.KEY_FIRSTNAME));
        accEmail.setText((String)user.get(session.KEY_EMAIL));
        accAddress.setText((String)user.get(session.KEY_ADDRESS));
        accPc.setText((String)user.get(session.KEY_PC));
        accCity.setText((String)user.get(session.KEY_CITY));
        accBirthday.setText((String)user.get(session.KEY_BIRTHDAY));
        accNewletter.setChecked((boolean)user.get(session.KEY_NEWSLETTER));
    }
}
