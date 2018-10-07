package com.carolbarbosa.service;

import com.carolbarbosa.dao.TalkDAO;
import com.carolbarbosa.models.Talk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TalkService {

    @Autowired
    private TalkDAO talkDAO;

    public List<Talk> findAll() {
        return talkDAO.findAll();
    }

    public void add(Talk talk){
        talkDAO.add(talk);
    }

    public void removeAll() {
        talkDAO.removeAll();
    }
}
