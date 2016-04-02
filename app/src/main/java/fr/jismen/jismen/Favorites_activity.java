package fr.jismen.jismen;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import cz.msebera.android.httpclient.Header;

public class Favorites_activity extends AppCompatActivity {
    private Context currentContext;
    private SessionManager session;
    private HashMap<String, Object> user;
    private LinkedList<JSONObject> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_activity);
        final LinearLayout favLayout = (LinearLayout) findViewById(R.id.favoritesLayout);

        currentContext = this;
        session = new SessionManager(this);
        user = session.getUser();
        RestClient.get(user.get(session.KEY_ID) + "/get_favorites", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                params.setMargins(0, 50, 0, 0);
                for (Iterator<String> iterCat = response.keys(); iterCat.hasNext();){
                    try {
                        String cat = iterCat.next();
                        JSONObject subcats = response.getJSONObject(cat);
                        TextView catTv = new TextView(currentContext);
                        catTv.setText(cat);
                        catTv.setLayoutParams(params);
                        catTv.setGravity(Gravity.CENTER);
                        catTv.setTypeface(Typeface.DEFAULT_BOLD);
                        catTv.setBackgroundColor(Color.parseColor("#FFB5DD"));
                        catTv.setPadding(0, 30, 0, 30);
                        catTv.setTextColor(Color.WHITE);
                        favLayout.addView(catTv);
                        for (Iterator<String> iterSubcat = subcats.keys(); iterSubcat.hasNext();){
                            String subcat = iterSubcat.next();
                            JSONObject products = subcats.getJSONObject(subcat);
                            TextView subcatTv = new TextView(currentContext);
                            subcatTv.setText(subcat);
                            subcatTv.setGravity(Gravity.CENTER);
                            subcatTv.setBackgroundColor(Color.parseColor("#34495E"));
                            subcatTv.setPadding(0, 30, 0, 30);
                            subcatTv.setTypeface(Typeface.DEFAULT_BOLD);
                            subcatTv.setTextColor(Color.WHITE);
                            subcatTv.setLayoutParams(params);
                            favLayout.addView(subcatTv);
                            for (Iterator<String> iterProduct = products.keys(); iterProduct.hasNext();){
                                String product = iterProduct.next();
                                final JSONObject prod = products.getJSONObject(product);
                                TextView productTv = new TextView(currentContext);
                                productTv.setText(product);
                                productTv.setGravity(Gravity.CENTER);
                                productTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                productTv.setTypeface(Typeface.DEFAULT_BOLD);
                                productTv.setLayoutParams(params);
                                favLayout.addView(productTv);
                                ImageView img = new ImageView(currentContext);
                                Glide.with(currentContext).load("http://192.168.1.48/jismen-symfony/web/images/" + prod.getString("path")).into(img);
                                favLayout.addView(img);
                                Button btn = new Button(currentContext);
                                btn.setText("Je me soigne...");
                                btn.setBackgroundColor(Color.parseColor("#34495E"));
                                btn.setTextColor(Color.parseColor("#FFB5DD"));
                                btn.setTypeface(Typeface.DEFAULT_BOLD);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            RequestParams par = new RequestParams();
                                            par.put("user_id", user.get(session.KEY_ID));
                                            par.put("prod_id", prod.getInt("id"));

                                            RestClient.post("remove_fav", par, new TextHttpResponseHandler() {
                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    throwable.printStackTrace();
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                    Toast.makeText(currentContext, responseString, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                favLayout.addView(btn);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable exception) {
                exception.printStackTrace();
            }
        });
        }
    }
