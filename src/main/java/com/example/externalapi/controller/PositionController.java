package com.example.externalapi.controller;

import com.example.externalapi.DTO.position.PositionDTO;
import com.example.externalapi.DTO.position.PositionInDTO;
import com.example.externalapi.service.PositionService;
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
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    private List<PositionDTO> getAll() {
        return positionService.getAll();
    }

    @GetMapping("/{id}")
    private PositionDTO get(@PathVariable Long id) {
        return positionService.get(id);
    }

    @PostMapping()
    private PositionDTO create(@RequestBody PositionInDTO positionInDTO) {
        return positionService.create(positionInDTO);
    }

    @PutMapping
    private PositionDTO update(@RequestBody PositionInDTO positionInDTO) {
        return positionService.update(positionInDTO);
    }

    @DeleteMapping("/{id}")
    private void delete(@PathVariable Long id) {
        positionService.delete(id);
    }


}
