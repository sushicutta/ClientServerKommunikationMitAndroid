package ch.hszt.semesterarbeit.hessian;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import ch.hszt.semesterarbeit.Product;

import com.caucho.hessian.client.HessianProxyFactory;

public class HessianClient {
	
	private static final long serialVersionUID = -8058880052876561279L;
	
	private ProductRegistration productRegistration = null;
	
	public HessianClient(String applicationServlet) throws MalformedURLException {
		
		String webServiceUrl = applicationServlet + "/ProductRegistrationService";

		HessianProxyFactory proxyFactory = new HessianProxyFactory();

		// Es geht sonst nicht, leider nur Hessian 1.0 unterstützt
		// Siehe ---> http://bugs.caucho.com/view.php?id=3036
		
		proxyFactory.setHessian2Reply(false);
		proxyFactory.setHessian2Request(false);
		
		// Es muss Thread.currentThread().getContextClassLoader() für den ClassLoader verwendet werden.
		// Siehe ---> http://code.google.com/p/hessdroid/
		
		productRegistration = (ProductRegistration) proxyFactory.create(ProductRegistration.class, webServiceUrl, Thread.currentThread().getContextClassLoader());
		
	}
	
	public int add(int a, int b) {
		return productRegistration.add(1, 2);
	}
	
	public List<Product> allProducts() {
//		return productRegistration.allProducts();
		Product [] products = productRegistration.allProducts();
		return Arrays.asList(products);
	}

	public Product register(Product product) {
		return productRegistration.register(product);
	}

	public Product get(Long id) throws Exception {
		return productRegistration.get(id);
	}

	public Product delete(Long id) throws Exception {
		return productRegistration.delete(id);
	}

	public Product update(Long id, Product product) throws Exception {
		return productRegistration.update(id, product);
	}
}
