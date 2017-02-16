/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventWeather;
import io.dropwizard.hibernate.AbstractDAO;
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
public class EventWeatherDAO extends AbstractDAO<EventWeather> {

  private final static Logger LOG = LoggerFactory.getLogger(FireEventDAO.class);

    private int pageNumber = 0;
    private int pageSize = 0;
    Validator validator;


    public EventWeatherDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;

    }

    public EventWeather findById(String id) {

        //return Optional.fromNullable(get(id));
        Criteria criteria = this.criteria();

        //contract id, page, page size
        criteria.add(Restrictions.eq("warnid", id));
        EventWeather event = null;
        event = (EventWeather) criteria.uniqueResult();
        
        return event;
    }
    public EventWeather selectForUpdate(EventWeather event) {
        return ((EventWeather) this.currentSession().load(EventWeather.class, event.getWarnid()));
    }
    
    public EventWeather update(EventWeather event) {
        EventWeather merge = (EventWeather)  currentSession().merge(event);
        return  merge;
    }

    public void flush() {
        this.currentSession().flush();
    }
}
