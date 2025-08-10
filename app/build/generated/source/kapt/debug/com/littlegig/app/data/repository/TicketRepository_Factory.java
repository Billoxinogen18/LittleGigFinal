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
public final class TicketRepository_Factory implements Factory<TicketRepository> {
  private final Provider<FirebaseFirestore> firestoreProvider;

  public TicketRepository_Factory(Provider<FirebaseFirestore> firestoreProvider) {
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public TicketRepository get() {
    return newInstance(firestoreProvider.get());
  }

  public static TicketRepository_Factory create(Provider<FirebaseFirestore> firestoreProvider) {
    return new TicketRepository_Factory(firestoreProvider);
  }

  public static TicketRepository newInstance(FirebaseFirestore firestore) {
    return new TicketRepository(firestore);
  }
}
