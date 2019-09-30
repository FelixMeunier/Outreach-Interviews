package com.outreach.interviews.map.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.outreach.interviews.map.enums.MapModes;
import com.outreach.interviews.map.enums.MapOperations;
import com.outreach.interviews.map.enums.MapRegions;

public class MapRoutesHelper
{
	public static class RoutesBuilder {

		private String address;
		//private String origin;
		//private String destination;
		//private MapRegions region;
		private MapOperations operation;
		private MapModes mode;
		private JsonObject result;

		private final String URL = "https://maps.googleapis.com/maps/api/";
		private CloseableHttpClient httpclient = HttpClients.createDefault();
		
		
		/**
		 * Set the address to look for
		 * @param origin String representing the address
		 * @return {@link RoutesBuilder}
		 */
		
		public RoutesBuilder setAddress(String address)  {
			this.address = address;
			return this;
		}
		
		/**
		 * Create the URL to communicate with the Google Maps API
		 * @param type URL to provide to Apache HttpClient
		 * @return {@link RoutesBuilder}
		 */
		public RoutesBuilder setURL(MapOperations type) {
			if(type.equals(MapOperations.directions))
				throw new UnsupportedOperationException();

			this.operation = type;
			return this;

		}
		
		/**
		 * Perform the HTTP request and retrieve the data from the HttpClient object
		 * @return {@link RoutesBuilder}  
		 * @throws UnsupportedOperationException Thrown to indicate that the requested operation is not supported.
		 * @throws IOException Thrown to indicate that the requested operation is not supported.
		 * @throws IllegalArgumentException Thrown to indicate that a method has been passed an illegal or inappropriate argument.
		 */
		public RoutesBuilder build() throws UnsupportedOperationException, IOException, IllegalArgumentException {
			String requestURL = this.getURL()  	+ "address=" + getAddress() 
												+ "&key=" + this.getAPIKey();

			
			
			HttpGet httpGet = new HttpGet(requestURL);
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			try {
				HttpEntity entity = response.getEntity();
				String result = IOUtils.toString(entity.getContent(), "UTF-8");
				this.result = new JsonParser().parse(result).getAsJsonObject();
			}
			finally {
				response.close();
			}
			
			return this;
		}
		
		/**
		 * Retrieve the latitude and longitude of the address
		 * @return float[2] with lat and lng
		 */
		
		public float[] getLatLng(){
			float[] latLng = new float[2];

			latLng[0] = (this.result.get("results").getAsJsonArray().get(0)
									.getAsJsonObject()
									.get("geometry")
									.getAsJsonObject()
									.get("location")
									.getAsJsonObject()
									.get("lat")
									.getAsFloat());
			
			latLng[1] = (this.result.get("results").getAsJsonArray()
									.get(0)
									.getAsJsonObject()
									.get("geometry")
									.getAsJsonObject()
									.get("location")
									.getAsJsonObject()
									.get("lng")
									.getAsFloat());
			return latLng;
		}
		

		//*************************For Internal Use Only***********************************//
		private final String getAddress() {
			return this.address;
		}
		
		private final String getURL() {
			return this.URL + this.operation.name() + "/json?";
		}

		private final String getAPIKey() {
			return System.getenv("OUTREACH_MAPS_KEY");
		}
		
		
		private final boolean zeroResults(JsonObject obj) {
			return !obj.get("status").getAsString().equals("ZERO_RESULTS");
		}

	}
}