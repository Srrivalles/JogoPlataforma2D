package org.example.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gerenciador de High Scores com salvamento persistente
 */
public class HighScoreManager {
    
    private static final String SCORES_FILE = "highscores.json";
    private static final int MAX_SCORES = 10; // Top 10 scores
    
    private List<ScoreEntry> highScores;
    
    public HighScoreManager() {
        this.highScores = new ArrayList<>();
        loadScores();
    }
    
    /**
     * Classe para representar uma entrada de score
     */
    public static class ScoreEntry implements Comparable<ScoreEntry> {
        public String initials;
        public int score;
        public long timestamp;
        
        public ScoreEntry(String initials, int score) {
            this.initials = initials.toUpperCase().substring(0, Math.min(3, initials.length()));
            this.score = score;
            this.timestamp = System.currentTimeMillis();
        }
        
        public ScoreEntry(String initials, int score, long timestamp) {
            this.initials = initials;
            this.score = score;
            this.timestamp = timestamp;
        }
        
        @Override
        public int compareTo(ScoreEntry other) {
            return Integer.compare(other.score, this.score); // Ordem decrescente
        }
        
        @Override
        public String toString() {
            return String.format("%s - %d", initials, score);
        }
    }
    
    /**
     * Adiciona um novo score e retorna a posição no ranking (1-based) ou -1 se não entrou no top
     */
    public int addScore(String initials, int score) {
        ScoreEntry newEntry = new ScoreEntry(initials, score);
        
        // Adicionar e ordenar
        highScores.add(newEntry);
        Collections.sort(highScores);
        
        // Manter apenas os top scores
        if (highScores.size() > MAX_SCORES) {
            highScores = highScores.subList(0, MAX_SCORES);
        }
        
        // Encontrar posição do novo score
        int position = -1;
        for (int i = 0; i < highScores.size(); i++) {
            if (highScores.get(i) == newEntry) {
                position = i + 1; // 1-based position
                break;
            }
        }
        
        // Salvar scores atualizados
        saveScores();
        
        return position;
    }
    
    /**
     * Verifica se um score seria high score
     */
    public boolean isHighScore(int score) {
        if (highScores.size() < MAX_SCORES) {
            return true; // Ainda há espaço no ranking
        }
        return score > highScores.get(highScores.size() - 1).score;
    }
    
    /**
     * Retorna a lista de high scores
     */
    public List<ScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }
    
    /**
     * Retorna o menor score no ranking (para comparação)
     */
    public int getLowestHighScore() {
        if (highScores.isEmpty() || highScores.size() < MAX_SCORES) {
            return 0;
        }
        return highScores.get(highScores.size() - 1).score;
    }
    
    /**
     * Carrega scores do arquivo JSON
     */
    private void loadScores() {
        try {
            File file = new File(SCORES_FILE);
            if (!file.exists()) {
                createDefaultScores();
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                
                String json = jsonContent.toString().trim();
                if (json.isEmpty()) {
                    createDefaultScores();
                    return;
                }
                
                // Parse JSON simples
                parseJsonScores(json);
            }
            
            Collections.sort(highScores);
            
        } catch (IOException e) {
            createDefaultScores();
        }
    }
    
    /**
     * Parse JSON simples para scores
     */
    private void parseJsonScores(String json) {
        try {
            // Remove espaços e quebras de linha
            json = json.replaceAll("\\s+", "");
            
            // Verifica se é um array JSON
            if (!json.startsWith("[") || !json.endsWith("]")) {
                throw new IllegalArgumentException("Formato JSON inválido");
            }
            
            // Remove colchetes
            json = json.substring(1, json.length() - 1);
            
            if (json.isEmpty()) {
                return; // Array vazio
            }
            
            // Divide por objetos (assumindo que cada objeto está separado por vírgula)
            String[] objects = json.split("\\},\\{");
            
            for (int i = 0; i < objects.length; i++) {
                String obj = objects[i];
                
                // Adiciona chaves se necessário
                if (i == 0 && !obj.startsWith("{")) {
                    obj = "{" + obj;
                }
                if (i == objects.length - 1 && !obj.endsWith("}")) {
                    obj = obj + "}";
                }
                if (i > 0 && !obj.startsWith("{")) {
                    obj = "{" + obj;
                }
                if (i < objects.length - 1 && !obj.endsWith("}")) {
                    obj = obj + "}";
                }
                
                parseScoreObject(obj);
            }
            
        } catch (Exception e) {
            // Tenta carregar do formato antigo como fallback
            loadFromOldFormat();
        }
    }
    
    /**
     * Parse um objeto de score individual
     */
    private void parseScoreObject(String obj) {
        try {
            // Extrai initials
            Pattern initialsPattern = Pattern.compile("\"initials\"\\s*:\\s*\"([^\"]+)\"");
            Matcher initialsMatcher = initialsPattern.matcher(obj);
            if (!initialsMatcher.find()) {
                throw new IllegalArgumentException("Iniciais não encontradas");
            }
            String initials = initialsMatcher.group(1);
            
            // Extrai score
            Pattern scorePattern = Pattern.compile("\"score\"\\s*:\\s*(\\d+)");
            Matcher scoreMatcher = scorePattern.matcher(obj);
            if (!scoreMatcher.find()) {
                throw new IllegalArgumentException("Score não encontrado");
            }
            int score = Integer.parseInt(scoreMatcher.group(1));
            
            // Extrai timestamp (opcional)
            long timestamp = System.currentTimeMillis();
            Pattern timestampPattern = Pattern.compile("\"timestamp\"\\s*:\\s*(\\d+)");
            Matcher timestampMatcher = timestampPattern.matcher(obj);
            if (timestampMatcher.find()) {
                timestamp = Long.parseLong(timestampMatcher.group(1));
            }
            
            highScores.add(new ScoreEntry(initials, score, timestamp));
            
        } catch (Exception e) {
        }
    }
    
    /**
     * Fallback para carregar do formato antigo (CSV)
     */
    private void loadFromOldFormat() {
        try {
            File oldFile = new File("highscores.txt");
            if (oldFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(oldFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty()) {
                            try {
                                String[] parts = line.split(",");
                                if (parts.length >= 3) {
                                    String initials = parts[0];
                                    int score = Integer.parseInt(parts[1]);
                                    long timestamp = Long.parseLong(parts[2]);
                                    highScores.add(new ScoreEntry(initials, score, timestamp));
                                }
                            } catch (NumberFormatException e) {
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }
    
    /**
     * Salva scores no arquivo JSON
     */
    private void saveScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORES_FILE))) {
            writer.println("[");
            
            for (int i = 0; i < highScores.size(); i++) {
                ScoreEntry entry = highScores.get(i);
                writer.println("  {");
                writer.println("    \"initials\": \"" + entry.initials + "\",");
                writer.println("    \"score\": " + entry.score + ",");
                writer.println("    \"timestamp\": " + entry.timestamp);
                writer.print("  }");
                
                // Adiciona vírgula se não for o último item
                if (i < highScores.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            
            writer.println("]");
        } catch (IOException e) {
        }
    }
    
    /**
     * Cria scores padrão para começar o ranking
     */
    private void createDefaultScores() {
        highScores.clear();
        highScores.add(new ScoreEntry("AAA", 1000));
        highScores.add(new ScoreEntry("BBB", 800));
        highScores.add(new ScoreEntry("CCC", 600));
        highScores.add(new ScoreEntry("DDD", 400));
        highScores.add(new ScoreEntry("EEE", 200));
        
        Collections.sort(highScores);
        saveScores();
        
        
    }
    
    /**
     * Reseta todos os scores (para debug)
     */
    public void resetScores() {
        createDefaultScores();
    }
    
    /**
     * Mostra ranking no console
     */
    public void printHighScores() {
        for (int i = 0; i < highScores.size(); i++) {
            ScoreEntry entry = highScores.get(i);
        }
    }
}

