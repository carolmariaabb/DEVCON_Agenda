package com.carolbarbosa.util;

import com.carolbarbosa.models.Agenda;
import com.carolbarbosa.models.Knapsack;
import com.carolbarbosa.models.Talk;

import java.util.ArrayList;
import java.util.List;

public class AgendaSolver {

    private final static int TOTAL_MINUTES_DAY = 600;

    public List<Agenda> createAgenda(List<Talk> talks, int day){
        List<Agenda> agendaList = new ArrayList<Agenda>();
        int[][] matrix = createMatrix(talks);
        Knapsack knapsack = createKnapsack(matrix, talks);

        for(Talk t : knapsack.items){
            Agenda agenda = new Agenda(day,0, 0, t.getTitle());
            agendaList.add(agenda);
        }
        return agendaList;
    }

    public int[][] createMatrix(List<Talk> talks){
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
        return matrix;
    }

    public Knapsack createKnapsack(int[][] matrix, List<Talk> talks){
        int count = talks.size();
        int res = matrix[count][TOTAL_MINUTES_DAY];
        int w = TOTAL_MINUTES_DAY;
        List<Talk> itemsSolution = new ArrayList<>();

        for (int i = count; i > 0  &&  res > 0; i--) {
            if (res != matrix[i-1][w]) {
                itemsSolution.add(talks.get(i - 1));
                res -= talks.get(i - 1).getPriority();
                w -= talks.get(i - 1).getDuration();
            }
        }
        return new Knapsack(itemsSolution, matrix[count][TOTAL_MINUTES_DAY]);
    }
}
