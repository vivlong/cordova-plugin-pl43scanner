package com.sysmagic.plugins.pl43scanner;

import android.app.Activity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import android.net.Uri;
import android.util.Log;
import android.content.Intent;

public class PL43Scanner extends CordovaPlugin {
	
    public static final int REQUEST_CODE = 0x0ba7c0de;	
    private static final String SCAN = "scan";
    private static final String SCAN_INTENT = "com.sysmagic.mob.SCAN";
	private CallbackContext callbackContext;

	public PL43Scanner() {
	}
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		this.callbackContext = callbackContext;
        Log.e("PL43Scanner", "call execute");		
		if (action.equals(SCAN)) {			
            scan(args);
		} else {
            return false;
        }
		return true;
	}
	
	/**
     * Starts an intent to scan and decode a barcode.
     */
    public void scan(JSONArray args) {
        Log.e("PL43Scanner", "call scan");
        Intent intentScan = new Intent(SCAN_INTENT);
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);        
        intentScan.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
        this.cordova.startActivityForResult((CordovaPlugin) this, intentScan, REQUEST_CODE);
    }

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {				
				Log.e("PL43Scanner", "call onActivityResult");
			}
		}
	}
}
