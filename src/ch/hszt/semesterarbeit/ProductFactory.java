package ch.hszt.semesterarbeit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hszt.semesterarbeit.entity.Product;

public class ProductFactory {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static Product produceFromJson(JSONObject jsonObject) {

		try {
			
			return mapper.readValue(jsonObject.toString(), Product.class);
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;

	}
	
	public static Product produceFromJson(String jsonString) {
		
		try {
			return produceFromJson(new JSONObject(jsonString));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static List<Product> produceFromJson(JSONArray jsonArray) {
		
		List<Product> products = new ArrayList<Product>();
		
		try {

			for (int i = 0; i < jsonArray.length(); i++) {
				
				Object obj = jsonArray.get(i);
				
				JSONObject jsonProductObject = null;
				if (obj != null) {
					jsonProductObject = new JSONObject(obj.toString());
				}
				 
				Product product = null;
				if (jsonProductObject != null) {
					product = mapper.readValue(jsonProductObject.toString(), Product.class);
				}
				
				if (product != null) {
					products.add(product);
				}
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return products;
		
	}

}
