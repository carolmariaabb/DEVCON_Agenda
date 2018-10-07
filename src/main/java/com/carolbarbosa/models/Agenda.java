package com.carolbarbosa.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    private int day;
    private Integer start;
    private Integer end;
    private String title;

    public Agenda(int day, Integer start, Integer end, String title){
        this.day = day;
        this.start = start;
        this.end = end;
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
