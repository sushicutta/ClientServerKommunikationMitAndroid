package ch.hszt.semesterarbeit.entity;

public class Product implements Comparable<Product> {
	
	public static String OBJECT_NAME = "product";

	private String name;

	private int numberOfUnits;

	public Product() {
	}

	public Product(final String name, final int numberOfUnits) {
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

	@Override
	public String toString() {
		return "Product{" + "name=" + name + ", numberOfUnits=" + numberOfUnits
				+ "}";
	}

	@Override
	public int compareTo(Product compareProduct) {
		return name.compareToIgnoreCase(compareProduct.getName());
	}

}
