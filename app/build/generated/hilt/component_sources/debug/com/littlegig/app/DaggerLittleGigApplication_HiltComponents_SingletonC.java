package com.littlegig.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.littlegig.app.data.repository.AuthRepository;
import com.littlegig.app.data.repository.ChatRepository;
import com.littlegig.app.data.repository.ConfigRepository;
import com.littlegig.app.data.repository.EventRepository;
import com.littlegig.app.data.repository.NotificationRepository;
import com.littlegig.app.data.repository.PaymentRepository;
import com.littlegig.app.data.repository.RecapRepository;
import com.littlegig.app.data.repository.SharingRepository;
import com.littlegig.app.data.repository.TicketRepository;
import com.littlegig.app.data.repository.UserRepository;
import com.littlegig.app.di.AppModule_ProvideChatMediaServiceFactory;
import com.littlegig.app.di.AppModule_ProvideConfigRepositoryFactory;
import com.littlegig.app.di.AppModule_ProvideContextFactory;
import com.littlegig.app.di.AppModule_ProvideFirebaseAuthFactory;
import com.littlegig.app.di.AppModule_ProvideFirebaseFirestoreFactory;
import com.littlegig.app.di.AppModule_ProvideFirebaseFunctionsFactory;
import com.littlegig.app.di.AppModule_ProvideFirebaseStorageFactory;
import com.littlegig.app.di.AppModule_ProvideNetworkMonitorFactory;
import com.littlegig.app.di.AppModule_ProvideNotificationRepositoryFactory;
import com.littlegig.app.di.AppModule_ProvidePhoneAuthServiceFactory;
import com.littlegig.app.di.AppModule_ProvidePhoneNumberServiceFactory;
import com.littlegig.app.presentation.MainActivity;
import com.littlegig.app.presentation.MainActivity_MembersInjector;
import com.littlegig.app.presentation.account.AccountViewModel;
import com.littlegig.app.presentation.account.AccountViewModel_HiltModules;
import com.littlegig.app.presentation.auth.AuthViewModel;
import com.littlegig.app.presentation.auth.AuthViewModel_HiltModules;
import com.littlegig.app.presentation.business.BusinessDashboardViewModel;
import com.littlegig.app.presentation.business.BusinessDashboardViewModel_HiltModules;
import com.littlegig.app.presentation.chat.ChatDetailsViewModel;
import com.littlegig.app.presentation.chat.ChatDetailsViewModel_HiltModules;
import com.littlegig.app.presentation.chat.ChatSearchViewModel;
import com.littlegig.app.presentation.chat.ChatSearchViewModel_HiltModules;
import com.littlegig.app.presentation.chat.ChatViewModel;
import com.littlegig.app.presentation.chat.ChatViewModel_HiltModules;
import com.littlegig.app.presentation.events.EventDetailsViewModel;
import com.littlegig.app.presentation.events.EventDetailsViewModel_HiltModules;
import com.littlegig.app.presentation.events.EventsViewModel;
import com.littlegig.app.presentation.events.EventsViewModel_HiltModules;
import com.littlegig.app.presentation.inbox.InboxViewModel;
import com.littlegig.app.presentation.inbox.InboxViewModel_HiltModules;
import com.littlegig.app.presentation.map.MapViewModel;
import com.littlegig.app.presentation.map.MapViewModel_HiltModules;
import com.littlegig.app.presentation.payments.PaymentsViewModel;
import com.littlegig.app.presentation.payments.PaymentsViewModel_HiltModules;
import com.littlegig.app.presentation.payments.ReceiptsViewModel;
import com.littlegig.app.presentation.payments.ReceiptsViewModel_HiltModules;
import com.littlegig.app.presentation.recaps.RecapsUploadViewModel;
import com.littlegig.app.presentation.recaps.RecapsUploadViewModel_HiltModules;
import com.littlegig.app.presentation.recaps.RecapsViewerViewModel;
import com.littlegig.app.presentation.recaps.RecapsViewerViewModel_HiltModules;
import com.littlegig.app.presentation.settings.SettingsViewModel;
import com.littlegig.app.presentation.settings.SettingsViewModel_HiltModules;
import com.littlegig.app.presentation.tickets.TicketsViewModel;
import com.littlegig.app.presentation.tickets.TicketsViewModel_HiltModules;
import com.littlegig.app.presentation.upload.UploadViewModel;
import com.littlegig.app.presentation.upload.UploadViewModel_HiltModules;
import com.littlegig.app.service.LittleGigFirebaseMessagingService;
import com.littlegig.app.service.LittleGigFirebaseMessagingService_MembersInjector;
import com.littlegig.app.services.ChatMediaService;
import com.littlegig.app.services.LocationService;
import com.littlegig.app.services.PhoneAuthService;
import com.littlegig.app.services.PhoneNumberService;
import com.littlegig.app.services.PlacesService;
import com.littlegig.app.utils.NetworkMonitor;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerLittleGigApplication_HiltComponents_SingletonC {
  private DaggerLittleGigApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public LittleGigApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements LittleGigApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public LittleGigApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements LittleGigApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public LittleGigApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements LittleGigApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public LittleGigApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements LittleGigApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public LittleGigApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements LittleGigApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public LittleGigApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements LittleGigApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public LittleGigApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements LittleGigApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public LittleGigApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends LittleGigApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends LittleGigApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends LittleGigApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends LittleGigApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(17).put(LazyClassKeyProvider.com_littlegig_app_presentation_account_AccountViewModel, AccountViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_auth_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_business_BusinessDashboardViewModel, BusinessDashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_chat_ChatDetailsViewModel, ChatDetailsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_chat_ChatSearchViewModel, ChatSearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_chat_ChatViewModel, ChatViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_events_EventDetailsViewModel, EventDetailsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_events_EventsViewModel, EventsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_inbox_InboxViewModel, InboxViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_map_MapViewModel, MapViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_payments_PaymentsViewModel, PaymentsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_recaps_RecapsUploadViewModel, RecapsUploadViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_recaps_RecapsViewerViewModel, RecapsViewerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_payments_ReceiptsViewModel, ReceiptsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_tickets_TicketsViewModel, TicketsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_littlegig_app_presentation_upload_UploadViewModel, UploadViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPaymentRepository(instance, singletonCImpl.paymentRepositoryProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_littlegig_app_presentation_events_EventDetailsViewModel = "com.littlegig.app.presentation.events.EventDetailsViewModel";

      static String com_littlegig_app_presentation_upload_UploadViewModel = "com.littlegig.app.presentation.upload.UploadViewModel";

      static String com_littlegig_app_presentation_auth_AuthViewModel = "com.littlegig.app.presentation.auth.AuthViewModel";

      static String com_littlegig_app_presentation_inbox_InboxViewModel = "com.littlegig.app.presentation.inbox.InboxViewModel";

      static String com_littlegig_app_presentation_settings_SettingsViewModel = "com.littlegig.app.presentation.settings.SettingsViewModel";

      static String com_littlegig_app_presentation_recaps_RecapsUploadViewModel = "com.littlegig.app.presentation.recaps.RecapsUploadViewModel";

      static String com_littlegig_app_presentation_recaps_RecapsViewerViewModel = "com.littlegig.app.presentation.recaps.RecapsViewerViewModel";

      static String com_littlegig_app_presentation_chat_ChatSearchViewModel = "com.littlegig.app.presentation.chat.ChatSearchViewModel";

      static String com_littlegig_app_presentation_events_EventsViewModel = "com.littlegig.app.presentation.events.EventsViewModel";

      static String com_littlegig_app_presentation_account_AccountViewModel = "com.littlegig.app.presentation.account.AccountViewModel";

      static String com_littlegig_app_presentation_business_BusinessDashboardViewModel = "com.littlegig.app.presentation.business.BusinessDashboardViewModel";

      static String com_littlegig_app_presentation_payments_PaymentsViewModel = "com.littlegig.app.presentation.payments.PaymentsViewModel";

      static String com_littlegig_app_presentation_map_MapViewModel = "com.littlegig.app.presentation.map.MapViewModel";

      static String com_littlegig_app_presentation_chat_ChatDetailsViewModel = "com.littlegig.app.presentation.chat.ChatDetailsViewModel";

      static String com_littlegig_app_presentation_chat_ChatViewModel = "com.littlegig.app.presentation.chat.ChatViewModel";

      static String com_littlegig_app_presentation_payments_ReceiptsViewModel = "com.littlegig.app.presentation.payments.ReceiptsViewModel";

      static String com_littlegig_app_presentation_tickets_TicketsViewModel = "com.littlegig.app.presentation.tickets.TicketsViewModel";

      @KeepFieldType
      EventDetailsViewModel com_littlegig_app_presentation_events_EventDetailsViewModel2;

      @KeepFieldType
      UploadViewModel com_littlegig_app_presentation_upload_UploadViewModel2;

      @KeepFieldType
      AuthViewModel com_littlegig_app_presentation_auth_AuthViewModel2;

      @KeepFieldType
      InboxViewModel com_littlegig_app_presentation_inbox_InboxViewModel2;

      @KeepFieldType
      SettingsViewModel com_littlegig_app_presentation_settings_SettingsViewModel2;

      @KeepFieldType
      RecapsUploadViewModel com_littlegig_app_presentation_recaps_RecapsUploadViewModel2;

      @KeepFieldType
      RecapsViewerViewModel com_littlegig_app_presentation_recaps_RecapsViewerViewModel2;

      @KeepFieldType
      ChatSearchViewModel com_littlegig_app_presentation_chat_ChatSearchViewModel2;

      @KeepFieldType
      EventsViewModel com_littlegig_app_presentation_events_EventsViewModel2;

      @KeepFieldType
      AccountViewModel com_littlegig_app_presentation_account_AccountViewModel2;

      @KeepFieldType
      BusinessDashboardViewModel com_littlegig_app_presentation_business_BusinessDashboardViewModel2;

      @KeepFieldType
      PaymentsViewModel com_littlegig_app_presentation_payments_PaymentsViewModel2;

      @KeepFieldType
      MapViewModel com_littlegig_app_presentation_map_MapViewModel2;

      @KeepFieldType
      ChatDetailsViewModel com_littlegig_app_presentation_chat_ChatDetailsViewModel2;

      @KeepFieldType
      ChatViewModel com_littlegig_app_presentation_chat_ChatViewModel2;

      @KeepFieldType
      ReceiptsViewModel com_littlegig_app_presentation_payments_ReceiptsViewModel2;

      @KeepFieldType
      TicketsViewModel com_littlegig_app_presentation_tickets_TicketsViewModel2;
    }
  }

  private static final class ViewModelCImpl extends LittleGigApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AccountViewModel> accountViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<BusinessDashboardViewModel> businessDashboardViewModelProvider;

    private Provider<ChatDetailsViewModel> chatDetailsViewModelProvider;

    private Provider<ChatSearchViewModel> chatSearchViewModelProvider;

    private Provider<ChatViewModel> chatViewModelProvider;

    private Provider<EventDetailsViewModel> eventDetailsViewModelProvider;

    private Provider<EventsViewModel> eventsViewModelProvider;

    private Provider<InboxViewModel> inboxViewModelProvider;

    private Provider<MapViewModel> mapViewModelProvider;

    private Provider<PaymentsViewModel> paymentsViewModelProvider;

    private Provider<RecapsUploadViewModel> recapsUploadViewModelProvider;

    private Provider<RecapsViewerViewModel> recapsViewerViewModelProvider;

    private Provider<ReceiptsViewModel> receiptsViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<TicketsViewModel> ticketsViewModelProvider;

    private Provider<UploadViewModel> uploadViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.accountViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.businessDashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.chatDetailsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.chatSearchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.chatViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.eventDetailsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.eventsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.inboxViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.mapViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.paymentsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.recapsUploadViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.recapsViewerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
      this.receiptsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 13);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 14);
      this.ticketsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 15);
      this.uploadViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 16);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(17).put(LazyClassKeyProvider.com_littlegig_app_presentation_account_AccountViewModel, ((Provider) accountViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_auth_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_business_BusinessDashboardViewModel, ((Provider) businessDashboardViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_chat_ChatDetailsViewModel, ((Provider) chatDetailsViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_chat_ChatSearchViewModel, ((Provider) chatSearchViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_chat_ChatViewModel, ((Provider) chatViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_events_EventDetailsViewModel, ((Provider) eventDetailsViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_events_EventsViewModel, ((Provider) eventsViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_inbox_InboxViewModel, ((Provider) inboxViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_map_MapViewModel, ((Provider) mapViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_payments_PaymentsViewModel, ((Provider) paymentsViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_recaps_RecapsUploadViewModel, ((Provider) recapsUploadViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_recaps_RecapsViewerViewModel, ((Provider) recapsViewerViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_payments_ReceiptsViewModel, ((Provider) receiptsViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_tickets_TicketsViewModel, ((Provider) ticketsViewModelProvider)).put(LazyClassKeyProvider.com_littlegig_app_presentation_upload_UploadViewModel, ((Provider) uploadViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_littlegig_app_presentation_map_MapViewModel = "com.littlegig.app.presentation.map.MapViewModel";

      static String com_littlegig_app_presentation_payments_PaymentsViewModel = "com.littlegig.app.presentation.payments.PaymentsViewModel";

      static String com_littlegig_app_presentation_business_BusinessDashboardViewModel = "com.littlegig.app.presentation.business.BusinessDashboardViewModel";

      static String com_littlegig_app_presentation_chat_ChatViewModel = "com.littlegig.app.presentation.chat.ChatViewModel";

      static String com_littlegig_app_presentation_recaps_RecapsUploadViewModel = "com.littlegig.app.presentation.recaps.RecapsUploadViewModel";

      static String com_littlegig_app_presentation_inbox_InboxViewModel = "com.littlegig.app.presentation.inbox.InboxViewModel";

      static String com_littlegig_app_presentation_account_AccountViewModel = "com.littlegig.app.presentation.account.AccountViewModel";

      static String com_littlegig_app_presentation_settings_SettingsViewModel = "com.littlegig.app.presentation.settings.SettingsViewModel";

      static String com_littlegig_app_presentation_recaps_RecapsViewerViewModel = "com.littlegig.app.presentation.recaps.RecapsViewerViewModel";

      static String com_littlegig_app_presentation_events_EventsViewModel = "com.littlegig.app.presentation.events.EventsViewModel";

      static String com_littlegig_app_presentation_upload_UploadViewModel = "com.littlegig.app.presentation.upload.UploadViewModel";

      static String com_littlegig_app_presentation_payments_ReceiptsViewModel = "com.littlegig.app.presentation.payments.ReceiptsViewModel";

      static String com_littlegig_app_presentation_chat_ChatSearchViewModel = "com.littlegig.app.presentation.chat.ChatSearchViewModel";

      static String com_littlegig_app_presentation_tickets_TicketsViewModel = "com.littlegig.app.presentation.tickets.TicketsViewModel";

      static String com_littlegig_app_presentation_chat_ChatDetailsViewModel = "com.littlegig.app.presentation.chat.ChatDetailsViewModel";

      static String com_littlegig_app_presentation_events_EventDetailsViewModel = "com.littlegig.app.presentation.events.EventDetailsViewModel";

      static String com_littlegig_app_presentation_auth_AuthViewModel = "com.littlegig.app.presentation.auth.AuthViewModel";

      @KeepFieldType
      MapViewModel com_littlegig_app_presentation_map_MapViewModel2;

      @KeepFieldType
      PaymentsViewModel com_littlegig_app_presentation_payments_PaymentsViewModel2;

      @KeepFieldType
      BusinessDashboardViewModel com_littlegig_app_presentation_business_BusinessDashboardViewModel2;

      @KeepFieldType
      ChatViewModel com_littlegig_app_presentation_chat_ChatViewModel2;

      @KeepFieldType
      RecapsUploadViewModel com_littlegig_app_presentation_recaps_RecapsUploadViewModel2;

      @KeepFieldType
      InboxViewModel com_littlegig_app_presentation_inbox_InboxViewModel2;

      @KeepFieldType
      AccountViewModel com_littlegig_app_presentation_account_AccountViewModel2;

      @KeepFieldType
      SettingsViewModel com_littlegig_app_presentation_settings_SettingsViewModel2;

      @KeepFieldType
      RecapsViewerViewModel com_littlegig_app_presentation_recaps_RecapsViewerViewModel2;

      @KeepFieldType
      EventsViewModel com_littlegig_app_presentation_events_EventsViewModel2;

      @KeepFieldType
      UploadViewModel com_littlegig_app_presentation_upload_UploadViewModel2;

      @KeepFieldType
      ReceiptsViewModel com_littlegig_app_presentation_payments_ReceiptsViewModel2;

      @KeepFieldType
      ChatSearchViewModel com_littlegig_app_presentation_chat_ChatSearchViewModel2;

      @KeepFieldType
      TicketsViewModel com_littlegig_app_presentation_tickets_TicketsViewModel2;

      @KeepFieldType
      ChatDetailsViewModel com_littlegig_app_presentation_chat_ChatDetailsViewModel2;

      @KeepFieldType
      EventDetailsViewModel com_littlegig_app_presentation_events_EventDetailsViewModel2;

      @KeepFieldType
      AuthViewModel com_littlegig_app_presentation_auth_AuthViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.littlegig.app.presentation.account.AccountViewModel 
          return (T) new AccountViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.locationServiceProvider.get(), singletonCImpl.eventRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.paymentRepositoryProvider.get(), singletonCImpl.provideFirebaseFunctionsProvider.get());

          case 1: // com.littlegig.app.presentation.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.providePhoneAuthServiceProvider.get());

          case 2: // com.littlegig.app.presentation.business.BusinessDashboardViewModel 
          return (T) new BusinessDashboardViewModel(singletonCImpl.eventRepositoryProvider.get(), singletonCImpl.ticketRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 3: // com.littlegig.app.presentation.chat.ChatDetailsViewModel 
          return (T) new ChatDetailsViewModel(singletonCImpl.chatRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.provideChatMediaServiceProvider.get(), singletonCImpl.provideNotificationRepositoryProvider.get());

          case 4: // com.littlegig.app.presentation.chat.ChatSearchViewModel 
          return (T) new ChatSearchViewModel(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.chatRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 5: // com.littlegig.app.presentation.chat.ChatViewModel 
          return (T) new ChatViewModel(singletonCImpl.chatRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 6: // com.littlegig.app.presentation.events.EventDetailsViewModel 
          return (T) new EventDetailsViewModel(singletonCImpl.eventRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get(), singletonCImpl.paymentRepositoryProvider.get(), singletonCImpl.provideNotificationRepositoryProvider.get());

          case 7: // com.littlegig.app.presentation.events.EventsViewModel 
          return (T) new EventsViewModel(singletonCImpl.eventRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.sharingRepositoryProvider.get(), singletonCImpl.provideConfigRepositoryProvider.get());

          case 8: // com.littlegig.app.presentation.inbox.InboxViewModel 
          return (T) new InboxViewModel(singletonCImpl.provideNotificationRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 9: // com.littlegig.app.presentation.map.MapViewModel 
          return (T) new MapViewModel(singletonCImpl.eventRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.provideFirebaseFunctionsProvider.get());

          case 10: // com.littlegig.app.presentation.payments.PaymentsViewModel 
          return (T) new PaymentsViewModel(singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 11: // com.littlegig.app.presentation.recaps.RecapsUploadViewModel 
          return (T) new RecapsUploadViewModel(singletonCImpl.eventRepositoryProvider.get(), singletonCImpl.recapRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 12: // com.littlegig.app.presentation.recaps.RecapsViewerViewModel 
          return (T) new RecapsViewerViewModel(singletonCImpl.recapRepositoryProvider.get());

          case 13: // com.littlegig.app.presentation.payments.ReceiptsViewModel 
          return (T) new ReceiptsViewModel(singletonCImpl.paymentRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 14: // com.littlegig.app.presentation.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.authRepositoryProvider.get());

          case 15: // com.littlegig.app.presentation.tickets.TicketsViewModel 
          return (T) new TicketsViewModel(singletonCImpl.ticketRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 16: // com.littlegig.app.presentation.upload.UploadViewModel 
          return (T) new UploadViewModel(singletonCImpl.eventRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.placesServiceProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends LittleGigApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends LittleGigApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectLittleGigFirebaseMessagingService(LittleGigFirebaseMessagingService arg0) {
      injectLittleGigFirebaseMessagingService2(arg0);
    }

    @CanIgnoreReturnValue
    private LittleGigFirebaseMessagingService injectLittleGigFirebaseMessagingService2(
        LittleGigFirebaseMessagingService instance) {
      LittleGigFirebaseMessagingService_MembersInjector.injectNotificationRepository(instance, singletonCImpl.provideNotificationRepositoryProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends LittleGigApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<NetworkMonitor> provideNetworkMonitorProvider;

    private Provider<FirebaseAuth> provideFirebaseAuthProvider;

    private Provider<FirebaseFirestore> provideFirebaseFirestoreProvider;

    private Provider<FirebaseFunctions> provideFirebaseFunctionsProvider;

    private Provider<PaymentRepository> paymentRepositoryProvider;

    private Provider<Context> provideContextProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<LocationService> locationServiceProvider;

    private Provider<FirebaseStorage> provideFirebaseStorageProvider;

    private Provider<EventRepository> eventRepositoryProvider;

    private Provider<PhoneNumberService> providePhoneNumberServiceProvider;

    private Provider<UserRepository> userRepositoryProvider;

    private Provider<PhoneAuthService> providePhoneAuthServiceProvider;

    private Provider<TicketRepository> ticketRepositoryProvider;

    private Provider<ChatRepository> chatRepositoryProvider;

    private Provider<ChatMediaService> provideChatMediaServiceProvider;

    private Provider<NotificationRepository> provideNotificationRepositoryProvider;

    private Provider<SharingRepository> sharingRepositoryProvider;

    private Provider<ConfigRepository> provideConfigRepositoryProvider;

    private Provider<RecapRepository> recapRepositoryProvider;

    private Provider<PlacesService> placesServiceProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideNetworkMonitorProvider = DoubleCheck.provider(new SwitchingProvider<NetworkMonitor>(singletonCImpl, 0));
      this.provideFirebaseAuthProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseAuth>(singletonCImpl, 1));
      this.provideFirebaseFirestoreProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseFirestore>(singletonCImpl, 2));
      this.provideFirebaseFunctionsProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseFunctions>(singletonCImpl, 4));
      this.paymentRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PaymentRepository>(singletonCImpl, 3));
      this.provideContextProvider = DoubleCheck.provider(new SwitchingProvider<Context>(singletonCImpl, 6));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 5));
      this.locationServiceProvider = DoubleCheck.provider(new SwitchingProvider<LocationService>(singletonCImpl, 7));
      this.provideFirebaseStorageProvider = DoubleCheck.provider(new SwitchingProvider<FirebaseStorage>(singletonCImpl, 9));
      this.eventRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<EventRepository>(singletonCImpl, 8));
      this.providePhoneNumberServiceProvider = DoubleCheck.provider(new SwitchingProvider<PhoneNumberService>(singletonCImpl, 11));
      this.userRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserRepository>(singletonCImpl, 10));
      this.providePhoneAuthServiceProvider = DoubleCheck.provider(new SwitchingProvider<PhoneAuthService>(singletonCImpl, 12));
      this.ticketRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TicketRepository>(singletonCImpl, 13));
      this.chatRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ChatRepository>(singletonCImpl, 14));
      this.provideChatMediaServiceProvider = DoubleCheck.provider(new SwitchingProvider<ChatMediaService>(singletonCImpl, 15));
      this.provideNotificationRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<NotificationRepository>(singletonCImpl, 16));
      this.sharingRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SharingRepository>(singletonCImpl, 17));
      this.provideConfigRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ConfigRepository>(singletonCImpl, 18));
      this.recapRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<RecapRepository>(singletonCImpl, 19));
      this.placesServiceProvider = DoubleCheck.provider(new SwitchingProvider<PlacesService>(singletonCImpl, 20));
    }

    @Override
    public void injectLittleGigApplication(LittleGigApplication arg0) {
      injectLittleGigApplication2(arg0);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private LittleGigApplication injectLittleGigApplication2(LittleGigApplication instance) {
      LittleGigApplication_MembersInjector.injectNetworkMonitor(instance, provideNetworkMonitorProvider.get());
      LittleGigApplication_MembersInjector.injectAuth(instance, provideFirebaseAuthProvider.get());
      LittleGigApplication_MembersInjector.injectFirestore(instance, provideFirebaseFirestoreProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.littlegig.app.utils.NetworkMonitor 
          return (T) AppModule_ProvideNetworkMonitorFactory.provideNetworkMonitor(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.google.firebase.auth.FirebaseAuth 
          return (T) AppModule_ProvideFirebaseAuthFactory.provideFirebaseAuth();

          case 2: // com.google.firebase.firestore.FirebaseFirestore 
          return (T) AppModule_ProvideFirebaseFirestoreFactory.provideFirebaseFirestore();

          case 3: // com.littlegig.app.data.repository.PaymentRepository 
          return (T) new PaymentRepository(singletonCImpl.provideFirebaseFunctionsProvider.get());

          case 4: // com.google.firebase.functions.FirebaseFunctions 
          return (T) AppModule_ProvideFirebaseFunctionsFactory.provideFirebaseFunctions();

          case 5: // com.littlegig.app.data.repository.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideFirebaseAuthProvider.get(), singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideContextProvider.get());

          case 6: // android.content.Context 
          return (T) AppModule_ProvideContextFactory.provideContext(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.littlegig.app.services.LocationService 
          return (T) new LocationService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.provideFirebaseFunctionsProvider.get());

          case 8: // com.littlegig.app.data.repository.EventRepository 
          return (T) new EventRepository(singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseStorageProvider.get(), singletonCImpl.provideContextProvider.get());

          case 9: // com.google.firebase.storage.FirebaseStorage 
          return (T) AppModule_ProvideFirebaseStorageFactory.provideFirebaseStorage();

          case 10: // com.littlegig.app.data.repository.UserRepository 
          return (T) new UserRepository(singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseStorageProvider.get(), singletonCImpl.provideContextProvider.get(), singletonCImpl.providePhoneNumberServiceProvider.get());

          case 11: // com.littlegig.app.services.PhoneNumberService 
          return (T) AppModule_ProvidePhoneNumberServiceFactory.providePhoneNumberService();

          case 12: // com.littlegig.app.services.PhoneAuthService 
          return (T) AppModule_ProvidePhoneAuthServiceFactory.providePhoneAuthService(singletonCImpl.provideFirebaseAuthProvider.get());

          case 13: // com.littlegig.app.data.repository.TicketRepository 
          return (T) new TicketRepository(singletonCImpl.provideFirebaseFirestoreProvider.get());

          case 14: // com.littlegig.app.data.repository.ChatRepository 
          return (T) new ChatRepository(singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseFunctionsProvider.get());

          case 15: // com.littlegig.app.services.ChatMediaService 
          return (T) AppModule_ProvideChatMediaServiceFactory.provideChatMediaService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideFirebaseStorageProvider.get());

          case 16: // com.littlegig.app.data.repository.NotificationRepository 
          return (T) AppModule_ProvideNotificationRepositoryFactory.provideNotificationRepository(singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideContextProvider.get());

          case 17: // com.littlegig.app.data.repository.SharingRepository 
          return (T) new SharingRepository(singletonCImpl.provideFirebaseFunctionsProvider.get());

          case 18: // com.littlegig.app.data.repository.ConfigRepository 
          return (T) AppModule_ProvideConfigRepositoryFactory.provideConfigRepository(singletonCImpl.provideFirebaseFirestoreProvider.get());

          case 19: // com.littlegig.app.data.repository.RecapRepository 
          return (T) new RecapRepository(singletonCImpl.provideFirebaseFirestoreProvider.get(), singletonCImpl.provideFirebaseStorageProvider.get(), singletonCImpl.provideContextProvider.get());

          case 20: // com.littlegig.app.services.PlacesService 
          return (T) new PlacesService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
