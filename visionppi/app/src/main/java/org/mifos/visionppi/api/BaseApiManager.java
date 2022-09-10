package org.mifos.visionppi.api;

import javax.inject.Inject;
import org.mifos.visionppi.api.local.PreferencesHelper;
import org.mifos.visionppi.api.services.AuthenticationService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
public class BaseApiManager {

  private static Retrofit retrofit;
  private static AuthenticationService authenticationApi;

  @Inject
  public BaseApiManager(PreferencesHelper preferencesHelper) {
    createService(
        preferencesHelper.getBaseUrl(),
        preferencesHelper.getTenant(),
        preferencesHelper.getToken());
  }

  private static void init() {
    authenticationApi = createApi(AuthenticationService.class);
  }

  private static <T> T createApi(Class<T> clazz) {
    return retrofit.create(clazz);
  }

  public static void createService(String endpoint, String tenant, String authToken) {
    retrofit =
        new Retrofit.Builder()
            .baseUrl(new BaseURL().getUrl(endpoint))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(new VisionPPIOkHttpClient(tenant, authToken).getMifosOkHttpClient())
            .build();
    init();
  }

  public AuthenticationService getAuthenticationApi() {
    return authenticationApi;
  }
}
