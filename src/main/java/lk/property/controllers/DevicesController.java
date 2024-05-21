package lk.property.controllers;

import jakarta.validation.Valid;
import lk.property.dao.ActDAO;
import lk.property.dao.DeviceDAO;
import lk.property.dao.NomenclatureDAO;
import lk.property.models.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

@Controller
@RequestMapping("/device")
public class DevicesController {

    private final DeviceDAO deviceDAO;
    private final NomenclatureDAO nomenclatureDAO;
    private final ActDAO actDAO;
    public ArrayList<String> statuses;

    @Autowired
    public DevicesController(DeviceDAO deviceDAO, NomenclatureDAO nomenclatureDAO, ActDAO actDAO){
        this.deviceDAO = deviceDAO;
        this.nomenclatureDAO = nomenclatureDAO;
        this.actDAO = actDAO;
    }

    @GetMapping("/show_all")
    public String showAll(Model model) throws SQLException {
        model.addAttribute("devices", deviceDAO.showAll());
        //model.addAttribute("resultsByStatuses", deviceDAO.getAmountAndPriceByStatuses());
        return "property/devices.html";
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false, value = "inventoryCard") String inventoryCard, Model model){
        statuses = new ArrayList<>();
        statuses.add("Работает (на складе)");
        statuses.add("Не работает");
/*        statuses.add("Работает (у клиента)");
        statuses.add("Передан в филиал");
        statuses.add("Списан");
        statuses.add("В ремонте");*/
        model.addAttribute("device", new Device());
        model.addAttribute("statuses", statuses);
        model.addAttribute("avalNomenclatures", nomenclatureDAO.showAll());
        model.addAttribute("acts", actDAO.showActsByDevice(inventoryCard));
        model.addAttribute("editDevice", deviceDAO.showOne(inventoryCard));
    }

    @GetMapping("/new")
    public String newDevice(){
        return "property/new_device.html";
    }

    @PostMapping()
    public String createDevice(@ModelAttribute("device") @Valid Device device,
                               BindingResult bindingResult){
        //device.setNomenclature(nomenclatureDAO.showOne(nomenclature.getId()));
        try{
            deviceDAO.save(device);
        } catch(DuplicateKeyException e){
            bindingResult.rejectValue("inventoryCard", "error.inventoryCard", "Данная инвентарная карточка уже используется");
        }
        if(bindingResult.hasErrors()){
            return "property/new_device.html";
        }
        return "redirect:/device/show_all";
    }

    @GetMapping("/{inventoryCard}/edit")
    public String editDevice(@PathVariable("inventoryCard") String inventoryCard, Model model){
        Device device = deviceDAO.showOne(inventoryCard);
        if(device == null){
            return "property/404.html";
        } else {
            model.addAttribute("editDevice", device);
            model.addAttribute("acts", actDAO.showActsByDevice(inventoryCard));
            return "property/edit_device.html";
        }
    }

    @PatchMapping("/{inventoryCard}")
    public String updateDevice(@ModelAttribute("editDevice") @Valid Device device,
                               BindingResult bindingResult,
                               @PathVariable("inventoryCard") String inventoryCard){
        try{
            deviceDAO.update(inventoryCard, device);
        } catch(DuplicateKeyException e){
            String message = "Инвентарная карточка " +
                    device.getInventoryCard() + " уже используется";

            bindingResult.rejectValue("inventoryCard", "error.inventoryCard", message);
        }
        if(bindingResult.hasErrors()){
            return "property/edit_device.html";
        }
        return "redirect:/device/show_all";
    }

    @DeleteMapping("/{inventoryCard}")
    public String deleteDevice(@PathVariable("inventoryCard") String inventoryCard){
        deviceDAO.delete(inventoryCard);
        return "redirect:/device/show_all";
    }
}
