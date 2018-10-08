package com.carolbarbosa.util;

import com.carolbarbosa.models.AgendaItem;
import com.carolbarbosa.models.Knapsack;
import com.carolbarbosa.models.Talk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Gerando agenda a partir do algoritmo knapsack
 */
public class AgendaSolver {

    private Integer startTalk = 540; //inicio do dia = 9h em minutos
    private Integer durationInc = 0; //incrementar o total de minutos de palestras no dia

    public List<AgendaItem> createAgenda(List<Talk> talks, int day){
        //intervalos divide o dia em 4 partes - 4 mochilas com capacidades diferentes
        List<AgendaItem> agendaItemList = new ArrayList<AgendaItem>();

        //primeiro intervalo deve acontecer antes das 12h; 12 - 9 = 3h = 180min
        Knapsack knapsack1 = solveKnapsack(talks, 179);
        createAgendaItens(knapsack1, 30, agendaItemList, day);
        talks = new ArrayList<>(talks.stream().filter(not(Talk::getIsOnAgenda)).collect(Collectors.toList()));

        //segundo intervalo deve acontecer ate 13h30, pra n acabar dps das 15; e nao comecar antes das 13h
        Knapsack knapsack2 = solveKnapsack(talks, 270 - durationInc);
        createAgendaItens(knapsack2, 90, agendaItemList, day);
        talks = new ArrayList<>(talks.stream().filter(not(Talk::getIsOnAgenda)).collect(Collectors.toList()));

        //ultimo da lista acabou antes das 13h
        if(agendaItemList.get(agendaItemList.size() - 1).getEnd() < 780){
            int duration = agendaItemList.get(agendaItemList.size() - 1).getEnd() - agendaItemList.get(agendaItemList.size() - 1).getStart();
            agendaItemList.get(agendaItemList.size() - 1).setEnd(780);
            agendaItemList.get(agendaItemList.size() - 1).setStart(780 - duration);
            durationInc = 240 + 90; //intervalo redefinido para 13h
            startTalk = 780 + 90;
        }

        //terceiro intervalo deve acontecer antes das 16h30; 16h30 - 9 = 7h30min = 450min
        Knapsack knapsack3 = solveKnapsack(talks, 449 - durationInc);
        createAgendaItens(knapsack3, 30, agendaItemList, day);
        talks = new ArrayList<>(talks.stream().filter(not(Talk::getIsOnAgenda)).collect(Collectors.toList()));

        //segundo intervalo deve acontecer ate 19h; e nao comecar antes das 18h
        Knapsack knapsack4 = solveKnapsack(talks, 600 - durationInc);
        createAgendaItens(knapsack4, 0, agendaItemList, day);

        //ultimo da lista acabou antes das 18h
        if(agendaItemList.get(agendaItemList.size() - 1).getEnd() < 1080){
            int duration = agendaItemList.get(agendaItemList.size() - 1).getEnd() - agendaItemList.get(agendaItemList.size() - 1).getStart();
            agendaItemList.get(agendaItemList.size() - 1).setEnd(1080);
            agendaItemList.get(agendaItemList.size() - 1).setStart(1080 - duration);
        }
        return agendaItemList;
    }

    public void createAgendaItens(Knapsack knapsack, int breakDuration, List<AgendaItem> agendaItemList, int day){
        for(Map.Entry<Integer, Talk> item : knapsack.items.entrySet()) {
            Integer key = item.getKey();
            Talk value = item.getValue();
            agendaItemList.add(new AgendaItem(day, startTalk, startTalk + value.getDuration(), value.getTitle(), key));
            startTalk = startTalk + value.getDuration();
            durationInc = durationInc + value.getDuration();
        }
        startTalk = startTalk + breakDuration;
        durationInc = durationInc + breakDuration;
    }

    public Knapsack solveKnapsack(List<Talk> talks, int capactity){
        int count = talks.size();
        int[][] matrix = new int[count + 1][capactity + 1];
        for (int i = 0; i <= capactity; i++){ //zera primeira linha
            matrix[0][i] = 0;
        }
        //o peso de cada item sera a duracao da palestra
        for (int i = 1; i <= count; i++) {
            for (int j = 0; j <= capactity; j++) {
                if (talks.get(i - 1).getDuration() > j){
                    matrix[i][j] = matrix[i-1][j];
                }
                else{
                    matrix[i][j] = Math.max(matrix[i-1][j], matrix[i-1][j - talks.get(i - 1).getDuration()]
                            + talks.get(i - 1).getPriority());
                }
            }
        }
        return createKnapsack(matrix, talks, capactity);
    }

    public Knapsack createKnapsack(int[][] matrix, List<Talk> talks, int capactity){
        Map<Integer,Talk> itemsSolution = new HashMap<Integer, Talk>();
        int count = talks.size();
        int res = matrix[count][capactity];
        int w = capactity;

        for (int i = count; i > 0  &&  res > 0; i--) {
            if (res != matrix[i-1][w]) {
                itemsSolution.put(i - 1, talks.get(i - 1));
                talks.get(i - 1).setIsOnAgenda(true);
                res -= talks.get(i - 1).getPriority();
                w -= talks.get(i - 1).getDuration();
            }
        }
        return new Knapsack(itemsSolution, matrix[count][capactity]);
    }

    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}
