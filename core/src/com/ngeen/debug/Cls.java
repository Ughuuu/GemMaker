package com.ngeen.debug;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import javax.swing.JTextField;

class Cls {
	Object ref = null;
	String name, pkg;
	Field[] field;
	String[] text;

	public Cls(String name, String pkg, Field[] field, Object ref) {
		this.ref = ref;
		this.name = name;
		this.pkg = pkg;
		this.field = field;
		text = new String[field.length];
	}

	public Class<?> getClass(int i) {
		if (i < field.length) {
			return ((Field) field[i]).getType();
		}
		return null;
	}

	public String getType(int i) {
		if (i < field.length) {
			return ((Field) field[i]).getType().getName();
		}
		return null;
	}

	public String getName(int i) {
		if (i < field.length) {
			return ((Field) field[i]).getName();
		}
		return "null";
	}

	public Object getValue(int i) {
		if (i < field.length) {
			Object obj = null;
			try {
				if (getClass(i) == int.class) {
					Object obj1 = 0;
					if (ref != null)
						obj1 = ref;
					obj = ((Field) field[i]).get(obj1);
				} else if (getClass(i) == String.class) {
					Object obj1 = "null";
					if (ref != null)
						obj1 = ref;
					obj = ((Field) field[i]).get(obj1);
				} else if (getClass(i) == boolean.class) {
					boolean b = false;
					Object obj1 = b;
					if (ref != null)
						obj1 = ref;
					obj = ((Field) field[i]).get(obj1).toString().equals("false") ? false : true;
				} else if (getClass(i) == float.class) {
					Object obj1 = 0.f;
					if (ref != null)
						obj1 = ref;
					obj = ((Field) field[i]).get(obj1).toString();
				} else if (getClass(i) == Integer.class) {
					Object obj1 = new Integer(0);
					if (ref != null)
						obj1 = ref;
					obj = ((Field) field[i]).get(obj1).toString();
				} else if (getClass(i) == double.class) {
					Object obj1 = 0.d;
					if (ref != null)
						obj1 = ref;
					obj = ((Field) field[i]).get(obj1).toString();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (obj != null) {
				return obj;
			}
		}
		return new Object();
	}

	public void setValue(int i, Object val) {
		if (i < field.length) {
			Object conv = new Object();
			try {
				if (getClass(i) == int.class) {
					int v = Integer.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
				if (getClass(i).equals(String.class)) {
					String v = val.toString();
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
				if (getClass(i) == float.class) {
					float v = Float.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}

				if (getClass(i) == double.class) {
					double v = Double.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
				if (getClass(i) == Integer.class) {
					Integer v = Integer.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
				if (getClass(i) == Float.class) {
					Float v = Float.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
				if (getClass(i) == Double.class) {
					Double v = Double.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
				if (getClass(i) == Boolean.class) {
					Boolean v = Boolean.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
				if (getClass(i) == boolean.class) {
					boolean v = Boolean.valueOf(val.toString());
					conv = v;
					((Field) field[i]).set(ref, conv);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {

			}
		}
	}

	public int size() {
		return field.length;
	}
}