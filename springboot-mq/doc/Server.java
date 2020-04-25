package com;

public class Server{
	static{
		System.loadLibrary("serverNet");
	}
	
	public static void main(String[] args) throws Exception{
	    conntect();
	}

	public static native void conntect();
}
