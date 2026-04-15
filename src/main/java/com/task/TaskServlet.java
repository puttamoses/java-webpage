package com.task;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<String> tasks = (List<String>) session.getAttribute("tasks");

        if (tasks == null) {
            tasks = new ArrayList<>();
            session.setAttribute("tasks", tasks);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            json.append("\"").append(escapeJson(tasks.get(i))).append("\"");
            if (i < tasks.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        response.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String task = request.getParameter("task");

        HttpSession session = request.getSession();
        List<String> tasks = (List<String>) session.getAttribute("tasks");

        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        if (task != null && !task.trim().isEmpty()) {
            tasks.add(task.trim());
        }

        session.setAttribute("tasks", tasks);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String indexParam = request.getParameter("index");

        HttpSession session = request.getSession();
        List<String> tasks = (List<String>) session.getAttribute("tasks");

        if (tasks != null && indexParam != null) {
            try {
                int index = Integer.parseInt(indexParam);
                if (index >= 0 && index < tasks.size()) {
                    tasks.remove(index);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        session.setAttribute("tasks", tasks);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
