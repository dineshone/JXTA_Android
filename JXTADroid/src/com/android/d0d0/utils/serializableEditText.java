/**
 * 
 */
package com.android.d0d0.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;

import android.text.SpannableStringBuilder;
import android.widget.EditText;

/**
 * @author d0d0
 *
 */

public class serializableEditText implements Serializable {
	
	private EditText myEditText;
	

	
	// being a lazy man, I chose 1 as my serial Version ID
	private static final long serialVersionUID = 1L;

	
	public void setEdiTtext(EditText editText)
		{
			myEditText = editText;
		}
	
	private void writeObject(ObjectOutputStream stream)
	{
		try {
			stream.writeObject(myEditText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void readObject(ObjectInputStream stream)
	{
		try {
			myEditText = (EditText) stream.readObject();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


