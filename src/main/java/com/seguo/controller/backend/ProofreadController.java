package com.seguo.controller.backend;


import com.seguo.dto.CollectionDto;
import com.seguo.entity.Collection;
import com.seguo.service.ProofreadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller("backendProofread")
@RequestMapping("/admin/collections")
public class ProofreadController {
    @Autowired
    ProofreadService proofreadService;

    @GetMapping("")
    String index(Model model,
                 @RequestParam("page") Optional<Integer> page,
                 @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Collection> pageContent = proofreadService.findAll(currentPage, pageSize);
        model.addAttribute("page", pageContent);
        return "backend/collection/index";
    }

    @DeleteMapping("destroy/{id}")
    String destroy(@PathVariable Long id) {
        proofreadService.destroy(id);
        return "redirect:/admin/collections";
    }

    @DeleteMapping("destroy")
    @ResponseBody
    String destroyBatch(@RequestParam(value = "ids[]") List<Long> ids) {
        proofreadService.destroyAllById(ids);
        return "DONE";
    }

    @GetMapping("create")
    String create(Model model) {
        model.addAttribute("collection", new Collection());
        return "backend/collection/create";
    }

    @PostMapping("store")
    String store(@Valid @ModelAttribute("collection") CollectionDto collectionDto,
                 BindingResult result) {
        if (result.hasErrors()) {
            return "backend/collection/create";
        }
        proofreadService.save(collectionDto);
        return "redirect:/admin/collections";
    }
}
