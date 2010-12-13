package ch.hszt.semesterarbeit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestClient {

	private static HttpClient httpClient = new DefaultHttpClient();
	
	private static String JSON_EXTENSION = ".json";
	
	private final String ressource;
	
	public RestClient (String application, String language, String path) {
		ressource = application + "/" + language + "/" + path;
	}

	public JSONObject getJsonObject(Integer id) {
		
		String url = ressource + "/" + id + JSON_EXTENSION;
		
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;

		try {
			response = httpClient.execute(httpGet);

			// TODO: HTTP-Status (z.B. 404) in eigener Anwendung verarbeiten.

			HttpEntity entity = response.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = reader.readLine()) != null)
					sb.append(line + "n");

				String result = sb.toString();

				instream.close();

				// get JSON Object
				JSONObject json = new JSONObject(result);
				
				return json;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
		}
		return null;
	}
	
	public JSONArray getJsonArray() {
		
		String url = ressource + JSON_EXTENSION;
		
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;
		
		try {
			response = httpClient.execute(httpGet);
			
			// TODO: HTTP-Status (z.B. 404) in eigener Anwendung verarbeiten.
			
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();
				
				String line = null;
				while ((line = reader.readLine()) != null)
					sb.append(line + "n");
				
				String result = sb.toString();
				
				instream.close();
				
				// get JSON Object
				JSONArray jsonArray = new JSONArray(result);
				
				return jsonArray;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
		}
		return null;
	}
	
	public void insertJSONObject(JSONObject data) {
		HttpPost postMethod = new HttpPost(ressource + JSON_EXTENSION);
		postMethod.setHeader("Content-type", "application/json");

		try {
			
			StringEntity stringEntity = new StringEntity(data.toString(), "UTF-8");
			postMethod.setEntity(stringEntity);
			
			HttpResponse response = httpClient.execute(postMethod);
			response.getStatusLine().getStatusCode();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.abort();
		}
	}
	
	public void deleteJSONObject(String id) {
		HttpDelete deleteMethod = new HttpDelete(ressource + "/" + id);
		deleteMethod.setHeader("Content-type", "application/json");

		try {
			
			HttpResponse response = httpClient.execute(deleteMethod);
			Integer responseCode = response.getStatusLine().getStatusCode();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			deleteMethod.abort();
		}
		
	}
	
	public void updateJSONObject(String id, JSONObject data) {
		
		HttpPut putMethod = new HttpPut(ressource + "/" + id + JSON_EXTENSION);
		putMethod.setHeader("Content-type", "application/json");
		
		try {
			
			StringEntity stringEntity = new StringEntity(data.toString(), "UTF-8");
			putMethod.setEntity(stringEntity);
			
			HttpResponse response = httpClient.execute(putMethod);
			Integer responseCode = response.getStatusLine().getStatusCode();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			putMethod.abort();
		}
		
	}
	
}
