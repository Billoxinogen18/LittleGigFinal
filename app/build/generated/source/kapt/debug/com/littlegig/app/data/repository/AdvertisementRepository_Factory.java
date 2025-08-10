package com.littlegig.app.data.repository;

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
public final class AdvertisementRepository_Factory implements Factory<AdvertisementRepository> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  public AdvertisementRepository_Factory(Provider<FirebaseFirestore> firestoreProvider) {
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public AdvertisementRepository get() {
    return newInstance(firestoreProvider.get());
  }

  public static AdvertisementRepository_Factory create(
      Provider<FirebaseFirestore> firestoreProvider) {
    return new AdvertisementRepository_Factory(firestoreProvider);
  }

  public static AdvertisementRepository newInstance(FirebaseFirestore firestore) {
    return new AdvertisementRepository(firestore);
  }
}
