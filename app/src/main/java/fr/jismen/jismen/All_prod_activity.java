package fr.jismen.jismen;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.UrlQuerySanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class All_prod_activity extends AppCompatActivity {
    private SessionManager session;
    private HashMap<String, Object> user;
    private Context currentContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_prod_activity);
        session = new SessionManager(this);
        user = session.getUser();
        currentContext = this;
        RestClient.get("get_all_enabled_products", null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                LinearLayout allProds = (LinearLayout) findViewById(R.id.all_prods);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                params.setMargins(0, 50, 0, 0);
                for (Iterator<String> iterCat = response.keys(); iterCat.hasNext();){
                    try {
                        String categoryName = iterCat.next();
                        TextView catTv = new TextView(currentContext);
                        catTv.setText(categoryName);
                        catTv.setLayoutParams(params);
                        catTv.setGravity(Gravity.CENTER);
                        catTv.setTypeface(Typeface.DEFAULT_BOLD);
                        catTv.setBackgroundColor(Color.parseColor("#FFB5DD"));
                        catTv.setPadding(0, 30, 0, 30);
                        catTv.setTextColor(Color.WHITE);
                        allProds.addView(catTv);
                        JSONObject subcats = response.getJSONObject(categoryName);
                        for (Iterator<String> iterSubcat = subcats.keys(); iterSubcat.hasNext();){
                            String subcatName = iterSubcat.next();
                            TextView subcatTv = new TextView(currentContext);
                            subcatTv.setText(subcatName);
                            subcatTv.setGravity(Gravity.CENTER);
                            subcatTv.setBackgroundColor(Color.parseColor("#34495E"));
                            subcatTv.setPadding(0, 30, 0, 30);
                            subcatTv.setTypeface(Typeface.DEFAULT_BOLD);
                            subcatTv.setTextColor(Color.WHITE);
                            subcatTv.setLayoutParams(params);
                            allProds.addView(subcatTv);
                            JSONObject products = subcats.getJSONObject(subcatName);
                            for (Iterator<String> iterProd = products.keys(); iterProd.hasNext();){
                                String productName = iterProd.next();
                                final JSONObject product = products.getJSONObject(productName);
                                TextView prodTv = new TextView(currentContext);
                                prodTv.setText(productName);
                                prodTv.setGravity(Gravity.CENTER);
                                prodTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                prodTv.setTypeface(Typeface.DEFAULT_BOLD);
                                prodTv.setLayoutParams(params);
                                allProds.addView(prodTv);
                                ImageView img = new ImageView(currentContext);
                                Glide.with(currentContext).load("http://192.168.1.48/jismen-symfony/web/images/" + product.getString("path")).into(img);
                                allProds.addView(img);
                                Button btn = new Button(currentContext);
                                btn.setText("J'ai un coup de coeur !");
                                btn.setBackgroundColor(Color.parseColor("#FFB5DD"));
                                btn.setTypeface(Typeface.DEFAULT_BOLD);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            RequestParams par = new RequestParams();
                                            par.put("user_id", user.get(session.KEY_ID));
                                            par.put("prod_id", product.getInt("id"));
                                            RestClient.post("add_fav", par, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    try {
                                                        Toast.makeText(currentContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String error, Throwable exception) {
                                                    exception.printStackTrace();
                                                    Toast.makeText(currentContext, error, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                allProds.addView(btn);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable exception){
                exception.printStackTrace();
            }
        });
    }
}
