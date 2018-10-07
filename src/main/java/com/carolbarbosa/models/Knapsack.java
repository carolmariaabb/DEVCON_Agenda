package com.carolbarbosa.models;

import java.util.List;

public class Knapsack {
    public List<Talk> items;
    public int value;

    public Knapsack(List<Talk> items, int value) {
        this.items = items;
        this.value = value;
    }
}
