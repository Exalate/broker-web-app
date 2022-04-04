package com.example.externalapi.service.impl;

import com.example.externalapi.DTO.share.ShareCreateDTO;
import com.example.externalapi.DTO.share.ShareDTO;
import com.example.externalapi.DTO.share.ShareUpdateDTO;
import com.example.externalapi.entity.Share;
import com.example.externalapi.exception.ElementIsUsed;
import com.example.externalapi.exception.NotFoundException;
import com.example.externalapi.mapper.ShareMapper;
import com.example.externalapi.repository.ShareRepository;
import com.example.externalapi.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

    private static final String SHARE_NOT_FOUND = "Share not found ID=%d";
    private static final String SHARE_NOT_FOUND_TICKER = "Share not found ticker=%s";
    private static final String SHARE_CODE_ALREADY_EXIST = "Share code=%s already exist ID=%d";
    private static final String SHARE_IS_USED = "Share ID=%d is used";

    private final ShareRepository shareRepository;
    private final ShareMapper shareMapper;

    @Override
    public List<ShareDTO> getAll() {
        return shareRepository.findAll().stream()
                .map(shareMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShareDTO get(Long id) {
        return shareMapper.toDto(shareRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(SHARE_NOT_FOUND, id))));
    }

    @Override
    @Transactional
    public ShareDTO create(ShareCreateDTO shareCreateDTO) {
        shareRepository.findFirstByCode(shareCreateDTO.getCode())
                .ifPresent(share -> {
                    throw new EntityExistsException(
                            String.format(SHARE_CODE_ALREADY_EXIST, shareCreateDTO.getCode(), share.getId()));
                });
        return shareMapper.toDto(shareRepository.save(shareMapper.toEntity(shareCreateDTO)));
    }

    @Override
    public ShareDTO update(ShareUpdateDTO shareDTO) {
        Share shareDB = shareRepository.findById(shareDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format(SHARE_NOT_FOUND, shareDTO.getId())));
        shareDB.setTicker(shareDTO.getTicker());
        return shareMapper.toDto(shareDB);
    }

    @Override
    public void delete(Long id) {
        shareRepository.findById(id).ifPresentOrElse(share -> {
                    try {
                        shareRepository.delete(share);
                    } catch (Exception e) {
                        throw new ElementIsUsed(String.format(SHARE_IS_USED + " <<>> " + e.getMessage(), id));
                    }
                },
                () -> {
                    throw new NotFoundException(String.format(SHARE_NOT_FOUND, id));
                });
    }

    @Override
    public ShareDTO get(String ticker) {
        return shareMapper.toDto(shareRepository.findByTickerIsLike(ticker)
                .orElseThrow(() -> new NotFoundException(String.format(SHARE_NOT_FOUND_TICKER, ticker))));
    }

//    @Override
//    @Transactional
//    public ShareDTO saveIfNotExist(ShareCreateDTO shareCreateDTO) {
//        var shareDB = shareRepository.findFirstByCode(shareCreateDTO.getCode());
//        if (shareDB.isPresent()) {
//            return shareMapper.toDto(shareDB.get());
//        }
//        return shareMapper.toDto(shareRepository.save(shareMapper.toEntity(shareCreateDTO)));
//    }
}
