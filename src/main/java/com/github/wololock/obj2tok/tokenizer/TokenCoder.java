package com.github.wololock.obj2tok.tokenizer;

public interface TokenCoder {
	byte[] encode(byte[] bytes);
	byte[] decode(byte[] bytes);
}
