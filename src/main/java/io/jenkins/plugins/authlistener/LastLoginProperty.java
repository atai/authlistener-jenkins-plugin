package io.jenkins.plugins.authlistener;

import hudson.Extension;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import java.io.IOException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class LastLoginProperty extends UserProperty {
    private long lastLoginTime;

    @DataBoundConstructor
    public LastLoginProperty(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        try {
            user.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Extension
    public static final class DescriptorImpl extends UserPropertyDescriptor {
        @Override
        public UserProperty newInstance(User user) {
            return new LastLoginProperty(System.currentTimeMillis());
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Last Login Timestamp";
        }

        @Override
        public UserProperty newInstance(StaplerRequest req, JSONObject formData) {
            if (req == null) {
                throw new IllegalArgumentException("req is null");
            }
            return req.bindJSON(LastLoginProperty.class, formData);
        }
    }
}
