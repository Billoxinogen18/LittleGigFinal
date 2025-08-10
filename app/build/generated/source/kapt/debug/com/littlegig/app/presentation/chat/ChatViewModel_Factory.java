package com.littlegig.app.presentation.chat;

import com.littlegig.app.data.repository.AuthRepository;
import com.littlegig.app.data.repository.ChatRepository;
import com.littlegig.app.data.repository.UserRepository;
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<ChatRepository> chatRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public ChatViewModel_Factory(Provider<ChatRepository> chatRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.chatRepositoryProvider = chatRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(chatRepositoryProvider.get(), userRepositoryProvider.get(), authRepositoryProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<ChatRepository> chatRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new ChatViewModel_Factory(chatRepositoryProvider, userRepositoryProvider, authRepositoryProvider);
  }

  public static ChatViewModel newInstance(ChatRepository chatRepository,
      UserRepository userRepository, AuthRepository authRepository) {
    return new ChatViewModel(chatRepository, userRepository, authRepository);
  }
}
