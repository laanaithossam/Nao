package org.xmlrpc.android;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

// bilock@gmail.com 2011/03/11 : declare this class as visible 
public class XMLRPCCommon {

	protected XmlSerializer serializer;
	protected IXMLRPCSerializer iXMLRPCSerializer;
	
	public XMLRPCCommon() {
		serializer = Xml.newSerializer();
		iXMLRPCSerializer = new XMLRPCSerializer();
	}

	/**
	 * Sets custom IXMLRPCSerializer serializer (in case when server doesn't support
	 * standard XMLRPC protocol)
	 * 
	 * @param serializer custom serializer
	 */
	public void setSerializer(IXMLRPCSerializer serializer) {
		iXMLRPCSerializer = serializer;
	}
			
	protected void serializeParams(Object[] params) throws IllegalArgumentException, IllegalStateException, IOException {
		if (params != null && params.length != 0)
		{
			// set method params
			serializer.startTag(null, Tag.PARAMS);
			for (int i=0; i<params.length; i++) {
				serializer.startTag(null, Tag.PARAM).startTag(null, IXMLRPCSerializer.TAG_VALUE);
				iXMLRPCSerializer.serialize(serializer, params[i]);
				serializer.endTag(null, IXMLRPCSerializer.TAG_VALUE).endTag(null, Tag.PARAM);
			}
			serializer.endTag(null, Tag.PARAMS);
		}
	}

}
