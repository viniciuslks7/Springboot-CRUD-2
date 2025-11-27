package br.com.vinicius.oficina3d.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("Erro global capturado", e);
        model.addAttribute("error", e.getClass().getSimpleName());
        model.addAttribute("message", e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        model.addAttribute("trace", sw.toString());
        return "error";
    }
}