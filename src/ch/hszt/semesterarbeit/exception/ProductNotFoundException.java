package ch.hszt.semesterarbeit.exception;

public class ProductNotFoundException extends Exception {

	private static final long serialVersionUID = 739484335704263243L;

	public ProductNotFoundException() {
		super();
	}

	public ProductNotFoundException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ProductNotFoundException(String detailMessage) {
		super(detailMessage);
	}

}
