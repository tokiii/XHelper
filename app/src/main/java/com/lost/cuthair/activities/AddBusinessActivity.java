package com.lost.cuthair.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lost.cuthair.R;
import com.lost.cuthair.dao.Business;
import com.lost.cuthair.dao.BusinessDao;
import com.lost.cuthair.dao.DaoMaster;
import com.lost.cuthair.dao.DaoSession;
import com.lost.cuthair.utils.ImageUtils;
import com.lost.cuthair.views.SelectPicPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by lost on 2016/4/13.
 */
public class AddBusinessActivity extends AppCompatActivity implements View.OnClickListener {

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


        et_business = (AppCompatEditText) findViewById(R.id.et_business);
        iv_business = (ImageView) findViewById(R.id.iv_business);
        iv_business.setOnClickListener(this);

    }


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

            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_business:
                picPopupWindow = new SelectPicPopupWindow(this, itemOnclick);
                picPopupWindow.showAtLocation(AddBusinessActivity.this.findViewById(R.id.addbusiness), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.right:

                if (right.getText().equals("编辑")) {
                    enableEdit(); //设置可编辑状态
                    right.setText("保存");
                }else {
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
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor imageCursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            imageCursor.moveToFirst();
            String img_path = imageCursor.getString(actual_image_column_index);
            Log.i("info", "根据图片路径获取到的uri----->" + uri.toString());
            image = img_path;
            ImageUtils.useImageLoaderSetImage(imageLoader, iv_business, image);
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
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        businessDao = daoSession.getBusinessDao();
        Business business = new Business();
        business.setImage(image);
        business.setBusinessInfo(et_business.getText().toString());
        business.setPersonId(PersonActivity.personId);

        Log.i("info", "得到的PersonId=========> " + PersonActivity.personId);
        if (getIntent().hasExtra("businessId")) {
            business.setDate(businessDao.queryBuilder().where(BusinessDao.Properties.Id.eq(businessId)).list().get(0).getDate());
            businessDao.insertOrReplace(business);
        }else  {
            business.setDate(new Date());// 时间是唯一的，根据时间来创建业务
            businessDao.insert(business);
        }
        helper.close();

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
     * @param businessId
     */
    private void setData(long businessId) {
        List<Business> businesses = businessDao.queryBuilder().where(BusinessDao.Properties.Id.eq(businessId)).list();
        Business business = businesses.get(0);
        et_business.setText(business.getBusinessInfo());
        image = business.getImage();
        ImageUtils.useImageLoaderSetImage(imageLoader, iv_business, business.getImage());

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
}
