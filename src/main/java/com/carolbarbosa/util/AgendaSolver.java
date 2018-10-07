package com.carolbarbosa.util;

import com.carolbarbosa.models.AgendaItem;
import com.carolbarbosa.models.Knapsack;
import com.carolbarbosa.models.Talk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaSolver {

    private final static int TOTAL_MINUTES_DAY = 600;

    public List<AgendaItem> createAgenda(List<Talk> talks, int day){
        List<AgendaItem> agendaItemList = new ArrayList<AgendaItem>();
        Knapsack knapsack = solveKnapsack(talks);

        for(Map.Entry<Integer, Talk> item : knapsack.items.entrySet()) {
            Integer key = item.getKey();
            Talk value = item.getValue();
            agendaItemList.add(new AgendaItem(day,540, 570, value.getTitle(), key));
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
