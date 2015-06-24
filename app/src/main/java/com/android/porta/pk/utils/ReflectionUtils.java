package com.android.porta.pk.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

	public static Map<String, String> getFieldNamesAndStringValues(
			final Object valueObj) {
		Map<String, String> fieldMap = new HashMap<String, String>();
		Field[] valueObjFields = valueObj.getClass().getDeclaredFields();
		for (int i = 0; i < valueObjFields.length; i++) {
			String fieldName = valueObjFields[i].getName();
			Object objValue = null;
			try {
				objValue = valueObjFields[i].get(valueObj);
			} catch (Exception e) {// ignore
			}
			String value = null;
			if (objValue != null) {
				value = objValue.toString();
			}
			fieldMap.put(fieldName, value);
		}
		return fieldMap;
	}
}
