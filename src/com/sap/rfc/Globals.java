package com.sap.rfc;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *  Class Name : Globals.java
 *  Description : 시스템 구동 시 프로퍼티를 통해 사용될 전역변수를 정의한다.
 *  Modification Information
 */
public class Globals {
	// 프로퍼티 파일 위치
	String propFile = "D:\\workspace_mars\\doroTEST\\resource\\globals.properties";
	//String propFile = "/voc2016/hansol_batch/resource/globals.properties";
	// SAP ID
	String SAP_ID = "";
	// SAP Password
	String SAP_PW = "";
	// SAP IP
	String SAP_IP = "";
	// SAP System Number
	String SAP_SNUMBER = "";
	// SAP System Server
	String SAP_SERVER = "";
	// SAP System Group
	String SAP_GROUP = "";

	public Globals() {
		try{
			// 프로퍼티 객체 생성
			Properties props = new Properties();
			
			// 프로퍼티 파일 스트림에 담기
			FileInputStream fis = new FileInputStream(propFile);
		
			// 프로퍼티 파일 로딩
			props.load(new java.io.BufferedInputStream(fis));

			SAP_ID = props.getProperty("SapJco.sapID");
			SAP_PW = props.getProperty("SapJco.sapPW");
			SAP_IP = props.getProperty("SapJco.sapIP");
			SAP_SNUMBER = props.getProperty("SapJco.sapSNumber");
			SAP_SERVER = props.getProperty("SapJco.sapServer");
			SAP_GROUP = props.getProperty("SapJco.sapGroup");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
