package com.cgi.poc.dw.dao.impl;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.cgi.poc.dw.auth.model.EventType;
import com.cgi.poc.dw.dao.EventDao;
import com.cgi.poc.dw.dao.mapper.EventMapper;
import com.cgi.poc.dw.dao.model.Event;

//Uncomment @LogSqlFactory to enable SQL debugging
//@LogSqlFactory
@RegisterMapper(EventMapper.class)
public interface EventDaoImpl extends EventDao{

	@Override
	@SqlUpdate("INSERT INTO users (id, name, evenType, url, timestamp, description) values (:id, :name, :evenType, :url, :timestamp, :description)")
	public void createEvent(Event event) throws Exception;

	@Override
	@SqlQuery("SELECT * FROM users WHERE eventType = :eventType")
	public List<Event> findEventByType(EventType eventType);

	@Override
	@SqlQuery("SELECT * FROM events")
	public List<Event> getAllEvents();

}
