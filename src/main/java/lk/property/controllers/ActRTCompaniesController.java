package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.ActDAO;
import lk.property.dao.ActRTCompaniesDAO;
import lk.property.dao.CompanyDAO;
import lk.property.dao.DeviceDAO;
import lk.property.models.Act;
import lk.property.models.ActRTCompanies;
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
    private final ActDAO actDAO;

    @Autowired
    public ActRTCompaniesController(ActRTCompaniesDAO actRTCompaniesDAO, DeviceDAO deviceDAO, CompanyDAO companyDAO, ActDAO actDAO) {
        this.actRTCompaniesDAO = actRTCompaniesDAO;
        this.deviceDAO = deviceDAO;
        this.companyDAO = companyDAO;
        this.actDAO = actDAO;
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false, value="id") Integer id, Model model){
        model.addAttribute("companyAct", new ActRTCompanies());
        model.addAttribute("companies", companyDAO.showAll());
        model.addAttribute("devicesInStock", deviceDAO.showAllByStatuses(new String[]{"Работает (на складе)"}));
        ActRTCompanies editActRTCompanies = actRTCompaniesDAO.showOne(id);
        model.addAttribute("editCompanyAct", editActRTCompanies);
        if(editActRTCompanies == null){
            model.addAttribute("thisActIsLast", false);
        } else{
            ActRTCompanies checkLastActRTCompanies = actRTCompaniesDAO.showOne(actDAO.showLastAct(editActRTCompanies.getDevice().getInventoryCard()).getId());
            model.addAttribute("thisActIsLast", editActRTCompanies.equals(checkLastActRTCompanies));
        }
/*        if(editActRTCompanies == null){
            model.addAttribute("lastAct", new Act());
        } else {
            model.addAttribute("lastAct", actDAO.showLastAct(editActRTCompanies.getDevice().getInventoryCard()));
        }
        try{
            model.addAttribute("checkLastAct",
                    actRTCompaniesDAO.showOne(((ActRTCompanies) model.getAttribute("lastAct")).getId()));
        } catch(Exception exception){
            model.addAttribute("checkLastAct", null);
        }*/
    }

    @GetMapping("/show_all")
    public String showAll(Model model){
        model.addAttribute("companiesActs", actRTCompaniesDAO.showAll());
        return "property/acts_rt_companies.html";
    }

    @GetMapping("/report")
    public String showAllInCompanies(Model model){
        model.addAttribute("currentActs", actRTCompaniesDAO.showCurrentActs());
        return "property/report_all_devices_in_companies.html";
    }

    @GetMapping("/new_transmission")
    public String newTransmissionAct(){
        return "property/new_act_rt_companies.html";
    }

    @PostMapping()
    public String createAct(@ModelAttribute("companyAct") @Valid ActRTCompanies actRTCompanies,
                            BindingResult bindingResult){
        try{
            actRTCompaniesDAO.save(actRTCompanies);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("inn_foreign_key")) {
                bindingResult.rejectValue("company", "error.company", "Компании с таким ИНН не существует");
            } else if (e.getMessage().contains("inventory_card_foreign_key")) {
                bindingResult.rejectValue("device", "error.device", "Такой инвентарной карточки не существует");
            } else if (e.getMessage().contains("Акт_ПП_контрагенты_check")) {
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
            return "property/new_act_rt_companies.html";
        }
        return "redirect:/companies_act/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editAct(@PathVariable(value="id") Integer id){
        if(actRTCompaniesDAO.showOne(id) == null){
            return "property/404.html";
        } else{
            return "property/edit_act_rt_companies.html";
        }
        //model.addAttribute("editCompanyAct", actRTCompaniesDAO.showOne(id));
        //model.addAttribute("lastAct", actDAO.showLastAct(actRTCompaniesDAO.showOne(id).getDevice().getInventoryCard()));
        //model.addAttribute("lastAct", actDAO.showLastAct(((ActRTCompanies) model.getAttribute("editCompanyAct")).getDevice().getInventoryCard()));
    }

    @PatchMapping("/{id}")
    public String updateAct(@PathVariable("id") Integer id,
                            @ModelAttribute("editCompanyAct") @Valid ActRTCompanies actRTCompanies,
                            BindingResult bindingResult){
        try{
            actRTCompaniesDAO.update(id, actRTCompanies);
        } catch (DataIntegrityViolationException dataIntegrityViolationException){
            if(dataIntegrityViolationException.getMessage().contains("inn_foreign_key")){
                bindingResult.rejectValue("company", "error.company", "Компании с таким ИНН не существует");
            } else if(dataIntegrityViolationException.getMessage().contains("inventory_card_foreign_key")) {
                bindingResult.rejectValue("device", "error.device", "Такой инвентарной карточки не существует");
            } else if(dataIntegrityViolationException.getMessage().contains("Акт_ПП_контрагенты_check")){
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
            return "property/edit_act_rt_companies.html";
        }
        return "redirect:/companies_act/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteAct(@PathVariable("id") Integer id){
        actRTCompaniesDAO.delete(id);
        return "redirect:/companies_act/show_all";
    }
}
