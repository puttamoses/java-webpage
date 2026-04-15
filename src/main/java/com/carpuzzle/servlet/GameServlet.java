package com.carpuzzle.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * GameServlet - Handles the main game logic for car ordering puzzle.
 *
 * Routes:
 *   GET  /game          → renders game page (forwards to game.jsp)
 *   POST /game?action=  → handles shuffle, reset, validate
 */
public class GameServlet extends HttpServlet {

    // Car data: id, name, year, speed (km/h), price (USD), svgColor
    private static final List<Map<String, Object>> ALL_CARS = new ArrayList<>();

    static {
        ALL_CARS.add(car(1, "Ferrari 488",   2023, 330, 330000, "#E8001D"));
        ALL_CARS.add(car(2, "Lamborghini Huracán", 2023, 325, 280000, "#F5A800"));
        ALL_CARS.add(car(3, "Porsche 911 GT3", 2023, 318, 190000, "#0A3D91"));
        ALL_CARS.add(car(4, "McLaren 720S",   2023, 341, 300000, "#FF6600"));
        ALL_CARS.add(car(5, "Bugatti Chiron", 2023, 420, 3000000, "#1A1A2E"));
        ALL_CARS.add(car(6, "Aston Martin DB12", 2023, 310, 245000, "#006B3C"));
        ALL_CARS.add(car(7, "Koenigsegg CC850", 2023, 442, 3500000, "#C0C0C0"));
        ALL_CARS.add(car(8, "Pagani Huayra",  2023, 383, 2800000, "#9B59B6"));
    }

    private static Map<String, Object> car(int id, String name, int year,
                                            int speed, int price, String color) {
        Map<String, Object> c = new LinkedHashMap<>();
        c.put("id", id);
        c.put("name", name);
        c.put("year", year);
        c.put("speed", speed);
        c.put("price", price);
        c.put("color", color);
        return c;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(true);

        // Initialize game state if not present
        if (session.getAttribute("cards") == null) {
            initGame(session);
        }

        req.setAttribute("cards", session.getAttribute("cards"));
        req.setAttribute("correct", session.getAttribute("correct"));
        req.setAttribute("moves",   session.getAttribute("moves"));
        req.setAttribute("mode",    session.getAttribute("mode"));
        req.setAttribute("score",   session.getAttribute("score"));

        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(true);
        String action = req.getParameter("action");

        if ("shuffle".equals(action)) {
            String mode = req.getParameter("mode");
            if (mode == null) mode = "speed";
            session.setAttribute("mode", mode);
            initGame(session);

        } else if ("reset".equals(action)) {
            session.invalidate();

        } else if ("validate".equals(action)) {
            validateOrder(req, session);
        }

        resp.sendRedirect(req.getContextPath() + "/game");
    }

    @SuppressWarnings("unchecked")
    private void validateOrder(HttpServletRequest req, HttpSession session) {
        String[] orderedIds = req.getParameterValues("order[]");
        if (orderedIds == null) return;

        String mode = (String) session.getAttribute("mode");
        List<Map<String, Object>> cards = (List<Map<String, Object>>) session.getAttribute("cards");

        // Build user's ordered list
        List<Map<String, Object>> userOrder = new ArrayList<>();
        Map<Integer, Map<String, Object>> cardMap = new HashMap<>();
        for (Map<String, Object> c : cards) cardMap.put((Integer) c.get("id"), c);

        for (String sid : orderedIds) {
            int id = Integer.parseInt(sid);
            if (cardMap.containsKey(id)) userOrder.add(cardMap.get(id));
        }

        // Correct order
        List<Map<String, Object>> correctOrder = new ArrayList<>(userOrder);
        if ("speed".equals(mode)) {
            correctOrder.sort((a, b) -> (Integer) b.get("speed") - (Integer) a.get("speed"));
        } else if ("price".equals(mode)) {
            correctOrder.sort((a, b) -> (Integer) b.get("price") - (Integer) a.get("price"));
        } else {
            correctOrder.sort(Comparator.comparing(a -> (String) a.get("name")));
        }

        boolean correct = true;
        for (int i = 0; i < userOrder.size(); i++) {
            if (!userOrder.get(i).get("id").equals(correctOrder.get(i).get("id"))) {
                correct = false;
                break;
            }
        }

        int moves = (Integer) session.getAttribute("moves") + 1;
        session.setAttribute("moves", moves);
        session.setAttribute("correct", correct);

        if (correct) {
            int score = Math.max(0, 1000 - (moves - 1) * 50);
            session.setAttribute("score", score);
        }
    }

    private void initGame(HttpSession session) {
        List<Map<String, Object>> shuffled = new ArrayList<>(ALL_CARS);
        Collections.shuffle(shuffled);
        session.setAttribute("cards", shuffled);
        session.setAttribute("correct", null);
        session.setAttribute("moves", 0);
        session.setAttribute("score", 0);
        if (session.getAttribute("mode") == null) {
            session.setAttribute("mode", "speed");
        }
    }
}
