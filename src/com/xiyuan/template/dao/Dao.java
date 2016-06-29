package com.xiyuan.template.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BinaryType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.CharacterType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class Dao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public <T> ArrayList<T> all(Class<T> clazz) {
		return all(clazz, "");
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> all(Class<T> clazz, String queryStr) {
		Session session = getSession();
		ArrayList<T> all = null;
		try {
			Query query = session.createQuery("from " + clazz.getSimpleName() + " " + queryStr);
			all = (ArrayList<T>) query.list();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if(all == null) {
			all = new ArrayList<T>();
		}
		return all;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T find(long id, Class<T> clazz) {
		Session session = getSession();
		Query query = session.createQuery("from " + clazz.getSimpleName() + " where id=" + id);
		return (T) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> query(Class<T> clazz, String queryStr) {
		Session session = getSession();
		ArrayList<T> all = null;
		try {
			Query query = session.createQuery("from " + clazz.getSimpleName() + " queryStr");
			all = (ArrayList<T>) query.list();
		}
		catch (Exception e) {
			e.printStackTrace();
			all = new ArrayList<T>();
		}
		return all;
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> sqlQuery(Class<T> clazz, String sql) {
		if(sql.contains("_TABLE")) {
			String tableName = getTableName(clazz);
			sql = sql.replaceAll("_TABLE", tableName);
		}
		
		Session session = getSession();
		ArrayList<T> all = null;
		try {
			SQLQuery query = session.createSQLQuery(sql);
			setSqlResultTransformer(clazz, query);
			all = (ArrayList<T>) query.list();
		}
		catch (Exception e) {
			e.printStackTrace();
			all = new ArrayList<T>();
		}
		return all;
	}
	
	public String getTableName(Class<?> clazz)
	{
		Table table = (Table)clazz.getAnnotation(Table.class);
		if(table != null) {
			return table.name();
		}
		else {
			return "";
		}
	}
	
	private boolean setSqlResultTransformer(Class<?> clazz, SQLQuery query)
	{
		if(query == null)
		{
			return false;
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			if(fieldName != null && fieldName.equals("serialVersionUID"))
			{
				continue;
			}
			else if(fieldName != null && fieldName.length() > 0)
			{
				Class<?> fieldType = field.getType();
				if(fieldType != null)
				{
					if(fieldType == String.class)
					{
						query.addScalar(fieldName,new StringType());
					}
					else if(fieldType == int.class || fieldType == Integer.class)
					{
						query.addScalar(fieldName, new IntegerType());
					}
					else if(fieldType == long.class || fieldType == Long.class)
					{
						query.addScalar(fieldName,new LongType());
					}
					else if(fieldType == Date.class)
					{
						query.addScalar(fieldName,new TimestampType());
					}
					else if(fieldType == boolean.class || fieldType == Boolean.class)
					{
						query.addScalar(fieldName,new BooleanType());
					}
					else if(fieldType == short.class || fieldType == Short.class)
					{
						query.addScalar(fieldName,new ShortType());
					}
					else if(fieldType == double.class || fieldType == Double.class) 
					{
						query.addScalar(fieldName,new DoubleType());
					}
					else if(fieldType == float.class || fieldType == Float.class)
					{
						query.addScalar(fieldName,new FloatType());
					}
					else if(fieldType == Character.class)
					{
						query.addScalar(fieldName,new CharacterType());
					}
					else if(fieldType == BigDecimal.class)
					{
						query.addScalar(fieldName,new BigDecimalType());
					}
					else if(fieldType == byte[].class || fieldType == Byte[].class)
					{
						query.addScalar(fieldName,new BinaryType());
					}
				}
			}
		}
		query.setResultTransformer(Transformers.aliasToBean(clazz));
		return true;
	}
	
	public <T> T insert(T obj) {
		Session session = getSession();
		session.save(obj);
		return obj;
	}
	
	public <T> void insert(List<T> obj) {
		Session session = getSession();
		for (T t : obj) {
			session.save(t);
		}
	}
	
	public <T> T update(T obj) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		session.update(obj);
		transaction.commit();
		return obj;
	}
	
	public <T> void update(ArrayList<T> objs) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		for (T t : objs) {
			session.update(t);
		}
		transaction.commit();
	}
	
	public <T> boolean delete(T obj) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		session.delete(obj);
		transaction.commit();
		return true;
	}
	
	private Session getSession() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception e) {
		}
		if(session == null || !session.isConnected() || !session.isOpen()) {
			session = sessionFactory.openSession();
		}
		return session;
	}
	
}
