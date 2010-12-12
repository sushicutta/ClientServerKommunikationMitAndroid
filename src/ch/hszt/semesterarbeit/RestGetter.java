package ch.hszt.semesterarbeit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestGetter {

	private static HttpClient httpClient = new DefaultHttpClient();

	public static JSONObject getJsonObject(String application, String language, String path, Integer id) {
		
		String url = application + "/" + language + "/" + path + "/" + id + ".json";
		
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
	
	public static JSONArray getJsonArray(String application, String language, String path) {
		
		String url = application + "/" + language + "/" + path + ".json";
		
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
	
	public static void postJSONObject(String url, JSONObject data) {
		HttpPost postMethod = new HttpPost(url);
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
	
}
