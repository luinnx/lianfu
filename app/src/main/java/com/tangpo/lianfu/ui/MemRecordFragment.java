package com.tangpo.lianfu.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tangpo.lianfu.R;
import com.tangpo.lianfu.adapter.MemRecourdAdapter;
import com.tangpo.lianfu.entity.MemRecord;
import com.tangpo.lianfu.http.NetConnection;
import com.tangpo.lianfu.parms.CheckConsumeRecord;
import com.tangpo.lianfu.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 果冻 on 2015/11/8.
 */
public class MemRecordFragment extends Fragment implements View.OnClickListener {

    private PullToRefreshListView listView;
    private MemRecourdAdapter adapter = null;
    private List<MemRecord> list = new ArrayList<>();

    private int page = 1;
    private Gson gson = new Gson();
    private String user_id = null;

    private ProgressDialog dialog = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Tools.closeActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mem_record_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("userid");
            Log.e("tag", "userid=" + user_id);
        }
        init(view);
        return view;
    }

    private void init(View view) {
        getConsumeRecord();
        listView = (PullToRefreshListView) view.findViewById(R.id.list);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("下拉刷新");
        listView.getLoadingLayoutProxy(true, false).setPullLabel("");
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
        // 上拉加载更多时的提示文本设置
        listView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("上拉加载");
        listView.getLoadingLayoutProxy(false, true).setPullLabel("");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以加载");

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                list.clear();

                // 下拉的时候刷新数据
                int flags = DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL;

                String label = DateUtils.formatDateTime(
                        getActivity(),
                        System.currentTimeMillis(), flags);

                // 更新最后刷新时间
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                getConsumeRecord();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = page + 1;
                getConsumeRecord();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                list = (List<MemRecord>) msg.obj;
                Log.e("tag", "list = " + list.size());
                adapter = new MemRecourdAdapter(getActivity(), list);
                listView.setAdapter(adapter);
            }
        }
    };

    private void getConsumeRecord() {
        if(!Tools.checkLAN()) {
            Log.e("tag", "check");
            Tools.showToast(getActivity(), "网络未连接，请联网后重试");
            return;
        }

        dialog = ProgressDialog.show(getActivity(), getString(R.string.connecting), getString(R.string.please_wait));

        String kvs[] = new String[]{user_id, "10", page + ""};
        String param = CheckConsumeRecord.packagingParam(getActivity(), kvs);

        new NetConnection(new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.e("tag", "tag = " + result.toString());
                listView.onRefreshComplete();
                dialog.dismiss();
                try {
                    JSONArray jsonArray = result.getJSONArray("param");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        MemRecord member = gson.fromJson(object.toString(), MemRecord.class);
                        list.add(member);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.what = 1;
                msg.obj = list;
                handler.sendMessage(msg);

            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail(JSONObject result) {
                Log.e("tag", "fail " + result.toString());
                listView.onRefreshComplete();
                dialog.dismiss();
                try {
                    if("404".equals(result.getString("status"))){
                        Tools.showToast(getActivity(), "没有数据");
                    } else {
                        Tools.handleResult(getActivity(), result.getString("status"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, param);
    }
}
