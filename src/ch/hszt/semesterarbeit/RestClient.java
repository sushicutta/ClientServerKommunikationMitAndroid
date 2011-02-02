package ch.hszt.semesterarbeit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import ch.hszt.semesterarbeit.exception.ProductNotFoundException;

public class RestClient {

	private static final String TAG = "RestClient";
	
	private static HttpClient httpClient = new DefaultHttpClient();
	
	private final String ressource;
	
	private Integer statusCode;

	public RestClient (String application, String path) {
		ressource = application + "/" + path;
	}

	public JSONObject getJsonObject(Integer id)  throws ProductNotFoundException, Exception {
		
		String url = ressource + "/" + id;
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		
		try {
			
			HttpResponse response = httpClient.execute(httpGet);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == 200) {
	
				HttpEntity entity = response.getEntity();
	
				JSONObject json = null;
				
				if (entity != null) {
	
					InputStream instream = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(instream));
					StringBuilder sb = new StringBuilder();
	
					String line = null;
					while ((line = reader.readLine()) != null)
						sb.append(line + "\n");
	
					String result = sb.toString();
	
					instream.close();
	
					// get JSON Object
					json = new JSONObject(result);
					
				}
				
				return json;
				
			} else if (statusCode == 404) {
				Log.d(TAG, "onRead >>> Status: " + statusCode + " ---> Produkt [" + ressource + "/" + id + "] nicht gefunden.");
				throw new ProductNotFoundException(ressource);
			} else {
				Log.e(TAG, "onRead >>> Status: " + statusCode + " ---> Es ist ein Fehler aufgetreten.");
				throw new Exception("Es ist ein Fehler aufgetreten. HTTP-Status-Code: " + statusCode);
			}

		} finally {
			httpGet.abort();
		}
		
	}
	
	public JSONArray getJsonArray(String objectName) throws ProductNotFoundException, Exception {
		
		String url = ressource;
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		
		try {

			HttpResponse response = httpClient.execute(httpGet);
			statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == 200) {
				
				HttpEntity entity = response.getEntity();
				
				JSONArray jsonArray = null;
				
				if (entity != null) {
					
					InputStream instream = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(instream));
					StringBuilder sb = new StringBuilder();
					
					String line = null;
					while ((line = reader.readLine()) != null)
						sb.append(line + "\n");
					
					String result = sb.toString();
					
					instream.close();
					
					JSONObject jsonObject = new JSONObject(result);
					
					jsonArray = jsonObject.optJSONArray(objectName);
					
					Log.d(TAG, "onRead >>> Status: " + statusCode + " ---> " + jsonArray.length() + " Produkt" + (jsonArray.length()>1 ? "e" : "") + " gelesen.");

				}
				
				return jsonArray;
				
			} else if (statusCode == 404) {
				Log.d(TAG, "onRead >>> Status: " + statusCode + " ---> Die Ressource [" + ressource + "] nicht gefunden.");
				throw new ProductNotFoundException(ressource);
			} else {
				Log.e(TAG, "onRead >>> Status: " + statusCode + " ---> Es ist ein Fehler aufgetreten.");
				throw new Exception("Es ist ein Fehler aufgetreten. HTTP-Status-Code: " + statusCode);
			}

		} finally {
			httpGet.abort();
		}

	}
	
	public String insertJSONObject(JSONObject data) throws Exception {
		
		HttpPost postMethod = new HttpPost(ressource);
		postMethod.setHeader("Content-type", "application/json");

		try {
			
			StringEntity stringEntity = new StringEntity(data.toString(), "UTF-8");
			postMethod.setEntity(stringEntity);
			
			HttpResponse response = httpClient.execute(postMethod);
			statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == 201) {
				Log.d(TAG, "onInsert >>> Status: " + statusCode + " ---> Product eingefügt.");
				return response.getFirstHeader("Location").getValue();
			} else {
				Log.e(TAG, "onInsert >>> Status: " + statusCode + " ---> Es ist ein Fehler aufgetreten.");
				throw new Exception("Es ist ein Fehler aufgetreten. HTTP-Status-Code: " + statusCode);
			}

		} finally {
			postMethod.abort();
		}

	}
	
	public void deleteJSONObject(Integer id) throws ProductNotFoundException, Exception {
		
		HttpDelete deleteMethod = new HttpDelete(ressource + "/" + id);

		try {
			
			HttpResponse response = httpClient.execute(deleteMethod);
			statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == 200) {
				Log.d(TAG, "onDelete >>> Status: " + statusCode + " ---> Product [" + ressource + "/" + id + "] deleted.");
			} else if (statusCode == 404) {
				Log.d(TAG, "onDelete >>> Status: " + statusCode + " ---> Product [" + ressource + "/" + id + "] nicht gefunden.");
				throw new ProductNotFoundException(ressource + "/" + id);
			} else {
				Log.e(TAG, "onDelete >>> Status: " + statusCode + " ---> Es ist ein Fehler aufgetreten.");
				throw new Exception("Es ist ein Fehler aufgetreten. HTTP-Status-Code: " + statusCode);
			}

		} finally {
			deleteMethod.abort();
		}
		
	}
	
	public void updateJSONObject(Integer id, JSONObject data) throws ProductNotFoundException, Exception {
		
		HttpPut putMethod = new HttpPut(ressource + "/" + id);
		putMethod.setHeader("Content-type", "application/json");
		
		try {
			
			StringEntity stringEntity = new StringEntity(data.toString(), "UTF-8");
			putMethod.setEntity(stringEntity);
			
			HttpResponse response = httpClient.execute(putMethod);
			statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == 200) {
				Log.d(TAG, "onUpdate >>> Status: " + statusCode + " ---> Product [" + ressource + "/" + id + "] updated.");
			} else if (statusCode == 404) {
				Log.d(TAG, "onUpdate >>> Status: " + statusCode + " ---> Product [" + ressource + "/" + id + "] nicht gefunden.");
				throw new ProductNotFoundException(ressource + "/" + id);
			} else {
				Log.e(TAG, "onUpdate >>> Status: " + statusCode + " ---> Es ist ein Fehler aufgetreten.");
				throw new Exception("Es ist ein Fehler aufgetreten. HTTP-Status-Code: " + statusCode);
			}
			
		} finally {
			putMethod.abort();
		}
		
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}

	
}
