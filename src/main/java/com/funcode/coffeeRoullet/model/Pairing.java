package com.funcode.coffeeRoullet.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Match.class, name = "match"),
        @JsonSubTypes.Type(value = Triad.class, name = "triad")
})
public sealed interface Pairing permits Match, Triad {
    String icebreaker();
}
