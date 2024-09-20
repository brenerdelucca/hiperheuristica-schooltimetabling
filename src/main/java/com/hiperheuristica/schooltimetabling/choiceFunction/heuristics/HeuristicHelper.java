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

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HeuristicHelper {

    @Autowired
    private static final RestTemplate restTemplate = new RestTemplate();;

    public static String startHeuristic(JsonNode solution, String heuristic) {
        System.out.println("Iniciando execução do " + heuristic + ".");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JsonNode> request = new HttpEntity<>(solution, headers);

        String jobId = restTemplate.exchange(
                "http://localhost:8080/timetables/" + heuristic,
                HttpMethod.POST,
                request,
                String.class).getBody();

        System.out.println("JobId: " + jobId);

        //Adiciona jobId e heuristica no CSV
        try (FileWriter writer = new FileWriter("src/main/resources/csvs/log.csv", true)) {
            writer.append(jobId)
                    .append(";")
                    .append(heuristic)
                    .append(";");
        } catch (IOException e) {
            System.err.println("Erro ao adicionar jobId e heuristica no CSV: " + e.getMessage());
        }

        return jobId;
    }

    public static void waitHeuristicExecution() {
        //Faz a execução esperar
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            System.err.println("Erro ao esperar 3 minutos");
        }
    }

    public static JsonNode getSolution(String solutionId, String heuristic) {
        System.out.println("Execução do " + heuristic + " finalizada.");

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

        //Adiciona a melhor performance da heuristica no CSV
        try (FileWriter writer = new FileWriter("src/main/resources/csvs/log.csv", true)) {
            writer.append(newSolution.get("score").asText() + ";");
        } catch (IOException e) {
            System.err.println("Erro ao adicionar performance no CSV: " + e.getMessage());
        }

        return newSolution;
    }
}
