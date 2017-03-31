package com.jwkj.data;

public class AlarmMessage {
	private String deiceId;
	private String alarmType;
	private String alarmArea;
	private String alarmChannel;
	private String dataType;
	private String recordTime;
	private String imageNameOne;
	private String imageNameTwo;
	private String imageNameThree;
	public AlarmMessage(String deiceId, String alarmType, String alarmArea,
			String alarmChannel, String dataType, String recordTime,
			String imageNameOne, String imageNameTwo, String imageNameThree) {
		super();
		this.deiceId = deiceId;
		this.alarmType = alarmType;
		this.alarmArea = alarmArea;
		this.alarmChannel = alarmChannel;
		this.dataType = dataType;
		this.recordTime = recordTime;
		this.imageNameOne = imageNameOne;
		this.imageNameTwo = imageNameTwo;
		this.imageNameThree = imageNameThree;
	}
	public AlarmMessage() {
		super();
	}
	public String getDeiceId() {
		return deiceId;
	}
	public void setDeiceId(String deiceId) {
		this.deiceId = deiceId;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmArea() {
		return alarmArea;
	}
	public void setAlarmArea(String alarmArea) {
		this.alarmArea = alarmArea;
	}
	public String getAlarmChannel() {
		return alarmChannel;
	}
	public void setAlarmChannel(String alarmChannel) {
		this.alarmChannel = alarmChannel;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getImageNameOne() {
		return imageNameOne;
	}
	public void setImageNameOne(String imageNameOne) {
		this.imageNameOne = imageNameOne;
	}
	public String getImageNameTwo() {
		return imageNameTwo;
	}
	public void setImageNameTwo(String imageNameTwo) {
		this.imageNameTwo = imageNameTwo;
	}
	public String getImageNameThree() {
		return imageNameThree;
	}
	public void setImageNameThree(String imageNameThree) {
		this.imageNameThree = imageNameThree;
	}
	
}
