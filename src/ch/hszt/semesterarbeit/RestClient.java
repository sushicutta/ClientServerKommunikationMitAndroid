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
	
	private final String ressource;
	
	public RestClient (String application, String path) {
		ressource = application + "/" + path;
	}

	public JSONObject getJsonObject(Integer id) {
		
		String url = ressource + "/" + id;
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		
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
					sb.append(line + "\n");

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
	
	public JSONArray getJsonArray(String objectName) {
		
		String url = ressource;
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		
		try {

			HttpResponse response = httpClient.execute(httpGet);
			Integer responseCode = response.getStatusLine().getStatusCode();
			
			if (responseCode == 200) {
				
				HttpEntity entity = response.getEntity();
				
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
					
					JSONArray jsonArray = jsonObject.optJSONArray(objectName);					

					return jsonArray;
				}
				
			// TODO: HTTP-Status (z.B. 404) in eigener Anwendung verarbeiten.
			} else if (responseCode == 404) {
				
			// TODO: HTTP-Status unbekannt, in eigener Anwendung verarbeiten.
			} else {
				
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
	
	public String insertJSONObject(JSONObject data) {
		HttpPost postMethod = new HttpPost(ressource);
		postMethod.setHeader("Content-type", "application/json");

		try {
			
			StringEntity stringEntity = new StringEntity(data.toString(), "UTF-8");
			postMethod.setEntity(stringEntity);
			
			HttpResponse response = httpClient.execute(postMethod);
			Integer responseCode = response.getStatusLine().getStatusCode();
			
			if (responseCode == 201) {
				return response.getFirstHeader("Location").getValue();
			} else {
				return null;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.abort();
		}
		
		return null;
	}
	
	public void deleteJSONObject(Integer id) {
		HttpDelete deleteMethod = new HttpDelete(ressource + "/" + id);

		try {
			
			HttpResponse response = httpClient.execute(deleteMethod);
			Integer responseCode = response.getStatusLine().getStatusCode();
			
			if (responseCode == 200) {
				// Everythins All Right.... Write log or do something
			} else if (responseCode == 404) {
				// TODO throw new ProductNotFoundException
			} else {
				// TODO throw new Exception
			}

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
	
	public void updateJSONObject(Integer id, JSONObject data) {
		
		HttpPut putMethod = new HttpPut(ressource + "/" + id);
		putMethod.setHeader("Content-type", "application/json");
		
		try {
			
			StringEntity stringEntity = new StringEntity(data.toString(), "UTF-8");
			putMethod.setEntity(stringEntity);
			
			HttpResponse response = httpClient.execute(putMethod);
			Integer responseCode = response.getStatusLine().getStatusCode();
			
			if (responseCode == 200) {
				// Everythins All Right.... Write log or do something
			} else if (responseCode == 404) {
				// TODO throw new ProductNotFoundException
			} else {
				// TODO throw new Exception
			}
			
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
