package com.biometrics.rssolution.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefenrenceHelper {

private SharedPreferences sharedPreferences;
private Editor editor;

public PrefenrenceHelper(Context context) {
this.sharedPreferences = context.getSharedPreferences("mPrefs", Context.MODE_PRIVATE);
//getPreferences(MODE_PRIVATE);    
this.editor = sharedPreferences.edit(); }

public String GetPreferences(String key) {
    return sharedPreferences.getString(key, "null");
}

public void SavePreferences(String key, String value) {
editor.putString(key, value);    
editor.commit();  
}
}  
