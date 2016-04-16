package com.github.wololock.obj2tok.stringifier

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import spock.lang.Specification
import spock.lang.Unroll

class JacksonJSONStringifierSpec extends Specification {

    JacksonJSONStringifier stringifier = new JacksonJSONStringifier()

    @Unroll
    def "should convert #str to #object bidirectionally"() {
        expect:
        object == stringifier.createFromString(str, type)

        and:
        stringifier.convertToString(object) == str

        where:
        str                                         | type      || object
        '{"id":"aaa","num":2}'                      | A         || new A(id: "aaa", num: 2)
        '{"id":"test","num":0}'                     | A         || new A(id: "test", num: 0)
        '{"firstName":"Joe","lastName":"Doe"}'      | B         || new B(firstName: "Joe", lastName: "Doe", test: true)
        '{"firstName":"","lastName":""}'            | B         || new B(firstName: "", lastName: "")
    }

    @ToString(includePackage = false)
    @EqualsAndHashCode
    static class A {
        String id
        int num
    }

    @ToString(includePackage = false)
    @EqualsAndHashCode
    static class B {
        String firstName
        String lastName

        @JsonIgnore
        boolean test = true
    }
}
