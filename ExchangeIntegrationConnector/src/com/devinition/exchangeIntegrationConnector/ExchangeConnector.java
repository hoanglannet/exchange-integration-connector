package com.devinition.exchangeIntegrationConnector;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;

public class ExchangeConnector {
	
	public static boolean checkSharedCalendar(String exchangeURL, String username, String password, String domain, String hisSharedMailBox) throws Exception {
		boolean ret = true;
		
		String startDate = "2013-01-01";
		String endDate = "2013-01-02";
		
		String soapRequest = buildSOAPRequestGetAppointment(hisSharedMailBox, startDate, endDate);
		System.out.println(soapRequest);
		String xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
		
		Calendar retured = new Calendar();
		boolean isError = ResponseParser.parseSOAPCodeErrorResponse(xmlSoapResponse);
		retured.setError(isError);
		
		if (isError) {
			ret = false;
			throw new Exception();
		}
		return ret;
	}
	
	public static Calendar synHisSharedAppointmentList(String exchangeURL, String username, String password, String domain, String datePattern, String hisSharedMailBox, String syncStateKey) throws Exception {
		String soapRequest = buildSOAPRequestSynAppointment(syncStateKey, hisSharedMailBox);
		String xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
		System.out.println(xmlSoapResponse);
		
		Calendar retured = new Calendar();
		retured.setSyncStateKey(ResponseParser.parseSyncStateKey(xmlSoapResponse));
		
		if (xmlSoapResponse != null) {
			List<Appointment> appointments = ResponseParser.parseSOAPResponseWithFullStatus(xmlSoapResponse);
			
			if (appointments != null && appointments.size() > 0) {
				soapRequest = buildSOAPRequestGetAppointmentBody(appointments);
				xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
				
				List<Appointment> bodyAppointments = ResponseParser.parseSOAPResponse(xmlSoapResponse);
				if (bodyAppointments != null && bodyAppointments.size() > 0) {
					for (Appointment v1 : appointments) {
						for (Appointment v2 : bodyAppointments) {
							if (v1.getId().equalsIgnoreCase(v2.getId()) && v1.getChangeKey().equalsIgnoreCase(v2.getChangeKey())) {
								v1.setBody(v2.getBody());
							}
						}
					}
				}
			}
			
			retured.setAppointments(appointments.toArray(new Appointment[appointments.size()]));
		}
		return retured;
	}
	
	public static Calendar getHisSharedAppointmentList(String exchangeURL, String username, String password, String domain, String datePattern, String hisSharedMailBox, 
			String startDate, String endDate) throws Exception {
		String soapRequest = buildSOAPRequestGetAppointment(hisSharedMailBox, startDate, endDate);
		String xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
		System.out.println(xmlSoapResponse);
		
		Calendar retured = new Calendar();
		boolean isError = ResponseParser.parseSOAPCodeErrorResponse(xmlSoapResponse);
		retured.setError(isError);
		
		if (isError) {
			throw new Exception();
		}
		
		List<Appointment> appointments = ResponseParser.parseSOAPResponse(xmlSoapResponse);
		
		if (appointments != null && appointments.size() > 0) {
			soapRequest = buildSOAPRequestGetAppointmentBody(appointments);
			xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
			
			List<Appointment> bodyAppointments = ResponseParser.parseSOAPResponse(xmlSoapResponse);
			if (bodyAppointments != null && bodyAppointments.size() > 0) {
				for (Appointment v1 : appointments) {
					for (Appointment v2 : bodyAppointments) {
						if (v1.getId().equalsIgnoreCase(v2.getId()) && v1.getChangeKey().equalsIgnoreCase(v2.getChangeKey())) {
							v1.setBody(v2.getBody());
						}
					}
				}
			}
		}
		
		retured.setAppointments(appointments.toArray(new Appointment[appointments.size()]));
		return retured;
	}
	
	public static Appointment makeHisAppointment(String exchangeURL, String username, String password, String domain, String datePatternTime, 
			String subject, String location, String startDate, String endDate, String body, String hisSharedMailBox) throws Exception {
		String soapRequest = buildSOAPRequestMakeAppointment(hisSharedMailBox, subject, location, body, startDate, endDate);
		String xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
		
		List<Appointment> appointments = ResponseParser.parseSOAPResponse(xmlSoapResponse);
		
		if (appointments != null && appointments.size() > 0) { 
			return appointments.get(0);
		}
		return null;
	}
	
	public static Appointment updateHisAppointment(String exchangeURL, String username, String password, String domain, String datePatternTime, 
			String subject, String location, String startDate, String endDate, String body, String hisSharedMailBox, String exchangeId, String exchangeChangeId) throws Exception {
		String soapRequest = buildSOAPRequestUpdateAppointment(hisSharedMailBox, subject, location, body, startDate, endDate, exchangeId, exchangeChangeId);
		String xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
		
		List<Appointment> appointments = ResponseParser.parseSOAPResponse(xmlSoapResponse);
		
		if (appointments != null && appointments.size() > 0) { 
			return appointments.get(0);
		}
		return null;
	}
	
	public static Calendar deleteHisAppointment(String exchangeURL, String username, String password, String domain, String exchangeId, String hisSharedMailBox) throws Exception {
		String soapRequest = buildSOAPRequestDeleteAppointment(hisSharedMailBox, exchangeId);
		String xmlSoapResponse = callSOAPRequest(exchangeURL, username, password, domain, soapRequest);
		
		Calendar retured = new Calendar();
		boolean isError = ResponseParser.parseSOAPCodeErrorResponse(xmlSoapResponse);
		retured.setError(isError);
		
		if (isError) {
			throw new Exception();
		}
		
		return retured;
	}
	
	private static String buildSOAPRequestGetAppointment(String hisSharedMailBox, String startDate, String endDate) {
		String ret = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
					               " xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">" +
                      "<soap:Header>"+
					   		"<t:RequestServerVersion Version=\"Exchange2010_SP1\" />" +
					  " </soap:Header><soap:Body>" +
					    "<FindItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\"" +
					               " xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"" +
					              " Traversal=\"Shallow\">" +
					      "<ItemShape>" +
					        "<t:BaseShape>Default</t:BaseShape>" +
					        "<t:BodyType>Text</t:BodyType>" +
					        "<t:AdditionalProperties>" +
					          "<t:FieldURI FieldURI=\"calendar:Start\"/>" +
					          "<t:FieldURI FieldURI=\"calendar:End\"/>" +
					          "<t:FieldURI FieldURI=\"calendar:Location\"/>" +
					          "<t:FieldURI FieldURI=\"calendar:IsAllDayEvent\"/>" +
					          "<t:FieldURI FieldURI=\"item:Subject\"/>" +
					          "<t:FieldURI FieldURI=\"item:DateTimeCreated\"/>" +
					        "</t:AdditionalProperties>" +
					      "</ItemShape>" +
					      "<CalendarView MaxEntriesReturned=\"10000\" StartDate=\""+startDate+"T00:00:00-00:00\" EndDate=\""+endDate+"T00:00:00-00:00\"/>" +
					      "<ParentFolderIds>" +
					        "<t:DistinguishedFolderId Id=\"calendar\">" +
					            "<t:Mailbox>" +
					                "<t:EmailAddress>"+hisSharedMailBox+"</t:EmailAddress>" +
					            "</t:Mailbox>" +
					        "</t:DistinguishedFolderId>" +
					      "</ParentFolderIds>" +
					    "</FindItem>" +
					  "</soap:Body>" +
					"</soap:Envelope>";
		return ret;
	}
	
	private static String buildSOAPRequestSynAppointment(String syncStateKey, String hisSharedMailBox) {
		String s = "";
		if (syncStateKey != null && syncStateKey.trim() != "") {
			s = "<SyncState>"+syncStateKey+"</SyncState>";
		}
		
		String ret = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
						"  xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"> " +
						"<soap:Header>"+
				   		"<t:RequestServerVersion Version=\"Exchange2010_SP1\" />" +
				  " </soap:Header><soap:Body>" +
						 "   <SyncFolderItems xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\"> " +
						  "    <ItemShape> " +
						   "     <t:BaseShape>Default</t:BaseShape> " +
						    "    <t:BodyType>Text</t:BodyType> " +
							    "<t:AdditionalProperties>" +
						          "<t:FieldURI FieldURI=\"calendar:Start\"/>" +
						          "<t:FieldURI FieldURI=\"calendar:End\"/>" +
						          "<t:FieldURI FieldURI=\"calendar:Location\"/>" +
						          "<t:FieldURI FieldURI=\"calendar:IsAllDayEvent\"/>" +
						          "<t:FieldURI FieldURI=\"item:Subject\"/>" +
						          "<t:FieldURI FieldURI=\"item:DateTimeCreated\"/>" +
						        "</t:AdditionalProperties>" +
						     " </ItemShape> " +
						    "  <SyncFolderId> " +
						     "   <t:DistinguishedFolderId Id=\"calendar\"> " +
								"	<t:Mailbox> " +
								"		<t:EmailAddress>"+hisSharedMailBox+"</t:EmailAddress> " +
								"	</t:Mailbox> " +
							"	</t:DistinguishedFolderId> " +
						    "  </SyncFolderId>      " +
							s + 
						    "  <MaxChangesReturned>512</MaxChangesReturned> " +
						   " </SyncFolderItems> " +
						 " </soap:Body> " +
						" </soap:Envelope>";
		return ret;
	}
	
	private static String buildSOAPRequestGetAppointmentBody(List<Appointment> apps) {
		String ret = "";
		
		if (apps != null && apps.size() > 0){ 
			ret = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "+
						  " xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"> "+
						  "<soap:Header><t:RequestServerVersion Version=\"Exchange2010_SP1\" /></soap:Header>" +
						  " <soap:Body> "+
						  "   <GetItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\"> "+
						  "  <ItemShape> "+
						  "     <t:BaseShape>IdOnly</t:BaseShape> "+
						  "     <t:BodyType>Text</t:BodyType>" +
						  "      <t:AdditionalProperties> "+
						  "         <t:FieldURI FieldURI=\"item:Body\"/> "+
						  "       </t:AdditionalProperties> "+
						  "     </ItemShape> "+
						  "     <ItemIds> ";
			
			for (Appointment appointment : apps) {
				ret +=    "       <t:ItemId Id=\""+appointment.getId()+"\" ChangeKey=\""+appointment.getChangeKey()+"\"/> ";
			}
			
			 ret +=		  "     </ItemIds> "+
						  "   </GetItem> "+
						  "  </soap:Body> "+
						  " </soap:Envelope>";
		}
		
		return ret;
	}
	
	private static String buildSOAPRequestDeleteAppointment(String hisSharedMailBox, String exchangeId) {
		String ret = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
					  " xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"> " +
					  "<soap:Header><t:RequestServerVersion Version=\"Exchange2010_SP1\" /></soap:Header>" +
					  " <soap:Body> " +
					  "   <DeleteItem DeleteType=\"HardDelete\" SendMeetingCancellations=\"SendToNone\" xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\"> " +
					  "     <ItemIds> " +
					  "       <t:ItemId Id=\"" + exchangeId + "\"/> " +
					  "     </ItemIds> " +
					  "   </DeleteItem> " +
					  " </soap:Body> " +
					  " </soap:Envelope>";
		return ret;
	}
	
	private static String buildSOAPRequestMakeAppointment(String hisSharedMailBox, String subject, String location, String body, String startDate, String endDate) throws ParseException {
		startDate = Utils.formatDateTimeFromGTMToDateUTC(startDate, null);
		endDate = Utils.formatDateTimeFromGTMToDateUTC(endDate, null);
		
		String ret = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
					               "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
					               "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
					               "xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"> " +
					  "<soap:Header><t:RequestServerVersion Version=\"Exchange2010_SP1\" /></soap:Header>" +
					  "<soap:Body> " +
					    "<CreateItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" " +
					                "xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"  " +
					                "SendMeetingInvitations=\"SendToAllAndSaveCopy\" > " +
					     " <SavedItemFolderId> " +
					        "<t:DistinguishedFolderId Id=\"calendar\"> " +
					           " <t:Mailbox> " +
					               " <t:EmailAddress>"+hisSharedMailBox+"</t:EmailAddress> " +
					            "</t:Mailbox> " +
					        "</t:DistinguishedFolderId> " +
					     " </SavedItemFolderId> " +
					      "<Items> " +
					        "<t:CalendarItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\"> " +
					         " <Subject>"+subject+"</Subject> " +
					         " <Body BodyType=\"Text\">"+body+"</Body> " +
					         " <Start>"+startDate+"</Start> " +
					         " <End>"+endDate+"</End> " +
					         " <Location>"+location+"</Location> " +
					       " </t:CalendarItem> " +
					     " </Items> " +
					   " </CreateItem> " +
					  "</soap:Body> " +
					"</soap:Envelope>";
		return ret;
	}
	
	private static String buildSOAPRequestUpdateAppointment(String hisSharedMailBox, String subject, String location, String body, String startDate, String endDate, String exchangeId, String exchangeChangeId) throws ParseException {
		startDate = Utils.formatDateTimeFromGTMToDateUTC(startDate, null);
		endDate = Utils.formatDateTimeFromGTMToDateUTC(endDate, null);
		
		String ret = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:m=\"http://schemas.microsoft.com/exchange/services/2006/messages\" " +
					               " xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> " +
					  "<soap:Header><t:RequestServerVersion Version=\"Exchange2010_SP1\" /></soap:Header>" +
					  " <soap:Body> " +
					  "    <m:UpdateItem ConflictResolution=\"AlwaysOverwrite\" SendMeetingInvitationsOrCancellations=\"SendToNone\"> " +
					   "      <m:ItemChanges> " +
					    "        <t:ItemChange> " +
					     "          <t:ItemId Id=\""+exchangeId+"\" ChangeKey=\""+exchangeChangeId+"\" /> " +
					      "            <t:Updates> " +
					       "              <t:SetItemField> " +
					        "                <t:FieldURI FieldURI=\"item:Subject\" /> " +
					         "               <t:CalendarItem> " +
					          "                 <t:Subject>"+subject+"</t:Subject> " +
					           "             </t:CalendarItem> " +
					            "         </t:SetItemField> " +
					             "        <t:SetItemField> " +
					              "         <t:FieldURI FieldURI=\"calendar:Location\" /> " +
					               "        <t:CalendarItem> " +
					               "           <t:Location>"+location+"</t:Location> " +
					                "       </t:CalendarItem> " +
					                 "    </t:SetItemField> " +
								 "        <t:SetItemField> " +
								 "         <t:FieldURI FieldURI=\"item:Body\" /> " +
								  "        <t:CalendarItem> " +
								  "           <t:Body BodyType=\"Text\">"+body+"</t:Body> " +
								   "       </t:CalendarItem> " +
								    "    </t:SetItemField> " +
					                  "   <t:SetItemField> " +
					                   "    <t:FieldURI FieldURI=\"calendar:Start\" /> " +
					                 "      <t:CalendarItem> " +
					                "         <t:Start>"+startDate+"</t:Start> " +
					                "       </t:CalendarItem> " +
					                "     </t:SetItemField> " +
					               "      <t:SetItemField> " +
					               "        <t:FieldURI FieldURI=\"calendar:End\" /> " +
					               "        <t:CalendarItem> " +
					               "          <t:End>"+endDate+"</t:End> " +
					               "        </t:CalendarItem> " +
					               "      </t:SetItemField> " +
					              " </t:Updates> " +
					           " </t:ItemChange> " +
					        " </m:ItemChanges> " +
					     " </m:UpdateItem> " +
					 "  </soap:Body> " +
					"</soap:Envelope>";
		return ret;
	}
	
	private static String callSOAPRequest(String exchangeURL, String username, String password, String domain, String soap) throws Exception {
		Authenticator.setDefault(new ExchangeNTLMAuthenticator(domain, username, password));
		  
	    String responseString = "";
	    String outputString = "";
	    
	    URL url = new URL(exchangeURL);
	    URLConnection connection = url.openConnection();
	    HttpURLConnection httpConn = (HttpURLConnection)connection;
	    ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    
	    byte[] buffer = new byte[soap.length()];
	    buffer = soap.getBytes();
	    bout.write(buffer);
	    byte[] b = bout.toByteArray();
	    
	    // Set the appropriate HTTP parameters.
	    httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
	    httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	    httpConn.setRequestMethod("POST");
	    httpConn.setDoOutput(true);
	    httpConn.setDoInput(true);
	    OutputStream out = httpConn.getOutputStream();
	    
	    //Write the content of the request to the outputstream of the HTTP Connection.
	    out.write(b);
	    out.close();
	    
	    //Ready with sending the request.
	    //Read the response.
	    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
	    BufferedReader in = new BufferedReader(isr);
	    
	    //Write the SOAP message response to a String.
	    while ((responseString = in.readLine()) != null) {
	    	outputString = outputString + responseString;
	    }
	    
	    return outputString;
	}
}
