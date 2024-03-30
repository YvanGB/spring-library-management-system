package com.esmt.lms;

import com.esmt.lms.common.Constants;
import com.esmt.lms.model.User;
import com.esmt.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        initDatabaseEntities();
    }


    private void initDatabaseEntities() {

        if( userService.getAllUsers().size() == 0) {
            userService.addNew(new User("Admin", "admin", "admin", Constants.ROLE_ADMIN));
            userService.addNew(new User("Librarian", "librarian", "librarian", Constants.ROLE_LIBRARIAN));
        }

    }
}