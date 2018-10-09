package com.carolbarbosa.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Talk implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private Integer priority;
    private Integer duration;
    private Boolean isOnAgenda;

    public Talk(String title, Integer duration, Integer priority, Boolean isOnAgenda){
        this.title = title;
        this.duration = duration;
        this.priority = priority;
        this.isOnAgenda = isOnAgenda;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
