package com.uber.driver.onboarding.api.handler;

import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.core.event.model.Message;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BgVerificationHandler extends AbstractMsgHandler {

    private static final Logger log = LoggerFactory.getLogger(BgVerificationHandler.class);

    private final IUserService userService;

    public BgVerificationHandler(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void validateMessage(Message message) {
        User user = JsonUtil.toObject(message.getBody(), User.class);
        assert user != null;
    }

    @Override
    protected void processMessage(Message message) {
        User user = getUserFromMessage(message);
        User dbUser = userService.findUser(user.getId());
        log.info("User : " + user);
        log.info("DBUser : " + dbUser);
        if(user.getDriverState().equals(dbUser.getDriverState()) && user.getLastUpdateDate().equals(dbUser.getLastUpdateDate())) {

            // Process this stage.....
            System.out.println("Verify BG HERE");
            // This will trigger an entry in Background Verification table...
            // These dataset will be visible on a UI for action...
            // Furthermore, an api will be called to trigger completion of background verification.
        }
    }

}
