package com.github.wololock.obj2tok.examples

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.wololock.obj2tok.Tokenizer
import com.github.wololock.obj2tok.stringifier.JacksonJSONStringifier
import com.github.wololock.obj2tok.stringifier.Stringifier
import com.github.wololock.obj2tok.tokenizer.StaticKeyAESCipherTokenCoder
import com.github.wololock.obj2tok.tokenizer.TokenCoder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Stepwise
class TokenWithExpirationExampleSpec extends Specification {

    private static final String TOKEN_ID = UUID.randomUUID().toString()

    Tokenizer tokenizer
    LocalDateTime createdDate = LocalDateTime.parse("2016-04-06T15:45:23")
    LocalDateTime now = LocalDateTime.parse("2016-04-15T20:00:13")

    @Shared
    String tokenStr

    def setup() {
        ObjectMapper objectMapper = initObjectMapper()
        Stringifier stringifier = new JacksonJSONStringifier(objectMapper)
        TokenCoder tokenCoder = new StaticKeyAESCipherTokenCoder("this_is_secret_key")

        tokenizer = new Tokenizer(tokenCoder, stringifier)
    }

    def "should generate String token that represents object"() {
        given:
        Token token = new Token(id: TOKEN_ID, createdDate: createdDate, ttl: 48)

        when:
        tokenStr = tokenizer.toToken(token)

        then:
        !tokenStr.empty
    }

    def "should generate token object from string"() {
        when:
        Token token = tokenizer.fromToken(tokenStr, Token)

        then:
        token.id == TOKEN_ID

        and:
        token.createdDate == createdDate

        and:
        token.ttl == 48

        and:
        token.expired(now)
    }

    private static ObjectMapper initObjectMapper() {
        SimpleModule module = new SimpleModule()
        module.addDeserializer(LocalDateTime, new JsonDeserializer<LocalDateTime>() {
            @Override
            LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                String dateStr = p.readValueAs(String)
                return LocalDateTime.parse(dateStr)
            }
        })
        module.addSerializer(LocalDateTime, new JsonSerializer<LocalDateTime>() {
            @Override
            void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                String dateStr = value.format(DateTimeFormatter.ISO_DATE_TIME)
                gen.writeString(dateStr)
            }
        })
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.registerModule(module)
        return objectMapper
    }

    static class Token {
        String id
        LocalDateTime createdDate
        int ttl

        boolean expired(LocalDateTime now) {
            return now.isAfter(createdDate.plusHours(ttl))
        }
    }
}
