package fortunes.model;

/**
 * Entity model; represents a fortune cookie.
 * @author jenny
 *
 */
public class Fortune {
	
	private Integer id = null;
	private String saying = "";
	private Boolean isArchived = false;
	
	public Fortune(String saying) {
		this.saying = saying;
	}

	public Fortune(Integer id, String saying, Boolean isArchived) {
		this.id = id;
		this.saying = saying;
		this.isArchived = isArchived;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSaying() {
		return saying;
	}

	public void setSaying(String saying) {
		this.saying = saying;
	}
	
	public Boolean isArchived() {
		return isArchived;
	}

	public void setArchived(Boolean archived) {
		this.isArchived = archived;
	}

	public String toString() {
		return "--------\n" + id + (isArchived ? " (archived)" : "") + "\n" + saying + "\n--------";
	}

}
