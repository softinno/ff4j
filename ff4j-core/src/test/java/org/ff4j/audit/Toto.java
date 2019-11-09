package org.ff4j.audit;

import java.util.HashSet;
import java.util.Set;

public class Toto {

    public static String asStringListWithComma(Set<String> items) {
        return new StringBuilder("('").append(String.join("','", items)).append("')").toString();
    }
    
    public static void main(String[] args) {
        Set<String> s = new HashSet<>();
        s.add("1");
        s.add("2");
        s.add("10");
        System.out.println(asStringListWithComma(s));
    }
}
