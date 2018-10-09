package com.carolbarbosa.models;

import java.util.List;
import java.util.Map;

public class Knapsack {
    public Map<Integer,Talk> items;
    public int value;

    public Knapsack(Map<Integer,Talk> items, int value) {
        this.items = items;
        this.value = value;
    }
}
