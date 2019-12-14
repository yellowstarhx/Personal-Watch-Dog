package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Image {
	private String filename;
	private String camera_id;
	private String url;
	private String capture_time;
	private String mlResult;
	private String comment;
	
	public String getFilename() {
		return filename;
	}
	
	public String getCameraID() {
		return camera_id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getCaptureTime() {
		return capture_time;
	}
	
	public String getMLResult() {
		return mlResult;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	private Image(ImageBuilder builder) {
		this.filename = builder.filename;
		this.camera_id = builder.camera_id;
		this.url = builder.url;
		this.capture_time = builder.capture_time;
		this.mlResult = builder.mlResult;
		this.comment = builder.comment;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("filename", filename);
			obj.put("camera_id", camera_id);
			obj.put("url", url);
			obj.put("mlResult", mlResult);
			obj.put("capture_time", capture_time);
			obj.put("comment", comment);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static class ImageBuilder {
		private String filename;
		private String camera_id;
		private String url;
		private String capture_time;
		private String mlResult;
		private String comment;

		public ImageBuilder() {
			// TODO Auto-generated constructor stub
		}

		public Image build() {
			return new Image(this);
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public void setCamera_id(String camera_id) {
			this.camera_id = camera_id;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setCapture_time(String capture_time) {
			this.capture_time = capture_time;
		}

		public void setMLResult(String mlResult) {
			this.mlResult = mlResult;
		}
		
		public void setComment(String comment) {
			this.comment = comment;
		}
	}
}
