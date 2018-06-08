/* Program Created Spring 2018
 * CSC 492: Senior Project II
 * By: Kory Bilak
 * Version .1 created 2/02/2018: dummy program sends data to Server class, and Client class
 * displays data on screen
 * Version .5  created 2/16/2018: can connect to thingworx using restAPI
 * Version 1.0 created 3/3/2018: can accept data from sensors by reading lines of data
 * and converting the strings into floats and ints
 * it then sends the values to thingworx
 * Version 1.1 created 3/5/2018: can accept data from sensors by reading ints sent by the 
 * sensors via a packet
 * Version 1.2 created 4/9/2018: code cleanup, removed alot of test code to make program
 * smaller
*/

import java.io.IOException;
import org.json.JSONException;

//Our main class begins the program and calls the Server.Recieve() function
public class MainClass 
{

	//Calls the Server.Recieve function
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException, JSONException 
		{
		Server.Receive(); //runs the Receive() function
		}
}
//End of MainClass
