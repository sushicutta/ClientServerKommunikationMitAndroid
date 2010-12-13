package ch.hszt.semesterarbeit;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectFactory {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static JSONObject produceFromMovie(MoviePost movie) {

		try {

			String jsonMovieString = mapper.writeValueAsString(movie);
		
			JSONObject jsonMovieObject = new JSONObject(jsonMovieString);
			
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(Movie.OBJECT_NAME, jsonMovieObject);
			
			return jsonObject;

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
	
	public static JSONObject produceFromMovie(Movie movie) {
		
		try {
			
			String jsonMovieString = mapper.writeValueAsString(movie);
			
			JSONObject jsonMovieObject = new JSONObject(jsonMovieString);
			
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put(Movie.OBJECT_NAME, jsonMovieObject);
			
			return jsonObject;
			
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
