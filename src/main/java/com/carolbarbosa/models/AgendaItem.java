package com.carolbarbosa.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AgendaItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int day;
    private int idTalk;
    private Integer start;
    private Integer end;
    private String title;

    public AgendaItem(int day, Integer start, Integer end, String title, int idTalk){
        this.day = day;
        this.start = start;
        this.end = end;
        this.title = title;
        this.idTalk = idTalk;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
