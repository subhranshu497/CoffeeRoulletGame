package com.funcode.coffeeRoullet.model;


public record Match(User user1, User user2, String icebreaker) implements Pairing{}
