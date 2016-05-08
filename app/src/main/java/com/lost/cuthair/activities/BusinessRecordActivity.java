package com.lost.cuthair.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lost.cuthair.R;
import com.lost.cuthair.adapters.BusinessAdapter;
import com.lost.cuthair.dao.Business;
import com.lost.cuthair.dao.BusinessDao;
import com.lost.cuthair.dao.DaoMaster;
import com.lost.cuthair.dao.DaoSession;
import com.lost.cuthair.utils.ImageUtils;
import com.lost.cuthair.utils.SharePreferenceUtils;

import java.util.Collections;
import java.util.List;

/**
 * 业务记录界面
 * Created by lost on 2016/4/13.
 */
public class BusinessRecordActivity extends BaseActivity {

    private ListView lv;
    public static int screenWidth;
    public static int screenHeight;

    private TextView right;
    private TextView middle;
    private TextView left;
    private ImageView iv_head;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businessrecord);
        lv = (ListView) findViewById(R.id.lv);
        right = (TextView) findViewById(R.id.right);
        left = (TextView) findViewById(R.id.left);
        middle = (TextView) findViewById(R.id.middle);
        View head = LayoutInflater.from(this).inflate(R.layout.list_head, null);
        View footer = LayoutInflater.from(this).inflate(R.layout.list_footer, null);
        lv.addHeaderView(head);
        lv.addFooterView(footer);
        middle.setText("客户资料");
        right.setVisibility(View.VISIBLE);
        left.setVisibility(View.VISIBLE);
        right.setText("新建");
        right.setTextColor(Color.WHITE);
        right.setTextSize(16);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(BusinessRecordActivity.this, AddBusinessActivity.class);
                startActivity(addIntent);
            }
        });

        left = (TextView) findViewById(R.id.left);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        if (getIntent().hasExtra("imagePath")) {
            ImageUtils.setImageFromFilePath(getIntent().getStringExtra("imagePath"), iv_head);
        }
        searchBusiness();

        screenWidth =  getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
         screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）



                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BusinessRecordActivity.this, AddBusinessActivity.class);
                        startActivity(intent);
                    }
                });



    }


    @Override
    protected void onResume() {
        super.onResume();
        // 查询是否存在业务记录
        searchBusiness();
    }

    private static SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static BusinessDao businessDao;

    /**
     * 查询业务是否存在
     */
    private void searchBusiness() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "business-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        businessDao = daoSession.getBusinessDao();
        Long personId = Long.valueOf((String) SharePreferenceUtils.get(this, "personId", ""));
        List<Business> businesses = businessDao.queryBuilder().where(BusinessDao.Properties.PersonId.eq(personId)).list();
        Collections.reverse(businesses);
        Log.i("info", "是否有数据------》" + businesses.size());
        BusinessAdapter adapter = new BusinessAdapter(businesses, this);
        if (businesses != null && businesses.size() != 0) {
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            lv.setAdapter(null);
        }

        helper.close();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
