package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.ActDAO;
import lk.property.dao.ActRTRepairDAO;
import lk.property.dao.DeviceDAO;
import lk.property.dao.RepairCompaniesDAO;
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
@RequestMapping("/act_rt_repair")
public class ActRTRepairController {

    private final ActRTRepairDAO actRTRepairDAO;
    private final DeviceDAO deviceDAO;
    private final RepairCompaniesDAO repairCompaniesDAO;
    private final ActDAO actDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    public ActRTRepairController(ActRTRepairDAO actRTRepairDAO, DeviceDAO deviceDAO, RepairCompaniesDAO repairCompaniesDAO, ActDAO actDAO) {
        this.actRTRepairDAO = actRTRepairDAO;
        this.deviceDAO = deviceDAO;
        this.repairCompaniesDAO = repairCompaniesDAO;
        this.actDAO = actDAO;
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false, value = "id") Integer id, Model model){
        model.addAttribute("repairCompanies", repairCompaniesDAO.showAll());
        model.addAttribute("devices", deviceDAO.showAllByStatuses(new String[]{"Не работает"}));
        ActRTRepair editActRTRepair = actRTRepairDAO.showOne(id);
        model.addAttribute("editActRTRepair", editActRTRepair);
        if(editActRTRepair == null){
            model.addAttribute("thisActIsLast", false);
        } else{
            ActRTRepair checkLastActRTRepair = actRTRepairDAO.showOne(actDAO.showLastAct(editActRTRepair.getDevice().getInventoryCard()).getId());
            model.addAttribute("thisActIsLast", editActRTRepair.equals(checkLastActRTRepair));
        }
    }

    @GetMapping("/show_all")
    public String showAll(Model model){
        model.addAttribute("actsRTRepair", actRTRepairDAO.showAll());
        return "property/acts_rt_repair.html";
    }

    @GetMapping("/new")
    public String newActRTRepair(Model model){
        model.addAttribute("actRTRepair", new ActRTRepair());
        return "property/new_act_rt_repair.html";
    }

    @PostMapping()
    public String createActRTRepair(@ModelAttribute("actRTRepair") @Valid ActRTRepair actRTRepair,
                                    BindingResult bindingResult){
        try{
            actRTRepairDAO.save(actRTRepair);
        } catch(DataIntegrityViolationException e){
            bindingResult.rejectValue("device", "error.device", "Данной инвентарной карточки не существует");
        } catch(UncategorizedSQLException uncategorizedSQLException){
            if(uncategorizedSQLException.getMessage().contains("check_status_act_of_rt_repair")){
                bindingResult.rejectValue("device", "error.device", "Данное оборудование либо не находится на складе, либо исправно");
            } else if (uncategorizedSQLException.getMessage().contains("check_date_act_rt")) {
                bindingResult.rejectValue("dateOfTransmission", "error.dateOfTransmission", "Дата передачи не может быть раньше даты предыдущего документа");
            }
        }
        if(bindingResult.hasErrors()){
            return "property/new_act_rt_repair.html";
        }
        return "redirect:/act_rt_repair/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editActRTRepair(@PathVariable("id") Integer id, Model model){
        ActRTRepair actRTRepair = actRTRepairDAO.showOne(id);
        if(actRTRepair == null){
            return "property/404.html";
        } else{
            model.addAttribute("lastAct", actDAO.showLastAct(actRTRepair.getDevice().getInventoryCard()));
            return "property/edit_act_rt_repair.html";
        }
    }

    @PatchMapping("/{id}")
    public String updateActRTRepair(@ModelAttribute("editActRTRepair") @Valid ActRTRepair actRTRepair,
                                    BindingResult bindingResult,
                                    @PathVariable("id") Integer id){
        try{
            actRTRepairDAO.update(actRTRepair, id);
        } catch(DataIntegrityViolationException e){
            bindingResult.rejectValue("device", "error.device", "Данной инвентарной карточки не существует");
        } catch(UncategorizedSQLException uncategorizedSQLException){
            if(uncategorizedSQLException.getMessage().contains("check_status_act_of_rt_repair")){
                bindingResult.rejectValue("device", "error.device", "Данное оборудование не находится на складе");
            } else if (uncategorizedSQLException.getMessage().contains("check_date_act_rt")) {
                bindingResult.rejectValue("dateOfTransmission", "error.dateOfTransmission", "Данное оборудование либо не находится на складе, либо исправно");
            }
        }
        if(bindingResult.hasErrors()){
            return "property/edit_act_rt_repair.html";
        }
        return "redirect:/act_rt_repair/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteActRTRepair(@PathVariable("id") Integer id){
        actRTRepairDAO.delete(id);
        return "redirect:/act_rt_repair/show_all";
    }
}
