package com.kyee.monitor.base.common.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonExclusionStrategy implements ExclusionStrategy {
	
	@Override
	public boolean shouldSkipClass(Class<?> cls) {
		
		if(cls == Throwable.class){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldSkipField(FieldAttributes attributes) {
		
		return false;
	}
}
