package com.carpuzzle.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ScoreServlet - REST API for leaderboard scores.
 * GET  /api/score  → returns JSON list of top scores
 * POST /api/score  → accepts JSON {name, score, mode, moves}
 */
public class ScoreServlet extends HttpServlet {

    private static final List<Map<String, Object>> LEADERBOARD = new CopyOnWriteArrayList<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void init() {
        // Seed with example scores
        addScore("Speed Racer", 950, "speed", 2);
        addScore("PuzzlePro",   850, "price", 4);
        addScore("CarMaster",   900, "name",  3);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        List<Map<String, Object>> top = new ArrayList<>(LEADERBOARD);
        top.sort((a, b) -> (Integer) b.get("score") - (Integer) a.get("score"));
        List<Map<String, Object>> top10 = top.subList(0, Math.min(10, top.size()));

        MAPPER.writeValue(resp.getOutputStream(), top10);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = MAPPER.readValue(req.getInputStream(), Map.class);
            String name = (String) body.getOrDefault("name", "Anonymous");
            int score    = (Integer) body.getOrDefault("score", 0);
            String mode  = (String) body.getOrDefault("mode", "speed");
            int moves    = (Integer) body.getOrDefault("moves", 0);
            addScore(name, score, mode, moves);
            resp.setStatus(201);
            MAPPER.writeValue(resp.getOutputStream(),
                Map.of("status", "saved", "score", score));
        } catch (Exception e) {
            resp.setStatus(400);
            MAPPER.writeValue(resp.getOutputStream(),
                Map.of("error", e.getMessage()));
        }
    }

    private static void addScore(String name, int score, String mode, int moves) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("name",  name);
        entry.put("score", score);
        entry.put("mode",  mode);
        entry.put("moves", moves);
        entry.put("date",  new Date().toString());
        LEADERBOARD.add(entry);
    }
}
