package com.lanmei.lijia.utils;

import android.content.Context;

public class SharedAccount {

	private static SharedPreferencesTool share;
	private SharedAccount(){}
	private static SharedAccount instance = null;
	public static SharedAccount getInstance(Context context){
		if (instance == null) {
			instance = new SharedAccount();
		}
		share = SharedPreferencesTool.getInstance(context, "account");
		return instance;
	}
	

	public void saveMobile(String mobile){
		share.edit().putString("mobile", mobile).commit();
	}

	public void savePcode(String pcode){
		share.edit().putString("pcode", pcode).commit();
	}

	public void saveUid(String uid){//
		share.edit().putString("uid", uid).commit();
	}

	public String getUid(){
		return share.getString("uid", "");
	}

	public String getMobile(){
		return share.getString("mobile", "");
	}

	public String getPcode(){
		return share.getString("pcode","");
	}

	public void setNoFirstLogin(boolean isFirstLogin){
		share.edit().putBoolean("isNoFirstLogin", isFirstLogin).commit();
	}

	public boolean isNoFirstLogin(){
		return share.getBoolean("isNoFirstLogin", false);
	}

	public void clear(){
		share.clear();
	}
}
