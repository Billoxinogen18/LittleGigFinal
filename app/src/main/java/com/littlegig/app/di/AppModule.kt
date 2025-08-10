package com.littlegig.app.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.littlegig.app.services.FlutterwavePaymentService
import com.littlegig.app.data.repository.NotificationRepository
import com.littlegig.app.data.repository.ConfigRepository
import com.littlegig.app.utils.ErrorHandler
import com.littlegig.app.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.littlegig.app.services.PhoneNumberService
import com.littlegig.app.services.ContactsService
import com.littlegig.app.services.PhoneAuthService
import com.littlegig.app.services.ChatMediaService

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    
    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions = FirebaseFunctions.getInstance()
    
    @Provides
    @Singleton
    fun provideNotificationRepository(
        firestore: FirebaseFirestore,
        context: Context
    ): NotificationRepository = NotificationRepository(firestore, context)
    
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideConfigRepository(
        firestore: FirebaseFirestore
    ): ConfigRepository = ConfigRepository(firestore)
    
    @Provides
    @Singleton
    fun provideErrorHandler(@ApplicationContext context: Context): ErrorHandler {
        return ErrorHandler(context)
    }
    
    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }

    @Provides
    @Singleton
    fun providePhoneNumberService(): PhoneNumberService = PhoneNumberService()

    @Provides
    @Singleton
    fun provideContactsService(
        @ApplicationContext context: Context,
        phoneNumberService: PhoneNumberService
    ): ContactsService = ContactsService(context, phoneNumberService)

    @Provides
    @Singleton
    fun providePhoneAuthService(auth: FirebaseAuth): PhoneAuthService = PhoneAuthService(auth)

    @Provides
    @Singleton
    fun provideChatMediaService(@ApplicationContext context: Context, storage: FirebaseStorage): ChatMediaService =
        ChatMediaService(storage, context.contentResolver)

    @Provides
    @Singleton
    fun provideLinkPreviewService(): com.littlegig.app.services.LinkPreviewService = com.littlegig.app.services.LinkPreviewService()
}