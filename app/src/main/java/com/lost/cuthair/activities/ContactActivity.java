package com.lost.cuthair.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lost.cuthair.R;
import com.lost.cuthair.adapters.SortAdapter;
import com.lost.cuthair.dao.DaoMaster;
import com.lost.cuthair.dao.DaoSession;
import com.lost.cuthair.dao.Person;
import com.lost.cuthair.dao.PersonDao;
import com.lost.cuthair.model.SortModel;
import com.lost.cuthair.utils.CharacterParser;
import com.lost.cuthair.utils.PinyinComparator;
import com.lost.cuthair.views.SideBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通讯录界面
 * Created by lost on 2016/4/12.
 */
public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView sortListView;
    private SideBar sideBar;
    private AppCompatTextView middle;
    private LinearLayout ll_add;
    private LinearLayout ll_add_phone;
    private TextView left;
    private LinearLayout ll_search;
    /**
     * 显示字母的TextView
     */
    private TextView dialog;
    private SortAdapter adapter;
    private TextView mClearEditText;


    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    private List<Person> persons;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;


    /**
     * 显示图片用的类
     */
    private ImageLoader imageLoader;
    private ImageLoaderConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressbook);
        initViews();

        configuration = ImageLoaderConfiguration
                .createDefault(this);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
    }

    private void initViews() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();
        left = (TextView) findViewById(R.id.left);
        left.setVisibility(View.GONE);

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        middle = (AppCompatTextView) findViewById(R.id.middle);

        middle.setText("业务档案");
        middle.setTextColor(Color.WHITE);
        middle.setTextSize(20);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        ll_add_phone = (LinearLayout) findViewById(R.id.ll_add_phone);
        ll_add.setOnClickListener(this);
        ll_add_phone.setOnClickListener(this);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);

        // 点击从通讯录导入
        ll_add_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
            }
        });

        // 点击添加新客户
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactActivity.this, PersonActivity.class));
            }
        });

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (persons != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position);
                    }
                }


            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(getApplication(), String.valueOf(((SortModel) adapter.getItem(position)).getNumber()), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ContactActivity.this, PersonActivity.class);
                intent.putExtra("date", ((SortModel) adapter.getItem(position)).getDate());
                startActivity(intent);
            }
        });


        searchDb();
        /*SourceDateList = filledData(getResources().getStringArray(R.array.date));

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);*/


        mClearEditText = (TextView) findViewById(R.id.filter_edit);


        mClearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                Log.i("info", "是否有焦点-------》" + hasFocus);

                if (hasFocus) {
                    ll_search.setVisibility(View.GONE);
                }else {
                    ll_search.setVisibility(View.VISIBLE);
                }
            }
        });


        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表

                if (persons != null) {
                    filterData(s.toString());
                }

                if(s == "") {
                    ll_search.setVisibility(View.VISIBLE);
                }else {
                    ll_search.setVisibility(View.GONE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    ll_search.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    /**
     * 为ListView填充数据
     *
     * @return
     */
    private List<SortModel> filledData(List<Person> lists) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < lists.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(lists.get(i).getName());
            sortModel.setPath(lists.get(i).getImage());
            sortModel.setNumber(lists.get(i).getNumber());
            sortModel.setDate(lists.get(i).getDate());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(lists.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name == null) {
                    name = "未命名";
                }
                if (name.toUpperCase().indexOf(
                        filterStr.toString().toUpperCase()) != -1
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String userName = "";
        String userNumber = "";

        if (resultCode == Activity.RESULT_OK) {
            ContentResolver reContentResolverol = getContentResolver();
            Uri contactData = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            userName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phone.moveToNext()) {
                userNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

//            Toast.makeText(ContactActivity.this, "点击的通讯录名字----》" + userName + "号码----->" + userNumber, Toast.LENGTH_SHORT).show();

            Intent addIntent = new Intent(ContactActivity.this, PersonActivity.class);
            addIntent.putExtra("name", userName);
            addIntent.putExtra("number", userNumber);
            startActivity(addIntent);


        }

    }


    private static SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static PersonDao personDao;
    private Cursor cursor;

    /**
     * 查询数据库
     */
    private void searchDb() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "person-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        personDao = daoSession.getPersonDao();
        List<Person> lists = personDao.queryBuilder().list();
        if (lists.size() != 0) {
            persons = lists;
            SourceDateList = filledData(lists);

            // 根据a-z进行排序源数据
            Collections.sort(SourceDateList, pinyinComparator);
            adapter = new SortAdapter(this, SourceDateList);
            sortListView.setAdapter(adapter);
        }

        helper.close();

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchDb();
    }
}