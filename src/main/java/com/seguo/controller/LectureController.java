package com.seguo.controller;

import com.seguo.dto.BlockDto;
import com.seguo.entity.Block;
import com.seguo.entity.Lecture;
import com.seguo.service.BlockService;
import com.seguo.service.LectureService;
import com.seguo.service.TextTranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class LectureController {
    @Autowired
    LectureService lectureService;

    @Autowired
    TextTranslatorService translatorService;

    @Autowired
    BlockService blockService;

    @GetMapping("/docs/lecture/{id}")
    String show(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("lecture", lecture);

        StringBuilder allBlocks = new StringBuilder();
        if (lecture.getBlocks().isEmpty()) {
            allBlocks.append(lecture.getContent());
        } else {
            for (Block block : lecture.getBlocks()) {
                allBlocks.append(block.getContent()).append(System.lineSeparator());
            }
        }
        model.addAttribute("content", allBlocks);

        return "collection/doc/lecture/show";
    }

    @GetMapping("/docs/lecture/{id}/proofread")
    @PreAuthorize("hasRole('admin')")
    String showProofread(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("lecture", lecture);
        return "collection/doc/lecture/show-proofread";
    }

    @GetMapping("/docs/lecture/{id}/auto-translate")
    @PreAuthorize("hasRole('admin')")
    String autoTranslateBlocks(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!lecture.getBlocks().isEmpty()) {
            for (Block block : lecture.getBlocks()) {
                block.setContentTranslation(translatorService.translateText(block.getContent(), "en", "zh"));
                blockService.save(mapBlockToBlockDto(block));
            }
        }
        return "redirect:/docs/lecture/" +  id + "/proofread";
    }

    @PutMapping("/docs/lecture/block/proofread/update")
    @PreAuthorize("hasRole('admin')")
    @ResponseBody
    String updateBlock(
            @Validated @ModelAttribute("block") BlockDto blockDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return result.getAllErrors().toString();
        }

        blockService.save(blockDto);

        return "SUCCESS";
    }
    private BlockDto mapBlockToBlockDto(Block block) {
        BlockDto blockDto = new BlockDto();
        blockDto.setId(block.getId());
        blockDto.setContent(block.getContent());
        blockDto.setContentTranslation(block.getContentTranslation());
        blockDto.setSortOrder(block.getSortOrder());
        blockDto.setPublished(block.isPublished());
        blockDto.setLecture_id(block.getLecture().getId());
        blockDto.setCollection_id(block.getCollection().getId());
        return blockDto;
    }
}
