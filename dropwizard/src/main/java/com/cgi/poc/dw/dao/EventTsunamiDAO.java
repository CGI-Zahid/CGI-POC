/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventTsunami;
import io.dropwizard.hibernate.AbstractDAO;
import java.math.BigDecimal;
 import javax.validation.Validator;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author candy.giles
 */
public class EventTsunamiDAO extends AbstractDAO<EventTsunami> {

  private final static Logger LOG = LoggerFactory.getLogger(FireEventDAO.class);

    private int pageNumber = 0;
    private int pageSize = 0;
    Validator validator;


    public EventTsunamiDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;

    }

    public EventTsunami findById(BigDecimal id) {

        //return Optional.fromNullable(get(id));
        Criteria criteria = this.criteria();

        //contract id, page, page size
        criteria.add(Restrictions.eq("id", id));
        EventTsunami event = null;
        event = (EventTsunami) criteria.uniqueResult();
        
        return event;
    }
    public EventTsunami selectForUpdate(EventTsunami event) {
        return ((EventTsunami) this.currentSession().load(EventTsunami.class, event.getId()));
    }
    
    public EventTsunami save(EventTsunami event) {
        EventTsunami merge = (EventTsunami)  currentSession().merge(event);
        return  merge;
    }

    public void flush() {
        this.currentSession().flush();
    }
}
