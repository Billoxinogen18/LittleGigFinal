package com.littlegig.app.services;

import com.google.firebase.functions.FirebaseFunctions;
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
public final class FlutterwavePaymentService_Factory implements Factory<FlutterwavePaymentService> {
  private final Provider<FirebaseFunctions> functionsProvider;

  public FlutterwavePaymentService_Factory(Provider<FirebaseFunctions> functionsProvider) {
    this.functionsProvider = functionsProvider;
  }

  @Override
  public FlutterwavePaymentService get() {
    return newInstance(functionsProvider.get());
  }

  public static FlutterwavePaymentService_Factory create(
      Provider<FirebaseFunctions> functionsProvider) {
    return new FlutterwavePaymentService_Factory(functionsProvider);
  }

  public static FlutterwavePaymentService newInstance(FirebaseFunctions functions) {
    return new FlutterwavePaymentService(functions);
  }
}
