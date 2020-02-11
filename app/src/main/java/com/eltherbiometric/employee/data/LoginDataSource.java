package com.eltherbiometric.employee.data;

import android.content.Context;

import com.eltherbiometric.employee.data.model.LoggedInUser;
import com.eltherbiometric.employee.data.model.User;
import com.eltherbiometric.employee.data.sqllite.Services;
import com.eltherbiometric.employee.utils.Config;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, Context context) {
        Services services = new Services(context);
        User user = services.FindUser(username, password);
        if (user != null){
//        if(username.equals(Config.UserName) && password.equals(Config.Password)){
            Hawk.put("user", user);
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            user.getNik(),
                            user.getName());
            return new Result.Success<>(fakeUser);
        }
        return new Result.Error(new IOException("Error logging in"));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
