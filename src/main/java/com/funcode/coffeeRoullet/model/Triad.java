package com.funcode.coffeeRoullet.model;

public record Triad(User user1, User user2, User user3, String icebreaker) implements Pairing{}
