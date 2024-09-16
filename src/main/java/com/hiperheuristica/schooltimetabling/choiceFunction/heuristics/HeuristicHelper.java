package com.hiperheuristica.schooltimetabling.choiceFunction.heuristics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class HeuristicHelper {

    @Autowired
    private static final RestTemplate restTemplate = new RestTemplate();;

    public static String startHeuristic(JsonNode solution, String heuristic) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JsonNode> request = new HttpEntity<>(solution, headers);

        return restTemplate.exchange(
                "http://localhost:8080/timetables/" + heuristic,
                HttpMethod.POST,
                request,
                String.class).getBody();
    }

    public static void waitHeuristicExecution() {
        //Faz a execução esperar por 2 minutos e 5 segundos até a heuristica terminar de rodar
        try {
            Thread.sleep(180000); // 2 minutos e 5 segundos
        } catch (InterruptedException e) {
            System.err.println("Erro ao esperar 3 minutos");
        }
    }

    public static JsonNode getSolution(String solutionId) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode newSolution = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<JsonNode> request = new HttpEntity<>(headers);

        try {
            newSolution = objectMapper.readTree(restTemplate.exchange(
                    "http://localhost:8080/timetables/" + solutionId,
                    HttpMethod.GET,
                    request,
                    String.class).getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return newSolution;
    }
}
