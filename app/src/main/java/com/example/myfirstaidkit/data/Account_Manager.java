package com.example.myfirstaidkit.data;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

public class Account_Manager {
    private void addAccount(AccountAuthenticatorActivity activity, String username, String password) {
        AccountManager accnt_manager = AccountManager.get(activity);

        Account[] accounts = accnt_manager.getAccountsByType("user"); // account name identifier.

        if (accounts.length > 0) {
            return;
        }

        final Account account = new Account(username, "user");

        accnt_manager.addAccountExplicitly(account, password, null);

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
        intent.putExtra(AccountManager.KEY_PASSWORD, password);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, "user");
        // intent.putExtra(AccountManager.KEY_AUTH_TOKEN_LABEL,
        // PARAM_AUTHTOKEN_TYPE);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, "token");
        activity.setAccountAuthenticatorResult(intent.getExtras());
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    // method to retrieve account.
    private boolean validateAccount(AccountAuthenticatorActivity activity) {
        AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {

            @Override
            public void run(AccountManagerFuture<Bundle> arg0) {

                try {
                    Bundle b = arg0.getResult();
                    if (b.getBoolean(AccountManager.KEY_ACCOUNT_MANAGER_RESPONSE)) {
                        //User account exists!!..
                    }
                } catch (OperationCanceledException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (AuthenticatorException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        AccountManager accnt_manager = AccountManager
                .get(activity);

        Account[] accounts = accnt_manager
                .getAccountsByType("user");

        if (accounts.length <= 0) {
            return false;
        } else {
            /*loginNameVal = accounts[0].name;
            loginPswdVal = accnt_manager.getPassword(accounts[0]);*/
            return true;
        }
    }
}
