package com.seguo.controller.backend;

import com.seguo.dto.SectionDto;
import com.seguo.entity.Section;
import com.seguo.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("backendSectionController")
@RequestMapping("/admin/sections/")
public class SectionController {
    @Autowired
    SectionService sectionService;

    @GetMapping("create")
    String create(Model model) {
        model.addAttribute("section", new Section());
        return "backend/section/create";
    }

    @PostMapping("store")
    String store(@Valid @ModelAttribute("section") SectionDto sectionDto,
                 BindingResult result) {
        if (result.hasErrors()) {
            return "backend/section/create";
        }
        sectionService.save(sectionDto);
        return "redirect:/admin/collections/edit/" + sectionDto.getCollection_id();
    }
}
