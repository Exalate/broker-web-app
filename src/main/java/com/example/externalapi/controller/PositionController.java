package com.example.externalapi.controller;

import com.example.externalapi.DTO.position.PositionDTO;
import com.example.externalapi.DTO.position.PositionInDTO;
import com.example.externalapi.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/positions")
@Api(tags = "Позиции")
public class PositionController {

    private final PositionService positionService;

    @ApiOperation("Получить все позиции")
    @GetMapping
    private List<PositionDTO> getAll() {
        return positionService.getAll();
    }

    @ApiOperation("Получить позицию по ID")
    @GetMapping("/{id}")
    private PositionDTO get(@PathVariable Long id) {
        return positionService.get(id);
    }

    @ApiOperation("Создать позицию")
    @PostMapping()
    private PositionDTO create(@RequestBody PositionInDTO positionInDTO) {
        return positionService.create(positionInDTO);
    }

    @ApiOperation("Изменить позицию")
    @PutMapping
    private PositionDTO update(@RequestBody PositionInDTO positionInDTO) {
        return positionService.update(positionInDTO);
    }

    @ApiOperation("Удалить позицию")
    @DeleteMapping("/{id}")
    private void delete(@PathVariable Long id) {
        positionService.delete(id);
    }


}
