package com.game;

import java.io.IOException;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/guess")
public class GuessServlet extends HttpServlet {

    private int number = new Random().nextInt(100) + 1;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int guess = Integer.parseInt(request.getParameter("number"));
        String message;

        if (guess < number) {
            message = "Too low!";
        } else if (guess > number) {
            message = "Too high!";
        } else {
            message = "Correct! 🎉";
        }

        request.setAttribute("result", message);
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}