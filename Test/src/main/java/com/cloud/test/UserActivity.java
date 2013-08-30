package com.cloud.test;

import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.widget.ImageView;
import com.cloud.util.DialogUtil;
import com.cloud.util.HttpUtil;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class UserActivity extends Activity
{
	private EditText etName, etPass;
	private Button bnLogin, bnCancel;
    private ImageView imgLogo;
    private JSONObject user;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());


        etName = (EditText) findViewById(R.id.userEditText);
		etPass = (EditText) findViewById(R.id.pwdEditText);
		etName.setText("tomcat");
		etPass.setText("tomcat");
		bnLogin = (Button) findViewById(R.id.bnLogin);
		bnCancel = (Button) findViewById(R.id.bnCancel);
        imgLogo = (ImageView) findViewById(R.id.logoImageView);
		bnCancel.setOnClickListener(new FinishListener(this));
		bnLogin.setOnClickListener(new OnClickListener()
		{
//			@Override
			public void onClick(View v)
			{
				if (validate())
				{
					if (loginPro())
					{
						Intent intent = new Intent(UserActivity.this, MainActivity.class);
                        intent.putExtra("user", user.toString());
						startActivity(intent);
						finish();
					}
					else
					{
						DialogUtil.showDialog(UserActivity.this,"login failure!", false);
					}
				}
			}
		});

        imgLogo.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
	}

	private boolean loginPro()
	{
		String username = etName.getText().toString();
		String pwd = etPass.getText().toString();
		try
		{
			user = query(username, pwd);
			if (user.getInt("id") > 0)
			{
				return true;
			}
		}
		catch (Exception e)
		{
			DialogUtil.showDialog(this, "on response!", false);
			e.printStackTrace();
		}

		return false;
	}

	private boolean validate()
	{
		String username = etName.getText().toString().trim();
		if (username.equals(""))
		{
			DialogUtil.showDialog(this,"username not empty!", false);
			return false;
		}
		String pwd = etPass.getText().toString().trim();
		if (pwd.equals(""))
		{
			DialogUtil.showDialog(this, "password not empty!", false);
			return false;
		}
		return true;
	}

	private JSONObject query(String username, String password) throws Exception
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", username);
		map.put("password", password);
		String url = HttpUtil.BASE_URL + "AUserServlet?method=login";
		return new JSONObject(HttpUtil.postRequest(url, map));
	}
}