package com.carolbarbosa.util;

import com.carolbarbosa.models.AgendaItem;
import com.carolbarbosa.models.Knapsack;
import com.carolbarbosa.models.Talk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaSolver {

    private final static int TOTAL_MINUTES_DAY = 450;

    public List<AgendaItem> createAgenda(List<Talk> talks, int day){
        List<AgendaItem> agendaItemList = new ArrayList<AgendaItem>();
        Knapsack knapsack = solveKnapsack(talks);

        int start = 540; //9h em minutos
        int durationInc = 0; //incrementar soma da duracao das palestras
        int intervalo = 1; //primeiro intervalo

        for(Map.Entry<Integer, Talk> item : knapsack.items.entrySet()) {
            Integer key = item.getKey();
            Talk value = item.getValue();
            durationInc = value.getDuration() + durationInc;

            if(durationInc >= 180 && intervalo == 1){ //se a soma passou de 12 - 9 = 3*60min, tem que fazer intervalo antes
                start = start + 30;
                durationInc = durationInc + 30;
                intervalo = 2;
            }else if(durationInc >= 450 && intervalo == 3){ //se a soma passou de 16h30 - 9 = (7*60)+30min, tem que fazer intervalo antes
                start = start + 30;
                durationInc = durationInc + 30;
                intervalo = 4;
            }
            agendaItemList.add(new AgendaItem(day, start, start + value.getDuration(), value.getTitle(), key));
            start = start + value.getDuration();
            if(durationInc > 240 && durationInc <= 270 && intervalo == 2){
                start = start + 90;
                durationInc = durationInc + 90;
                intervalo = 3;
            }else if(durationInc > 270 && intervalo == 2){ //passou de 13h30, terceiro intervalo vai acabar dps das 15h
                intervalo = 3;
            }
        }
        if(durationInc < 540){ //acabou antes das 18h
            int duration = agendaItemList.get(agendaItemList.size() - 1).getEnd() - agendaItemList.get(agendaItemList.size() - 1).getStart();
            agendaItemList.get(agendaItemList.size() - 1).setEnd(1140);
            agendaItemList.get(agendaItemList.size() - 1).setStart(1140 - duration);
            //19h = 1140 min
        }
        return agendaItemList;
    }

    public Knapsack solveKnapsack(List<Talk> talks){
        int count = talks.size();
        //capacidade da mochila eh total de minutos 9h ate 19h = 600minutos
        int[][] matrix = new int[count + 1][TOTAL_MINUTES_DAY + 1];
        for (int i = 0; i <= TOTAL_MINUTES_DAY; i++){ //zera primeira linha
            matrix[0][i] = 0;
        }
        //o peso de cada item sera a duracao da palestra
        for (int i = 1; i <= count; i++) {
            for (int j = 0; j <= TOTAL_MINUTES_DAY; j++) {
                if (talks.get(i - 1).getDuration() > j){
                    matrix[i][j] = matrix[i-1][j];
                }
                else{
                    matrix[i][j] = Math.max(matrix[i-1][j], matrix[i-1][j - talks.get(i - 1).getDuration()]
                            + talks.get(i - 1).getPriority());
                }
            }
        }
        return createKnapsack(matrix, talks);
    }

    public Knapsack createKnapsack(int[][] matrix, List<Talk> talks){
        Map<Integer,Talk> itemsSolution = new HashMap<Integer, Talk>();
        int count = talks.size();
        int res = matrix[count][TOTAL_MINUTES_DAY];
        int w = TOTAL_MINUTES_DAY;

        for (int i = count; i > 0  &&  res > 0; i--) {
            if (res != matrix[i-1][w]) {
                itemsSolution.put(i - 1, talks.get(i - 1));
                res -= talks.get(i - 1).getPriority();
                w -= talks.get(i - 1).getDuration();
            }
        }
        return new Knapsack(itemsSolution, matrix[count][TOTAL_MINUTES_DAY]);
    }
}
