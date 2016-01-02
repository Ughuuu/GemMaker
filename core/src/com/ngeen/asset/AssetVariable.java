package com.ngeen.asset;

import java.util.List;

/**
 * Asset containing an array of elements.
 * @author Dragos
 *
 */
public class AssetVariable<T> extends AssetBase<T> {
	private List<T> _Array;
	
	/**
	 * Constructor with array of elements.
	 * @param array The data.
	 */
	public AssetVariable(List<T> array){
		super();
		_Array = array;
	}

	@Override
	public int getSize() {
		return _Array.size();
	}

	@Override
	public List<T> getData() {
		return _Array;
	}

	@Override
	public T getElement() {
		return _Array.get(0);
	}

	@Override
	public T getDataAt(int i) {
		return _Array.get(i);
	}

	@Override
	public void setData(List<T> data) {
		_Array = data;
	}

	@Override
	public void setElement(T data) {
		_Array.set(0, data);
	}

	@Override
	public void setDataAt(int i, T element) {
		_Array.set(i, element);
	}
}
