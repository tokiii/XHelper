package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * greenDao生成类
 */
public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.lost.cuthair.dao");

        addPersonInfo(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addPersonInfo(Schema schema) {
        Entity person = schema.addEntity("Person");
        person.addIdProperty().autoincrement();
        person.addStringProperty("name").notNull();
        person.addBooleanProperty("sex");
        person.addStringProperty("height");
        person.addStringProperty("size"); //体型
        person.addStringProperty("color"); //肤色
        person.addStringProperty("birthday");
        person.addStringProperty("hate"); //忌讳
        person.addStringProperty("weixin");
        person.addStringProperty("qq");
        person.addStringProperty("constellation"); //星座
        person.addStringProperty("phone");
        person.addStringProperty("address"); //地址
        person.addStringProperty("image"); //头像
        person.addStringProperty("job"); //职业
        person.addStringProperty("remark"); //备注
        person.addStringProperty("label"); //标签
        person.addStringProperty("money"); //账户余额
        person.addStringProperty("discount"); //折扣
        person.addStringProperty("number"); //卡号


        person.addStringProperty("date");// 唯一标识

        Entity business = schema.addEntity("Business");
        business.setTableName("BUSINESS");
        business.addIdProperty();
        business.addStringProperty("image");
        business.addStringProperty("businessInfo");
        business.addDateProperty("date").unique();
        Property personId = business.addLongProperty("personId").notNull().getProperty();
        person.addToMany(business, personId).setName("businesses");
        business.addToOne(person, personId);

    }

}
