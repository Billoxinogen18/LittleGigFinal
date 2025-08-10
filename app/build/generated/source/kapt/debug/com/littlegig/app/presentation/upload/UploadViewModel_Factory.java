package com.littlegig.app.presentation.upload;

import com.littlegig.app.data.repository.AuthRepository;
import com.littlegig.app.data.repository.EventRepository;
import com.littlegig.app.services.PlacesService;
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
public final class UploadViewModel_Factory implements Factory<UploadViewModel> {
  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<PlacesService> placesServiceProvider;

  public UploadViewModel_Factory(Provider<EventRepository> eventRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<PlacesService> placesServiceProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.placesServiceProvider = placesServiceProvider;
  }

  @Override
  public UploadViewModel get() {
    return newInstance(eventRepositoryProvider.get(), authRepositoryProvider.get(), placesServiceProvider.get());
  }

  public static UploadViewModel_Factory create(Provider<EventRepository> eventRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<PlacesService> placesServiceProvider) {
    return new UploadViewModel_Factory(eventRepositoryProvider, authRepositoryProvider, placesServiceProvider);
  }

  public static UploadViewModel newInstance(EventRepository eventRepository,
      AuthRepository authRepository, PlacesService placesService) {
    return new UploadViewModel(eventRepository, authRepository, placesService);
  }
}
