package com.atguigu.shoppingmall_1020.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.shoppingmall_1020.R;
import com.atguigu.shoppingmall_1020.greendao.GreenBean;
import com.atguigu.shoppingmall_1020.greendao.GreenBeanDao;
import com.atguigu.shoppingmall_1020.home.activity.GoodsListActivity;
import com.atguigu.shoppingmall_1020.utils.JsonParser;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{


    @InjectView(R.id.et_search_home)
    EditText etSearchHome;
    @InjectView(R.id.tv_yuying_home)
    TextView tvYuyingHome;
    @InjectView(R.id.bt_search_home)
    TextView btSearchHome;
    @InjectView(R.id.rv_search)
    ListView rvSearch;
    @InjectView(R.id.bt_clear)
    Button btClear;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private GreenBeanDao greenBeanDao;
    private GreenBean greenBean;
    private ArrayList<String> datas;
    private ArrayAdapter adapter;
    private List<GreenBean> greenBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5838f0d9");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        datas=new ArrayList<>();

        greenBeanDao = Application.getDaoInstant().getGreenBeanDao();


        getData();

    }

    private void getData() {
        greenBeen = greenBeanDao.loadAll();
            if(greenBeen !=null&& greenBeen.size()>0) {
                for (int i = 0; i< greenBeen.size(); i++){
                    datas.add(greenBeen.get(i).getName());
                }
                adapter = new ArrayAdapter(this,android.R.layout.test_list_item,datas);
                rvSearch.setAdapter(adapter);
            }else{
                btClear.setVisibility(View.GONE);
            }
    }


    @OnClick({R.id.bt_search_home, R.id.bt_clear,R.id.tv_yuying_home})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_search_home:

                String text=etSearchHome.getText().toString();

                if(TextUtils.isEmpty(text)) {
                    Toast.makeText(SearchActivity.this, "不能为空", Toast.LENGTH_SHORT).show();

                }else{

                    greenBean = new GreenBean(null, text);
                    greenBeanDao.insert(greenBean);
                    datas.add(greenBean.getName());

                    adapter = new ArrayAdapter(this,android.R.layout.test_list_item,datas);
                    rvSearch.setAdapter(adapter);

                    if(adapter!=null) {
                        adapter.notifyDataSetChanged();
                    }

                    btClear.setVisibility(View.VISIBLE);
                    startActivity( new Intent(this, GoodsListActivity.class));

                   // finish();

                }


                break;

            case R.id.bt_clear:
                List<GreenBean> greenBeen = greenBeanDao.loadAll();
                for(int i = 0; i < greenBeen.size(); i++) {
                    greenBeanDao.deleteByKey(greenBeen.get(i).getId());
                }
                btClear.setVisibility(View.GONE);
                datas.clear();

                    adapter.notifyDataSetChanged();
                break;


            case R.id.tv_yuying_home:
                showDialogVoice();
                break;
        }
    }


    private void showDialogVoice() {

        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String result = recognizerResult.getResultString();
            System.out.println(result);
            String text = JsonParser.parseIatResult(recognizerResult.getResultString());

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            String reulst = resultBuffer.toString();
            reulst = reulst.replace("。","");
            etSearchHome.setText(reulst);
            etSearchHome.setSelection(etSearchHome.length());

        }

        @Override
        public void onError(SpeechError speechError) {

            Toast.makeText(SearchActivity.this, "出错了哦", Toast.LENGTH_SHORT).show();
        }
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {

        }
    }
}
