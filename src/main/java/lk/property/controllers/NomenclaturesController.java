package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.NomenclatureDAO;
import lk.property.models.Nomenclature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@Controller
@RequestMapping("/nomenclature")
public class NomenclaturesController {

    private final NomenclatureDAO nomenclatureDAO;

    @Autowired
    public NomenclaturesController(NomenclatureDAO nomenclatureDAO) {
        this.nomenclatureDAO = nomenclatureDAO;
    }

    @GetMapping("/show_all")
    public String showAll(Model model) throws SQLException {
        model.addAttribute("nomenclatures", nomenclatureDAO.showAll());
        return "property/nomenclatures.html";
    }

    @GetMapping("/new")
    public String newNomenclature(Model model){
        model.addAttribute("nomenclature", new Nomenclature());
        return "property/new_nomenclature.html";
    }

    @PostMapping()
    public String createNomenclature(@ModelAttribute("nomenclature") @Valid Nomenclature nomenclature,
                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "property/new_nomenclature.html";
        }
        nomenclatureDAO.save(nomenclature);
        return "redirect:/nomenclature/show_all";
    }

    @GetMapping("/{id}/edit")
    public String editNomenclature(@PathVariable("id") int id, Model model){
        model.addAttribute("nomenclature", nomenclatureDAO.showOne(id));
        model.addAttribute("haveDevices", nomenclatureDAO.haveDevices(id));
        return "property/edit_nomenclature.html";
    }

    @PatchMapping("/{id}")
    public String updateNomenclature(@ModelAttribute("nomenclature") @Valid Nomenclature nomenclature,
                                     BindingResult bindingResult,
                                     @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            return "property/edit_nomenclature.html";
        }
        nomenclatureDAO.update(id, nomenclature);
        return "redirect:/nomenclature/show_all";
    }

    @DeleteMapping("/{id}")
    public String deleteNomenclature(@PathVariable("id") int id){
        nomenclatureDAO.delete(id);
        return "redirect:/nomenclature/show_all";
    }
}
