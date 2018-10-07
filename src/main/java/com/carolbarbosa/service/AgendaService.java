package com.carolbarbosa.service;

import com.carolbarbosa.dao.AgendaDAO;
import com.carolbarbosa.models.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaService {
    @Autowired
    private AgendaDAO agendaDAO;

    public List<Agenda> findAll() {
        return agendaDAO.findAll();
    }

    public void add(Agenda agenda){
        agendaDAO.add(agenda);
    }

    public void removeAll() {
        agendaDAO.removeAll();
    }

    public void setAgenda(List<Agenda> agendaList) { agendaDAO.setAgenda(agendaList); }
}
