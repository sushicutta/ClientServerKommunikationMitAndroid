//package ch.hszt.semesterarbeit;
//
//import javax.ws.rs.core.MediaType;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientHandlerException;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//import com.sun.jersey.spi.service.ServiceFinder;
//
//public class RestClient {
//	
//	private static final String PROTOCOLL = "http://";
//	private static final String URL_DELIMITTER = "/";
//	
//	private ClientConfig clientConfig;
//	private Client restClient;
//	
//	private WebResource webResource;
//
//	public RestClient(final String host, final String language, final String path) {
//		super();
//	    setup(host, language, path);
//	}
//	
//	@SuppressWarnings("unchecked")
//	private void setup(final String host, final String language, final String path) {
//		
//		clientConfig = new DefaultClientConfig();
//		clientConfig.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
//		restClient = Client.create(clientConfig);
//		webResource = restClient.resource(PROTOCOLL + host + URL_DELIMITTER +
//				language + URL_DELIMITTER +
//				path);
//		
//		ServiceFinder.setIteratorProvider(new ServiceIteratorProviderWorkAround());
//
//	}
//	
//	public String get() {
//		try {
//		    final String response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
//		    return response;
//		} catch (ClientHandlerException che) {
//			che.printStackTrace();
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//}
