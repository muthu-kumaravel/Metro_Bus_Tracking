package com.example.mtc_bus;

import java.io.Serializable;

public class ViewItem implements Serializable
{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -608345155532293438L;
	private String task;

	 

	
	 public String gettask() {
	  return task;
	 }

	 public void settask(String task) {
	  this.task = task;
	 }
	 
}
