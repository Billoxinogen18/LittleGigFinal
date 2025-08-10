package com.littlegig.app.presentation.recaps;

import com.littlegig.app.data.repository.AuthRepository;
import com.littlegig.app.data.repository.EventRepository;
import com.littlegig.app.data.repository.RecapRepository;
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
public final class RecapsUploadViewModel_Factory implements Factory<RecapsUploadViewModel> {
  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<RecapRepository> recapRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public RecapsUploadViewModel_Factory(Provider<EventRepository> eventRepositoryProvider,
      Provider<RecapRepository> recapRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.recapRepositoryProvider = recapRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public RecapsUploadViewModel get() {
    return newInstance(eventRepositoryProvider.get(), recapRepositoryProvider.get(), authRepositoryProvider.get());
  }

  public static RecapsUploadViewModel_Factory create(
      Provider<EventRepository> eventRepositoryProvider,
      Provider<RecapRepository> recapRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new RecapsUploadViewModel_Factory(eventRepositoryProvider, recapRepositoryProvider, authRepositoryProvider);
  }

  public static RecapsUploadViewModel newInstance(EventRepository eventRepository,
      RecapRepository recapRepository, AuthRepository authRepository) {
    return new RecapsUploadViewModel(eventRepository, recapRepository, authRepository);
  }
}
