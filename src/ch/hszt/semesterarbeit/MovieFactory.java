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

public class MovieFactory {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static Movie produceFromJson(JSONObject jsonObject) {

		try {
			
			// get Value of given Name, otherwise null
			JSONObject jsonMovieObject = jsonObject.optJSONObject(Movie.OBJECT_NAME);
			
			if (jsonMovieObject != null) {
				return mapper.readValue(jsonMovieObject.toString(), Movie.class);
			} else {
				return null;
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;

	}
	
	public static Movie produceFromJson(String jsonString) {
		
		try {
			return produceFromJson(new JSONObject(jsonString));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static List<Movie> produceFromJson(JSONArray jsonArray) {
		
		List<Movie> movies = new ArrayList<Movie>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				
				Object obj = jsonArray.get(i);
				
				JSONObject jsonObject = null;
				if (obj != null) {
					jsonObject = new JSONObject(obj.toString());
				}
				 
				JSONObject jsonMovieObject = jsonObject.optJSONObject(Movie.OBJECT_NAME);
				
				Movie movie = null;
				if (jsonMovieObject != null) {
					movie = mapper.readValue(jsonMovieObject.toString(), Movie.class);
				}
				
				if (movie != null) {
					movies.add(movie);
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
		}
		
		return movies;
		
	}

}
