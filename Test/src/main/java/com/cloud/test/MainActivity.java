package com.cloud.test;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.cloud.util.DialogUtil;
import com.cloud.util.HttpUtil;
import org.json.JSONArray;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private ListView alist;
    private Button bnAdd;


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item);
        final Bundle extras = getIntent().getExtras();
        System.out.print("###########################" + extras.toString());

        alist = (ListView) findViewById(R.id.aList);
        bnAdd = (Button) findViewById(R.id.bn_home);
        String url = HttpUtil.BASE_URL + "AArticleServlet?typeid=100&method=query";

        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        //StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());




        try
        {
            // 向指定URL发送请求，并把服务器响应转换成JSONArray对象
            System.out.println("**********************************"+url);
            JSONArray jsonArray = new JSONArray(HttpUtil.getRequest(url));
            System.out.println(jsonArray.toString()+"***********************************");
            // 将JSONArray包装成Adapter
            JSONArrayAdapter adapter = new JSONArrayAdapter(this
                    , jsonArray, "title", true);
            alist.setAdapter(adapter);
        }
        catch (Exception e)
        {
            DialogUtil.showDialog(this, "no response", false);
            e.printStackTrace();
        }

        alist.setOnItemClickListener(new OnItemClickListener()
        {
            //			@Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                // 查看指定物品的详细情况。
                viewItemDetail(position);
            }
        });

        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddArticleActivity.class);
                intent.putExtra("user",extras.getString("user"));
                startActivity(intent);
            }
        });
    }


    private void viewItemDetail(int position)  {
        // 加载detail.xml界面布局代表的视图
        View detailView = getLayoutInflater().inflate(R.layout.detail,null);
        // 获取detail.xml界面布局中的文本框
        EditText itemName = (EditText) detailView
                .findViewById(R.id.itemName);
        EditText itemKind = (EditText) detailView
                .findViewById(R.id.itemContent);
        /*
        EditText maxPrice = (EditText) detailView
                .findViewById(R.id.maxPrice);
        EditText itemRemark = (EditText) detailView
                .findViewById(R.id.itemRemark);
        */
        // 获取被单击的列表项
        JSONObject jsonObj = (JSONObject) alist.getAdapter().getItem(
                position);
        try
        {
            // 通过文本框显示物品详情
            itemName.setText(jsonObj.getString("title"));
            itemKind.setText(jsonObj.getString("content"));
            //maxPrice.setText(jsonObj.getString("maxPrice"));
            //itemRemark.setText(jsonObj.getString("desc"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        DialogUtil.showDialog(MainActivity.this, detailView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
