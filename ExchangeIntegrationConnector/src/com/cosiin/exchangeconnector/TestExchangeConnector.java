package com.cosiin.exchangeconnector;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class TestExchangeConnector {

	public TestExchangeConnector() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String xmlInput ="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"  " +
				 " xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">" +
				 " <soap:Body>" +
				 "   <GetItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">" +
				 "     <ItemShape>" +
				 "       <t:BaseShape>IdOnly</t:BaseShape>" +
				"        <t:AdditionalProperties>" +
				"          <t:FieldURI FieldURI=\"item:Subject\"/>" +
				"  <t:FieldURI FieldURI=\"item:Body\"/>        " +  
				"        </t:AdditionalProperties>" +
				"      </ItemShape>" +
				"      <ItemIds>" +
				"        <t:ItemId Id=\"AAASAHNtYW5hZ2VyQGtncm9sZi5ubABGAAAAAACIhWcO5E/TRIx4WrRHA4slBwAVcHM6zv4WT5Zh2/4mE8dWAAAAZVBeAAAVcHM6zv4WT5Zh2/4mE8dWAAAAZ5KXAAA=\" ChangeKey=\"DwAAABYAAAAVcHM6zv4WT5Zh2/4mE8dWAAAAZ5be\"/>" +
				"      </ItemIds>" +
				"    </GetItem>" +
				"  </soap:Body>" +
			"	</soap:Envelope>";
		 
		
			
		
	  
		Authenticator.setDefault(new ExchangeNTLMAuthenticator("cosiin","tester","tester"));
		
	 
	  
	    String responseString = "";
	    String outputString = "";
	    String wsURL = "https://cosiin.com/EWS/Exchange.asmx";
	    URL url = new URL(wsURL);
	    URLConnection connection = url.openConnection();
	    HttpURLConnection httpConn = (HttpURLConnection)connection;
	    ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    
	    byte[] buffer = new byte[xmlInput.length()];
	    buffer = xmlInput.getBytes();
	    bout.write(buffer);
	    byte[] b = bout.toByteArray();
	    
	    String SOAPAction = "http://litwinconsulting.com/webservices/GetWeather";
//	    String SOAPAction = "http://schemas.microsoft.com/exchange/services/2006/messages/CreateItem";
	    
	    // Set the appropriate HTTP parameters.
	    httpConn.setRequestProperty("Content-Length",
	    String.valueOf(b.length));
	    httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	    httpConn.setRequestProperty("SOAPAction", SOAPAction);
	    httpConn.setRequestMethod("POST");
	    httpConn.setDoOutput(true);
	    httpConn.setDoInput(true);
	    OutputStream out = httpConn.getOutputStream();
	    //Write the content of the request to the outputstream of the HTTP Connection.
	    out.write(b);
	    out.close();
	    //Ready with sending the request.
	    //Read the response.
	    InputStreamReader isr =
	    new InputStreamReader(httpConn.getInputStream());
	    BufferedReader in = new BufferedReader(isr);
	    //Write the SOAP message response to a String.
	    while ((responseString = in.readLine()) != null) {
	    outputString = outputString + responseString;
	    }
	 
	    System.out.println(outputString);



	}

}
