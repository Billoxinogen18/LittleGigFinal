package com.littlegig.app.presentation.business;

import com.littlegig.app.data.repository.AuthRepository;
import com.littlegig.app.data.repository.EventRepository;
import com.littlegig.app.data.repository.TicketRepository;
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
public final class BusinessDashboardViewModel_Factory implements Factory<BusinessDashboardViewModel> {
  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<TicketRepository> ticketRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public BusinessDashboardViewModel_Factory(Provider<EventRepository> eventRepositoryProvider,
      Provider<TicketRepository> ticketRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.ticketRepositoryProvider = ticketRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public BusinessDashboardViewModel get() {
    return newInstance(eventRepositoryProvider.get(), ticketRepositoryProvider.get(), authRepositoryProvider.get());
  }

  public static BusinessDashboardViewModel_Factory create(
      Provider<EventRepository> eventRepositoryProvider,
      Provider<TicketRepository> ticketRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new BusinessDashboardViewModel_Factory(eventRepositoryProvider, ticketRepositoryProvider, authRepositoryProvider);
  }

  public static BusinessDashboardViewModel newInstance(EventRepository eventRepository,
      TicketRepository ticketRepository, AuthRepository authRepository) {
    return new BusinessDashboardViewModel(eventRepository, ticketRepository, authRepository);
  }
}
