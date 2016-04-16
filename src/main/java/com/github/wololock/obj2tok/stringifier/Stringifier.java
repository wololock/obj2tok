package com.github.wololock.obj2tok.stringifier;

public interface Stringifier {
	String convertToString(Object object);
	<T> T createFromString(String str, Class<T> type);
}
