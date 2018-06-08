import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//Client class relays data accepted by the Server Class to ThingWorx
public class Client 
{
	 //properties used to connect to ThingWorx
	  private static String url = "https://34.224.244.58:443/Thingworx/Things/";
	  private static String thingName = "Greenhouse1Thing_gtsstudent";
	  private static String appKey = "29bd1f2b-65ad-43d8-8101-ce6018d1583b";
	  private static String dataPayload;
	  private static String httpUrlString;
	  private static URL ourUrl;
	  private static HttpURLConnection urlConnection;
	  private static OutputStreamWriter output;
	  private static JSONObject obj;
	  private static int statusCode;
	  //get data from the server class and copy it to these variables
	  private static float temperature;
	  private static int soilMoisture;
	  private static int light;
	  private static float humidity;
	  
	  static
	  {
		  	//allows us to bypass SSL security and connect to thingworx
		  	//(overrides default host verifier),
		    //checks if our host name matches with the server authentication scheme
		  	HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
		  		public boolean verify(String hostname, SSLSession session) 
		  		{
		  			return true;
		  		}

	     });
	   }
	
	//Send() takes our url, appkey, and thingname
	//It also places the information that was recieved from the client class
	//and stores it in its own variables
	// it then calls updateProperties() which will send the data
	public static void Send() throws JSONException{
		  
		  dataPayload = createJSONObject();
		  
		  //try to send this data to the ThingWorx server
		  try
		  {
			  System.out.println("Attempting to connect to the ThingWorx Server");
			  System.out.println("Connecting to "+ url + thingName + "/Properties/*");
			  System.out.println("Using appkey "+ appKey);
			  System.out.println("Data Payload: " + dataPayload);
			  //call update property, respose is used for testing, will give status code
			  // 200 means it was connected
			 statusCode =  updateProperties(url, appKey, thingName, dataPayload);
			 System.out.println("ThingWorx Server Response Code = " + statusCode);
			 System.out.println();
		  }catch(Exception e)
		  {
			  e.printStackTrace();
			  System.out.println(e);
			  // can recieve an exception if something failed, used for testing
		  }

		  
	   }
	//update properties takes establishes the connection with our ThingWorx Server
	//it will then attempt to send our payload
	// it returns then closes the connection, and returns a response status code
	//(status code used for testing)
	private static int updateProperties(String url, String appKey,
		String thingName, String dataPayload) throws IOException 
	{
		//our url, so the program can find thingworx
		httpUrlString = url + thingName + "/Properties/*";
		// creates the URL
		ourUrl = new URL(httpUrlString);
		//attempts to open the connection
	    urlConnection = (HttpURLConnection) ourUrl.openConnection();
		// use this for the PUT Request
		urlConnection.setDoOutput(true);
	    //we are updateing a property so we would use PUT
	    urlConnection.setRequestMethod("PUT");
	    //shows the type of content we are sending
	    urlConnection.setRequestProperty ("Content-Type", "application/json");
	    //our appkey
	    urlConnection.setRequestProperty ("appKey",appKey);
	    // createst an outputstream to send our payload
	    output = new OutputStreamWriter(urlConnection.getOutputStream());
		//sends our data to thingworx
		output.write(dataPayload);
		output.flush();
		//closes our connection
		output.close();
		//returns response code for printing (200 is OK, anything else is bad)
		return urlConnection.getResponseCode();
	}
	
	private static String createJSONObject() throws JSONException
	{
		  //gets the data from our server
		  temperature = Server.getTemperature();
		  humidity = Server.getHumidity();
		  light = Server.getLight();
		  soilMoisture = Server.getSoilMoisture();
		  //creates our JSONObject, which will be converted to a readable input for the
		  //thingworx server
		  obj = new JSONObject();
		  //stores the values into our JSON object
		  obj.put("temperature", temperature);
		  obj.put("humidity", humidity);
		  obj.put("soilMoisture", soilMoisture);
		  obj.put("light", light);
		  //converts the JSONObject into a readable string
		  dataPayload = obj.toString();
		return dataPayload;
	}
	
}
//End of Client Class
