package com.common;

import java.text.*;
import java.util.*;
import com.common.DataSet;

public class Common {

	public Common(){

	}


	public String dateFormat(String strDate) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

		Date date = dateFormat.parse(strDate);
		String afterDate = "";

		if("00:00:00".equals(sdf.format(date).split(" ")[1])){
			afterDate = sdf.format(date).split(" ")[0];
		}else{
		    if(!"1970-01-01".equals(sdf.format(date).split(" ")[0])){
		        afterDate = sdf.format(date).split(" ")[0];
		    }else{
		        afterDate = sdf.format(date).split(" ")[1];
		    }
		}

		return afterDate;
	}
}
