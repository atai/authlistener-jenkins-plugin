package io.jenkins.plugins.authlistener;

import hudson.Extension;
import hudson.model.User;
import java.util.logging.Logger;
import jenkins.security.SecurityListener;

@Extension
public class AuthenticationListener extends SecurityListener {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationListener.class.getName());

    @Override
    protected void loggedIn(String username) {
        LOGGER.info("User " + username + " has logged in.");
        User user = User.getById(username, false);
        if (user != null) {
            LastLoginProperty property = user.getProperty(LastLoginProperty.class);
            if (property != null) {
                try {
                    user.addProperty(new LastLoginProperty(System.currentTimeMillis()));
                    user.save();
                    LOGGER.info("Added LastLoginProperty to user and save logged in time to profile for " + username);
                } catch (Exception e) {
                    LOGGER.severe("Failed to add LastLoginProperty value: " + e.getMessage());
                }
            } else {
                LOGGER.severe("There is no LastLoginProperty.class in user profile.");
            }
        }
    }
}
