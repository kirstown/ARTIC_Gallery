package dev.kirstenbaker.gallery

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.kirstenbaker.gallery.data.GalleryDataRepository
import dev.kirstenbaker.gallery.data.GalleryDataRepositoryImpl
import dev.kirstenbaker.gallery.data.remote.GalleryDataApi
import dev.kirstenbaker.gallery.data.remote.RequestInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val articApiBaseUrl = "https://api.artic.edu/"

@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    @Provides
    fun provideRequestInterceptor(): RequestInterceptor = RequestInterceptor

    @Provides
    fun provideOkHttpClient(interceptor: RequestInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    fun provideMoshi() = Moshi.Builder()
        .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(articApiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(moshi)
            )
            .build()
    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(GalleryDataApi::class.java)

    @Provides
    fun provideRepository(galleryDataApi: GalleryDataApi): GalleryDataRepository =
        GalleryDataRepositoryImpl(galleryDataApi)
}