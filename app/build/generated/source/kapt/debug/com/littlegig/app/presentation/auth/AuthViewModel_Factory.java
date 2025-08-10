package com.littlegig.app.presentation.auth;

import com.littlegig.app.data.repository.AuthRepository;
import com.littlegig.app.services.PhoneAuthService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<PhoneAuthService> phoneAuthServiceProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<PhoneAuthService> phoneAuthServiceProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.phoneAuthServiceProvider = phoneAuthServiceProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authRepositoryProvider.get(), phoneAuthServiceProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<PhoneAuthService> phoneAuthServiceProvider) {
    return new AuthViewModel_Factory(authRepositoryProvider, phoneAuthServiceProvider);
  }

  public static AuthViewModel newInstance(AuthRepository authRepository,
      PhoneAuthService phoneAuthService) {
    return new AuthViewModel(authRepository, phoneAuthService);
  }
}
