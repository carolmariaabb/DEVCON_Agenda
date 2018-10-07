package com.carolbarbosa.dao;

import com.carolbarbosa.models.Talk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TalkDAO {
    private List<Talk> talks = new ArrayList<Talk>();

    public List<Talk> findAll(){
        return this.talks;
    }

    public void add(Talk talk){
        this.talks.add(talk);
    }

    public void removeAll(){
        this.talks = new ArrayList<Talk>();
    }

}
