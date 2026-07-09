package com.mindslab.web.common.support.utils;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.support.JdbcUtils;

@SuppressWarnings("serial")
public class CustomMap extends HashMap<String, Object> implements Serializable{


    /**
    * # Mybatis에서 return 받는 결과물을 CamelCase 처리
    */
    @Override
    public Object put(String key, Object value){
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
    }
    
    /**
     *  carmerCase 처리를 안하는 put
     */
    public Object orginPut(String key, Object value){
        return super.put(key, value);
    }
    
	/**
	 * <PRE>
	 * 1. MethodName : getString
	 * 2. ClassName  : CustomMap
	 * 3. Comment   : CustomMap 에서 String 형태로 값추출
	 * </PRE>
	 * @param key
	 * @return
	 */
	public String getString(String key){
		return getString(key, "");
	}

	/**
	 * <PRE>
	 * 1. MethodName : getString
	 * 2. ClassName  : CustomMap
	 * 3. Comment   : CustomMap 에서 String 형태로 값추출
	 * </PRE>
	 * @param key
	 * @param def
	 * @return
	 */
	public String getString(String key, String def){
		if (this.get(key) == null || "".equals(String.valueOf(this.get(key)))){
			return def;
		} else{
			return (String.valueOf(this.get(key))).trim();
		}
	}
	
	
	
	/**
	 * <PRE>
	 * 1. MethodName : getBoolean
	 * 2. ClassName  : CustomMap
	 * 3. Comment   : CustomMap 에서 boolean 형태로 값추출
	 * </PRE>
	 *   @return boolean
	 *   @param key
	 *   @return
	 */
	public boolean getBoolean(String key) {
		String value = getString(key);
		boolean isTrue = false;
		try {
			isTrue = (new Boolean(value)).booleanValue();
		} catch (Exception e) {
		}
		return isTrue;
	}
	
	/**
	 * @return
	 */
	public String[] getKeysName(){
		String[] keys = null;
		try{
			Set<?> _set = this.keySet();
			keys = new String[_set.size()];
			Iterator<?> iter = _set.iterator();
			for (int i = 0; iter.hasNext(); i++){
				keys[i] = (String) iter.next();
			}
		} catch (Exception e){
		}
		return keys;
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : getList
	 * 2. ClassName  : CustomMap
	 * 3. Comment   : CustomMap 에서 List 형태로 값추출
	 * </PRE>
	 *   @return List<Object>
	 *   @param key
	 *   @return
	 */
	public List<Object> getList(String key) {

		List<Object> list = null;

		try {
			list = new ArrayList<Object>();
			list = (List<Object>) super.get(key);
		} catch (ClassCastException e) {

			try {

				list = new ArrayList<Object>();
				list.add((String) super.get(key));
			} catch (Exception se) {
				list = null;
			}
		}

		return list;
	}	
    
	public String getHtml(String key) {
		String value = null;
		try {
			Object o = (Object) super.get(key);
			Class c = o.getClass();
			if (o == null)
				value = "";
			else if (c.isArray()) {
				int length = Array.getLength(o);
				if (length == 0)
					value = "";
				else {
					Object item = Array.get(o, 0);
					if (item == null)
						value = "";
					else {
						value = item.toString();
						value = value.replaceAll("\n", "<br/>");
						value = value.replaceAll(" ", "&nbsp;");
					}
				}
			} else {
				value = o.toString();
				value = value.replaceAll("\n", "<br/>");
				value = value.replaceAll(" ", "&nbsp;");
			}
		} catch (Exception e) {
			value = "";
		}
		return value;
	}
	
	@Override
	public Object get(Object arg0){
		if (arg0 == null){
			return null;
		} else if (arg0 instanceof String){
			Object o = super.get(arg0);
			if (o instanceof Clob){
				String content = null;
				try{
					Clob lobData = ClobTransport.wrap(o);
					Reader in = lobData.getCharacterStream();
					Writer sw = new StringWriter();
					char buffer[] = new char[4096];
					int n;
					while ((n = in.read(buffer)) != -1){
						sw.write(buffer, 0, n);
					}
					content = sw.toString();
				} catch (Exception ex){
					ex.printStackTrace();
				}
				return content;
			} else if (o instanceof Blob){
				Blob lobData = BlobTransport.wrap(o);
				return lobData;
			} else if (o instanceof String){
				return StringUtil.nvl(o.toString(), "");
			} else{
				return o;
			}
		} else{
			return super.get(arg0);
		}
	}
	
	/**
	 * @param key
	 * @return
	 */
	public int getInt(String key){
		return getInt(key, 0);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public int getInt(String key, int value){
		return Integer.parseInt(getString(key, String.valueOf(value)));
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public int getInt(String key, String value){
		return Integer.parseInt(getString(key, value));
	}	
}
