package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.DeviceDAO;
import lk.property.dao.WriteOffActDAO;
import lk.property.models.WriteOffAct;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void initModel(Model model){
        model.addAttribute("writeOffAct", new WriteOffAct());
        model.addAttribute("devicesOnStock", deviceDAO.showAllByStatuses(
                new String[]{"Работает (на складе)", "Не работает"}));
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
        if(bindingResult.hasErrors()){
            return "property/new_write-off_act.html";
        }
        writeOffActDAO.save(writeOffAct);
        return "redirect:/write-off_act/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editAct(@PathVariable("id") Integer id, Model model){
        model.addAttribute("writeOffAct", writeOffActDAO.showOne(id));
        return "property/edit_write-off_act.html";
    }

    @PatchMapping("/{id}")
    public String updateAct(@PathVariable("id") Integer id,
                            @ModelAttribute("writeOffAct") @Valid WriteOffAct writeOffAct,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "property/edit_write-off_act.html";
        }
        writeOffActDAO.update(writeOffAct, id);
        return "redirect:/write-off_act/show_all";
    }

    @DeleteMapping("{id}")
    public String deleteAct(@PathVariable("id") Integer id){
        writeOffActDAO.delete(id);
        return "redirect:/write-off_act/show_all";
    }
}
