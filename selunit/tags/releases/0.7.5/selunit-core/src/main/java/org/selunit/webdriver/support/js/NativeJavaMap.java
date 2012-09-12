package org.selunit.webdriver.support.js;

import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

public class NativeJavaMap extends NativeJavaObject {
	private static final long serialVersionUID = 2718712060476324481L;
	private Map<String, Object> map;

	@Override
	public String getClassName() {
		return "JavaMap";
	}

	public static <ValueClass> NativeJavaMap wrap(Scriptable scope,
			Map<String, ValueClass> map) {
		return new NativeJavaMap(scope, map);
	}

	@Override
	public Object unwrap() {
		return map;
	}

	@SuppressWarnings("unchecked")
	public <ValueClass> NativeJavaMap(Scriptable scope,
			Map<String, ValueClass> map) {
		super(scope, null, ScriptRuntime.ObjectClass);
		this.map = (Map<String, Object>) map;
	}

	@Override
	public boolean has(String id, Scriptable start) {
		return map.containsKey(id);
	}

	@Override
	public boolean has(int index, Scriptable start) {
		return has(Integer.toString(index), start);
	}

	@Override
	public Object get(String id, Scriptable start) {
		Context cx = ContextFactory.getGlobal().enterContext();
		if (map.containsKey(id) && map.get(id) != null) {
			Object obj = map.get(id);
			return cx.getWrapFactory().wrap(cx, this, obj, obj.getClass());
		} else {
			return Undefined.instance;
		}
	}

	@Override
	public Object get(int index, Scriptable start) {
		return get(Integer.toString(index), start);
	}

	@Override
	public void put(String id, Scriptable start, Object value) {
		if (value != null) {
			map.put(id, Context.jsToJava(value, value.getClass()));
		} else {
			map.put(id, null);
		}
	}

	@Override
	public void put(int index, Scriptable start, Object value) {
		put(Integer.toString(index), start, value);
	}

	@Override
	public Object getDefaultValue(Class<?> hint) {
		if (hint == null || hint == ScriptRuntime.StringClass)
			return map.toString();
		if (hint == ScriptRuntime.BooleanClass)
			return Boolean.TRUE;
		if (hint == ScriptRuntime.NumberClass)
			return ScriptRuntime.NaNobj;
		return this;
	}

	@Override
	public Object[] getIds() {
		Object[] result = new Object[map.size()];
		int i = 0;
		for (String key : map.keySet()) {
			result[i++] = key;
		}
		return result;
	}

	@Override
	public Scriptable getPrototype() {
		if (prototype == null) {
			prototype = ScriptableObject.getClassPrototype(
					this.getParentScope(), "Object");
		}
		return prototype;
	}

}
