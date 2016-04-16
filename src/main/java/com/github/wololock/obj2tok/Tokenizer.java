package com.github.wololock.obj2tok;

import com.github.wololock.obj2tok.stringifier.Stringifier;
import com.github.wololock.obj2tok.tokenizer.TokenCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public class Tokenizer {

	private static final Logger log = LoggerFactory.getLogger(Tokenizer.class);

	private final TokenCoder tokenCoder;
	private final Stringifier stringifier;

	public Tokenizer(TokenCoder tokenCoder, Stringifier stringifier) {
		this.tokenCoder = tokenCoder;
		this.stringifier = stringifier;
	}

	public String toToken(Object object) {
		log.debug("Tokenizer.toToken(object) starts...");

		try {
			String objectAsString = stringifier.convertToString(object);
			log.debug("Object converted to String = {}", objectAsString);

			byte[] encoded = tokenCoder.encode(objectAsString.getBytes("UTF-8"));
			String result = Base64.getEncoder().encodeToString(encoded);
			log.debug("Object encoded to String = {}", result);

			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T fromToken(String token, Class<T> type) {
		log.debug("Tokenizer.fromToken(token, type) starts...");
		log.debug("Input token = {}", token);
		log.debug("Expected object type = {}", type);

		try {
			byte[] bytes = Base64.getDecoder().decode(token);
			byte[] decoded = tokenCoder.decode(bytes);
			String objectAsStirng = new String(decoded, "UTF-8");
			log.debug("Object as String = {}", objectAsStirng);

			T obj = stringifier.createFromString(objectAsStirng, type);
			log.debug("Final object = {}", obj);

			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
