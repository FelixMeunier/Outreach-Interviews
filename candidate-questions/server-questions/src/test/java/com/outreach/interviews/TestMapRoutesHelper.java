package com.outreach.interviews;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.outreach.interviews.map.builder.MapRoutesHelper;
import com.outreach.interviews.map.builder.MapRoutesHelper.RoutesBuilder;
import com.outreach.interviews.map.enums.MapOperations;
import com.outreach.interviews.map.enums.MapRegions;

public class TestMapRoutesHelper 
{	
	
	@Test
	public void testMapRoutesHelperApiKey1() throws UnsupportedOperationException, IOException {
		float[] result = new MapRoutesHelper.RoutesBuilder()
			.setAddress("Sudbury")
			.setURL(MapOperations.geocode)
			.build()
			.getLatLng();
		
		System.out.println("Latitude : " + result[0]);
		System.out.println("Longitude : " + result[1]);
	}
	
}
