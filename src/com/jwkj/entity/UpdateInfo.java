package com.jwkj.entity;

//更新信息
public class UpdateInfo {
	public String versionCode = "";
	public String versionName = "";
	public String message = "";
	public String url = "";

	@Override
	public String toString() {
		return " [versionCode=" + versionCode + ", versionName=" + versionName + ", message=" + message + ", url=" + url + "]";
	}
}
