package com.example.externalapi.mapper;

import com.example.externalapi.DTO.share.ShareCreateDTO;
import com.example.externalapi.DTO.share.ShareDTO;
import com.example.externalapi.DTO.share.ShareUpdateDTO;
import com.example.externalapi.entity.Share;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ShareMapper {

    final ModelMapper modelMapper;

    public ShareMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Share toEntity(ShareDTO dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Share.class);
    }

    public Share toEntity(ShareCreateDTO dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Share.class);
    }

    public Share toEntity(ShareUpdateDTO dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Share.class);
    }

    public ShareDTO toDto(Share share) {
        return Objects.isNull(share) ? null : modelMapper.map(share, ShareDTO.class);
    }

}
