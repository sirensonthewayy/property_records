package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.*;
import lk.property.models.Act;
import lk.property.models.ActRTCompanies;
import lk.property.models.ActRTOffices;
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
@RequestMapping("/act_rt_offices")
public class ActRTOfficesController {

    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    private final ActRTOfficesDAO actRTOfficesDAO;
    private final DeviceDAO deviceDAO;
    private final OfficeDAO officeDAO;
    private final ActDAO actDAO;

    @Autowired
    public ActRTOfficesController(ActRTOfficesDAO actRTOfficesDAO, DeviceDAO deviceDAO, OfficeDAO officeDAO, ActDAO actDAO) {
        this.actRTOfficesDAO = actRTOfficesDAO;
        this.deviceDAO = deviceDAO;
        this.officeDAO = officeDAO;
        this.actDAO = actDAO;
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false, value="id") Integer id, Model model){
        model.addAttribute("actRTOffices", new ActRTOffices());
        model.addAttribute("offices", officeDAO.showAll());
        model.addAttribute("devicesInStock", deviceDAO.showAllByStatuses(new String[]{"Работает (на складе)"}));
        ActRTOffices editActRTOffices = actRTOfficesDAO.showOne(id);
        model.addAttribute("editActRTOffices", editActRTOffices);
        if(editActRTOffices == null){
            model.addAttribute("thisActIsLast", false);
        } else{
            ActRTOffices checkLastActRTOffices = actRTOfficesDAO.showOne(actDAO.showLastAct(editActRTOffices.getDevice().getInventoryCard()).getId());
            model.addAttribute("thisActIsLast", editActRTOffices.equals(checkLastActRTOffices));
        }
    }

    @GetMapping("/show_all")
    public String showAll(Model model){
        model.addAttribute("officesActs", actRTOfficesDAO.showAll());
        return "property/acts_rt_offices.html";
    }

    @GetMapping("/new")
    public String newRTActOffices(){
        return "property/new_act_rt_offices.html";
    }

    @PostMapping
    public String createAct(@ModelAttribute("actRTOffices") @Valid ActRTOffices actRTOffices,
                            BindingResult bindingResult){
        try{
            actRTOfficesDAO.save(actRTOffices);
        } catch (DataIntegrityViolationException e){
            if(e.getMessage().contains("inventory_card_foreign_key")){
                bindingResult.rejectValue("device", "error.device", "Такой инвентарной карточки не существует");
            } else if(e.getMessage().contains("Акт_ПП_офисы_check")){
                bindingResult.rejectValue("dateOfReception", "error.dateOfReception", "Приемка не может быть раньше передачи");
            }
        } catch(UncategorizedSQLException uncategorizedSQLException){
            if(uncategorizedSQLException.getMessage().contains("check_status_act_of_rt")){
                bindingResult.rejectValue("device", "error.device", "Данное оборудование либо не находится на складе, либо не в рабочем состоянии");
            } else if (uncategorizedSQLException.getMessage().contains("check_date_act_rt")) {
                bindingResult.rejectValue("dateOfTransmission", "error.dateOfTransmission", "Дата передачи не может быть раньше даты предыдущего документа");
            }
        }
        if(bindingResult.hasErrors()){
            return "property/new_act_rt_offices.html";
        }
        return "redirect:/act_rt_offices/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editAct(@PathVariable(value="id") Integer id, Model model){
        ActRTOffices actRTOffices = actRTOfficesDAO.showOne(id);
        if(actRTOffices == null){
            return "property/404.html";
        } else{
            model.addAttribute("lastAct", actDAO.showLastAct(actRTOffices.getDevice().getInventoryCard()));
            return "property/edit_act_rt_offices.html";
        }
    }

    @PatchMapping("/{id}")
    public String updateAct(@PathVariable("id") Integer id,
                            @ModelAttribute("editActRTOffices") ActRTOffices actRTOffices,
                            BindingResult bindingResult){
        try{
            actRTOfficesDAO.update(id, actRTOffices);
        } catch(DataIntegrityViolationException e){
            if(e.getMessage().contains("inventory_card_foreign_key")){
                bindingResult.rejectValue("device", "error.device", "Такой инвентарной карточки не существует");
            } else if(e.getMessage().contains("Акт_ПП_офисы_check")){
                bindingResult.rejectValue("dateOfReception", "error.dateOfReception", "Приемка не может быть раньше передачи");
            }
        } catch(UncategorizedSQLException uncategorizedSQLException){
            if(uncategorizedSQLException.getMessage().contains("check_status_act_of_rt")){
                bindingResult.rejectValue("device", "error.device", "Данное оборудование либо не находится на складе, либо не в рабочем состоянии");
            } else if (uncategorizedSQLException.getMessage().contains("check_date_act_rt")) {
                bindingResult.rejectValue("dateOfTransmission", "error.dateOfTransmission", "Дата передачи не может быть раньше даты предыдущего документа");
            }
        }
        if(bindingResult.hasErrors()){
            return "property/edit_act_rt_offices.html";
        }
        return "redirect:/act_rt_offices/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteAct(@PathVariable("id") Integer id){
        actRTOfficesDAO.delete(id);
        return "redirect:/act_rt_offices/show_all";
    }
}
