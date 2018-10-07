package com.carolbarbosa.dao;

import com.carolbarbosa.models.Agenda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgendaDAO {
    private List<Agenda> agenda = new ArrayList<Agenda>();

    public List<Agenda> findAll(){
        return this.agenda;
    }

    public void add(Agenda agenda){
        this.agenda.add(agenda);
    }

    public void removeAll(){
        this.agenda = new ArrayList<Agenda>();
    }
}
