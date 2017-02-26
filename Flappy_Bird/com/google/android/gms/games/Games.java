package com.google.android.gms.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Api.ClientKey;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza.zzb;
import com.google.android.gms.common.internal.zze;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.appcontent.AppContents;
import com.google.android.gms.games.event.Events;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.games.internal.api.AchievementsImpl;
import com.google.android.gms.games.internal.api.AclsImpl;
import com.google.android.gms.games.internal.api.AppContentsImpl;
import com.google.android.gms.games.internal.api.EventsImpl;
import com.google.android.gms.games.internal.api.GamesMetadataImpl;
import com.google.android.gms.games.internal.api.InvitationsImpl;
import com.google.android.gms.games.internal.api.LeaderboardsImpl;
import com.google.android.gms.games.internal.api.MultiplayerImpl;
import com.google.android.gms.games.internal.api.NotificationsImpl;
import com.google.android.gms.games.internal.api.PlayersImpl;
import com.google.android.gms.games.internal.api.QuestsImpl;
import com.google.android.gms.games.internal.api.RealTimeMultiplayerImpl;
import com.google.android.gms.games.internal.api.RequestsImpl;
import com.google.android.gms.games.internal.api.SnapshotsImpl;
import com.google.android.gms.games.internal.api.TurnBasedMultiplayerImpl;
import com.google.android.gms.games.internal.game.Acls;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.multiplayer.Invitations;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.quest.Quests;
import com.google.android.gms.games.request.Requests;
import com.google.android.gms.games.snapshot.Snapshots;
import java.util.ArrayList;

public final class Games {
    public static final Api<GamesOptions> API;
    public static final Achievements Achievements;
    public static final String EXTRA_PLAYER_IDS = "players";
    public static final String EXTRA_STATUS = "status";
    public static final Events Events;
    public static final GamesMetadata GamesMetadata;
    public static final Invitations Invitations;
    public static final Leaderboards Leaderboards;
    public static final Notifications Notifications;
    public static final Players Players;
    public static final Quests Quests;
    public static final RealTimeMultiplayer RealTimeMultiplayer;
    public static final Requests Requests;
    public static final Scope SCOPE_GAMES;
    public static final Snapshots Snapshots;
    public static final TurnBasedMultiplayer TurnBasedMultiplayer;
    static final ClientKey<GamesClientImpl> zzNX;
    private static final zza<GamesClientImpl, GamesOptions> zzNY;
    public static final Multiplayer zzanA;
    public static final Acls zzanB;
    public static final Scope zzanx;
    public static final Api<GamesOptions> zzany;
    public static final AppContents zzanz;

    /* renamed from: com.google.android.gms.games.Games.1 */
    static class C04261 implements zza<GamesClientImpl, GamesOptions> {
        C04261() {
        }

        public int getPriority() {
            return 1;
        }

        public GamesClientImpl zza(Context context, Looper looper, zze com_google_android_gms_common_internal_zze, GamesOptions gamesOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new GamesClientImpl(context, looper, com_google_android_gms_common_internal_zze, gamesOptions == null ? new GamesOptions() : gamesOptions, connectionCallbacks, onConnectionFailedListener);
        }
    }

    public static abstract class BaseGamesApiMethodImpl<R extends Result> extends com.google.android.gms.common.api.zza.zza<R, GamesClientImpl> {
        public BaseGamesApiMethodImpl(GoogleApiClient googleApiClient) {
            super(Games.zzNX, googleApiClient);
        }
    }

    public static final class GamesOptions implements Optional {
        public final boolean zzanC;
        public final boolean zzanD;
        public final int zzanE;
        public final boolean zzanF;
        public final int zzanG;
        public final String zzanH;
        public final ArrayList<String> zzanI;

        public static final class Builder {
            boolean zzanC;
            boolean zzanD;
            int zzanE;
            boolean zzanF;
            int zzanG;
            String zzanH;
            ArrayList<String> zzanI;

            private Builder() {
                this.zzanC = false;
                this.zzanD = true;
                this.zzanE = 17;
                this.zzanF = false;
                this.zzanG = 4368;
                this.zzanH = null;
                this.zzanI = new ArrayList();
            }

            public GamesOptions build() {
                return new GamesOptions();
            }

            public Builder setSdkVariant(int variant) {
                this.zzanG = variant;
                return this;
            }

            public Builder setShowConnectingPopup(boolean showConnectingPopup) {
                this.zzanD = showConnectingPopup;
                this.zzanE = 17;
                return this;
            }

            public Builder setShowConnectingPopup(boolean showConnectingPopup, int gravity) {
                this.zzanD = showConnectingPopup;
                this.zzanE = gravity;
                return this;
            }
        }

        private GamesOptions() {
            this.zzanC = false;
            this.zzanD = true;
            this.zzanE = 17;
            this.zzanF = false;
            this.zzanG = 4368;
            this.zzanH = null;
            this.zzanI = new ArrayList();
        }

        private GamesOptions(Builder builder) {
            this.zzanC = builder.zzanC;
            this.zzanD = builder.zzanD;
            this.zzanE = builder.zzanE;
            this.zzanF = builder.zzanF;
            this.zzanG = builder.zzanG;
            this.zzanH = builder.zzanH;
            this.zzanI = builder.zzanI;
        }

        public static Builder builder() {
            return new Builder();
        }

        public Bundle zzrI() {
            Bundle bundle = new Bundle();
            bundle.putBoolean("com.google.android.gms.games.key.isHeadless", this.zzanC);
            bundle.putBoolean("com.google.android.gms.games.key.showConnectingPopup", this.zzanD);
            bundle.putInt("com.google.android.gms.games.key.connectingPopupGravity", this.zzanE);
            bundle.putBoolean("com.google.android.gms.games.key.retryingSignIn", this.zzanF);
            bundle.putInt("com.google.android.gms.games.key.sdkVariant", this.zzanG);
            bundle.putString("com.google.android.gms.games.key.forceResolveAccountKey", this.zzanH);
            bundle.putStringArrayList("com.google.android.gms.games.key.proxyApis", this.zzanI);
            return bundle;
        }
    }

    private static abstract class SignOutImpl extends BaseGamesApiMethodImpl<Status> {
        private SignOutImpl(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result createFailedResult(Status x0) {
            return zzb(x0);
        }

        public Status zzb(Status status) {
            return status;
        }
    }

    /* renamed from: com.google.android.gms.games.Games.2 */
    static class C09452 extends SignOutImpl {
        C09452(GoogleApiClient x0) {
            super(null);
        }

        protected void zza(GamesClientImpl gamesClientImpl) throws RemoteException {
            gamesClientImpl.zzb((zzb) this);
        }
    }

    static {
        zzNX = new ClientKey();
        zzNY = new C04261();
        SCOPE_GAMES = new Scope(Scopes.GAMES);
        API = new Api("Games.API", zzNY, zzNX, SCOPE_GAMES);
        zzanx = new Scope("https://www.googleapis.com/auth/games.firstparty");
        zzany = new Api("Games.API_1P", zzNY, zzNX, zzanx);
        GamesMetadata = new GamesMetadataImpl();
        Achievements = new AchievementsImpl();
        zzanz = new AppContentsImpl();
        Events = new EventsImpl();
        Leaderboards = new LeaderboardsImpl();
        Invitations = new InvitationsImpl();
        TurnBasedMultiplayer = new TurnBasedMultiplayerImpl();
        RealTimeMultiplayer = new RealTimeMultiplayerImpl();
        zzanA = new MultiplayerImpl();
        Players = new PlayersImpl();
        Notifications = new NotificationsImpl();
        Quests = new QuestsImpl();
        Requests = new RequestsImpl();
        Snapshots = new SnapshotsImpl();
        zzanB = new AclsImpl();
    }

    private Games() {
    }

    public static String getAppId(GoogleApiClient apiClient) {
        return zzd(apiClient).zzsK();
    }

    public static String getCurrentAccountName(GoogleApiClient apiClient) {
        return zzd(apiClient).zzsv();
    }

    public static int getSdkVariant(GoogleApiClient apiClient) {
        return zzd(apiClient).zzsJ();
    }

    public static Intent getSettingsIntent(GoogleApiClient apiClient) {
        return zzd(apiClient).zzsI();
    }

    public static void setGravityForPopups(GoogleApiClient apiClient, int gravity) {
        GamesClientImpl zzb = zzb(apiClient, false);
        if (zzb != null) {
            zzb.zzfC(gravity);
        }
    }

    public static void setViewForPopups(GoogleApiClient apiClient, View gamesContentView) {
        zzu.zzu(gamesContentView);
        GamesClientImpl zzb = zzb(apiClient, false);
        if (zzb != null) {
            zzb.zzn(gamesContentView);
        }
    }

    public static PendingResult<Status> signOut(GoogleApiClient apiClient) {
        return apiClient.zzb(new C09452(apiClient));
    }

    public static GamesClientImpl zzb(GoogleApiClient googleApiClient, boolean z) {
        zzu.zzb(googleApiClient != null, (Object) "GoogleApiClient parameter is required.");
        zzu.zza(googleApiClient.isConnected(), (Object) "GoogleApiClient must be connected.");
        return zzc(googleApiClient, z);
    }

    public static GamesClientImpl zzc(GoogleApiClient googleApiClient, boolean z) {
        zzu.zza(googleApiClient.zza(API), (Object) "GoogleApiClient is not configured to use the Games Api. Pass Games.API into GoogleApiClient.Builder#addApi() to use this feature.");
        boolean hasConnectedApi = googleApiClient.hasConnectedApi(API);
        if (!z || hasConnectedApi) {
            return hasConnectedApi ? (GamesClientImpl) googleApiClient.zza(zzNX) : null;
        } else {
            throw new IllegalStateException("GoogleApiClient has an optional Games.API and is not connected to Games. Use GoogleApiClient.hasConnectedApi(Games.API) to guard this call.");
        }
    }

    public static GamesClientImpl zzd(GoogleApiClient googleApiClient) {
        return zzb(googleApiClient, true);
    }
}
