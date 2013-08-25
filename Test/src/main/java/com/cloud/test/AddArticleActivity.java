package com.cloud.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.cloud.util.DialogUtil;
import com.cloud.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 13-8-25.
 */
public class AddArticleActivity extends Activity {

    private Button bnAdd, bnCancel;
    private EditText title, author, content;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        bnAdd = (Button) findViewById(R.id.bnAdd);
        bnCancel = (Button) findViewById(R.id.bnCancel);

        title = (EditText) findViewById(R.id.itemTitle);
        author = (EditText) findViewById(R.id.itemAuthor);
        content = (EditText) findViewById(R.id.itemContent);

        bnCancel.setOnClickListener(new FinishListener(this));
        bnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String a = author.getText().toString();
                String c = content.getText().toString();

                try{
                    String result = addArticle(t, a, c);
                }catch (Exception e) {
                    DialogUtil.showDialog(AddArticleActivity.this, "no response", false);
                    e.printStackTrace();
                }
            }
        });
    }

    private String addArticle(String name, String author, String content) throws Exception{
        Map<String , String> map = new HashMap<String, String>();
        map.put("title" , name);
        map.put("author" , author);
        map.put("content" , content);
        map.put("userid", null);
        String url = HttpUtil.BASE_URL + "AArticleServlet?typeid=100&method=insert";
        return HttpUtil.postRequest(url, map);
    }
}
