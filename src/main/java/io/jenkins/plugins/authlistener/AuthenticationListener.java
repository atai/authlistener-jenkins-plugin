package io.jenkins.plugins.authlistener;

import hudson.Extension;
import hudson.model.User;
import jenkins.security.SecurityListener;

import java.util.logging.Logger;

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
                    property = new LastLoginProperty(System.currentTimeMillis());
                } catch (Exception e) {
                    LOGGER.severe("Failed to add LastLoginProperty: " + e.getMessage());
                }
            } else {
                property.setLastLoginTime(System.currentTimeMillis());
            }
        }
    }
}