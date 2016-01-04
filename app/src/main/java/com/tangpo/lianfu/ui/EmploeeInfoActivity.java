package com.tangpo.lianfu.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tangpo.lianfu.R;
import com.tangpo.lianfu.entity.Employee;
import com.tangpo.lianfu.http.NetConnection;
import com.tangpo.lianfu.parms.EditStaff;
import com.tangpo.lianfu.parms.GetTypeList;
import com.tangpo.lianfu.utils.ToastUtils;
import com.tangpo.lianfu.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 果冻 on 2015/11/7.
 */
public class EmploeeInfoActivity extends Activity implements View.OnClickListener {

    private Button back;
    private Button confirm;
    private String[] entries = null;
    private String[] banks = null;
    private String[] grade = null;
    private ListView lv = null;
    private ImageView level;
    private TextView manage_level;
    private Spinner sex;
    private EditText user_name;
    private EditText contact_tel;
    private EditText rel_name;
    private EditText id_card;
    private TextView bank;
    private ImageView select_bank;
    private EditText bank_card;
    private EditText bank_name;
    private TextView registe_date;
    private TextView upgrade;
    private ImageView up;
    private Employee employee = null;
    private String userid = null;
    private ProgressDialog dialog = null;
    private boolean[] flag = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.deleteActivity(this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.employee_info_activity);

        Tools.gatherActivity(this);

        employee = getIntent().getExtras().getParcelable("employee");
        userid = getIntent().getStringExtra("userid");

        init();
    }

    private void init() {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        level = (ImageView) findViewById(R.id.level);
        level.setOnClickListener(this);
        manage_level = (TextView) findViewById(R.id.manage_level);
        manage_level.setOnClickListener(this);
        user_name = (EditText) findViewById(R.id.user_name);
        contact_tel = (EditText) findViewById(R.id.contact_tel);
        rel_name = (EditText) findViewById(R.id.rel_name);
        sex = (Spinner) findViewById(R.id.sex);
        id_card = (EditText) findViewById(R.id.id_card);
        bank = (TextView) findViewById(R.id.bank);
        bank.setOnClickListener(this);
        select_bank = (ImageView) findViewById(R.id.select_bank);
        select_bank.setOnClickListener(this);
        bank_card = (EditText) findViewById(R.id.bank_card);
        bank_name = (EditText) findViewById(R.id.bank_name);
        registe_date = (TextView) findViewById(R.id.registe_date);
        upgrade = (TextView) findViewById(R.id.upgrade);
        upgrade.setOnClickListener(this);
        up = (ImageView) findViewById(R.id.up);
        up.setOnClickListener(this);

        user_name.setText(employee.getUsername());
        contact_tel.setText(employee.getPhone());
        rel_name.setText(employee.getName());
        manage_level.setText(employee.getRank());
        if (employee.getSex().equals("0"))
            sex.setSelection(0);
        else
            sex.setSelection(1);
        id_card.setText(employee.getId_number());
        bank.setText(employee.getBank());
        bank_card.setText(employee.getBank_account());
        bank_name.setText(employee.getBank_name());
        registe_date.setText(employee.getRegister_time());
        upgrade.setText(employee.getUpgrade());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                updateEmployee();
                break;
            case R.id.level:
            case R.id.manage_level:
                if (entries == null) {
                    getList("ygtype");
                } else {
                    setLevelList();
                }
                break;
            case R.id.bank:
            case R.id.select_bank:
                if (banks == null) {
                    getList("bank");
                } else {
                    setBankList();
                }
                break;
            case R.id.up:
            case R.id.upgrade:
                if (grade == null) {
                    getList("uptype");
                } else {
                    //
                    setGradeList();
                }
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String[] tmp = null;
            switch (msg.what) {
                case 1:
                    tmp = (String[]) msg.obj;
                    entries = new String[tmp.length];
                    entries = tmp.clone();
                    setLevelList();
                    break;
                case 2:
                    tmp = (String[]) msg.obj;
                    banks = new String[tmp.length];
                    banks = tmp.clone();
                    setBankList();
                    break;
                case 3:
                    tmp = (String[]) msg.obj;
                    grade = new String[tmp.length];
                    grade = tmp.clone();
                    setGradeList();
                    break;
            }
        }
    };

    private void setGradeList() {
        flag = new boolean[grade.length];
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择类型列表").setMultiChoiceItems(grade, flag, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //设置多选响应事件
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                String tmp = "";
                for (int i = 0; i < grade.length; i++) {
                    if (lv.getCheckedItemPositions().get(i)) {
                        tmp += lv.getAdapter().getItem(i) + " ";
                    }
                }
                upgrade.setText(tmp);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                dialog.dismiss();
            }
        }).create();
        lv = dialog.getListView();
        dialog.show();
    }

    private void setLevelList() {
        new AlertDialog.Builder(this).setTitle("请选择员工管理级别").setItems(entries, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                manage_level.setText(entries[which]);
            }
        }).show();
    }

    private void setBankList() {
        new AlertDialog.Builder(this).setTitle("请选择开户银行").setItems(banks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                bank.setText(banks[which]);
            }
        }).show();
    }

    private void getList(final String list) {
        if(!Tools.checkLAN()) {
            Tools.showToast(getApplicationContext(), "网络未连接，请联网后重试");
            return;
        }

        String[] kvs = new String[]{list, ""};
        String param = GetTypeList.packagingParam(getApplicationContext(), kvs);
        new NetConnection(new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                //
                String[] tmp = null;
                try {
                    tmp = result.getJSONObject("param").getString("listtxts").split("\\,");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                if ("ygtype".equals(list)) {
                    msg.what = 1;
                } else if ("bank".equals(list)){
                    msg.what = 2;
                } else {
                    msg.what = 3;
                }
                msg.obj = tmp;
                handler.sendMessage(msg);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail(JSONObject result) {
                //
                try {
                    Tools.showToast(getApplicationContext(), result.getString("info"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, param);

    }

    private void updateEmployee() {
        if(!Tools.checkLAN()) {
            Tools.showToast(getApplicationContext(), "网络未连接，请联网后重试");
            return;
        }

        String employee_id = employee.getUser_id();
        String rank = "";
        if ("管理员".equals(manage_level.getText().toString().trim())) {
            rank = "0";
        } else {
            rank = "1";
        }
        String username = user_name.getText().toString();
        String name = rel_name.getText().toString();
        String id_number = id_card.getText().toString();
        String upgrade = "BNZZ";
        String phone=contact_tel.getText().toString();
        String bank_account = bank_card.getText().toString();
        String bankStr = bank.getText().toString();
        String sexStr =null;
        if (sex.getSelectedItemId()==0) {
            sexStr = "0";
        } else {
            sexStr = "1";
        }

        if(username == null || username.length() == 0){
            Tools.showToast(getApplicationContext(), "请输入用户名");
            return;
        }
        if(name == null || name.length() == 0) {
            Tools.showToast(getApplicationContext(), "请输入真实姓名");
            return;
        }
        if(id_number == null || id_number.length() == 0) {
            Tools.showToast(getApplicationContext(), "请输入身份证号码");
            return;
        }
        if (!Tools.isMobileNum(phone)) {
            Tools.showToast(getApplicationContext(), "请输入正确的电话号码");
            return;
        }
        if (bank_account == null || bank_account.length() == 0) {
            Tools.showToast(getApplicationContext(), "请输入银行卡账户");
            return;
        }
        if ((bankStr == null || bankStr.length() == 0)) {
            Tools.showToast(getApplicationContext(), "请输入银行名称");
            return;
        }

        dialog = ProgressDialog.show(this, getString(R.string.connecting), getString(R.string.please_wait));
        String kvs[] = new String[]{userid, employee_id, rank, username, name, id_number,
                upgrade,phone,bank_account, bankStr, sexStr};
        String param = EditStaff.packagingParam(this, kvs);

        new NetConnection(new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                dialog.dismiss();
                ToastUtils.showToast(EmploeeInfoActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT);
                EmploeeInfoActivity.this.finish();
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail(JSONObject result) {
                dialog.dismiss();
                try {
                    String status=result.getString("status");
                    Tools.handleResult(EmploeeInfoActivity.this, result.getString("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, param);
    }
}
