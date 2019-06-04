package com.sap.rfc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.common.Common;
import com.common.DataSet;

import com.sap.conn.jco.ConversionException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class SapTransRFC {

	/**
	 * RFC 연결 신버젼
	 * 
	 * @param funcName : SAP 펑션명
	 * @param blockNames : SAP 펑션내 block 정의
	 * @param param : SAP 펑션 IMPORT 필드요소
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings({"unchecked", "rawtypes"})
	public DataSet transRFCNew(String funcName, String[][] blockNames, DataSet param, TableAdapterReader tableAdapter) throws Exception {
    	
		// SAP System
		Connection connect 	= null;
		Globals globals 		= new Globals();
		SapSystem system 	= new SapSystem();
		JCoTable timport 		= null;
		JCoStructure simport = null;
		
		// 반환 Structure 리스트
		String jcoExportJson = "";
		// 반환 Table 리스트
		String jcoTableJson = "";
		// 반환 Table 필드 리스트
		String jcoFieldJson = "";

		String key = "";
		String value = "";

		// Sap 정보
		system.setClient("100");
		system.setHost(globals.SAP_IP);
		system.setLanguage("KO");
		system.setSystemNumber(globals.SAP_SNUMBER);
		system.setUser(globals.SAP_ID);
		system.setPassword(globals.SAP_PW);

		connect = new Connection(system);

		//System.out.println("funcName ===> " + funcName + " blockNames[0].length "+blockNames[0].length) ;
		
		// 펑션 셋팅
		JCoFunction function = connect.getFunction(funcName);
		if(blockNames != null) {
			// 펑션의 block 정의를 토대로 RFC 필드 및 값 셋팅
			for(int j=0; j<blockNames[0].length; j++){ 
				// 해당 block 의 RFC 필드 리스트 값 취득
	    		DataSet map = (DataSet) param.get(blockNames[0][j]);
	
	    		if(map != null) {
					Iterator iter = (Iterator) map.iterator();
					
					// Table 요소는 Table 변수에 정의
					if("T".equals(blockNames[1][j])){
						timport = function.getTableParameterList().getTable(blockNames[0][j]);
					// Structure 요소는 Structure 변수에 정의 
					}else if("S".equals(blockNames[1][j])){
						simport = function.getImportParameterList().getStructure(blockNames[0][j]);
					}
		
					// 리스트를 쪼개서... Map을 쪼개서... 셋팅
					while (iter.hasNext()) {
						Map<Object, Object> m = (HashMap<Object, Object>) iter.next();
		
						Set<Map.Entry<Object, Object>> entrySet2 = m.entrySet();
						Iterator<Map.Entry<Object, Object>> it2 = entrySet2.iterator();
		
						// 단순 Input
						if("noname".equals(blockNames[0][j])){
							while (it2.hasNext()) {
								Map.Entry<Object, Object> entry2 = it2.next();
			
								key = entry2.getKey().toString();
								value = entry2.getValue().toString();
			
								System.out.format("map2 ===> %s : %s \r\n", key, value);
								function.getImportParameterList().setValue(key, value);
							}
						// Table
						}else if("T".equals(blockNames[1][j])){
							timport.appendRow();
							
							while (it2.hasNext()) {
								Map.Entry<Object, Object> entry2 = it2.next();
			
								key = entry2.getKey().toString();
								value = entry2.getValue().toString();
			
								System.out.format("map2 ===> %s : %s \r\n", key, value);
								timport.setValue(key, value);
							}
						// Structure
						}else if("S".equals(blockNames[1][j])){
							while (it2.hasNext()) {
								Map.Entry<Object, Object> entry2 = it2.next();
			
								key = entry2.getKey().toString();
								value = entry2.getValue().toString();
			
								System.out.format("map2 ===> %s : %s \r\n", key, value);
								simport.setValue(key, value);
							}
						}
					}
				}
			}
		}

		// End 가 뜰 때까지 접속 끝난 거 아님.
		System.out.println("connect start!@!!!!");
		connect.execute(function);
		System.out.println("connect end!@!!!!");
		//System.out.println(function.toXML());
		
		JCoFieldIterator jcoit_st = null;
		JCoFieldIterator jcoit_tb = null;
		
		String returns =	tableAdapter.getReturns() == null ? "E_RETURN"  : tableAdapter.getReturns();
		//System.out.println(returns+"<<<<<<<<<<<<<< tableAdapter MODE");
		if(returns.equals("E_RETURN")){
			JCoStructure structure2 = function.getExportParameterList().getStructure("E_RETURN");
			tableAdapter.setStructure(structure2);
			jcoit_st = function.getExportParameterList() != null ? function.getExportParameterList().getFieldIterator() : null;
			jcoit_tb = function.getTableParameterList() != null ? function.getTableParameterList().getFieldIterator() : null;
			
			//System.out.println("TYPE=>"+ structure2.getString("TYPE")+" | message =>" + structure2.getString("MESSAGE"));
		}else{
			//저장 후 값 리턴용.
			JCoTable table = function.getTableParameterList().getTable(returns);
			tableAdapter.setTable(table);
			jcoit_tb = table.getFieldIterator();
		}

		// Structure 리스트 GET
		if(jcoit_st != null){
			while(jcoit_st.hasNextField()){
				JCoField sapExportName = jcoit_st.nextField();
				jcoExportJson = "".equals(jcoExportJson) ? sapExportName.getName() : jcoExportJson + "," + sapExportName.getName();
			}
		}

		// Table 리스트 GET
		if(jcoit_tb != null){
			while(jcoit_tb.hasNextField()){
				JCoField sapExportName = jcoit_tb.nextField();
				jcoTableJson = "".equals(jcoTableJson) ? sapExportName.getName() : jcoTableJson + "," + sapExportName.getName();
			}
		}

		// Structure, Table 리스트
		String[] jcoExports = jcoExportJson.split(",");
		String[] jcoTables = jcoTableJson.split(",");

		DataSet list = new DataSet();
		// 반환 Table
		DataSet t_export;
		// 반환 Structure
		DataSet s_export;
		DataSet e_export = new DataSet();
		
		// 총 데이터, Structure 및 Table 데이터, Structure 의 기타 데이터
		String jcoField = "";

		// Table 의 반환 데이터가 있을 때
		if(returns.equals("E_RETURN")){
			if(jcoTables.length > 0 && !"".equals(jcoTables[0])){
				for(int i=0; i<jcoTables.length; i++){
					t_export = new DataSet();
					JCoTable table = function.getTableParameterList().getTable(jcoTables[i]);
					tableAdapter.setTable(table);
					
					// 메세지
					/*
					t_export.put("MESSAGE", structure2.getString("MESSAGE"));
					t_export.put("TYPE", structure2.getString("TYPE"));
					
					// 해당 Table 의 반환 갯수 정의
					t_export.put("TOT_CNT", Integer.toString(tableAdapter.size()));
					t_export.add();
					 */
					
					JCoFieldIterator jcoit3 = table.getFieldIterator();
	
					jcoFieldJson = "";
					// 셋팅한 Table 의 필드 리스트를 취득
					while(jcoit3.hasNextField()){
						JCoField sapFieldName = jcoit3.nextField();
	
						jcoFieldJson = "".equals(jcoFieldJson) ? sapFieldName.getName() : jcoFieldJson + "," + sapFieldName.getName();
					}
	
					String[] jcoTableFields = jcoFieldJson.split(",");
					Common cmd = new Common();
	
					// Table 의 Row 만큼 돌리고 그 Row 에서 전에 셋팅해둔 필드 값에 따라 데이터 취득
					for (int row=0; row<tableAdapter.size(); row++) {
						for (int j=0; j<jcoTableFields.length; j++) {
							jcoField = jcoTableFields[j].trim();
	
							if (jcoField != null && !"".equals(jcoField)) {
								// 날짜는 따로 변환 작업을 안해주면 영어용 값이 반환됨
								if(tableAdapter.get(jcoField).indexOf("KST") != -1){
									t_export.put(jcoField, cmd.dateFormat(tableAdapter.get(jcoField)));
								}else if("REGNO".equals(jcoField) || "STCD".equals(jcoField) || "STCD1".equals(jcoField) || "TREGNO".equals(jcoField)){
									t_export.put(jcoField, "".equals(tableAdapter.get(jcoField)) ? " " :  tableAdapter.get(jcoField).substring(0,7));
								}else {
									t_export.put(jcoField, "".equals(tableAdapter.get(jcoField)) ? " " : tableAdapter.get(jcoField));
								}
							}
						}
						t_export.add();
						tableAdapter.next();
					}
	
					list.put(jcoTables[i], t_export);
				}
			}
		}else{
			
			Common cmd = new Common();
			t_export = new DataSet();
			
			// Table 의 Row 만큼 돌리고 그 Row 에서 전에 셋팅해둔 필드 값에 따라 데이터 취득
			for (int row=0; row<tableAdapter.size(); row++) {
				for (int j=0; j<jcoTables.length; j++) {
					jcoField = jcoTables[j].trim();
					if (jcoField != null && !"".equals(jcoField)) {
						// 날짜는 따로 변환 작업을 안해주면 영어용 값이 반환됨
						if(tableAdapter.get(jcoField).indexOf("KST") != -1){
							t_export.put(jcoField, cmd.dateFormat(tableAdapter.get(jcoField)));
						}else if("REGNO".equals(jcoField) || "STCD".equals(jcoField) || "STCD1".equals(jcoField) || "TREGNO".equals(jcoField)){
							t_export.put(jcoField, "".equals(tableAdapter.get(jcoField)) ? " " :  tableAdapter.get(jcoField).substring(0,7));
						}else {
							t_export.put(jcoField, "".equals(tableAdapter.get(jcoField)) ? " " : tableAdapter.get(jcoField));
						}
					}
				}
				t_export.add();
				tableAdapter.next();
			}
			list.put(returns, t_export);
		}


		// Structure 의 반환 데이터가 있을 때
		if(jcoExports.length > 0 && !"".equals(jcoExports[0])){
			for(int i=0; i<jcoExports.length; i++){
				s_export = new DataSet();

				try{
					JCoStructure structure = function.getExportParameterList().getStructure(jcoExports[i]);

					tableAdapter.setStructure(structure);
					JCoFieldIterator jcoit2 = structure.getFieldIterator();

					jcoFieldJson = "";
					// 셋팅한 Structure 의 필드 리스트를 취득
					while(jcoit2.hasNextField()){
						JCoField sapFieldName = jcoit2.nextField();

						jcoFieldJson = "".equals(jcoFieldJson) ? sapFieldName.getName() : jcoFieldJson + "," + sapFieldName.getName();
					}

					String[] jcoStructsFields = jcoFieldJson.split(",");

					// Structure 는 1줄 뿐이므로 전에 셋팅해둔 필드 값에 따라 데이터 취득
					for (int j=0; j<jcoStructsFields.length; j++) {
						jcoField = jcoStructsFields[j].trim();
						s_export.put(jcoField, structure.getString(jcoStructsFields[j]));
					}

					s_export.add();
				}catch(ConversionException ce){
					e_export.put(jcoExports[i], function.getExportParameterList().getString(jcoExports[i]));
					continue;
				}
				
				list.put(jcoExports[i], s_export);
			}

			// Struncture 인데 값을 여러개가 아닌 1개만 넘기는 경우가 있음
			// 그건 ETC_VALUE 라는 JSON 으로 따로 기록해서 넘김
			if(e_export != null && e_export.size() > 0){
				e_export.add();

				list.put("ETC_VALUE", e_export);
			}
		}
		
		return list;
	}
    @SuppressWarnings({"unchecked", "rawtypes"})
	public DataSet transRFCNews(String funcName, String[][] blockNames, DataSet param, TableAdapterReader tableAdapter) throws Exception {
    	
		// SAP System
		Connection connect 	= null;
		Globals globals 		= new Globals();
		SapSystem system 	= new SapSystem();
		JCoTable timport 		= null;
		JCoStructure simport = null;
		
		// 반환 Structure 리스트
		String jcoExportJson = "";
		// 반환 Table 리스트
		String jcoTableJson = "";
		// 반환 Table 필드 리스트
		String jcoFieldJson = "";

		String key = "";
		String value = "";

		// Sap 정보
		system.setClient("100");
		system.setHost(globals.SAP_IP);
		system.setLanguage("KO");
		system.setSystemNumber(globals.SAP_SNUMBER);
		system.setUser(globals.SAP_ID);
		system.setPassword(globals.SAP_PW);

		connect = new Connection(system);

		//System.out.println("funcName ===> " + funcName + " blockNames[0].length "+blockNames[0].length) ;
		
		// 펑션 셋팅
		JCoFunction function = connect.getFunction(funcName);
		if(blockNames != null) {
			// 펑션의 block 정의를 토대로 RFC 필드 및 값 셋팅
			for(int j=0; j<blockNames[0].length; j++){ 
				// 해당 block 의 RFC 필드 리스트 값 취득
	    		DataSet map = (DataSet) param.get(blockNames[0][j]);
	
	    		if(map != null) {
					Iterator iter = (Iterator) map.iterator();
					
					// Table 요소는 Table 변수에 정의
					if("T".equals(blockNames[1][j])){
						timport = function.getTableParameterList().getTable(blockNames[0][j]);
					// Structure 요소는 Structure 변수에 정의 
					}else if("S".equals(blockNames[1][j])){
						simport = function.getImportParameterList().getStructure(blockNames[0][j]);
					}
		
					// 리스트를 쪼개서... Map을 쪼개서... 셋팅
					while (iter.hasNext()) {
						Map<Object, Object> m = (HashMap<Object, Object>) iter.next();
		
						Set<Map.Entry<Object, Object>> entrySet2 = m.entrySet();
						Iterator<Map.Entry<Object, Object>> it2 = entrySet2.iterator();
		
						// 단순 Input
						if("noname".equals(blockNames[0][j])){
							while (it2.hasNext()) {
								Map.Entry<Object, Object> entry2 = it2.next();
			
								key = entry2.getKey().toString();
								value = entry2.getValue().toString();
			
								System.out.format("map2 ===> %s : %s \r\n", key, value);
								function.getImportParameterList().setValue(key, value);
							}
						// Table
						}else if("T".equals(blockNames[1][j])){
							timport.appendRow();
							
							while (it2.hasNext()) {
								Map.Entry<Object, Object> entry2 = it2.next();
			
								key = entry2.getKey().toString();
								value = entry2.getValue().toString();
			
								System.out.format("map2 ===> %s : %s \r\n", key, value);
								timport.setValue(key, value);
							}
						// Structure
						}else if("S".equals(blockNames[1][j])){
							while (it2.hasNext()) {
								Map.Entry<Object, Object> entry2 = it2.next();
			
								key = entry2.getKey().toString();
								value = entry2.getValue().toString();
			
								System.out.format("map2 ===> %s : %s \r\n", key, value);
								simport.setValue(key, value);
							}
						}
					}
				}
			}
		}

		// End 가 뜰 때까지 접속 끝난 거 아님.
		System.out.println("connect start!@!!!!");
		connect.execute(function);
		System.out.println("connect end!@!!!!");
		//System.out.println(function.toXML());
		
		JCoFieldIterator jcoit_st = null;
		JCoFieldIterator jcoit_tb = null;
		
		String returns = "";
		if(tableAdapter.getReturns() != null && !"".equals(tableAdapter.getReturns())){
			returns = tableAdapter.getReturns();
			JCoStructure structure2 = function.getExportParameterList().getStructure(returns);
			tableAdapter.setStructure(structure2);
			
			//System.out.println("TYPE=>"+ structure2.getString("TYPE")+" | message =>" + structure2.getString("MESSAGE"));
		}

		jcoit_st = function.getExportParameterList() != null ? function.getExportParameterList().getFieldIterator() : null;
		jcoit_tb = function.getTableParameterList() != null ? function.getTableParameterList().getFieldIterator() : null;

		// Structure 리스트 GET
		if(jcoit_st != null){
			while(jcoit_st.hasNextField()){
				JCoField sapExportName = jcoit_st.nextField();
				jcoExportJson = "".equals(jcoExportJson) ? sapExportName.getName() : jcoExportJson + "," + sapExportName.getName();
			}
		}

		// Table 리스트 GET
		if(jcoit_tb != null){
			while(jcoit_tb.hasNextField()){
				JCoField sapExportName = jcoit_tb.nextField();
				jcoTableJson = "".equals(jcoTableJson) ? sapExportName.getName() : jcoTableJson + "," + sapExportName.getName();
			}
		}

		// Structure 리스트 GET
		if(jcoit_st != null){
			while(jcoit_st.hasNextField()){
				JCoField sapExportName = jcoit_st.nextField();
				jcoExportJson = "".equals(jcoExportJson) ? sapExportName.getName() : jcoExportJson + "," + sapExportName.getName();
			}
		}

		// Table 리스트 GET
		if(jcoit_tb != null){
			while(jcoit_tb.hasNextField()){
				JCoField sapExportName = jcoit_tb.nextField();
				jcoTableJson = "".equals(jcoTableJson) ? sapExportName.getName() : jcoTableJson + "," + sapExportName.getName();
			}
		}

		// Structure, Table 리스트
		String[] jcoExports = jcoExportJson.split(",");
		String[] jcoTables = jcoTableJson.split(",");

		DataSet list = new DataSet();
		// 반환 Table
		DataSet t_export;
		// 반환 Structure
		DataSet s_export;
		DataSet e_export = new DataSet();
		
		// 총 데이터, Structure 및 Table 데이터, Structure 의 기타 데이터
		String jcoField = "";

		// Table 의 반환 데이터가 있을 때
		if(jcoTables.length > 0 && !"".equals(jcoTables[0])){
			for(int i=0; i<jcoTables.length; i++){
				t_export = new DataSet();
				JCoTable table = function.getTableParameterList().getTable(jcoTables[i]);
				tableAdapter.setTable(table);
				
				// 메세지
				/*
				t_export.put("MESSAGE", structure2.getString("MESSAGE"));
				t_export.put("TYPE", structure2.getString("TYPE"));
				
				// 해당 Table 의 반환 갯수 정의
				t_export.put("TOT_CNT", Integer.toString(tableAdapter.size()));
				t_export.add();
				 */
				
				JCoFieldIterator jcoit3 = table.getFieldIterator();

				jcoFieldJson = "";
				// 셋팅한 Table 의 필드 리스트를 취득
				while(jcoit3.hasNextField()){
					JCoField sapFieldName = jcoit3.nextField();

					jcoFieldJson = "".equals(jcoFieldJson) ? sapFieldName.getName() : jcoFieldJson + "," + sapFieldName.getName();
				}

				String[] jcoTableFields = jcoFieldJson.split(",");
				Common cmd = new Common();

				// Table 의 Row 만큼 돌리고 그 Row 에서 전에 셋팅해둔 필드 값에 따라 데이터 취득
				for (int row=0; row<tableAdapter.size(); row++) {
					for (int j=0; j<jcoTableFields.length; j++) {
						jcoField = jcoTableFields[j].trim();

						if (jcoField != null && !"".equals(jcoField)) {
							// 날짜는 따로 변환 작업을 안해주면 영어용 값이 반환됨
							if(tableAdapter.get(jcoField).indexOf("KST") != -1){
								t_export.put(jcoField, cmd.dateFormat(tableAdapter.get(jcoField)));
							}else if("REGNO".equals(jcoField) || "STCD".equals(jcoField) || "STCD1".equals(jcoField) || "TREGNO".equals(jcoField)){
								t_export.put(jcoField, "".equals(tableAdapter.get(jcoField)) ? " " :  tableAdapter.get(jcoField).substring(0,7));
							}else {
								t_export.put(jcoField, "".equals(tableAdapter.get(jcoField)) ? " " : tableAdapter.get(jcoField));
							}
						}
					}
					t_export.add();
					tableAdapter.next();
				}

				list.put(jcoTables[i], t_export);
			}
		}

		// Structure 의 반환 데이터가 있을 때
		if(jcoExports.length > 0 && !"".equals(jcoExports[0])){
			for(int i=0; i<jcoExports.length; i++){
				s_export = new DataSet();

				try{
					JCoStructure structure = function.getExportParameterList().getStructure(jcoExports[i]);

					tableAdapter.setStructure(structure);
					JCoFieldIterator jcoit2 = structure.getFieldIterator();

					jcoFieldJson = "";
					// 셋팅한 Structure 의 필드 리스트를 취득
					while(jcoit2.hasNextField()){
						JCoField sapFieldName = jcoit2.nextField();

						jcoFieldJson = "".equals(jcoFieldJson) ? sapFieldName.getName() : jcoFieldJson + "," + sapFieldName.getName();
					}

					String[] jcoStructsFields = jcoFieldJson.split(",");

					// Structure 는 1줄 뿐이므로 전에 셋팅해둔 필드 값에 따라 데이터 취득
					for (int j=0; j<jcoStructsFields.length; j++) {
						jcoField = jcoStructsFields[j].trim();
						s_export.put(jcoField, structure.getString(jcoStructsFields[j]));
					}

					s_export.add();
				}catch(ConversionException ce){
					e_export.put(jcoExports[i], function.getExportParameterList().getString(jcoExports[i]));
					continue;
				}
				
				list.put(jcoExports[i], s_export);
			}

			// Struncture 인데 값을 여러개가 아닌 1개만 넘기는 경우가 있음
			// 그건 ETC_VALUE 라는 JSON 으로 따로 기록해서 넘김
			if(e_export != null && e_export.size() > 0){
				e_export.add();

				list.put("ETC_VALUE", e_export);
			}
		}
		
		return list;
	}
}
