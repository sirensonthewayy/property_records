package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.DeviceDAO;
import lk.property.dao.WriteOffActDAO;
import lk.property.models.WriteOffAct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/write-off_act")
public class WriteOffActsController {

    private final WriteOffActDAO writeOffActDAO;
    private final DeviceDAO deviceDAO;

    @Autowired
    public WriteOffActsController(WriteOffActDAO writeOffActDAO, DeviceDAO deviceDAO) {
        this.writeOffActDAO = writeOffActDAO;
        this.deviceDAO = deviceDAO;
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false, value="id") Integer id, Model model){
        model.addAttribute("writeOffAct", new WriteOffAct());
        model.addAttribute("devicesInStock", deviceDAO.showAllByStatuses(
                new String[]{"Не работает"}));
        model.addAttribute("editWriteOffAct", writeOffActDAO.showOne(id));
    }

    @GetMapping("/show_all")
    public String showAll(Model model){
        model.addAttribute("allActs", writeOffActDAO.showAll());
        return "property/acts_write_off.html";
    }

    @GetMapping("/new_writeoffact")
    public String newWriteOffAct(){
        return "property/new_write-off_act.html";
    }

    @PostMapping()
    public String createAct(@ModelAttribute("writeOffAct") @Valid WriteOffAct writeOffAct,
                            BindingResult bindingResult){
        try{
            writeOffActDAO.save(writeOffAct);
        } catch(DataIntegrityViolationException e){
            bindingResult.rejectValue("device", "error.device", "Данной инвентарной карточки не существует");
        } catch(UncategorizedSQLException exception){
            if(exception.getMessage().contains("check_date_write_off_act")){
                bindingResult.rejectValue("date", "error.date", "Дата списания должна быть позже даты предыдущего документа");
            } else if(exception.getMessage().contains("check_status_write_off_act")){
                bindingResult.rejectValue("device", "error.device", "Данное оборудование не находится на складе");
            }
        }
        if(bindingResult.hasErrors()){
            return "property/new_write-off_act.html";
        }

        return "redirect:/write-off_act/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editAct(@PathVariable("id") Integer id){
        WriteOffAct writeOffAct = writeOffActDAO.showOne(id);
        if(writeOffAct == null){
            return "property/404.html";
        } else{
            return "property/edit_write-off_act.html";
        }
    }

    @PatchMapping("/{id}")
    public String updateAct(@PathVariable("id") Integer id,
                            @ModelAttribute("editWriteOffAct") @Valid WriteOffAct editWriteOffAct,
                            BindingResult bindingResult){
        try{
            writeOffActDAO.update(editWriteOffAct, id);
        } catch(DataIntegrityViolationException e){
            bindingResult.rejectValue("device", "error.device", "Данной инвентарной карточки не существует");
        } catch(UncategorizedSQLException exception){
            if(exception.getMessage().contains("check_date_write_off_act")){
                bindingResult.rejectValue("date", "error.date", "Дата списания должна быть позже даты предыдущего документа");
            } else if(exception.getMessage().contains("check_status_write_off_act")){
                bindingResult.rejectValue("device", "error.device", "Данное оборудование не находится на складе");
            }
        }
        if(bindingResult.hasErrors()){
            return "property/edit_write-off_act.html";
        }
        return "redirect:/write-off_act/show_all";
    }

    @DeleteMapping("{id}")
    public String deleteAct(@PathVariable("id") Integer id){
        writeOffActDAO.delete(id);
        return "redirect:/write-off_act/show_all";
    }
}
