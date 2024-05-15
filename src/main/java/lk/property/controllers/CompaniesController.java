package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.CompanyDAO;
import lk.property.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/company")
public class CompaniesController {

    private final CompanyDAO companyDAO;

    @Autowired
    public CompaniesController(CompanyDAO companyDAO){this.companyDAO = companyDAO;}

    @ModelAttribute
    public void initModel(@PathVariable(required = false, value = "inn") String inn, Model model){
        model.addAttribute("haveActs", companyDAO.haveActs(inn));
    }

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
        try{
            companyDAO.save(company);
        } catch(DuplicateKeyException e){
            bindingResult.rejectValue("inn", "error.inn", "Данный ИНН уже используется");
        }
        if(bindingResult.hasErrors()){
            return "property/new_company.html";
        }
        return "redirect:/company/show_all";
    }

    @GetMapping("/{inn}/edit")
    public String editCompany(@PathVariable("inn") String inn, Model model){
        Company company = companyDAO.showOne(inn);
        if(company == null){
            return "property/404.html";
        } else{
            model.addAttribute("company", companyDAO.showOne(inn));
            return "property/edit_company.html";
        }
    }

    @PatchMapping("/{inn}")
    public String updateCompany(@ModelAttribute("company") @Valid Company company,
                                BindingResult bindingResult,
                                @PathVariable("inn") String inn){
        try {
            companyDAO.update(company, inn);
        } catch(DuplicateKeyException e){
            bindingResult.rejectValue("inn", "error.inn", "Данный ИНН уже используется");
        }
        if(bindingResult.hasErrors()){
            return "property/edit_company.html";
        }
        return "redirect:/company/show_all";
    }

    @DeleteMapping("/{inn}")
    public String deleteCompany(@PathVariable("inn") String inn){
        companyDAO.delete(inn);
        return "redirect:/company/show_all";
    }
}
