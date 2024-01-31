package com.efjpr.rejob.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StopWords {
    private static final String[] PORTUGUESE_STOPWORDS = {
            "a", "o", "e", "do", "da", "em", "as", "os", "das", "dos", "ao", "aos", "à", "às", "pelo", "pela", "pelos", "pelas",
            "no", "na", "nos", "nas", "num", "numa", "nuns", "numas", "por", "para", "pelas", "com", "sem", "como", "mais", "menos",
            "mas", "ou", "se", "porque", "que", "por", "quando", "onde", "quem", "qual", "quais", "esse", "essa", "esses", "essas", "este", "esta",
            "estes", "estas", "isso", "aquilo", "aquele", "aquela", "aqueles", "aquelas", "ser", "estar", "foi", "foram", "são", "seu",
            "sua", "seus", "suas", "nosso", "nossa", "nossos", "nossas"
    };

    public static Set<String> getPortugueseStopwords() {
        return new HashSet<>(Arrays.asList(PORTUGUESE_STOPWORDS));
    }
}