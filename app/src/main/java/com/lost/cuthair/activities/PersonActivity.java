package com.lost.cuthair.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lost.cuthair.R;
import com.lost.cuthair.dao.Business;
import com.lost.cuthair.dao.BusinessDao;
import com.lost.cuthair.dao.DaoMaster;
import com.lost.cuthair.dao.DaoSession;
import com.lost.cuthair.dao.Person;
import com.lost.cuthair.dao.PersonDao;
import com.lost.cuthair.utils.ImageUtils;
import com.lost.cuthair.utils.SharePreferenceUtils;
import com.lost.cuthair.utils.StringUtils;
import com.lost.cuthair.views.HeadChangePopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 个人信息界面
 * Created by lost on 2016/4/13.
 */
public class PersonActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_head;
    private EditText et_name;
    private EditText et_job;
    private EditText et_height;
    private EditText et_body_type;
    private EditText et_hate;
//    private EditText et_money;
//    private EditText et_discount;
    private EditText et_remark;
    private EditText et_label;
//    private EditText et_number;
    private EditText et_color;
    private EditText et_weixin;
    private EditText et_qq;
    private EditText et_phone;
    private TextView et_birthday;
    private EditText et_address;
    private EditText et_constellation;
    private RelativeLayout rl_business;
    private RadioGroup rg_sex;
    private TextView tv_man;
    private TextView tv_woman;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private ImageView iv_call;

    private TextView middle;
    private AppCompatCheckBox right;
    private TextView left;
    private final int SELECT_PICTURE = 102;
    private final int SELECT_CAMERA = 101;
    private Uri imageUri;

    private HeadChangePopWindow picPopupWindow;

    private String name;
    private Boolean sex;
    private String height;
    private String size;
    private String color;
    private String birthday;
    private String hate;
    private String weixin;
    private String qq;
    private String constellation;
    private String phone;
    private String address;
    private String money = "1";
    private String discount = "1";
    private String label;
    private String remark;
    private String number = "1";
    private String image;
    private String job;
    private long personId;
    private String date;

    private boolean isSave = false; //是否点击了保存
    private boolean isNewCreate = true; //是否是第一次创建
    private boolean isFromHome = false; //是否是从主页过来


    private ImageLoaderConfiguration configuration;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        configuration = ImageLoaderConfiguration
                .createDefault(this);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);

        bindViews();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "person-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        personDao = daoSession.getPersonDao();

        if (getIntent().hasExtra("date")) {
            isFromHome = true;
            date = getIntent().getStringExtra("date");
        }else  {
            date = new Date().toString();
        }

        /**
         * 适配数据
         */
            setData();
    }

    // End Of Content View Elements

    /**
     * 绑定组件
     */
    private void bindViews() {

        iv_head = (ImageView) findViewById(R.id.iv_head);
        et_name = (EditText) findViewById(R.id.et_name);
        et_job = (EditText) findViewById(R.id.et_job);
        et_height = (EditText) findViewById(R.id.et_height);
        et_body_type = (EditText) findViewById(R.id.et_body_type);
        et_hate = (EditText) findViewById(R.id.et_hate);
        et_color = (EditText) findViewById(R.id.et_color);
        et_weixin = (EditText) findViewById(R.id.et_weixin);
        et_qq = (EditText) findViewById(R.id.et_qq);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_birthday = (TextView) findViewById(R.id.et_birthday);
        et_birthday.setOnClickListener(this);
        et_address = (EditText) findViewById(R.id.et_address);
//        et_money = (EditText) findViewById(R.id.et_money);
//        et_discount = (EditText) findViewById(R.id.et_discount);
        et_label = (EditText) findViewById(R.id.et_label);
//        et_number = (EditText) findViewById(R.id.et_number);
        rl_business = (RelativeLayout) findViewById(R.id.rl_business);
        et_remark = (EditText) findViewById(R.id.et_remark);
        rl_business.setOnClickListener(this);
        et_constellation = (EditText) findViewById(R.id.et_constellation);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        tv_man = (TextView) findViewById(R.id.tv_man);
        tv_woman = (TextView) findViewById(R.id.tv_woman);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);
        iv_call = (ImageView) findViewById(R.id.iv_call);
        iv_call.setOnClickListener(this);

        rb_man.setChecked(true);
        sex = true;
        tv_man.setTextColor(getResources().getColor(R.color.colorBackGround));
        rb_man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_man.setTextColor(getResources().getColor(R.color.colorBackGround));
                    sex = true;// 设置为男
                }else {
                    tv_man.setTextColor(Color.BLACK);
                }
            }
        });

        rb_woman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_woman.setTextColor(getResources().getColor(R.color.colorBackGround));
                    sex = false;// 设置为女
                }else {
                    tv_woman.setTextColor(Color.BLACK);
                }
            }
        });

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


            }
        });
        pic1 = (ImageView) findViewById(R.id.iv_pic1);
        pic2 = (ImageView) findViewById(R.id.iv_pic2);
        pic3 = (ImageView) findViewById(R.id.iv_pic3);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);

        iv_head.setOnClickListener(this);

        middle = (TextView) findViewById(R.id.middle);
        middle.setVisibility(View.GONE);
        right = (AppCompatCheckBox) findViewById(R.id.right);
        left = (TextView) findViewById(R.id.left);
        right.setVisibility(View.VISIBLE);
        right.setTextSize(16);
        right.setTextColor(Color.WHITE);
        middle.setText("个人信息");
        right.setText("保存");
        left.setOnClickListener(this);

        // 点击进行编辑或保存
        right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (isNewCreate) {
                        isSave = true;
                        if (TextUtils.isEmpty(et_name.getText())) {
                            Toast.makeText(PersonActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(et_phone.getText())) {
                            Toast.makeText(PersonActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        save();
                        finish();
                    } else {
                        right.setText("保存");
                        setAbleEdit();// 设置可编辑
                    }
                } else {
                    if (TextUtils.isEmpty(et_name.getText())) {
                        Toast.makeText(PersonActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(et_phone.getText())) {
                        Toast.makeText(PersonActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    save();
                    finish();
                }
            }
        });


        if (getIntent().hasExtra("name") && getIntent().hasExtra("number")) {
            et_name.setText(getIntent().getStringExtra("name"));
            et_phone.setText(getIntent().getStringExtra("number"));
        }
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
            case R.id.left:
                if (isNewCreate && !isSave) {
                    deletePersonAndBusiness();
                }
                finish();

                break;

            case R.id.iv_head:
                picPopupWindow = new HeadChangePopWindow(this, itemOnclick);
                picPopupWindow.showAtLocation(PersonActivity.this.findViewById(R.id.person), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            // 查看更多业务
            case R.id.rl_business:
                // 查看更多业务时，创建新的业务，此时会自动保存当前界面数据到数据库，并与之后操作进行关联
                save();
                Intent intent = new Intent(PersonActivity.this, BusinessRecordActivity.class);
                if (image != null){
                    intent.putExtra("imagePath", image); //发送图片路径
                }
                startActivity(intent);
                break;

            case R.id.et_birthday:
                new DatePickerDialog(PersonActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        et_birthday.setText(monthOfYear + "月" + dayOfMonth + "日" + "(" + year + "年" + ")");

                    }
                }, 2016, 4,4).show();
                break;

            // 调用系统打电话功能
            case R.id.iv_call:
                if (!TextUtils.isEmpty(et_phone.getText())) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(this.checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
                            Intent telIntent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+et_phone.getText().toString()));
                            startActivity(telIntent);
                        }else{
                            //
                        }
                    }else{
                        Intent telIntent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+et_phone.getText().toString()));
                        startActivity(telIntent);
                    }
                }
                break;

            // 添加业务记录
            case R.id.iv_add:
                save();
                Intent addBusinessIntent = new Intent(PersonActivity.this, AddBusinessActivity.class);
                startActivity(addBusinessIntent);
                break;
        }

    }


    /**
     * 设置不可编辑
     */
    private void setUnableEdit() {
        rg_sex.setEnabled(false);
        et_name.setEnabled(false);
        et_job.setEnabled(false);
        et_height.setEnabled(false);
        et_body_type.setEnabled(false);
        et_hate.setEnabled(false);
//        et_number.setEnabled(false);
//        et_money.setEnabled(false);
        et_label.setEnabled(false);
//        et_discount.setEnabled(false);
        et_remark.setEnabled(false);
        et_color.setEnabled(false);
        et_weixin.setEnabled(false);
        et_qq.setEnabled(false);
        rb_man.setEnabled(false);
        rb_woman.setEnabled(false);
        et_phone.setEnabled(false);
        et_birthday.setEnabled(false);
        et_address.setEnabled(false);
        et_constellation.setEnabled(false);
        iv_head.setEnabled(false);
    }

    /**
     * 设置可编辑
     */
    private void setAbleEdit() {
        rg_sex.setEnabled(true);
        et_name.setEnabled(true);
        et_job.setEnabled(true);
        et_height.setEnabled(true);
        et_body_type.setEnabled(true);
        et_hate.setEnabled(true);
//        et_number.setEnabled(true);
        et_remark.setEnabled(true);
        rb_man.setEnabled(true);
        rb_woman.setEnabled(true);
//        et_money.setEnabled(true);
//        et_discount.setEnabled(true);
        et_constellation.setEnabled(true);
        et_label.setEnabled(true);
        et_color.setEnabled(true);
        et_weixin.setEnabled(true);
        et_qq.setEnabled(true);
        et_phone.setEnabled(true);
        et_birthday.setEnabled(true);
        et_address.setEnabled(true);
        iv_head.setEnabled(true);
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

            Log.i("info", "图片的URI------->" + uri);

            // 是否是从图库选择图片
            if (DocumentsContract.isDocumentUri(this, uri) || requestCode == SELECT_CAMERA){
                ImageUtils.setImageFromFilePath(ImageUtils.getPath(this, uri), iv_head);
                image = ImageUtils.getPath(this, uri);
            }else {
                ImageUtils.setImageFromFilePath(ImageUtils.selectImage(this, data), iv_head);
                image = ImageUtils.selectImage(this, data);
            }



            Log.i("info", "图片的绝对路径------->" + ImageUtils.getPath(this, uri));
        }
    }


    private static SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static PersonDao personDao;

    /**
     * 保存
     */
    private void save() {
        name = et_name.getText().toString();
        if (TextUtils.isEmpty(et_name.getText())) {
            name = "未命名";
        }
        height = et_height.getText().toString();
        size = et_body_type.getText().toString();
        color = et_color.getText().toString();
        birthday = et_birthday.getText().toString();
        hate = et_hate.getText().toString();
        weixin = et_weixin.getText().toString();
        qq = et_qq.getText().toString();
        constellation = et_constellation.getText().toString();
        phone = et_phone.getText().toString();
        address = et_address.getText().toString();
        job = et_job.getText().toString();
        label = et_label.getText().toString();
        remark = et_remark.getText().toString();
//        money = et_money.getText().toString();
//        discount = et_discount.getText().toString();
//        number = et_number.getText().toString();
        Person person = new Person();

        person = setToPerson(person);

        Log.i("info", "person----number--->" + number);
        if (isFromHome && getIntent().hasExtra("date")) {
            personDao.insertOrReplace(setToPerson(personDao.queryBuilder().where(PersonDao.Properties.Date.eq(getIntent().getStringExtra("date"))).list().get(0)));
        } else if (personId != 0){
            personDao.insertOrReplace(setToPerson(personDao.queryBuilder().where(PersonDao.Properties.Id.eq(personId)).list().get(0)));
        }else {
            personDao.insert(person);
        }
        personId = personDao.queryBuilder().where(PersonDao.Properties.Date.eq(date)).list().get(0).getId();
        Log.i("info", "保存的personId---->" + personId);
        SharePreferenceUtils.put(this, "personId", String.valueOf(personId));
    }

    @Override
    protected void onResume() {
        super.onResume();

        setImageList();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("info", "isSave---->" + isSave + "       isNewCreate----->" + isNewCreate);
        if (isNewCreate && !isSave) {
            deletePersonAndBusiness();
        }
    }

    /**
     * 如果未保存，则删除数据
     */
    private void deletePersonAndBusiness() {
        if (!isSave && isNewCreate) {
            Log.i("info", "删除了文件");
            if (personDao.queryBuilder().where(PersonDao.Properties.Date.eq(date)).list().size() != 0) {
                personDao.delete(personDao.queryBuilder().where(PersonDao.Properties.Date.eq(date)).list().get(0));
                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "business-db", null);
                db = helper.getWritableDatabase();
                daoMaster = new DaoMaster(db);
                daoSession = daoMaster.newSession();
                BusinessDao businessDao = daoSession.getBusinessDao();
                businessDao.deleteInTx(businessDao.queryBuilder().where(BusinessDao.Properties.PersonId.eq(personId)).list());
            }

        }
    }


    /**
     * 如果从主页过来适配页面数据
     */

    private ImageView pic1;
    private ImageView pic2;
    private ImageView pic3;
    private ImageView iv_add;
    private void setData() {
        if (getIntent().hasExtra("date") && !getIntent().hasExtra("name")) {
            Log.i("info", "是否获得到数据---》" + getIntent().hasExtra("date"));
            isNewCreate = false;// 有数据表示不是新创建
            Person person = personDao.queryBuilder().where(PersonDao.Properties.Date.eq(getIntent().getStringExtra("date"))).list().get(0);
            name = person.getName();
            address = person.getAddress();
            birthday = person.getBirthday();
            color = person.getColor();
            constellation = person.getConstellation();
            hate = person.getHate();
            image = person.getImage();
            job = person.getJob();
            qq = person.getQq();
            weixin = person.getWeixin();
            size = person.getSize();
            phone = person.getPhone();
            height = person.getHeight();
            money = person.getMoney();
            remark = person.getRemark();
            label = person.getLabel();
            sex = person.getSex();
            discount = person.getDiscount();
            number = person.getNumber();
            constellation = person.getConstellation();

            personId = person.getId();
            Log.i("info", "设置界面获得personId--->" + personId);

            et_name.setText(name);
            et_job.setText(job);
            et_height.setText(height);
            et_body_type.setText(size);
            et_hate.setText(hate);
//            et_number.setText(number);
//            et_money.setText(money);
            et_label.setText(label);
//            et_discount.setText(discount);
            et_remark.setText(remark);
            et_color.setText(color);
            et_weixin.setText(weixin);
            et_qq.setText(qq);
            et_phone.setText(phone);
            et_birthday.setText(birthday);
            et_address.setText(address);
            et_constellation.setText(constellation);

            if (image != null) {
                ImageUtils.setImageFromFilePath(image, iv_head);
            }

            if (sex) {
                rb_man.setChecked(true);
                tv_man.setTextColor(getResources().getColor(R.color.colorBackGround));
            }else {
                rb_woman.setChecked(true);
                tv_woman.setTextColor(getResources().getColor(R.color.colorBackGround));
            }


            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "business-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            BusinessDao businessDao = daoSession.getBusinessDao();
            List<Business> businesses =  businessDao.queryBuilder().where(BusinessDao.Properties.PersonId.eq(personId)).list();



            Log.i("info", "业务大小为....> " + businesses.size());
            Log.i("info", "personId....> " + personId);
            if (businesses.size() > 3) {
                iv_add.setVisibility(View.GONE);
                pic1.setVisibility(View.VISIBLE);
                pic2.setVisibility(View.VISIBLE);
                pic3.setVisibility(View.VISIBLE);
                ImageUtils.useImageLoaderSetImage( imageLoader,pic1, StringUtils.stringToList(businesses.get(0).getImage()).get(0));
                ImageUtils.useImageLoaderSetImage( imageLoader,pic2, StringUtils.stringToList(businesses.get(1).getImage()).get(0));
                ImageUtils.useImageLoaderSetImage( imageLoader,pic3, StringUtils.stringToList(businesses.get(2).getImage()).get(0));
            } else if (businesses.size() == 1){
                iv_add.setVisibility(View.GONE);
                pic1.setVisibility(View.VISIBLE);
                pic2.setVisibility(View.GONE);
                pic3.setVisibility(View.GONE);
                ImageUtils.useImageLoaderSetImage( imageLoader,pic1, StringUtils.stringToList(businesses.get(0).getImage()).get(0));
            }else if (businesses.size() == 2) {
                iv_add.setVisibility(View.GONE);
                pic1.setVisibility(View.VISIBLE);
                pic2.setVisibility(View.VISIBLE);
                pic3.setVisibility(View.GONE);
                ImageUtils.useImageLoaderSetImage( imageLoader,pic1, StringUtils.stringToList(businesses.get(0).getImage()).get(0));
                ImageUtils.useImageLoaderSetImage( imageLoader,pic2, StringUtils.stringToList(businesses.get(1).getImage()).get(0));
            }

            // 设置不可点击
            setUnableEdit();

            right.setText("编辑");
        }


    }

    /**
     * 从业务记录里面返回更新列表图片到界面
     */
    private void setImageList() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "business-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        BusinessDao businessDao = daoSession.getBusinessDao();
        List<Business> businesses =  businessDao.queryBuilder().where(BusinessDao.Properties.PersonId.eq(personId)).list();

        Log.i("info", "业务大小为....> " + businesses.size());
        Log.i("info", "personId....> " + personId);
        if (businesses.size() > 3) {
            iv_add.setVisibility(View.GONE);
            pic1.setVisibility(View.VISIBLE);
            pic2.setVisibility(View.VISIBLE);
            pic3.setVisibility(View.VISIBLE);
            ImageUtils.useImageLoaderSetImage( imageLoader,pic1, StringUtils.stringToList(businesses.get(0).getImage()).get(0));
            ImageUtils.useImageLoaderSetImage( imageLoader,pic2, StringUtils.stringToList(businesses.get(1).getImage()).get(0));
            ImageUtils.useImageLoaderSetImage( imageLoader,pic3, StringUtils.stringToList(businesses.get(2).getImage()).get(0));
        } else if (businesses.size() == 1){
            iv_add.setVisibility(View.GONE);
            pic1.setVisibility(View.VISIBLE);
            pic2.setVisibility(View.GONE);
            pic3.setVisibility(View.GONE);
            ImageUtils.useImageLoaderSetImage( imageLoader,pic1, StringUtils.stringToList(businesses.get(0).getImage()).get(0));
        }else if (businesses.size() == 2) {
            iv_add.setVisibility(View.GONE);
            pic1.setVisibility(View.VISIBLE);
            pic2.setVisibility(View.VISIBLE);
            pic3.setVisibility(View.GONE);
            ImageUtils.useImageLoaderSetImage( imageLoader,pic1, StringUtils.stringToList(businesses.get(0).getImage()).get(0));
            ImageUtils.useImageLoaderSetImage( imageLoader,pic2, StringUtils.stringToList(businesses.get(1).getImage()).get(0));
        }

    }

    /**
     * 给person添加数据
     * @param person
     */
    private Person setToPerson(Person person) {

        if (name == null) {
            name = "未命名";
        }

        person.setName(name);
        person.setAddress(address);
        person.setBirthday(birthday);
        person.setColor(color);
        person.setConstellation(constellation);
        person.setHate(hate);
        person.setImage(image);
        person.setJob(job);
        person.setQq(qq);
        person.setWeixin(weixin);
        person.setSize(size);
        person.setPhone(phone);
        person.setHeight(height);
        person.setMoney(money);
        person.setRemark(remark);
        person.setLabel(label);
        person.setSex(false);
        person.setDiscount(discount);
        person.setNumber(number);
        person.setConstellation(constellation);
        person.setDate(date);
        person.setSex(sex);


        return person;
    }

}
