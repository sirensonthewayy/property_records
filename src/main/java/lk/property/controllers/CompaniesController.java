package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.CompanyDAO;
import lk.property.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/company")
public class CompaniesController {

    private final CompanyDAO companyDAO;

    @Autowired
    public CompaniesController(CompanyDAO companyDAO){this.companyDAO = companyDAO;}

    @GetMapping("/show_all")
    public String showAll(Model model) throws SQLException {
        model.addAttribute("companies", companyDAO.showAll());
        return "property/companies.html";
    }

    @GetMapping("/{new}")
    public String newCompany(Model model){
        model.addAttribute("company", new Company());
        return "property/new_company.html";
    }

    @PostMapping()
    public String createCompany(@ModelAttribute("company") @Valid Company company,
                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "property/new_company.html";
        }
        companyDAO.save(company);
        return "redirect:/company/show_all";
    }

    @GetMapping("/{inn}/edit")
    public String editCompany(@PathVariable("inn") String inn, Model model){
        model.addAttribute("company", companyDAO.showOne(inn));
        model.addAttribute("haveActs", companyDAO.haveActs(inn));
        return "property/edit_company.html";
    }

    @PatchMapping("/{inn}")
    public String updateCompany(@PathVariable("inn") String inn,
                                @ModelAttribute("company") @Valid Company company,
                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "property/edit_company.html";
        }
        companyDAO.update(company, inn);
        return "redirect:/company/show_all";
    }

    @DeleteMapping("/{inn}")
    public String deleteCompany(@PathVariable("inn") String inn){
        companyDAO.delete(inn);
        return "redirect:/company/show_all";
    }
}
