/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventFlood;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import javax.validation.Validator;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author dawna.floyd
 */
public class EventFloodDAO extends AbstractDAO<EventFlood> {

  private final static Logger LOG = LoggerFactory.getLogger(FireEventDAO.class);

    private int pageNumber = 0;
    private int pageSize = 0;
    Validator validator;
 
    public EventFloodDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;

    }

    public List<EventFlood> findById(String id) {

        //return Optional.fromNullable(get(id));
        Criteria criteria = this.criteria();

        //contract id, page, page size
        criteria.add(Restrictions.eq("eventFloodPK.waterbody", id));
        List<EventFlood> resultList  = null;
        resultList  =  criteria.list();
 
        return resultList;
    }
    public EventFlood selectForUpdate(EventFlood event) {
        return ((EventFlood) this.currentSession().load(EventFlood.class,event.getEventFloodPK()));
    }
    
    public EventFlood save(EventFlood event) {
        EventFlood merge = (EventFlood)  currentSession().merge(event);
        return  merge;
    }

    public void flush() {
        this.currentSession().flush();
    }
}
