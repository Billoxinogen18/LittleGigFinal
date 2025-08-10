package com.littlegig.app.data.repository;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<FirebaseAuth> authProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<Context> contextProvider;

  public AuthRepository_Factory(Provider<FirebaseAuth> authProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<Context> contextProvider) {
    this.authProvider = authProvider;
    this.firestoreProvider = firestoreProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(authProvider.get(), firestoreProvider.get(), contextProvider.get());
  }

  public static AuthRepository_Factory create(Provider<FirebaseAuth> authProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<Context> contextProvider) {
    return new AuthRepository_Factory(authProvider, firestoreProvider, contextProvider);
  }

  public static AuthRepository newInstance(FirebaseAuth auth, FirebaseFirestore firestore,
      Context context) {
    return new AuthRepository(auth, firestore, context);
  }
}
