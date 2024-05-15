package lk.property.controllers;

import lk.property.dao.OfficeDAO;
import lk.property.models.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/office")
public class OfficesController {

    private final OfficeDAO officeDAO;

    @Autowired
    public OfficesController(OfficeDAO officeDAO){
        this.officeDAO = officeDAO;
    }

    @GetMapping("/show_all")
    public String showAll(Model model) throws SQLException{
        model.addAttribute("offices", officeDAO.showAll());
        return "property/offices.html";
    }

    @GetMapping("/new")
    public String newOffice(Model model) throws SQLException{
        model.addAttribute("office", new Office());
        return "property/new_office.html";
    }

    @PostMapping()
    public String createOffice(@ModelAttribute("office") Office office){
        officeDAO.save(office);
        return "redirect:/office/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editOffice(@PathVariable("id") int id, Model model){
        Office office = officeDAO.showOne(id);
        if(office == null){
            return "property/404.html";
        } else{
            model.addAttribute("office", office);
            return "property/edit_office.html";
        }
    }

    @PatchMapping("/{id}")
    public String updateOffice(@ModelAttribute("office") Office office,
                               @PathVariable("id") int id){
        officeDAO.update(id, office);
        return "redirect:/office/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteOffice(@PathVariable("id") int id){
        officeDAO.delete(id);
        return "redirect:/office/show_all";
    }
}
