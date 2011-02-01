package ch.hszt.semesterarbeit.hessian;

import ch.hszt.semesterarbeit.Product;

public interface ProductRegistration {
	
	public int add(int a, int b);

	public Product [] allProducts();

	public Product register(Product product);

	public Product get(Long id) throws Exception;

	public Product delete(Long id) throws Exception;

	public Product update(Long id, Product product) throws Exception;
}