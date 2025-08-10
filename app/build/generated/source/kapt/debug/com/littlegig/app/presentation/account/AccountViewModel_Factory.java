package com.littlegig.app.presentation.account;

import com.google.firebase.functions.FirebaseFunctions;
import com.littlegig.app.data.repository.AuthRepository;
import com.littlegig.app.data.repository.EventRepository;
import com.littlegig.app.data.repository.PaymentRepository;
import com.littlegig.app.data.repository.UserRepository;
import com.littlegig.app.services.LocationService;
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
public final class AccountViewModel_Factory implements Factory<AccountViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<LocationService> locationServiceProvider;

  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<PaymentRepository> paymentRepositoryProvider;

  private final Provider<FirebaseFunctions> functionsProvider;

  public AccountViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<EventRepository> eventRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<PaymentRepository> paymentRepositoryProvider,
      Provider<FirebaseFunctions> functionsProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.locationServiceProvider = locationServiceProvider;
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.paymentRepositoryProvider = paymentRepositoryProvider;
    this.functionsProvider = functionsProvider;
  }

  @Override
  public AccountViewModel get() {
    return newInstance(authRepositoryProvider.get(), locationServiceProvider.get(), eventRepositoryProvider.get(), userRepositoryProvider.get(), paymentRepositoryProvider.get(), functionsProvider.get());
  }

  public static AccountViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<LocationService> locationServiceProvider,
      Provider<EventRepository> eventRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<PaymentRepository> paymentRepositoryProvider,
      Provider<FirebaseFunctions> functionsProvider) {
    return new AccountViewModel_Factory(authRepositoryProvider, locationServiceProvider, eventRepositoryProvider, userRepositoryProvider, paymentRepositoryProvider, functionsProvider);
  }

  public static AccountViewModel newInstance(AuthRepository authRepository,
      LocationService locationService, EventRepository eventRepository,
      UserRepository userRepository, PaymentRepository paymentRepository,
      FirebaseFunctions functions) {
    return new AccountViewModel(authRepository, locationService, eventRepository, userRepository, paymentRepository, functions);
  }
}
