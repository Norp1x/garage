package com.garage.qr.domain;

import com.garage.qr.api.ToolMapper;
import com.garage.qr.infrastructure.QrCodeEntity;
import com.garage.qr.infrastructure.QrCodeRepository;
import com.garage.qr.infrastructure.ToolEntityMapper;
import com.garage.qr.infrastructure.ToolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;
    private final QrCodeRepository qrCodeRepository;
    private final GenerateQrImageService generateQrImageService;
    private final ReadQrCodeService readQrCodeService;
    private final ToolMapper toolMapper;
    private final ToolEntityMapper toolEntityMapper;

//    public Tool createTool(Tool tool) {
//        log.info("Creating new tool: {}", tool.name());
//
//        if (toolRepository.getToolByName(tool.name()).isPresent()) {
//            throw new IllegalArgumentException("Narzędzie o tej nazwie już istnieje");
//        }
//
//        ToolEntity toolEntity = toolEntityMapper.mapToEntity(tool);
//        ToolEntity savedTool = toolJpaRepository.save(toolEntity);
//
//        log.info("Tool created successfully with ID: {}", savedTool.getId());
//        return tool;
//    }

    public QrCode generateQrCodeForTool(String toolName) {
        log.info("Generating QR code for tool ID: {}", toolName);

        Tool tool = toolRepository.getToolByName(toolName)
                .orElseThrow(() -> new EntityNotFoundException("Narzędzie nie zostało znalezione"));

        QrCode qrCode = generateQrImageService.generateQRCode(tool);

        // Zapisz informacje o QR code w bazie
        QrCodeEntity qrCodeEntity = QrCodeEntity.builder()
                .name(qrCode.name())
                .filePath(qrCode.image())
//                .tool(tool)
                .build();

        qrCodeRepository.save(qrCodeEntity);

        log.info("QR code generated and saved for tool: {}", tool.name());
        return QrCode.builder()
                .name(qrCode.name())
                .image(qrCode.image())
                .build();
    }

//    @Transactional(readOnly = true)
//    public List<ToolDto> getAllTools() {
//        return toolJpaRepository.findAll()
//                .stream()
//                .map(toolMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<ToolDto> getToolById(Long id) {
//        return toolJpaRepository.findById(id)
//                .map(toolMapper::toDto);
//    }
//
//    @Transactional(readOnly = true)
//    public List<ToolDto> searchTools(String searchTerm) {
//        return toolJpaRepository.searchTools(searchTerm)
//                .stream()
//                .map(toolMapper::toDto)
//                .collect(Collectors.toList());
//    }

//    public ToolDto updateTool(Long id, ToolDto toolDto) {
//        log.info("Updating tool with ID: {}", id);
//
//        ToolEntity existingTool = toolRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Narzędzie nie zostało znalezione"));
//
//        // Aktualizuj pola
//        existingTool.setName(toolDto.getName());
//        existingTool.setType(toolDto.getType());
//        existingTool.setDescription(toolDto.getDescription());
//        existingTool.setSize(ToolSize.valueOf(toolDto.getSize().toUpperCase()));
//        existingTool.setColor(toolDto.getColor());
//        existingTool.setQuantity(Integer.valueOf(toolDto.getQuantity()));
//        existingTool.setToolPlacing(toolDto.getToolPlacing());
//
//        ToolEntity updatedTool = toolRepository.save(existingTool);
//
//        log.info("Tool updated successfully: {}", updatedTool.getName());
//        return toolMapper.toDto(updatedTool);
//    }
//
//    public void deleteTool(Long id) {
//        log.info("Deleting tool with ID: {}", id);
//
//        if (!toolRepository.existsById(id)) {
//            throw new EntityNotFoundException("Narzędzie nie zostało znalezione");
//        }
//
//        // Usuń najpierw QR codes
//        qrCodeRepository.deleteByToolId(id);
//
//        // Następnie usuń narzędzie
//        toolRepository.deleteById(id);
//
//        log.info("Tool and associated QR codes deleted successfully");
//    }
//
//    public Optional<ToolDto> decodeToolFromQrCode(String qrCodeContent) {
//        return readQrCodeService.parseToolFromQrCode(qrCodeContent)
//                .map(toolMapper::toDto);
//    }
}
