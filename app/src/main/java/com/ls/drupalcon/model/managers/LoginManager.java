package com.ls.drupalcon.model.managers;

import com.android.volley.RequestQueue;
import com.ls.drupal.login.ILoginManager;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.ResponseData;

public class LoginManager implements ILoginManager {

    @Override
    public ResponseData login(String userName, String password, RequestQueue queue) {
        //Perform login here
        return null;
    }

    @Override
    public boolean shouldRestoreLogin() {
        //return true if login session restoring with no user interaction is required during runtime (e.g - it can be timed out but you would like to restore it with no user interaction)
        return false;
    }

    @Override
    public boolean canRestoreLogin() {
        //return true if you are able to restore login without user interaction
        return false;
    }

    @Override
    public void applyLoginDataToRequest(BaseRequest request) {
        //apply some session-dependent data to request
    }

    @Override
    public boolean restoreLoginData(RequestQueue queue) {
        return false;
    }

    @Override
    public void onLoginRestoreFailed() {
        //handle background login restore failure here
    }

    @Override
    public Object logout(RequestQueue queue) {
        return null;
    }
}
