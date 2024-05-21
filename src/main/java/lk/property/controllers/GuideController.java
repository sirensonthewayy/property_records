package lk.property.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guide")
public class GuideController {

    @GetMapping("/statuses")
    public String guideStatuses(){
        return "property/guide_statuses.html";
    }

    @GetMapping("/export_to_excel")
    public String guideExportToExcel(){
        return "property/guide_export_to_excel.html";
    }
}
