package com.seguo.controller.backend;


import com.seguo.dto.CollectionDto;
import com.seguo.entity.Collection;
import com.seguo.service.ProofreadService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    String store(@RequestParam(value = "coverFile", required = false) MultipartFile file,@Valid @ModelAttribute("collection") CollectionDto collectionDto,
                 BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "backend/collection/create";
        }
        doCover(file,collectionDto);
        proofreadService.save(collectionDto);
        return "redirect:/admin/collections";
    }

    @Value("${custom.upload.base-path}")
    String uploadBasePath;
    @Value("${custom.upload.collection-cover-dir-under-base-path}")
    String postCoverDirUnderBasePath;
    private void doCover(MultipartFile file, CollectionDto collectionDto) throws IOException {
        if (file != null && !file.isEmpty()) {
            File dir = new File(uploadBasePath + File.separator + postCoverDirUnderBasePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + suffix;
            file.transferTo(new File(dir.getAbsolutePath() + File.separator + newFilename));
            collectionDto.setCover("/" + postCoverDirUnderBasePath + File.separator + newFilename);
        }
    }

    @GetMapping("edit/{id}")
    String edit(@PathVariable Long id, Model model) {
        Optional<Collection> optionalCollection = proofreadService.findById(id);
        if (optionalCollection.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection Not Found");
        } else {
            Collection collection = optionalCollection.get();
            model.addAttribute("collection", collection);
            return "backend/collection/edit";
        }
    }

    @PutMapping("update")
    @PreAuthorize("#collectionDto.user_id == authentication.principal.user.id")
    String update(@RequestParam(value = "coverFile", required = false) MultipartFile file, @Valid @ModelAttribute("collection") CollectionDto collectionDto, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("collection", collectionDto);
            return "backend/collection/edit";
        }

        doCover(file, collectionDto);

        proofreadService.save(collectionDto);

        return "redirect:/admin/collections";
    }

    @PostMapping("togglePublished/{id}")
    @ResponseBody
    @Transactional
    public String togglePublished(@PathVariable Long id) {
        Optional<Collection> optionalPost = proofreadService.findById(id);

        if (optionalPost.isEmpty()
                || !"doc".equals(optionalPost.get().getType())
        ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doc Not Found");
        }

        proofreadService.togglePublished(id);
        return "SUCCESS";
    }
}
