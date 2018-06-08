import java.net.*;
import java.util.InputMismatchException;
import java.io.*;

import org.json.JSONException;

//Our server will establish a connection with the sensors, and will store
//information that will later be relayed to ThingWorx by the Client Class
public class Server 
{
	
//used to connect to the sensors
//our ServerSocket
private static ServerSocket server;
private static Socket socket;
//Our first sensor uses port number 9876
private static int port = 9876;

//used to store our information
private static float temperature, humidity;
private static int soilMoisture, light;
    
	//this function will wait until a connection is established with the sensors
	// it will then accept 4 integers, in the specific order
	//temperature, humidity, light, and soilMoisture, which will be used in the Client.Send()
	//Function
	public static void Receive() throws NumberFormatException, IOException, ClassNotFoundException, InterruptedException, JSONException
	{
		//try to run without error
	  try
	  {
			//Tries to create a new socket using the specified port number
			server = new ServerSocket(port);
			
			//infinite loop, only exits its there is an exception
		    while(true)
			{
				
				getData();
				
				//calls the send function in client class
				printData();
				
				//calls Client.send()
				Client.Send(); 
		 }
				//end of infinite loop
     }
		
	 catch(InterruptedIOException IIOE)
     {
    	IIOE.printStackTrace();
    	System.out.println(IIOE);
     }
	  
     catch(IOException IOE)
     {
    	 IOE.printStackTrace();
    	 System.out.println(IOE);
     }
		
	 catch(Exception e)
	 {
		e.printStackTrace();
		System.out.println(e);
	 }
        //closes the server
        server.close();
        
   }
    	//used to get temperature from client without the ability to change its value
    	public static float getTemperature()
    	{
    		return temperature;
    	}
    	//used to get Humidity from client without the ability to change its value
    	public static float getHumidity()
    	{
    		return humidity;
    	}
    	//used to get Soil Moisture from client without the ability to change its value
    	public static int  getSoilMoisture()
    	{
    		return soilMoisture;
    	}
    	
    	//used to get light from client without the ability to change its value
    	public static int getLight()
    	{
    		return light;
    	}
    	
    	private static void getData() throws IOException
    	{
    		//accepts the clients request
		    socket = server.accept(); 
			//Creates the input stream
		    System.out.println("Connection to Sensor unit established using port " + port +".");
		    DataInputStream input = new DataInputStream(socket.getInputStream());
			
			//reads in the 4 ints in a specific order 
			//(order of packet on featherhuzzah)
			
			temperature = input.readInt();
			humidity = input.readInt();
			light = input.readInt();
			soilMoisture = input.readInt();
			
			//temperature and humidity are floats, so re-convert back
			temperature = temperature/100;
			humidity = humidity/100;
			
			//close our connections
			socket.close();
    	}
    	
    	//prints the data we obtained from sensors  to screen (used for testing)
    	private static void printData()
    	{
    	//print out temperature in degrees Fahrenheit
    	System.out.println("Temperature: " + getTemperature() + "F");
    	//print out percent humidity
    	System.out.println("Humidity: " + getHumidity() + "%");
    	
    	//print out text data for soil moisture
    	if(getSoilMoisture() < 1000 )
    		System.out.println("Soil: Dry " + getSoilMoisture());
    	else if(getSoilMoisture() >=1000)
    		System.out.println("Soil level:  Wet " + getSoilMoisture());
    	
    	//print out our text data for the light level
    	if(getLight() < 0)
    		System.out.println("Light level: Dark " + getLight());
    	else if(getLight() < 10 && light >= 0)
    		System.out.println("Light level: Dim " + getLight());
    	else if(getLight() >= 10 && light < 2000)
    		System.out.println("Light level: Light " + getLight());
    	else if(getLight() >= 2000)
    		System.out.println("Light level: Bright " + getLight());
    	else
    		System.out.println("Light level: Unknown " + getLight());
    	}
  
    }
	//end of Server Class




