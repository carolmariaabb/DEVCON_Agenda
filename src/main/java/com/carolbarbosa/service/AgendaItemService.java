package com.carolbarbosa.service;

import com.carolbarbosa.dao.AgendaItemDAO;
import com.carolbarbosa.models.AgendaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaItemService {
    @Autowired
    private AgendaItemDAO agendaItemDAO;

    public List<AgendaItem> findAll() {
        return agendaItemDAO.findAll();
    }

    public void add(AgendaItem agendaItem){
        agendaItemDAO.add(agendaItem);
    }

    public void removeAll() {
        agendaItemDAO.removeAll();
    }

    public void setAgenda(List<AgendaItem> agendaItemList) { agendaItemDAO.setAgendaItems(agendaItemList); }
}
