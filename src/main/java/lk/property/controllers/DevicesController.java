package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.DeviceDAO;
import lk.property.dao.NomenclatureDAO;
import lk.property.models.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/device")
public class DevicesController {

    private final DeviceDAO deviceDAO;
    private final NomenclatureDAO nomenclatureDAO;
    public ArrayList<String> statuses;

    @Autowired
    public DevicesController(DeviceDAO deviceDAO, NomenclatureDAO nomenclatureDAO){
        this.deviceDAO = deviceDAO;
        this.nomenclatureDAO = nomenclatureDAO;
        statuses = new ArrayList<>();
        statuses.add("Работает (на складе)");
        statuses.add("Работает (у клиента)");
        statuses.add("Передан в филиал");
        statuses.add("Не работает");
        statuses.add("Списан");
        statuses.add("В ремонте");
    }

    @GetMapping("/show_all")
    public String showAll(Model model) throws SQLException {
        model.addAttribute("devices", deviceDAO.showAll());
        model.addAttribute("resultsByStatuses", deviceDAO.getAmountAndPriceByStatuses());
        return "property/devices.html";
    }

    @ModelAttribute
    public void initModel(Model model){
        model.addAttribute("device", new Device());
        model.addAttribute("statuses", statuses);
        model.addAttribute("avalNomenclatures", nomenclatureDAO.showAll());
    }

    @GetMapping("/new")
    public String newDevice(){
        return "property/new_device.html";
    }

    @PostMapping()
    public String createDevice(@ModelAttribute("device") @Valid Device device,
                               BindingResult bindingResult){
        //device.setNomenclature(nomenclatureDAO.showOne(nomenclature.getId()));
        if(bindingResult.hasErrors()){
            return "property/new_device.html";
        }
        deviceDAO.save(device);
        return "redirect:/device/show_all";
    }

    @GetMapping("/{inventoryCard}/edit")
    public String editDevice(@PathVariable("inventoryCard") String inventoryCard, Model model){
        model.addAttribute("device", deviceDAO.showOne(inventoryCard));
        return "property/edit_device.html";
    }

    @PatchMapping("/{inventoryCard}")
    public String updateDevice(@ModelAttribute("device") @Valid Device device,
                               BindingResult bindingResult,
                               @PathVariable("inventoryCard") String inventoryCard){
        if(bindingResult.hasErrors()){
            return "property/edit_device.html";
        }
        deviceDAO.update(inventoryCard, device);
        return "redirect:/device/show_all";
    }

    @DeleteMapping("/{inventoryCard}")
    public String deleteDevice(@PathVariable("inventoryCard") String inventoryCard){
        deviceDAO.delete(inventoryCard);
        return "redirect:/device/show_all";
    }
}
