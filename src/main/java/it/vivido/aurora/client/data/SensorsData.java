package it.vivido.aurora.client.data;

import java.io.Serializable;

public class SensorsData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String key_ = "";
	private String value_ = "";
	
	public SensorsData(String key, String value)
	{
		this.key_ = key;
		this.value_ = value;
	}
	
	public String getKey() { return key_;}
	public String getValue() {return value_;}
}
