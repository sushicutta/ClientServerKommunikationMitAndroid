package ch.hszt.semesterarbeit;

public class Movie {

	public static String OBJECT_NAME = "movie";

	private String id;
	private String created_at;
	private String updated_at;
	private String title;
	private String description;
	private String rating_all;
	
	public String getRating_all() {
		return rating_all;
	}

	public void setRating_all(String rating_all) {
		this.rating_all = rating_all;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	@Override
	public String toString() {
		return "Movie [title=" + getTitle() + ", id" + id + ", created_at="
				+ created_at + ", updated_at=" + updated_at + ", description="
				+ getDescription() + "]";
	}

}
