package com.garage.qr.infrastructure;

import com.garage.qr.domain.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by  on 29.07.2025.
 * Description:
 */
@Repository
@RequiredArgsConstructor
class ToolRepositoryImpl implements ToolRepository {

    private final ToolJpaRepository toolJpaRepository;
    private final ToolEntityMapper toolEntityMapper;

    @Override
    public Optional<Tool> getToolByName(String name) {
        return Optional.ofNullable(toolJpaRepository.findByName(name))
                .map(toolEntityMapper::mapToTool);
    }
}
