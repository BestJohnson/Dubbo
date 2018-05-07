package com.kaisheng.Controller;

import com.github.pagehelper.PageInfo;
import com.kaisheng.entity.Kaola;
import com.kaisheng.entity.KaolaType;
import com.kaisheng.exception.NotFoundException;
import com.kaisheng.service.KaolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/kaola")
public class KaolaController {
    @Autowired
    private KaolaService kaolaService;

    @GetMapping("/{id:\\d+}")
    public String viewKaola(@PathVariable Integer id, Model model) {

        Kaola kaola = kaolaService.findById(id);
        model.addAttribute("id",id);
        model.addAttribute("kaola",kaola);
        if(kaola == null) {
            throw  new NotFoundException();
        }
        return "kaola/kaola";
    }

    @GetMapping
    public String list(@RequestParam(name = "p",defaultValue = "1",required = false) Integer pageNo,
                       @RequestParam(required = false) String kaolaName,
                       @RequestParam(required = false) String place,
                       @RequestParam(required = false) Float minPrice,
                       @RequestParam(required = false) Float maxPrice,
                       @RequestParam(required = false) Integer typeId,
                       Model model) {
        Map<String, Object> map = new HashMap<>();
        map.put("kaolaName",kaolaName);
        map.put("place",place);
        map.put("minPrice",minPrice);
        map.put("maxPrice",maxPrice);
        map.put("typeId",typeId);
        PageInfo<Kaola> pageInfo = kaolaService.findAllByPageNoAndParam(pageNo,map);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("typeList",kaolaService.findAllType());
        return "kaola/list";
    }

    @GetMapping("/new")
    public String newKaola(Model model) {
         List<KaolaType> kaolaTypeList = kaolaService.findAllType();
         model.addAttribute("typeList",kaolaTypeList);
         return "kaola/new";
    }

    @PostMapping("/new")
    public String newKaola(Kaola kaola,RedirectAttributes redirectAttributes) {
        kaolaService.saveKaola(kaola);
        redirectAttributes.addFlashAttribute("message","saved already");
        return "redirect:/kaola";
    }

    @GetMapping("/{id:\\d+}/del")
    public String delKaola(@PathVariable Integer id,
                           RedirectAttributes redirectAttributes) {
        kaolaService.deleteKaolaById(id);
        redirectAttributes.addFlashAttribute("message","delete already");
        return "redirect:/kaola";
    }

    @GetMapping("/{id:\\d+}/edit")
    public String editKaola(@PathVariable Integer id,
                            Model model) {
        Kaola kaola = kaolaService.findById(id);
        if(kaola == null) {
            throw new NotFoundException();
        }
        List<KaolaType> kaolaTypeList = kaolaService.findAllType();
        model.addAttribute("kaola",kaola);
        model.addAttribute("typeList",kaolaTypeList);
        return "kaola/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editKaola(Kaola kaola, RedirectAttributes redirectAttributes) {
        kaolaService.updateKaola(kaola);
        redirectAttributes.addFlashAttribute("message","changed already");
        return "redirect:/kaola";
    }
}
