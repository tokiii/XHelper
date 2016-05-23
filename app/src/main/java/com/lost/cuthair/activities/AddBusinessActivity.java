package com.lost.cuthair.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lost.cuthair.R;
import com.lost.cuthair.adapters.ImageAdapter;
import com.lost.cuthair.dao.Business;
import com.lost.cuthair.dao.BusinessDao;
import com.lost.cuthair.dao.DaoMaster;
import com.lost.cuthair.dao.DaoSession;
import com.lost.cuthair.utils.ImageUtils;
import com.lost.cuthair.utils.SharePreferenceUtils;
import com.lost.cuthair.utils.StringUtils;
import com.lost.cuthair.utils.Utils;
import com.lost.cuthair.views.ImageListView;
import com.lost.cuthair.views.SelectPicPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 添加业务界面
 * Created by lost on 2016/4/13.
 */
public class AddBusinessActivity extends BaseActivity implements View.OnClickListener, ImageAdapter.isDelete {

    private ImageView iv_business;
    private AppCompatEditText et_business;

    private TextView middle;
    private TextView right;
    private TextView left;
    private ImageView iv_delete;

    private final int SELECT_PICTURE = 102;
    private final int SELECT_CAMERA = 101;
    private Uri imageUri;
    private SelectPicPopupWindow picPopupWindow;
    private String image;
    private ImageLoader imageLoader;
    private ImageLoaderConfiguration configuration;
    private long businessId;
    private List<String> lists;
//    private GridView gv_image;
    private ImageListView lv_business;
    private ImageAdapter imageAdapter;
//    private ImageView iv_big;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbusiness);
        bindViews();
        configuration = ImageLoaderConfiguration
                .createDefault(this);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);

        // 创建数据库
        helper = new DaoMaster.DevOpenHelper(this, "business-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        businessDao = daoSession.getBusinessDao();

        if (getIntent().hasExtra("businessId")) {
            businessId = getIntent().getLongExtra("businessId", 0);
            setData(businessId);
            right.setText("编辑");
            unableEdit();// 设置不可编辑状态
        }

        Collections.reverse(lists);
        imageAdapter = new ImageAdapter(this, lists);

        imageAdapter.setIsDelete(this);// 设置监听接口
        lv_business.setAdapter(imageAdapter);

//        ListAdapter listAdapter = lv_business.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//        View listItem = listAdapter.getView(0, null, lv_business);
//        listItem.measure(0, 0);
//        listItem.getMeasuredHeight();
//        new ListViewUtils().setListViewHeightBasedOnChildren(lv_business);
//        gv_image.setAdapter(imageAdapter);

//        gv_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ImageUtils.useImageLoaderSetImage(imageLoader, iv_big, lists.get(position));
//                Log.i("info", "是否点击了图片--");
//            }
//        });

//        if (lists.size()!= 0) {
//            ImageUtils.useImageLoaderSetImage(imageLoader, iv_big, lists.get(0));
//        }


        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBusiness();
            }
        });
    }


    private void bindViews() {
        middle = (TextView) findViewById(R.id.middle);
        middle.setText("详细信息");
        right = (TextView) findViewById(R.id.right);
        left = (TextView) findViewById(R.id.left);
        right.setVisibility(View.VISIBLE);
        left.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setText("添加");
//        gv_image = (GridView) findViewById(R.id.gv_image);
        lv_business = (ImageListView) findViewById(R.id.lv_business);
//        iv_big = (ImageView) findViewById(R.id.iv_big);
        lists = new ArrayList<>();
        et_business = (AppCompatEditText) findViewById(R.id.et_business);
        iv_business = (ImageView) findViewById(R.id.iv_business);
        iv_business.setOnClickListener(this);

    }


    // 选择图片弹出菜单
    private View.OnClickListener itemOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            picPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                    String fileName = dateFormat.format(new Date());
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(photoIntent, SELECT_CAMERA);
                    break;
                case R.id.btn_select_photo:
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    galleryIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(galleryIntent, "选择图片"), SELECT_PICTURE);
                    break;


                case R.id.btn_delete:

                    // 设置图片为默认图片

                    iv_business.setImageResource(R.mipmap.img_select_img);
                    image = "";// 清空图片路径

                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_business:
                View view = this.getCurrentFocus();
                Utils.closeInput(this, view);
                picPopupWindow = new SelectPicPopupWindow(this, itemOnclick);
                picPopupWindow.showAtLocation(AddBusinessActivity.this.findViewById(R.id.addbusiness), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.right:

                if (right.getText().equals("编辑")) {
                    enableEdit(); //设置可编辑状态
                    right.setText("保存");
                } else {
                    saveOrUpdateBusiness();
                    Toast.makeText(AddBusinessActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case R.id.left:
                finish();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;
        if (resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
            } else {
                uri = imageUri;
            }

            Log.i("info", "根据图片路径获取到的uri----->" + uri.toString());


            String img_path = "";
            // 是否是从图库选择图片
            if (DocumentsContract.isDocumentUri(this, uri)|| requestCode == SELECT_CAMERA) {
                img_path = ImageUtils.getPath(this, uri);
            } else {
                img_path = ImageUtils.selectImage(this, data);
            }
            image = img_path;


            // 设置图片展示
            if (lists.size()< 10) {
                lists.add(image);
                Log.i("info", "list-------->" + lists.toString());
                Collections.reverse(lists);
                imageAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(AddBusinessActivity.this, "图片不能大于十张！", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private static SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static BusinessDao businessDao;
    private DaoMaster.DevOpenHelper helper;

    /**
     * 保存业务记录
     */
    private void saveOrUpdateBusiness() {

        Long personId = Long.valueOf((String)SharePreferenceUtils.get(this, "personId", ""));
        if (personId != 0) {
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            businessDao = daoSession.getBusinessDao();
            Business business = new Business();
            business.setImage(StringUtils.listToString(lists));
            business.setBusinessInfo(et_business.getText().toString());
            business.setPersonId(personId);

            Log.i("info", "得到的PersonId=========> " + SharePreferenceUtils.get(this, "personId", ""));
            if (getIntent().hasExtra("businessId")) {
                business.setDate(businessDao.queryBuilder().where(BusinessDao.Properties.Id.eq(businessId)).list().get(0).getDate());
                businessDao.insertOrReplace(business);
            } else {
                business.setDate(new Date());// 时间是唯一的，根据时间来创建业务
                businessDao.insert(business);
            }
            helper.close();
        }


    }

    /**
     * 可编辑
     */
    private void enableEdit() {
        et_business.setEnabled(true);
        iv_business.setEnabled(true);

    }


    /**
     * 不可编辑
     */
    private void unableEdit() {
        et_business.setEnabled(false);
        iv_business.setEnabled(false);
    }


    /**
     * 设置数据
     *
     * @param businessId
     */
    private void setData(long businessId) {
        List<Business> businesses = businessDao.queryBuilder().where(BusinessDao.Properties.Id.eq(businessId)).list();
        Business business = businesses.get(0);
        et_business.setText(business.getBusinessInfo());
        image = business.getImage();
        lists = StringUtils.stringToList(image);
    }

    /**
     * 删除数据库
     */
    private void deleteBusiness() {
        if (getIntent().hasExtra("businessId")) {
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            businessDao = daoSession.getBusinessDao();
            businessDao.delete(businessDao.queryBuilder().where(BusinessDao.Properties.Id.eq(businessId)).list().get(0));
            helper.close();
        }

        finish();
    }

    @Override
    public void deleted(List<String> list, int position) {
        lists = list;
        imageAdapter.notifyDataSetChanged();
    }
}
