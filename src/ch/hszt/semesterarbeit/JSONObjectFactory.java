package ch.hszt.semesterarbeit;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hszt.semesterarbeit.Product;

public class JSONObjectFactory {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static JSONObject produceFromProduct(Product product) {

		try {

			String jsonProductString = mapper.writeValueAsString(product);
		
			JSONObject jsonProductObject = new JSONObject(jsonProductString);
			
			return jsonProductObject;

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return null;
		
	}
	
}
