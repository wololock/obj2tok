package com.github.wololock.obj2tok.tokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class StaticKeyAESCipherTokenCoder implements TokenCoder {
	private static final String ALGORITHM = "AES";

	private final String key;
	private final Cipher encoder;
	private final Cipher decoder;

	public StaticKeyAESCipherTokenCoder(String key) {
		this.key = key;

		SecretKey secretKey = getSecretKey(key);

		this.encoder = initializeCipher(secretKey, Cipher.ENCRYPT_MODE);
		this.decoder = initializeCipher(secretKey, Cipher.DECRYPT_MODE);
	}

	private SecretKey getSecretKey(String key) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return new SecretKeySpec(Arrays.copyOf(md.digest(key.getBytes()), 16), ALGORITHM);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Cipher initializeCipher(SecretKey secretKey, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(mode, secretKey);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getKey() {
		return key;
	}

	@Override
	public byte[] encode(byte[] bytes) {
		try {
			return encoder.doFinal(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] decode(byte[] bytes) {
		try {
			return decoder.doFinal(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
