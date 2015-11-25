package com.ngeen.misc;

public class ShaderData {
	Float[] data;
	int array_len = 1, type;

	public ShaderData(Float[] el, int type, int len) {
		this.data = el;
		this.type = type;
		this.array_len = len;
	}

	public Float[] getData() {
		return data;
	}
}
