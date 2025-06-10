package com.garage.qr.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/garage")
public class WebController {

    @GetMapping("/")
    public String getHomepage(Model model) {
        model.addAttribute("appName", "Garage Tool Manager");
        return "index";
    }

    @GetMapping("/tools")
    public String getToolsPage(Model model) {
        // TODO: Get Tools from service
        return "tools";
    }

    @GetMapping("/qr-scanner")
    public String getQrScannerPage() {
        return "qr-scanner";
    }
}
