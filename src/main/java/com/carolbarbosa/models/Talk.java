package com.carolbarbosa.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Talk {

    private static final long serialVersionUID = 1L;

    private String title;
    private Integer priority;
    private Double duration;

    public Talk(String title, Integer priority, Double duration){
        this.title = title;
        this.priority = priority;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
