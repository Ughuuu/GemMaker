package com.ngeen.asset;

import java.util.List;

/**
 * The base class for assets. Has to be extended.
 * @author Dragos
 *
 */
public abstract class AssetBase<T> {
	/**
	 * Unique id of this Asset. Not unique with other types. First id will be 0.
	 */
	protected int Id;
	
	private static int _UniqueId = 0;
	
	/**
	 * Input this asset has from other assets.
	 */
	public int[] InIds;
	
	/**
	 * Output this asset has to other assets.
	 */
	public int[] OutIds;
	
	public AssetBase(){
		Id = _UniqueId++; 
	}
	
	/**
	 * Get the dimension of this asset.
	 * @return A number representing the size of this assets objects.
	 */
	public abstract int getSize();
	
	/**
	 * Get all the objects from this asset.
	 * @return An array with all the objects.
	 */
	public abstract List<T> getData();

	/**
	 * Get the first element.
	 * @return the first element.
	 */
	public abstract T getElement();
	
	/**
	 * Get the element at position i.
	 * @param i Position in array. Can be to getSize().
	 * @return The object at position i.
	 */
	public abstract T getDataAt(int i);
	
	/**
	 * Set all the objects in this asset to the parameter data.
	 * @param data An array with the necessary objects.
	 */
	public abstract void setData(List<T> data);
	
	/**
	 * Set the first element of the array.
	 * @param data The element to be put in the array.
	 */
	public abstract void setElement(T data);
	
	/**
	 * Set the element at position i.
	 * @param i Position in array of data. Can be to getSize().
	 * @param element The element to put in the array.
	 */
	public abstract void setDataAt(int i, T element);

	/**
	 * Get the id of the object.
	 */
	public final int getId(){
		return Id;
	}
}
