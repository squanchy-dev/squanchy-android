package net.squanchy.settings.view;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.Optional;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class SettingsHeaderLayout extends AppBarLayout {

    private static final String PROVIDER_ID_GOOGLE = "google.com";

    @Nullable
    private ImageLoader imageLoader;

    private ImageView userPhotoView;
    private TextView userNameView;

    public SettingsHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            imageLoader = ImageLoaderInjector.obtain(unwrapToActivityContext(context))
                    .imageLoader();
        }

        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        userPhotoView = (ImageView) findViewById(R.id.user_photo);
        userNameView = (TextView) findViewById(R.id.user_name);
    }

    public void updateWith(Optional<FirebaseUser> user) {
        if (user.isPresent() && !user.get().isAnonymous()) {
            updateWithAuthenticatedUser(user.get());
        } else {
            updateWithNoOrAnonymousUser();
        }
    }

    private void updateWithAuthenticatedUser(FirebaseUser firebaseUser) {
        Optional<UserInfo> googleUserInfo = googleUserInfoFrom(firebaseUser);
        if (googleUserInfo.isPresent()) {
            UserInfo userInfo = googleUserInfo.get();
            updateUserPhotoFrom(userInfo);
            userNameView.setText(userInfo.getDisplayName());
        }
    }

    private Optional<UserInfo> googleUserInfoFrom(FirebaseUser firebaseUser) {
        List<? extends UserInfo> providerData = firebaseUser.getProviderData();
        List<? extends UserInfo> googleData = Lists.filter(providerData, data -> PROVIDER_ID_GOOGLE.equalsIgnoreCase(data.getProviderId()));
        if (googleData.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(googleData.get(0));
    }

    private void updateUserPhotoFrom(UserInfo userInfo) {
        if (imageLoader == null) {
            return;
        }

        Optional<String> photoUrl = photoUrlFor(userInfo);
        if (photoUrl.isPresent()) {
            imageLoader.load(photoUrl.get())
                    .into(userPhotoView);
        }
    }

    private Optional<String> photoUrlFor(UserInfo userInfo) {
        return Optional.fromNullable(userInfo.getPhotoUrl())
                .map(Uri::toString);
    }

    private void updateWithNoOrAnonymousUser() {
        userPhotoView.setImageDrawable(null);
        userNameView.setText(null);
    }
}
