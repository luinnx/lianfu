package com.tangpo.lianfu.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tangpo.lianfu.R;
import com.tangpo.lianfu.entity.Employee;
import com.tangpo.lianfu.http.NetConnection;
import com.tangpo.lianfu.parms.EditStaff;
import com.tangpo.lianfu.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 果冻 on 2015/11/7.
 */
public class EmploeeInfoActivity extends Activity implements View.OnClickListener {

    private Button back;
    private Button confirm;

    private Spinner manage_level;
    private EditText user_name;
    private EditText contact_tel;
    private EditText rel_name;
    private EditText sex;
    private EditText id_card;
    private EditText bank;
    private EditText bank_card;
    private EditText bank_name;

    private Employee employee = null;

    private String userid = "";

    private ProgressDialog dialog = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        Tools.deleteActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.employee_info_activity);

        Tools.gatherActivity(this);

        employee = getIntent().getExtras().getParcelable("employee");
        userid = getIntent().getExtras().getString("userid");
        init();
    }

    private void init() {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        manage_level = (Spinner) findViewById(R.id.manage_level);
        user_name = (EditText) findViewById(R.id.user_name);
        contact_tel = (EditText) findViewById(R.id.contact_tel);
        rel_name = (EditText) findViewById(R.id.rel_name);
        sex = (EditText) findViewById(R.id.sex);
        id_card = (EditText) findViewById(R.id.id_card);
        bank = (EditText) findViewById(R.id.bank);
        bank_card = (EditText) findViewById(R.id.bank_card);
        bank_name = (EditText) findViewById(R.id.bank_name);

        Log.e("tag", employee.getRank() + " ");
        Log.e("tag", employee.toString());

        if (employee.getRank().equals("管理员"))
            manage_level.setSelection(0);
        else
            manage_level.setSelection(1);

        user_name.setText(employee.getUser_id());
        contact_tel.setText(employee.getPhone());
        rel_name.setText(employee.getZsname());
        if (employee.getSex().equals("0"))
            sex.setText("男");
        else
            sex.setText("女");
        id_card.setText(employee.getId_number());
        bank.setText(employee.getBank());
        bank_card.setText(employee.getBank_account());
        bank_name.setText(employee.getBank_name());
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
        }
    }

    private void updateEmployee() {
        dialog = ProgressDialog.show(this, getString(R.string.connecting), getString(R.string.please_wait));
        String employee_id = employee.getUser_id();
        String rank = "";
        if (manage_level.getSelectedItemId() == 0) {
            rank = 0 + "";
        } else {
            rank = 1 + "";
        }
        String username = user_name.getText().toString();
        String name = rel_name.getText().toString();
        String id_number = id_card.getText().toString();
        String upgrade = "BNZZ";
        String bank_account = bank_card.getText().toString();
        String bankStr = bank.getText().toString();
        String sexStr = "";
        if (sex.getText().toString().equals("男")) {
            sexStr = "0";
        } else {
            sexStr = "1";
        }

        String kvs[] = new String[]{userid, employee_id, rank, username, name, id_number,
                upgrade, bank_account, bankStr, sexStr};

        String param = EditStaff.packagingParam(this, kvs);

        new NetConnection(new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                dialog.dismiss();
                Tools.showToast(EmploeeInfoActivity.this, getString(R.string.update_success));
                EmploeeInfoActivity.this.finish();
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail(JSONObject result) {
                //
                dialog.dismiss();
                try {
                    if (result.getString("status").equals("2")) {
                        Tools.showToast(EmploeeInfoActivity.this, getString(R.string.format_error));
                    } else if (result.getString("status").equals("10")) {
                        Tools.showToast(EmploeeInfoActivity.this, getString(R.string.server_exception));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, param);
    }
}
