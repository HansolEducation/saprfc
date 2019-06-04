package com.sap.rfc;

import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import com.sap.rfc.Globals;

public class MyDestinationDataProvider implements DestinationDataProvider {
	private DestinationDataEventListener eventListener;
	private Properties ABAP_AS_properties;

	@Override
	public Properties getDestinationProperties(String arg0) {
		return ABAP_AS_properties;
	}

	public void setDestinationDataEventListener(
			DestinationDataEventListener eventListener) {
		this.eventListener = eventListener;
	}

	@Override
	public boolean supportsEvents() {
		return true;
	}

	public void changePropertiesForABAP_AS(Properties properties) {
		Globals globals = new Globals();
		
		if (properties == null) {
			eventListener.deleted(globals.SAP_SERVER);
			ABAP_AS_properties = null;
		} else {
			if (ABAP_AS_properties != null
					&& !ABAP_AS_properties.equals(properties))
				eventListener.updated(globals.SAP_SERVER);
			ABAP_AS_properties = properties;
		}
	}
}
