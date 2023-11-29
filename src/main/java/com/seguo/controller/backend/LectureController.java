package com.seguo.controller.backend;

import com.seguo.dto.LectureDto;
import com.seguo.entity.Lecture;
import com.seguo.service.LectureService;
import com.seguo.service.ProofreadService;
import com.seguo.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller("backendLectureController")
@RequestMapping("/admin/lectures/")
public class LectureController {
    @Autowired
    LectureService lectureService;
    @Autowired
    SectionService sectionService;
    @Autowired
    ProofreadService proofreadService;
    @GetMapping("create")
    String create(@RequestParam("collection_id") Long collectionId, @RequestParam("section_id") Long sectionId, Model model) {
        model.addAttribute("collection", proofreadService.findById(collectionId).get());
        model.addAttribute("section", sectionService.findById(sectionId).get());
        model.addAttribute("lecture", new Lecture());
        return "backend/lecture/create";
    }

    @PostMapping("store")
    String store(@Valid @ModelAttribute("lecture") LectureDto lectureDto,
                 BindingResult result) {
        if (result.hasErrors()) {
            return "backend/lecture/create";
        }
        lectureService.save(lectureDto);
        return "redirect:/admin/collections/edit/" + lectureDto.getCollection_id();
    }

    @GetMapping("edit/{id}")
    String edit(
            @PathVariable Long id,
            @RequestParam("collection_id") Long collectionId,
            @RequestParam("section_id") Long sectionId,
            Model model
    ) {
        Optional<Lecture> optionalLecture = lectureService.findById(id);
        if (optionalLecture.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture Not Found");
        } else {
            Lecture lecture = optionalLecture.get();
            model.addAttribute("collection", proofreadService.findById(collectionId).get());
            model.addAttribute("section", sectionService.findById(sectionId).get());
            model.addAttribute("lecture", lecture);
            return "backend/lecture/edit";
        }
    }

    @PutMapping("update")
    String update(
            @Validated @ModelAttribute("lecture") LectureDto lectureDto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("lecture", lectureDto);
            model.addAttribute("collection", proofreadService.findById(lectureDto.getCollection_id()).get());
            model.addAttribute("section", sectionService.findById(lectureDto.getSection_id()).get());

            return "backend/lecture/edit";
        }

        lectureService.save(lectureDto);

        return "redirect:/admin/collections/edit/" + lectureDto.getCollection_id();
    }
}
