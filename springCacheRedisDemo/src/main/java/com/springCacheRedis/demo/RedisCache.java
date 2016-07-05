package com.springCacheRedis.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCache implements Cache{
	/**
	 * redis
	 */
	private RedisTemplate<String, Object> redisTemplate;
	/**
	 * »º´æÃû³Æ
	 */
	private String name;
	

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void clear() {
		redisTemplate.executePipelined(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.flushDb();
				return "clear OK!";
			}
		});
	}

	@Override
	public void evict(Object key) {
		final String keyf=(String) key;
		redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long del = connection.del(keyf.getBytes());
				return del;
			}
		});
	}

	@Override
	public ValueWrapper get(Object key) {
		final String keyf = (String) key;
		Object object = null;
		object = redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] key = keyf.getBytes();
				byte[] value = connection.get(key);
				if(value==null){
					return null;
				}else{
					return toObject(value);
				}
			}
		});
		return (object!=null?new SimpleValueWrapper(object):null);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Object toObject(byte[] value) {
		Object obj=null;
		try {
			ByteArrayInputStream bis= new ByteArrayInputStream(value);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public <T> T get(Object arg0, Class<T> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this.redisTemplate;
	}

	@Override
	public void put(Object key, Object value) {
		final String keyf = (String) key;
		final Object valuef = value;
		final long liveTime = 86400;
		redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keyb = keyf.getBytes();
				byte[] valueb = toByteArray(valuef);
				connection.set(keyb, valueb);
				if(liveTime>0){
					connection.expire(keyb, liveTime);
				}
				return 1l;
			}
		});
		
	}

	private byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.close();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}

	@Override
	public ValueWrapper putIfAbsent(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
