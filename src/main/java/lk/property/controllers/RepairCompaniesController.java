package lk.property.controllers;

import lk.property.dao.RepairCompaniesDAO;
import lk.property.models.RepairCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/repair_companies")
public class RepairCompaniesController {

    private final RepairCompaniesDAO repairCompaniesDAO;

    @Autowired
    public RepairCompaniesController(RepairCompaniesDAO repairCompaniesDAO) {
        this.repairCompaniesDAO = repairCompaniesDAO;
    }

    @GetMapping("/show_all")
    public String showAll(Model model) throws SQLException {
        model.addAttribute("repairCompanies", repairCompaniesDAO.showAll());
        return "property/repair_companies.html";
    }

    @GetMapping("/new")
    public String newRepairCompany(Model model){
        model.addAttribute("repairCompany", new RepairCompany());
        return "property/new_repair_company.html";
    }

    @PostMapping()
    public String createRepairCompany(RepairCompany repairCompany){
        repairCompaniesDAO.save(repairCompany);
        return "redirect:/repair_companies/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editRepairCompany(@PathVariable("id") Integer id, Model model){
        RepairCompany repairCompany = repairCompaniesDAO.showOne(id);
        if(repairCompany == null){
            return "property/404.html";
        } else{
            model.addAttribute("repairCompany", repairCompany);
            return "property/edit_repair_company.html";
        }
    }

    @PatchMapping("/{id}")
    public String updateRepairCompany(@ModelAttribute("repairCompany") RepairCompany repairCompany,
                                      @PathVariable("id") Integer id){
        repairCompaniesDAO.update(id, repairCompany);
        return "redirect:/repair_companies/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteRepairCompany(@PathVariable("id") Integer id){
        repairCompaniesDAO.delete(id);
        return "redirect:/repair_companies/show_all";
    }
}
