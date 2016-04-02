package fr.jismen.jismen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;

public class Menu_activity extends AppCompatActivity implements View.OnClickListener {
    private Intent baseIntent;
    private JSONObject jsonUser = new JSONObject();
    private Context currentContext;
    private Button btnAccount, btnFavorites, btnAllProd, btnDisconnect;
    private SessionManager session;
    private HashMap<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_activity);
        currentContext  = this;
        session         = new SessionManager(this);
        user            = session.getUser();

        btnAccount      = (Button) findViewById(R.id.btnAccount);
        btnFavorites    = (Button) findViewById(R.id.btnFav);
        btnAllProd      = (Button) findViewById(R.id.btnAllProd);
        btnDisconnect   = (Button) findViewById(R.id.btnDisconnect);
        btnAccount.setOnClickListener(this);
        btnFavorites.setOnClickListener(this);
        btnAllProd.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btnAccount:{
                intent = new Intent(getBaseContext(), Account_activity.class);
                break;
            }
            case R.id.btnFav:{
                intent = new Intent(getBaseContext(), Favorites_activity.class);
                break;
            }
            case R.id.btnAllProd:{
                intent = new Intent(getBaseContext(), All_prod_activity.class);
                break;
            }
            default:{
                session.disconnect();
                break;
            }
        }
        currentContext.startActivity(intent);
    }
}
