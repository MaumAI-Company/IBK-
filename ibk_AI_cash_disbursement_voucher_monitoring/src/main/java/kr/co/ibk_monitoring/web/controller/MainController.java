package kr.co.ibk_monitoring.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {


    @RequestMapping({"/", "/index"})
    public String index(Model model) {

        model.addAttribute("mc", "main");
        return "redirect:/system/monitoring";
    }
}
