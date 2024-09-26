package com.ar.reminder.di


import android.content.Context
import androidx.room.Room
import com.ar.reminder.api.ApiService
import com.ar.reminder.api.ResponseInterceptor
import com.ar.reminder.roomdatabase.AppDatabase
import com.ar.reminder.roomdatabase.ListDao
import com.ar.reminder.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(provideOkHttpClient(ResponseInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: ResponseInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(4, TimeUnit.MINUTES)
            .connectTimeout(4, TimeUnit.MINUTES).build()
    }

    @Singleton
    @Provides
    fun providesServiceAPI(retrofitBuilder: Retrofit.Builder): ApiService {
        return retrofitBuilder.build().create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideResponseModelDao(db: AppDatabase): ListDao {
        return db.responseModelDao()
    }
}