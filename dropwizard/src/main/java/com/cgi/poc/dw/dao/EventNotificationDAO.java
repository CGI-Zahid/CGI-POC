package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventNotification;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import javax.validation.Validator;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

public class EventNotificationDAO extends AbstractDAO<EventNotification> {

    Validator validator;

    @Inject
    public EventNotificationDAO(SessionFactory factory, Validator validator) {
        super(factory);
        this.validator = validator;
    }

    public EventNotification save(EventNotification eventNotification) {
        eventNotification = this.persist(eventNotification);
        return eventNotification;
    }

    public List<EventNotification> retrieveAll() {
        Criteria criteria = this.criteria();
        List<EventNotification> resultList = criteria.list();
        return resultList;
    }
}
