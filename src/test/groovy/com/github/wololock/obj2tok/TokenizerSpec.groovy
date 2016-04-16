package com.github.wololock.obj2tok

import com.github.wololock.obj2tok.stringifier.JacksonJSONStringifier
import com.github.wololock.obj2tok.tokenizer.StaticKeyAESCipherTokenCoder
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import spock.lang.Specification
import spock.lang.Unroll

class TokenizerSpec extends Specification {

    Tokenizer tokenizer

    def setup() {
        def tokenCoder = new StaticKeyAESCipherTokenCoder("test")
        def stringifier = new JacksonJSONStringifier()
        tokenizer = new Tokenizer(tokenCoder, stringifier)
    }

    @Unroll
    def "should create #person from token #token"() {
        expect:
        tokenizer.toToken(person) == token

        and:
        tokenizer.fromToken(token, Person) == person

        where:
        person                                                  | token
        new Person(name: "Test", age: 20)                       | "dOptXBuoPT3dx12wc4TrIP3xIycKYYlOLHYvj35rRRM="
        new Person(name: "Test", age: 22)                       | "dOptXBuoPT3dx12wc4TrIJwwxN8ztBHy2q0XbgpoVG0="
        new Person(name: "Joe Doe", age: 20)                    | "lKSpR9yClyRgYbrMD8c/Z3bscN24/+quyiew7M1Ptlw="
        new Person(name: "", age: 0)                            | "ZWTbnQ55lVVI+dL+kOjYdIWki79YYJEw4q14qoYYrJw="
        new Person(name: "Very long person name", age: 1024)    | "yf/pl4XtsfcRMrbTHozNOOChrWcb3FRAZA+8+hGZnOu/U9utcsq8HVxvRrz9jcZ+"
        null                                                    | "d9LJvKpeT9cdzNZKcpEEzg=="
    }

    @ToString(includePackage = false, includeNames = true)
    @EqualsAndHashCode
    static class Person {
        String name
        int age
    }
}
