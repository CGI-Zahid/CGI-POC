/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventHurricane;
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
public class EventHurricaneDAO extends AbstractDAO<EventHurricane> {

  private final static Logger LOG = LoggerFactory.getLogger(EventHurricaneDAO.class);

    private int pageNumber = 0;
    private int pageSize = 0;
    Validator validator;
 
    public EventHurricaneDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;

    }

    public List<EventHurricane> findById(String id) {

        //return Optional.fromNullable(get(id));
        Criteria criteria = this.criteria();

        //contract id, page, page size
        criteria.add(Restrictions.eq("eventHurricanePK.id", id));
        List<EventHurricane> resultList  = null;
        resultList  =  criteria.list();
 
        return resultList;
    }
    public EventHurricane selectForUpdate(EventHurricane event) {
        return ((EventHurricane) this.currentSession().load(EventHurricane.class,event.getEventHurricanePK()));
    }
    
    public EventHurricane save(EventHurricane event) {
        EventHurricane merge = (EventHurricane)  currentSession().merge(event);
        return  merge;
    }

    public void flush() {
        this.currentSession().flush();
    }
}
