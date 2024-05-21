package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.*;
import lk.property.models.ActOfCompletedWork;
import lk.property.models.ActRTCompanies;
import lk.property.models.ActRTRepair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/completed_work_act")
public class ActOfCompletedWorkController {

    private final ActOfCompletedWorkDAO actOfCompletedWorkDAO;
    private final ActRTRepairDAO actRTRepairDAO;
    private final ActDAO actDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    public ActOfCompletedWorkController(ActOfCompletedWorkDAO actOfCompletedWorkDAO, ActRTRepairDAO actRTRepairDAO, ActDAO actDAO) {
        this.actOfCompletedWorkDAO = actOfCompletedWorkDAO;
        this.actRTRepairDAO = actRTRepairDAO;
        this.actDAO = actDAO;
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false, value = "id") Integer id,
                          Model model){
        model.addAttribute("allActsRTRepair", actRTRepairDAO.showAllCurrent());
        ActOfCompletedWork editActOfCompletedWork = actOfCompletedWorkDAO.showOne(id);
        model.addAttribute("editActOfCompletedWork", editActOfCompletedWork);
        if(editActOfCompletedWork == null){
            model.addAttribute("thisActIsLast", false);
        } else{
            ActOfCompletedWork checkLastActOfCompletedWork = actOfCompletedWorkDAO.showOne(actDAO.showLastAct(editActOfCompletedWork.getActRTRepair().getDevice().getInventoryCard()).getId());
            model.addAttribute("thisActIsLast", editActOfCompletedWork.equals(checkLastActOfCompletedWork));
        }
    }

    @GetMapping("/show_all")
    public String showAll(Model model){
        model.addAttribute("actsOfCompletedWork", actOfCompletedWorkDAO.showAll());
        return "property/acts_of_completed_work.html";
    }

    @GetMapping("/new")
    public String newActOfCompletedWork(Model model){
        model.addAttribute("newActOfCompletedWork", new ActOfCompletedWork());
        return "property/new_act_of_completed_work.html";
    }

    @PostMapping()
    public String createActOfCompletedWork(@ModelAttribute("newActOfCompletedWork") @Valid ActOfCompletedWork actOfCompletedWork,
                                           BindingResult bindingResult){
/*        try{
            actOfCompletedWorkDAO.save(actOfCompletedWork);
        } catch(DataIntegrityViolationException e){
            if(e.getMessage().contains("Акты_выполненных_работ_check")){
                bindingResult.rejectValue("dateOfReception", "error.dateOfReception", "Приемка не может быть раньше передачи");
            }
        }*/
        try{
            actOfCompletedWorkDAO.save(actOfCompletedWork);
        } catch(DataIntegrityViolationException e){
            bindingResult.rejectValue("dateOfReception", "error.dateOfReception", "Дата приемки должна быть позже даты передачи из акта приема-передачи");
        }
        if(bindingResult.hasErrors()){
            return "property/new_act_of_completed_work.html";
        }
        return "redirect:/completed_work_act/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editActOfCompletedWork(@PathVariable("id") Integer id, Model model){
        if(actOfCompletedWorkDAO.showOne(id) == null){
            return "property/404.html";
        } else{
            return "property/edit_act_of_completed_work.html";
        }
        //model.addAttribute("lastAct", actDAO.showLastAct(actOfCompletedWorkDAO.showOne(id).getActRTRepair().getDevice().getInventoryCard()));
    }

    @PatchMapping("/{id}")
    public String updateActOfCompletedWork(@ModelAttribute("editActOfCompletedWork") @Valid ActOfCompletedWork actOfCompletedWork,
                                           BindingResult bindingResult,
                                           @PathVariable("id") Integer id){
        try{
            actOfCompletedWorkDAO.update(actOfCompletedWork, id);
        } catch(DataIntegrityViolationException e){
            bindingResult.rejectValue("dateOfReception", "error.dateOfReception", "Дата приемки должна быть позже даты передачи из акта приема-передачи");
        }
        if(bindingResult.hasErrors()){
            return "property/edit_act_of_completed_work.html";
        }

        return "redirect:/completed_work_act/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteActOfCompletedWork(@PathVariable("id") Integer id){
        actOfCompletedWorkDAO.delete(id);
        return "redirect:/completed_work_act/show_all";
    }
}
