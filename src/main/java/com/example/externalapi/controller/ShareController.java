package com.example.externalapi.controller;

import com.example.externalapi.DTO.share.ShareCreateDTO;
import com.example.externalapi.DTO.share.ShareDTO;
import com.example.externalapi.DTO.share.ShareUpdateDTO;
import com.example.externalapi.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shares")
public class ShareController {

    private final ShareService shareService;

    @GetMapping
    private List<ShareDTO> getAll() {
        return shareService.getAll();
    }

    @GetMapping("/{id}")
    private ShareDTO get(@PathVariable Long id) {
        return shareService.get(id);
    }

    @PostMapping
    private ShareDTO create(@RequestBody ShareCreateDTO shareCreateDTO) {
        return shareService.create(shareCreateDTO);
    }

    @PutMapping
    private ShareDTO update(@RequestBody ShareUpdateDTO shareUpdateDTO) {
        return shareService.update(shareUpdateDTO);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        shareService.delete(id);
    }
}
