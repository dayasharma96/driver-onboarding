package com.uber.driver.onboarding.api.service;

import com.uber.driver.onboarding.model.enums.DocumentType;
import org.springframework.web.multipart.MultipartFile;

public interface IDriverDocumentService {
    void saveDocument(String driverId, DocumentType type, MultipartFile file);
}
