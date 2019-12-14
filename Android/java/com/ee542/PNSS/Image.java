package com.ee542.PNSS;

public class Image {
    private String imageURL;
    private String captureTime;
    private String comment;
    private String cameraId;
    private String machineLearningResult;

    public Image() {}

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getMachineLearningResult() {
        return machineLearningResult;
    }

    public void setMachineLearningResult(String machineLearningResult) {
        this.machineLearningResult = machineLearningResult;
    }
}
