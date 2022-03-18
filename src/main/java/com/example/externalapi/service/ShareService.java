package com.example.externalapi.service;

import com.example.externalapi.DTO.share.ShareCreateDTO;
import com.example.externalapi.DTO.share.ShareDTO;
import com.example.externalapi.DTO.share.ShareUpdateDTO;

import java.util.List;

public interface ShareService {
    List<ShareDTO> getAll();

    ShareDTO get(Long id);

    ShareDTO create(ShareCreateDTO shareCreateDTO);

    ShareDTO update(ShareUpdateDTO shareDTO);

    void delete(Long id);

    ShareDTO get(String ticker);
}
