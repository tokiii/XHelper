package com.lost.cuthair.dao;

import com.lost.cuthair.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table BUSINESS.
 */
public class Business {

    private Long id;
    private String image;
    private String businessInfo;
    private java.util.Date date;
    private long personId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient BusinessDao myDao;

    private Person person;
    private Long person__resolvedKey;


    public Business() {
    }

    public Business(Long id) {
        this.id = id;
    }

    public Business(Long id, String image, String businessInfo, java.util.Date date, long personId) {
        this.id = id;
        this.image = image;
        this.businessInfo = businessInfo;
        this.date = date;
        this.personId = personId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBusinessDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    /** To-one relationship, resolved on first access. */
    public Person getPerson() {
        long __key = this.personId;
        if (person__resolvedKey == null || !person__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PersonDao targetDao = daoSession.getPersonDao();
            Person personNew = targetDao.load(__key);
            synchronized (this) {
                person = personNew;
            	person__resolvedKey = __key;
            }
        }
        return person;
    }

    public void setPerson(Person person) {
        if (person == null) {
            throw new DaoException("To-one property 'personId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.person = person;
            personId = person.getId();
            person__resolvedKey = personId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}