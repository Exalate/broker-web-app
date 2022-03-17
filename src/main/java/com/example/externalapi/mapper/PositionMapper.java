package com.example.externalapi.mapper;

import com.example.externalapi.DTO.position.PositionDTO;
import com.example.externalapi.DTO.position.PositionInDTO;
import com.example.externalapi.entity.Position;
import com.example.externalapi.repository.PortfolioRepository;
import com.example.externalapi.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PositionMapper {

    private final ModelMapper modelMapper;
    private final PortfolioRepository portfolioRepository;
    private final ShareRepository shareRepository;

    @PostConstruct
    private void init() {
        modelMapper.createTypeMap(PositionInDTO.class, Position.class)
                .addMappings(mapping -> {
                    mapping.skip(Position::setPortfolio);
                    mapping.skip(Position::setShare);
                }).setPostConverter(toEntityPostConverter());
    }

    public Position toEntity(PositionInDTO dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Position.class);
    }

    public Position toEntity(PositionDTO dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Position.class);
    }

    public PositionDTO toDto(Position position) {
        return Objects.isNull(position) ? null : modelMapper.map(position, PositionDTO.class);
    }

    private <T extends PositionInDTO> Converter<T, Position> toEntityPostConverter() {
        return ctx -> {
            var src = ctx.getSource();
            var dst = ctx.getDestination();
            if (Objects.nonNull(src.getPortfolioId())) {
                dst.setPortfolio(portfolioRepository.findById(src.getPortfolioId())
                        .orElseThrow(() -> new NoSuchElementException(
                                String.format("Нет портфеля с ID = %d", src.getPortfolioId()))));
            }
            if (Objects.nonNull(src.getShareId())) {
                dst.setShare(shareRepository.findById(src.getShareId())
                        .orElseThrow(() -> new NoSuchElementException(
                                String.format("Нет инструмента с ID = %d", src.getShareId()))));
            }
            return dst;
        };
    }

}
