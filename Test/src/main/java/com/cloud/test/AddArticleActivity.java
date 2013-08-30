package com.cloud.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.cloud.util.DialogUtil;
import com.cloud.util.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 13-8-25.
 */
public class AddArticleActivity extends Activity {

    private Button bnAdd, bnCancel;
    private EditText title, author, content;
    Bundle extras = null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        extras = getIntent().getExtras();
        String name = null;
        try {
            JSONObject user = new JSONObject(extras.getString("user"));
            name = user.getString("name") == null ? "admin" :  user.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bnAdd = (Button) findViewById(R.id.bnAdd);
        bnCancel = (Button) findViewById(R.id.bnCancel);

        title = (EditText) findViewById(R.id.itemTitle);
        author = (EditText) findViewById(R.id.itemAuthor);
        content = (EditText) findViewById(R.id.itemContent);

        author.setText(name);

        bnCancel.setOnClickListener(new FinishListener(this));
        bnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String a = author.getText().toString();
                String c = content.getText().toString();

                try{
                    String result = addArticle(t, a, c);
                    Intent intent = new Intent(AddArticleActivity.this, MainActivity.class);
                    intent.putExtra("user", new JSONObject(extras.getString("user")).toString());
                    startActivity(intent);
                    finish();
                }catch (Exception e) {
                    DialogUtil.showDialog(AddArticleActivity.this, "no response", false);
                    e.printStackTrace();
                }
            }
        });
    }

    private String addArticle(String name, String author, String content) throws Exception{
        Map<String , String> map = new HashMap<String, String>();
        JSONObject user = new JSONObject(extras.getString("user"));
        System.out.println("###############" + user.getString("id") + "#####################");
        map.put("title" , name);
        map.put("author" , author);
        map.put("content" , content);
        map.put("userid", user.getString("id"));
        String url = HttpUtil.BASE_URL + "AArticleServlet?typeid=100&method=insert";
        return HttpUtil.postRequest(url, map);
    }
}
