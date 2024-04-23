package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.*;
import lk.property.models.ActRTCompanies;
import lk.property.models.ActRTOffices;
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

    @Autowired
    public ActRTOfficesController(ActRTOfficesDAO actRTOfficesDAO, DeviceDAO deviceDAO, OfficeDAO officeDAO) {
        this.actRTOfficesDAO = actRTOfficesDAO;
        this.deviceDAO = deviceDAO;
        this.officeDAO = officeDAO;
    }

    @ModelAttribute
    public void initModel(Model model){
        model.addAttribute("actRTOffices", new ActRTOffices());
        model.addAttribute("offices", officeDAO.showAll());
        model.addAttribute("devicesInStock", deviceDAO.showAllByStatuses(new String[]{"Работает (на складе)"}));
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
        if(bindingResult.hasErrors()){
            return "property/new_act_rt_offices.html";
        }
        actRTOfficesDAO.save(actRTOffices);
        return "redirect:/act_rt_offices/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editAct(@PathVariable("id") Integer id, Model model){
        model.addAttribute("editingActRTOffices", actRTOfficesDAO.showOne(id));
        return "property/edit_act_rt_offices.html";
    }

    @PatchMapping("/{id}")
    public String updateAct(@PathVariable("id") Integer id,
                            @ModelAttribute("editingActRTOffices") ActRTOffices actRTOffices,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "property/edit_act_rt_offices.html";
        }
        actRTOfficesDAO.update(id, actRTOffices);
        return "redirect:/act_rt_offices/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteAct(@PathVariable("id") Integer id){
        actRTOfficesDAO.delete(id);
        return "redirect:/act_rt_offices/show_all";
    }
}
