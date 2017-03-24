package net.squanchy.home;

import net.squanchy.service.firebase.FirebaseAuthService;

import io.reactivex.Completable;

class HomeService {

    private final FirebaseAuthService authService;

    HomeService(FirebaseAuthService authService) {
        this.authService = authService;
    }

    public Completable signInAnonymouslyIfNecessary() {
        return authService
                .currentUser()
                .flatMapCompletable(user -> {
                    if (user.isPresent()) {
                        return Completable.complete();
                    }

                    return authService.signInAnonymously();
                });
    }
}
