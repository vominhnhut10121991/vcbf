package utility;

public class MyCustomMessage {
	private String header = null;
	private String message = null;
	private String date = null;
	private boolean selected = false;
	private boolean visible = false;

	public MyCustomMessage(String header, String message, String date,
			boolean selected) {
		super();
		this.header = header;
		this.message = message;
		this.date = date;
		this.selected = selected;
		this.visible = false;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}