/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventEarthquake;
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
public class EventEarthquakeDAO extends AbstractDAO<EventEarthquake> {

  private final static Logger LOG = LoggerFactory.getLogger(FireEventDAO.class);

    private int pageNumber = 0;
    private int pageSize = 0;
    Validator validator;
 
    public EventEarthquakeDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;

    }

    public List<EventEarthquake> findById(String id) {

        //return Optional.fromNullable(get(id));
        Criteria criteria = this.criteria();

        //contract id, page, page size
        criteria.add(Restrictions.eq("eventEarthquakePK.eqid", id));
        List<EventEarthquake> resultList  = null;
        resultList  =  criteria.list();
 
        return resultList;
    }
    public EventEarthquake selectForUpdate(EventEarthquake event) {
        return ((EventEarthquake) this.currentSession().load(EventEarthquake.class,event.getEventEarthquakePK()));
    }
    
    public EventEarthquake save(EventEarthquake event) {
        EventEarthquake merge = (EventEarthquake)  currentSession().merge(event);
        return  merge;
    }

    public void flush() {
        this.currentSession().flush();
    }
}
