package fortunes.model;

/**
 * Entity model; represents a fortune cookie.
 * @author jenny
 *
 */
public class Fortune {
	
	private Integer id = null;
	private String saying = "";
	
	public Fortune(String saying) {
		this.saying = saying;
	}

	public Fortune(Integer id, String saying) {
		this.id = id;
		this.saying = saying;
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
	
	public String toString() {
		return "--------\n" + id + "\n" + saying + "\n--------";
	}

}
