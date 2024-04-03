package it.unipd.dei.cyclek.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();
        out.printf("<!DOCTYPE html>%n");

        out.printf("<html lang=\"en\">%n");
        out.printf("<head>%n");
        out.printf("<meta charset=\"utf-8\">%n");
        out.printf("<title>HelloWorld Servlet Response</title>%n");
        out.printf("</head>%n");

        out.printf("<body>%n");
        out.printf("<h1>HelloWorld Servlet Response</h1>%n");
        out.printf("<hr/>%n");
        out.printf("<p>%n");
        out.printf("Hello, world!%n");
        out.printf("</p>%n");
        out.printf("</body>%n");

        out.printf("</html>%n");

        out.flush();
        out.close();
    }
}
