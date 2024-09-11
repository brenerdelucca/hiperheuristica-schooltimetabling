package com.hiperheuristica.schooltimetabling.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class HiperHeuristicService {

    @Autowired
    private ResourceLoader resourceLoader;

    private final static String TIME_TABLE_ALGORITHM_SOLVER_DIRECTORY = "timetableAlgorithmSolverDirectory";
    private final static String HIPER_HEURISTIC_SOLVERS_DIRECTORY = "hiperHeuristicSolversDirectory";

    public void start(){
        changeSolver("lateAcceptance");
    }

    public void changeSolver(String nextSolver) {
        Map<String, String> directoryPaths = getDirectoryPaths();

        // Especifica o caminho do arquivo que você deseja deletar
        Path origem = Paths.get((directoryPaths.get(HIPER_HEURISTIC_SOLVERS_DIRECTORY) + "/" + nextSolver + ".xml"));
        Path destino = Paths.get((directoryPaths.get(TIME_TABLE_ALGORITHM_SOLVER_DIRECTORY) + "/solverConfig.xml"));

        try {
            // Move o arquivo para o novo diretório
            Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Solver alterado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao alterar solver.");
            e.printStackTrace();
        }
    }

    public Map<String, String> getDirectoryPaths(){
        Map<String, String> paths = new HashMap<>();

        try {
            // Carrega o arquivo config.txt da pasta resources
            Resource resource = resourceLoader.getResource("classpath:directoryPaths.txt");

            // Abre o arquivo e lê linha por linha
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                // Separa cada linha pelo símbolo '='
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    paths.put(key, value);  // Adiciona ao mapa
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paths;
    }

}
