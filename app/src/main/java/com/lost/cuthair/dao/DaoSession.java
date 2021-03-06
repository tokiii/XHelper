package com.lost.cuthair.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.lost.cuthair.dao.Person;
import com.lost.cuthair.dao.Business;

import com.lost.cuthair.dao.PersonDao;
import com.lost.cuthair.dao.BusinessDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig personDaoConfig;
    private final DaoConfig businessDaoConfig;

    private final PersonDao personDao;
    private final BusinessDao businessDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        personDaoConfig = daoConfigMap.get(PersonDao.class).clone();
        personDaoConfig.initIdentityScope(type);

        businessDaoConfig = daoConfigMap.get(BusinessDao.class).clone();
        businessDaoConfig.initIdentityScope(type);

        personDao = new PersonDao(personDaoConfig, this);
        businessDao = new BusinessDao(businessDaoConfig, this);

        registerDao(Person.class, personDao);
        registerDao(Business.class, businessDao);
    }
    
    public void clear() {
        personDaoConfig.getIdentityScope().clear();
        businessDaoConfig.getIdentityScope().clear();
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public BusinessDao getBusinessDao() {
        return businessDao;
    }

}
