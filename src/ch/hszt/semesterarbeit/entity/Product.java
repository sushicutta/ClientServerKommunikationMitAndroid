package ch.hszt.semesterarbeit.entity;

public class Product implements Comparable<Product> {
	
	public static String OBJECT_NAME = "product";
	
	private Long id;

	private String name;

	private int numberOfUnits;

	public Product() {
	}

	public Product(Long id, String name, int numberOfUnits) {
		super();
		this.id = id;
		this.name = name;
		this.numberOfUnits = numberOfUnits;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(int numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Product{id=" + id + ", name=" + name + ", units=" + numberOfUnits
				+ "}";
	}

	@Override
	public int compareTo(Product compareProduct) {
		return name.compareToIgnoreCase(compareProduct.getName());
	}

}
