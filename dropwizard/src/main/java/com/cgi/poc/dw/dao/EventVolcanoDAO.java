/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventVolcano;
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
public class EventVolcanoDAO extends AbstractDAO<EventVolcano> {

  private final static Logger LOG = LoggerFactory.getLogger(EventVolcanoDAO.class);

    private int pageNumber = 0;
    private int pageSize = 0;
    Validator validator;
 
    public EventVolcanoDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;

    }

    public List<EventVolcano> findById(String id) {

        //return Optional.fromNullable(get(id));
        Criteria criteria = this.criteria();

        //contract id, page, page size
        criteria.add(Restrictions.eq("eventVolcanoPK.id", id));
        List<EventVolcano> resultList  = null;
        resultList  =  criteria.list();
 
        return resultList;
    }
    public EventVolcano selectForUpdate(EventVolcano event) {
        return ((EventVolcano) this.currentSession().load(EventVolcano.class,event.getEventVolcanoPK()));
    }
    
    public EventVolcano save(EventVolcano event) {
        EventVolcano merge = (EventVolcano)  currentSession().merge(event);
        return  merge;
    }

    public void flush() {
        this.currentSession().flush();
    }
}
