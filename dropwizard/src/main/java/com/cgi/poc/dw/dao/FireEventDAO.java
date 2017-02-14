/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import java.util.logging.Logger;

import javax.validation.Validator;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.cgi.poc.dw.dao.model.FireEvent;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import io.dropwizard.hibernate.AbstractDAO;

/**
 *
 * @author candy.giles
 */
public class FireEventDAO extends AbstractDAO<FireEvent> {

    private static final Logger logger = Logger.getLogger(FireEventDAO.class.getName());

    private int pageNumber = 0;
    private int pageSize = 0;
    Validator validator;
    
    @Inject
    public FireEventDAO(SessionFactory factory) {
        super(factory);

    }
    
    /**
     * These are the fields we allow searching on.
     */
    static final ImmutableMap<String, String> QUERY_MAP
            = new ImmutableMap.Builder<String, String>()
            .put("uniquefireidentifier", "uniquefireidentifier")
            .put("notificationId", "notificationId")
            .build();

    public FireEventDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;

    }

    public FireEvent findById(String id) {

        //return Optional.fromNullable(get(id));
        Criteria criteria = this.criteria();

        //contract id, page, page size
        criteria.add(Restrictions.eq("uniquefireidentifier", id));
        FireEvent event = null;
        try {
            event = (FireEvent) criteria.uniqueResult();
        } catch (Exception e) {
            System.out.println("Error: Exception");
            System.out.println(e);

        }
        return event;
    }
    public FireEvent selectForUpdate(FireEvent event) {
        return ((FireEvent) this.currentSession().load(FireEvent.class, event.getUniquefireidentifier()));
    }
    
    public FireEvent update(FireEvent event) {
        FireEvent merge = (FireEvent)  this.currentSession().merge(event);
        return  merge;
    }

    public void flush() {
        this.currentSession().flush();
    }
}
