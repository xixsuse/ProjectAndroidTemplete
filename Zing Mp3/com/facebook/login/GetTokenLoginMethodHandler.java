package com.facebook.login;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.PlatformServiceClient.CompletedListener;
import com.facebook.internal.Utility;
import com.facebook.internal.Utility.GraphMeRequestWithCacheCallback;
import com.facebook.login.LoginClient.Request;
import com.facebook.login.LoginClient.Result;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

class GetTokenLoginMethodHandler extends LoginMethodHandler {
    public static final Creator<GetTokenLoginMethodHandler> CREATOR;
    private GetTokenClient getTokenClient;

    /* renamed from: com.facebook.login.GetTokenLoginMethodHandler.1 */
    class C08851 implements CompletedListener {
        final /* synthetic */ Request val$request;

        C08851(Request request) {
            this.val$request = request;
        }

        public void completed(Bundle result) {
            GetTokenLoginMethodHandler.this.getTokenCompleted(this.val$request, result);
        }
    }

    /* renamed from: com.facebook.login.GetTokenLoginMethodHandler.2 */
    class C08862 implements GraphMeRequestWithCacheCallback {
        final /* synthetic */ Request val$request;
        final /* synthetic */ Bundle val$result;

        C08862(Bundle bundle, Request request) {
            this.val$result = bundle;
            this.val$request = request;
        }

        public void onSuccess(JSONObject userInfo) {
            try {
                this.val$result.putString(NativeProtocol.EXTRA_USER_ID, userInfo.getString(TtmlNode.ATTR_ID));
                GetTokenLoginMethodHandler.this.onComplete(this.val$request, this.val$result);
            } catch (JSONException ex) {
                GetTokenLoginMethodHandler.this.loginClient.complete(Result.createErrorResult(GetTokenLoginMethodHandler.this.loginClient.getPendingRequest(), "Caught exception", ex.getMessage()));
            }
        }

        public void onFailure(FacebookException error) {
            GetTokenLoginMethodHandler.this.loginClient.complete(Result.createErrorResult(GetTokenLoginMethodHandler.this.loginClient.getPendingRequest(), "Caught exception", error.getMessage()));
        }
    }

    /* renamed from: com.facebook.login.GetTokenLoginMethodHandler.3 */
    static class C08873 implements Creator {
        C08873() {
        }

        public GetTokenLoginMethodHandler createFromParcel(Parcel source) {
            return new GetTokenLoginMethodHandler(source);
        }

        public GetTokenLoginMethodHandler[] newArray(int size) {
            return new GetTokenLoginMethodHandler[size];
        }
    }

    GetTokenLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    String getNameForLogging() {
        return "get_token";
    }

    void cancel() {
        if (this.getTokenClient != null) {
            this.getTokenClient.cancel();
            this.getTokenClient.setCompletedListener(null);
            this.getTokenClient = null;
        }
    }

    boolean tryAuthorize(Request request) {
        this.getTokenClient = new GetTokenClient(this.loginClient.getActivity(), request.getApplicationId());
        if (!this.getTokenClient.start()) {
            return false;
        }
        this.loginClient.notifyBackgroundProcessingStart();
        this.getTokenClient.setCompletedListener(new C08851(request));
        return true;
    }

    void getTokenCompleted(Request request, Bundle result) {
        if (this.getTokenClient != null) {
            this.getTokenClient.setCompletedListener(null);
        }
        this.getTokenClient = null;
        this.loginClient.notifyBackgroundProcessingStop();
        if (result != null) {
            ArrayList<String> currentPermissions = result.getStringArrayList(NativeProtocol.EXTRA_PERMISSIONS);
            Set<String> permissions = request.getPermissions();
            if (currentPermissions == null || !(permissions == null || currentPermissions.containsAll(permissions))) {
                Set<String> newPermissions = new HashSet();
                for (String permission : permissions) {
                    if (!currentPermissions.contains(permission)) {
                        newPermissions.add(permission);
                    }
                }
                if (!newPermissions.isEmpty()) {
                    addLoggingExtra("new_permissions", TextUtils.join(",", newPermissions));
                }
                request.setPermissions(newPermissions);
            } else {
                complete(request, result);
                return;
            }
        }
        this.loginClient.tryNextHandler();
    }

    void onComplete(Request request, Bundle result) {
        this.loginClient.completeAndValidate(Result.createTokenResult(this.loginClient.getPendingRequest(), LoginMethodHandler.createAccessTokenFromNativeLogin(result, AccessTokenSource.FACEBOOK_APPLICATION_SERVICE, request.getApplicationId())));
    }

    void complete(Request request, Bundle result) {
        String userId = result.getString(NativeProtocol.EXTRA_USER_ID);
        if (userId == null || userId.isEmpty()) {
            this.loginClient.notifyBackgroundProcessingStart();
            Utility.getGraphMeRequestWithCacheAsync(result.getString(NativeProtocol.EXTRA_ACCESS_TOKEN), new C08862(result, request));
            return;
        }
        onComplete(request, result);
    }

    GetTokenLoginMethodHandler(Parcel source) {
        super(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    static {
        CREATOR = new C08873();
    }
}
