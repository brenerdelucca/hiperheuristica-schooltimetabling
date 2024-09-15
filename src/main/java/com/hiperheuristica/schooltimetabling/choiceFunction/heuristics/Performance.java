package com.hiperheuristica.schooltimetabling.choiceFunction.heuristics;

import lombok.Data;

@Data
public class Performance {
    private Integer hard;
    private Integer soft;

    public Performance(int hard, int soft){
        this.hard = hard;
        this.soft = soft;
    }

    public static Performance of(String performance){

        //hard/soft
        String[] criterios = performance.split("/");

        int hard = Integer.parseInt(criterios[0].replace("hard", ""));
        int soft = Integer.parseInt(criterios[1].replace("soft", ""));

        return new Performance(hard, soft);
    }
}
