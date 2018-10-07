package com.carolbarbosa.dao;

import com.carolbarbosa.models.AgendaItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgendaItemDAO {
    private List<AgendaItem> agendaItems = new ArrayList<AgendaItem>();

    public List<AgendaItem> findAll(){
        return this.agendaItems;
    }

    public void add(AgendaItem agendaItem){
        this.agendaItems.add(agendaItem);
    }

    public void removeAll(){
        this.agendaItems = new ArrayList<AgendaItem>();
    }

    public void setAgendaItems(List<AgendaItem> agendaItemList) {
        this.agendaItems = null;
        this.agendaItems = agendaItemList;
    }
}
