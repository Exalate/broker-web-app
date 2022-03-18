package com.example.externalapi.controller;

import com.example.externalapi.DTO.share.ShareCreateDTO;
import com.example.externalapi.DTO.share.ShareDTO;
import com.example.externalapi.DTO.share.ShareUpdateDTO;
import com.example.externalapi.service.ShareService;
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
@RequestMapping("/shares")
@Api(tags = "Акции")
public class ShareController {

    private final ShareService shareService;

    @ApiOperation("Получить все акции")
    @GetMapping
    private List<ShareDTO> getAll() {
        return shareService.getAll();
    }

    @ApiOperation("Получить акцию по ID")
    @GetMapping("/{id}")
    private ShareDTO get(@PathVariable Long id) {
        return shareService.get(id);
    }

    @ApiOperation("Создать акцию")
    @PostMapping
    private ShareDTO create(@RequestBody ShareCreateDTO shareCreateDTO) {
        return shareService.create(shareCreateDTO);
    }

    @ApiOperation("Изменить акцию")
    @PutMapping
    private ShareDTO update(@RequestBody ShareUpdateDTO shareUpdateDTO) {
        return shareService.update(shareUpdateDTO);
    }

    @ApiOperation("Удалить акцию")
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        shareService.delete(id);
    }

    @ApiOperation("Получить акцию по тикеру")
    @GetMapping("/ticker/{ticker}")
    private ShareDTO getByName(@PathVariable String ticker) {
        return shareService.get(ticker);
    }
}
