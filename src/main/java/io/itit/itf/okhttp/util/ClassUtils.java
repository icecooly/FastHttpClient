package io.itit.itf.okhttp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author skydu
 *
 */
public class ClassUtils {
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> objectToMap(Object obj){    
		Map<String, String> map = new HashMap<String, String>();
        List<Field> fields=ClassUtils.getFieldList(obj.getClass());
        for (Field field : fields) {
        		int mod = field.getModifiers();  
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){    
                continue;    
            }    
            field.setAccessible(true);
            try {
            	Object value=field.get(obj);
                if(value!=null) {
                		map.put(field.getName(), value.toString()); 
                }
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(),e);
			}
		}
        return map;  
    }
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<Field> getFieldList(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		Set<String> filedNames = new HashSet<>();
		for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
			try {
				Field[] list = c.getDeclaredFields();
				for (Field field : list) {
					String name = field.getName();
					if (filedNames.contains(name)) {
						continue;
					}
					filedNames.add(field.getName());
					fields.add(field);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(),e);
			}
		}
		return fields;
	}
}
