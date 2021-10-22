package com.uber.driver.onboarding.api.service.impl;

import com.uber.driver.onboarding.api.service.IDriverDocumentService;
import com.uber.driver.onboarding.core.repository.dao.IDriverInfoDao;
import com.uber.driver.onboarding.core.repository.dao.IUserDao;
import com.uber.driver.onboarding.core.repository.entity.DriverInfo;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.enums.DocumentType;
import com.uber.driver.onboarding.model.enums.DriverState;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

public class DriverDocService implements IDriverDocumentService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String DRIVER_DOCUMENT_FOLDER_PATH = "/tmp/driverDocuments/%s/";

    private final IUserDao userDao;
    private final IDriverInfoDao driverInfoDao;

    public DriverDocService(IUserDao userDao, IDriverInfoDao driverInfoDao) {
        this.userDao = userDao;
        this.driverInfoDao = driverInfoDao;
    }

    @Override
    public void saveDocument(String driverId, DocumentType type, MultipartFile file) {
        User driver = userDao.findById(driverId);
        if (driver == null) {
            throw new RuntimeException("No driver found with id : {}" + driverId);
        }
        driver.setDriverState(DriverState.DOCUMENT_COLLECTED);

        // update doc info received..
        updateDriverInfo(driver, type);

        // save documents to aws s3 bucket or GCS...
        saveFile(driverId, type, file);

        userDao.saveOrUpdate(driver);
    }

    private void updateDriverInfo(User user, DocumentType type) {
        DriverInfo driverInfo = user.getDriverInfo();
        switch (type) {
            case RC:
                driverInfo.setReceivedRC(true);
                break;
            case LICENSE:
                driverInfo.setReceivedLC(true);
                break;
        }
        driverInfoDao.saveOrUpdate(driverInfo);
    }

    private void saveFile(String driverId, DocumentType type, MultipartFile file) {
        try {
            String path = String.format(DRIVER_DOCUMENT_FOLDER_PATH, driverId);
            File dir = new File(path);
            if (!dir.exists()) {
                new File(path).mkdir();
            }
            IOUtils.copy(file.getInputStream(), new FileOutputStream(new File(path + type.name().toLowerCase() + ".pdf")));
            log.info("File Saved in server");
        } catch (Exception e) {
            log.error("Unable to save file in server", e);
            throw new RuntimeException(e);
        }
    }

}
