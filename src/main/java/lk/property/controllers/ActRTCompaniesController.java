package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.ActRTCompaniesDAO;
import lk.property.dao.CompanyDAO;
import lk.property.dao.DeviceDAO;
import lk.property.models.ActRTCompanies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;


@Controller
@RequestMapping("/companies_act")
public class ActRTCompaniesController {

    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    private final ActRTCompaniesDAO actRTCompaniesDAO;
    private final DeviceDAO deviceDAO;
    private final CompanyDAO companyDAO;

    @Autowired
    public ActRTCompaniesController(ActRTCompaniesDAO actRTCompaniesDAO, DeviceDAO deviceDAO, CompanyDAO companyDAO) {
        this.actRTCompaniesDAO = actRTCompaniesDAO;
        this.deviceDAO = deviceDAO;
        this.companyDAO = companyDAO;
    }

    @ModelAttribute
    public void initModel(Model model){
        model.addAttribute("companyAct", new ActRTCompanies());
        model.addAttribute("companies", companyDAO.showAll());
        model.addAttribute("devicesInStock", deviceDAO.showAllByStatuses(new String[]{"Работает (на складе)"}));
        model.addAttribute("workingDevices", deviceDAO.showAllByStatuses(new String[]{"Работает (на складе)", "Работает (у клиента)"}));
        model.addAttribute("workingOnClient", deviceDAO.showAllByStatuses(new String[]{"Работает (у клиента)"}));
    }

    @GetMapping("/show_all")
    public String showAll(Model model){
        model.addAttribute("companiesActs", actRTCompaniesDAO.showAll());
        return "property/acts_rt_companies.html";
    }

    @GetMapping("/new_transmission")
    public String newTransmissionAct(){
        return "property/new_act_rt_companies_transmission.html";
    }

/*    @GetMapping("/{id}/new_reception")
    public String newReceptionAct(Model model, @PathVariable("id") Integer id){
        model.addAttribute("companyAct", actRTCompaniesDAO.showOne(id));
        return "property/new_act_rt_companies_reception.html";
    }*/

    @PostMapping()
    public String createAct(@ModelAttribute("companyAct") @Valid ActRTCompanies actRTCompanies,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "property/new_act_rt_companies_transmission.html";
        }
        actRTCompaniesDAO.save(actRTCompanies);
        return "redirect:/companies_act/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editAct(@PathVariable("id") Integer id, Model model){
        model.addAttribute("companyAct", actRTCompaniesDAO.showOne(id));
        model.addAttribute("companies", companyDAO.showAll());
        return "property/edit_act_rt_companies.html";
    }

    @PatchMapping("/{id}")
    public String updateAct(@PathVariable("id") Integer id,
                            @ModelAttribute("companyAct") @Valid ActRTCompanies actRTCompanies,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "property/edit_act_rt_companies.html";
        }
        actRTCompaniesDAO.update(id, actRTCompanies);
        return "redirect:/companies_act/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteAct(@PathVariable("id") Integer id){
        actRTCompaniesDAO.delete(id);
        return "redirect:/companies_act/show_all";
    }
}
