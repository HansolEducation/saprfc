package com.common;

import java.util.*;

public class DataSet {
	// RFC 필드 구성 List
	List<Map<Object,Object>> paramList;
	// block 및 RFC 필드요소 Map
	HashMap<Object, Object> paramMap;
	
	
	public DataSet(){
		paramList = new ArrayList<Map<Object,Object>>();
		paramMap = new HashMap<Object, Object>();
	}
	
	
	/**
	 * RFC 필드명과 값 정의
	 * 
	 * @param key 필드명
	 * @param val 필드값
	 * @return Object
	 */
    public Object put(Object key, Object val){
        paramMap.put(key, val);
        return val;
    }
	
	
    /**
     * RFC 값 리스트 저장 및 새 ROW 추가
     */
    public void add(){
        paramList.add(paramMap);
		paramMap = new HashMap<Object, Object>();
    }
	
	
    /**
     * List 요소 취득
     * 
     * @return iterator
     */
    public Object iterator(){
        return paramList.iterator();
    }
	
	/**
	 * List 취득
	 * 
	 * @return Object
	 */
    public Object get(){
        return paramList;
    }
	
	
    /**
     * Map 요소 취득
     * 
     * @param key
     * @return Object
     */
    public Object get(Object key){
        return paramMap.get(key);
    }
	
	
    /**
     * Map 요소 취득
     * 
     * @param key
     * @return Object
     */
    public Object keySet(){
        return paramMap.keySet();
    }
	
	
    /**
     * Map size 취득
     * 
     * @return int
     */
    public int size(){
        return paramMap.size();
    }


    /**
     * From DataSet Object to List Convert 
     * 
     * @return List
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List convertList(DataSet obj){
    	// RFC 필드 구성 List
    	List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
    	List<Map<Object,Object>> listmap = null;
    	// block 및 RFC 필드요소 Map
    	HashMap<Object, Object> inmap = new HashMap<Object, Object>();
    	HashMap<Object, Object> keymap = new HashMap<Object, Object>();

		for( Object keyset : (Set<Object>) obj.keySet() ) {
			listmap = new ArrayList<Map<Object,Object>>();
    		DataSet map = (DataSet) obj.get(keyset);
			Iterator iter = (Iterator) map.iterator();
			
			// 반환 block 내의 값 출력
			while (iter.hasNext()) {
				inmap = (HashMap<Object, Object>) iter.next();

				listmap.add(inmap);
			}

			keymap.put(keyset, listmap);
		}

		list.add(keymap);

        return list;
    }
}
