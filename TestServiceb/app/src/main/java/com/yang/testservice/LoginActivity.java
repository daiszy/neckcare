package com.yang.testservice;

import com.yang.data.Data;
import com.yang.service.LoginService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.yang.database.Accounts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by krm on 2017/4/19.
 */
/*public abstract class LoginActivity extends Activity implements DialogInterface.OnClickListener,
        Button.OnClickListener,PopupWindow.OnDismissListener,
        AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {*/
public  class LoginActivity extends Activity {
    protected static final String TAG = "LoginActivity";
    private LinearLayout mLoginLinearLayout; // 登录内容的容器
    private LinearLayout mUserIdLinearLayout; // 将下拉弹出窗口在此容器下方显示
    private Animation mTranslate; // 位移动画
    private Dialog mLoginingDlg; // 显示正在登录的Dialog
    private EditText mIdEditText; // 登录ID编辑框
    private EditText mPwdEditText; // 登录密码编辑框
    private ImageView mMoreUser; // 下拉图标
    private Button mLoginButton; // 登录按钮
    private ImageView mLoginMoreUserView; // 弹出下拉弹出窗的按钮
    private String mIdString;
    private String mPwdString;
    private ArrayList<com.yang.testservice.User> mUsers; // 用户列表
    private ListView mUserIdListView; // 下拉弹出窗显示的ListView对象
    private MyAapter mAdapter; // ListView的监听器
    private PopupWindow mPop; // 下拉弹出窗
    private ProgressDialog progressDialog;
    private Intent jump_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(android.R.layout);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
        mLoginLinearLayout.startAnimation(mTranslate); // Y轴水平移动

		/* 获取已经保存好的用户密码 */
        mUsers = com.yang.testservice.Utils.getUserList(LoginActivity.this);

        if (mUsers.size() > 0) {
			/* 将列表中的第一个user显示在编辑框 */
            mIdEditText.setText(mUsers.get(0).getId());
            mPwdEditText.setText(mUsers.get(0).getPwd());
        }

        LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.userifo_listview, null);
        mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
        parent.removeView(mUserIdListView); // 必须脱离父子关系,不然会报错
        //mUserIdListView.setOnItemClickListener(this); // 设置点击事
        // 设置点击事
        mUserIdListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIdEditText.setText(mUsers.get(position).getId());
                mPwdEditText.setText(mUsers.get(position).getPwd());
                mPop.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAdapter = new MyAapter(mUsers);
        mUserIdListView.setAdapter(mAdapter);

        //注册
        Button register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jump_register = new Intent(LoginActivity.this,Register.class);
                startActivity(jump_register);
            }
        });



    }

    /* ListView的适配器 */
    class MyAapter extends ArrayAdapter<com.yang.testservice.User> {

        public MyAapter(ArrayList<com.yang.testservice.User> users) {
            super(LoginActivity.this, 0, users);
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.listview_item, null);
            }

            TextView userIdText = (TextView) convertView
                    .findViewById(R.id.listview_userid);
            userIdText.setText(getItem(position).getId());

            ImageView deleteUser = (ImageView) convertView
                    .findViewById(R.id.login_delete_user);
            deleteUser.setOnClickListener(new OnClickListener() {
                // 点击删除deleteUser时,在mUsers中删除选中的元素
                @Override
                public void onClick(View v) {

                    if (getItem(position).getId().equals(mIdString)) {
                        // 如果要删除的用户Id和Id编辑框当前值相等，则清空
                        mIdString = "";
                        mPwdString = "";
                        mIdEditText.setText(mIdString);
                        mPwdEditText.setText(mPwdString);
                    }
                    mUsers.remove(getItem(position));
                    mAdapter.notifyDataSetChanged(); // 更新ListView
                }
            });
            return convertView;
        }

    }

    private class MyThread implements Runnable {
        @Override
        public void run() {


        }
    }
    private void setListener() {
        mIdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mIdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mPwdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPwdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
       /* mLoginButton.setOnClickListener(this);*/
        // 启动登录
        mLoginButton.setOnClickListener(new OnClickListener() {

            public static final int IS_OK = 1;
            public static final int IS_NOT = 2;
            private Handler handler = new Handler(){
                public void handleMessage(Message msg){
                    switch(msg.what){
                        case IS_OK:
                            /*Toast.makeText(LoginActivity.this,"请先采集阈值", Toast.LENGTH_SHORT).show();*/
                            closeLoginingDlg();// 关闭对话框
//                            Intent intent = new Intent(LoginActivity.this,SlidingMenuActivity.class);
//                            startActivity(intent);
                            showNormalDialog();
                            SharedPreferences userSettings = getSharedPreferences("setting", 0);
                            SharedPreferences.Editor editor = userSettings.edit();
                            editor.putString("username",mIdString);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date =simpleDateFormat.format(new Date());
                            editor.putString("startTime",date);
                            editor.commit();
                            Data.setUserName(mIdString);
                            Data.setStartTime(new Date());
                            break;
                        case IS_NOT:
                            Toast.makeText(LoginActivity.this, "登录失败，请重新登录或注册账号！", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                    }
                }
            };
            @Override
            public void onClick(View v) {
                if (mIdString == null || mIdString.equals("")) { // 账号为空时
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT)
                            .show();
                } else if (mPwdString == null || mPwdString.equals("")) {// 密码为空时
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                            .show();
                } else {// 账号和密码都不为空时

                    final LoginService loginService = new LoginService();
                    final Thread thread = new Thread(new Runnable(){
                        public void run() {
                            boolean is = loginService.HttpPost(mIdString,mPwdString);
                            Message message = new Message();

                            if(is){
                                message.what = IS_OK;
                                handler.sendMessage(message);
                            }else {
                                message.what = IS_NOT;
                                handler.sendMessage(message);
                            }
                        }
                    });
                    thread.start();

                }
            }
        });



        // 点击下拉栏框
        mLoginMoreUserView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPop == null) {
                    initPop();
                }
                if (!mPop.isShowing() && mUsers.size() > 0) {
                    // Log.i(TAG, "切换为角向上图标");
                    mMoreUser.setImageResource(R.drawable.login_more_down); // 切换图标
                    mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); // 显示弹出窗口
                }
            }
        });
    }

    private void initView() {
        mIdEditText = (EditText) findViewById(R.id.login_edtId);
        mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
        mMoreUser = (ImageView) findViewById(R.id.login_more_user);
        mLoginButton = (Button) findViewById(R.id.login_btnLogin);
        mLoginMoreUserView = (ImageView) findViewById(R.id.login_more_user);
        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
        mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
        mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // 初始化动画对象
        progressDialog = new ProgressDialog(this);
       // initLoginingDlg();
    }

    public void initPop() {
        int width = mUserIdLinearLayout.getWidth() - 4;
        int height = LayoutParams.WRAP_CONTENT;
        mPop = new PopupWindow(mUserIdListView, width, height, true);
        /*mPop.setOnDismissListener(this);// 设置弹出窗口消失时监听器*/
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // Log.i(TAG, "切换为角向下图标");
                mMoreUser.setImageResource(R.drawable.login_more_up);
            }
        });

        // 注意要加这句代码，点击弹出窗口其它区域才会让窗口消失
        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));

    }

    /* 初始化正在登录对话框 */
    private void initLoginingDlg() {

        mLoginingDlg = new Dialog(this, R.style.loginingDlg);
        mLoginingDlg.setContentView(R.layout.logining_dlg);

        Window window = mLoginingDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

        // 获取屏幕的高宽
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int cxScreen = dm.widthPixels;
        int cyScreen = dm.heightPixels;

        /*int height = (int) getResources().getDimension(R.dimen.loginingdlg_height);// 高42dp
        int lrMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_lr_margin); // 左右边沿10dp
        int topMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_top_margin); // 上沿20dp*/
        int height = 42;// 高42dp
        int lrMargin = 10; // 左右边沿10dp
        int topMargin = 20; // 上沿20dp

        params.y = (-(cyScreen - height) / 2) + topMargin; // -199
		/* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */

        params.width = cxScreen;
        params.height = height;
        // width,height表示mLoginingDlg的实际大小

        mLoginingDlg.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
    }

    /* 显示正在登录对话框 */
    private void showLoginingDlg() {
        if (mLoginingDlg != null)
            mLoginingDlg.show();
    }

    /* 关闭正在登录对话框 */
    private void closeLoginingDlg() {
        if (mLoginingDlg != null && mLoginingDlg.isShowing())
            mLoginingDlg.dismiss();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btnLogin:
                // 启动登录
                showLoginingDlg(); // 显示"正在登录"对话框,因为此Demo没有登录到web服务器,所以效果可能看不出.可以结合情况使用
                Log.i(TAG, mIdString + "  " + mPwdString);
                if (mIdString == null || mIdString.equals("")) { // 账号为空时
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT)
                            .show();
                } else if (mPwdString == null || mPwdString.equals("")) {// 密码为空时
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                            .show();
                } else {// 账号和密码都不为空时
                    boolean mIsSave = true;
                    try {
                        Log.i(TAG, "保存用户列表");
                        for (com.yang.testservice.User user : mUsers) { // 判断本地文档是否有此ID用户
                            if (user.getId().equals(mIdString)) {
                                mIsSave = false;
                                break;
                            }
                        }
                        if (mIsSave) { // 将新用户加入users
                            com.yang.testservice.User user = new com.yang.testservice.User(mIdString, mPwdString);
                            mUsers.add(user);

                            Data.setUserName(mIdString);
                            String userName = Data.getUserName();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    closeLoginingDlg();// 关闭对话框
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.login_more_user: // 当点击下拉栏
                if (mPop == null) {
                    initPop();
                }
                if (!mPop.isShowing() && mUsers.size() > 0) {
                    // Log.i(TAG, "切换为角向上图标");
                    mMoreUser.setImageResource(R.drawable.login_more_down); // 切换图标
                    mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); // 显示弹出窗口
                }
                break;
            default:
                break;
        }

    }


    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        mIdEditText.setText(mUsers.get(position).getId());
        mPwdEditText.setText(mUsers.get(position).getPwd());
        mPop.dismiss();
    }


    /* 退出此Activity时保存users */
    @Override
    public void onPause() {
        super.onPause();
        try {
            Utils.saveUserList(LoginActivity.this, mUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private   void   showNormalDialog(){
        final AlertDialog.Builder normalDialog =
                new   AlertDialog.Builder(LoginActivity.this  );
        normalDialog.setTitle(  "请先采集阈值"  );
        normalDialog.setPositiveButton( "确定",
                new   DialogInterface.OnClickListener() {
                    @Override
                    public   void   onClick(DialogInterface dialog,int which) {
                        //...To-do
                        /*Intent intent=new Intent(LoginActivity.this,DataCollectionActivity.class);
                        startActivity(intent);*/
                        Intent intent=new Intent(LoginActivity.this,MainActivity2.class);
                        startActivity(intent);

                    }
                });
        normalDialog.setNegativeButton( "放弃",
                new   DialogInterface.OnClickListener() {
                    @Override
                    public   void   onClick(DialogInterface dialog,   int   which) {
                        //...To-do
                        Intent intent=new Intent(LoginActivity.this,SlidingMenuActivity.class);
                        startActivity(intent);
                    }
                });
        // 显示
        normalDialog.show();
    }

}
