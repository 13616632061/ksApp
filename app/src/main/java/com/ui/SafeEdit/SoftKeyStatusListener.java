package com.ui.SafeEdit;

public interface SoftKeyStatusListener {

	public void onPressed(SoftKey softKey);
	
	public void onDeleted();
	
	public void onConfirm();
	
}
