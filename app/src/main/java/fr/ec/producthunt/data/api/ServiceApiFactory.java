package fr.ec.producthunt.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceApiFactory {

  private static final String BASE_URL = "https://api.producthunt.com/v1/";
  private static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();


  public static  <T> T create(Class<T> service) {
    return retrofit.create(service);
  }
}
