package org.mifos.visionppi.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import org.jetbrains.annotations.Nullable;
import org.mifos.visionppi.R;
import org.mifos.visionppi.api.BaseApiManager;
import org.mifos.visionppi.api.DataManager;
import org.mifos.visionppi.api.local.PreferencesHelper;
import org.mifos.visionppi.injection.ApplicationContext;
import org.mifos.visionppi.models.User;
import org.mifos.visionppi.presenters.base.BasePresenter;
import org.mifos.visionppi.ui.views.LoginView;
import org.mifos.visionppi.utils.Constants;
import org.mifos.visionppi.utils.MFErrorParser;
import org.mifos.visionppi.utils.PrefManager;
import retrofit2.HttpException;

/**
 * @author yashk2000
 * @since 22/06/2020
 */
public class LoginPresenter extends BasePresenter<LoginView> {

  private final DataManager dataManager;
  private PreferencesHelper preferencesHelper;
  private CompositeDisposable compositeDisposable;
  private Activity activity;

  /**
   * Initialises the LoginPresenter by automatically injecting an instance of {@link DataManager}
   * and {@link Context}.
   *
   * @param dataManager DataManager class that provides access to the data via the API.
   * @param context Context of the view attached to the presenter. In this case it is that of an
   *     {@link androidx.appcompat.app.AppCompatActivity}
   */
  @Inject
  public LoginPresenter(
      @Nullable DataManager dataManager,
      @Nullable @ApplicationContext Context context,
      Activity activity) {
    super(context);
    this.dataManager = dataManager;
    preferencesHelper = this.dataManager.getPreferencesHelper();
    compositeDisposable = new CompositeDisposable();
    this.activity = activity;
  }

  @Override
  public void attachView(LoginView mvpView) {
    super.attachView(mvpView);
  }

  @Override
  public void detachView() {
    super.detachView();
    compositeDisposable.clear();
  }

  /**
   * This method validates the username and password entered by the user and reports any errors that
   * might exists in any of the inputs. If there are no errors, then we attempt to authenticate the
   * user from the server and then persist the authentication data if we successfully authenticate
   * the credentials or notify the view about any errors.
   *
   * @param username Username of the user trying to login.
   * @param password Password of the user trying to login.
   */
  public void login(final String username, final String password) {
    checkViewAttached();
    if (isCredentialsValid(username, password)) {
      getMvpView().showProgress();
      compositeDisposable.add(
          dataManager
              .login(username, password)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
              .subscribeWith(
                  new DisposableObserver<User>() {
                    @Override
                    public void onComplete() {}

                    @Override
                    public void onError(Throwable e) {
                      getMvpView().hideProgress();
                      String errorMessage;
                      try {
                        if (e instanceof HttpException) {
                          if (((HttpException) e).code() == 503) {
                            getMvpView()
                                .showMessage(getContext().getString(R.string.error_server_down));
                          } else {
                            errorMessage = ((HttpException) e).response().errorBody().string();
                            getMvpView()
                                .showMessage(
                                    MFErrorParser.parseError(errorMessage).getDeveloperMessage());
                          }
                        }
                      } catch (Throwable throwable) {
                        RxJavaPlugins.getErrorHandler();
                      }
                    }

                    @Override
                    public void onNext(User user) {
                      if (user != null) {
                        final String userName = user.getUsername();
                        final long userID = user.getUserId();
                        final String authToken =
                            Constants.BASIC + user.getBase64EncodedAuthenticationKey();
                        saveAuthenticationTokenForSession(user, userName, userID, authToken);
                        getMvpView().onLoginSuccess(userName);
                      } else {
                        getMvpView().hideProgress();
                      }
                    }
                  }));
    }
  }

  private boolean isCredentialsValid(final String username, final String password) {
    boolean credentialValid = true;
    final Resources resources = getContext().getResources();
    final String correctUsername = username.replaceFirst("\\s++$", "").trim();
    if (username == null || username.matches("\\s*") || username.isEmpty()) {
      getMvpView()
          .showUsernameError(
              getContext()
                  .getString(
                      R.string.error_validation_blank, getContext().getString(R.string.username)));
      credentialValid = false;
    } else if (username.length() < 5) {
      getMvpView()
          .showUsernameError(
              getContext()
                  .getString(
                      R.string.error_validation_minimum_chars,
                      resources.getString(R.string.username),
                      resources.getInteger(R.integer.username_minimum_length)));
      credentialValid = false;
    } else if (correctUsername.contains(" ")) {
      getMvpView()
          .showUsernameError(
              getContext()
                  .getString(
                      R.string.error_validation_cannot_contain_spaces,
                      resources.getString(R.string.username),
                      getContext().getString(R.string.not_contain_username)));
      credentialValid = false;
    } else {
      getMvpView().clearUsernameError();
    }

    if (password == null || password.isEmpty()) {
      getMvpView()
          .showPasswordError(
              getContext()
                  .getString(
                      R.string.error_validation_blank, getContext().getString(R.string.password)));
      credentialValid = false;
    } else if (password.length() < 6) {
      getMvpView()
          .showPasswordError(
              getContext()
                  .getString(
                      R.string.error_validation_minimum_chars,
                      resources.getString(R.string.password),
                      resources.getInteger(R.integer.password_minimum_length)));
      credentialValid = false;
    } else {
      getMvpView().clearPasswordError();
    }

    return credentialValid;
  }

  /**
   * Save the authentication token from the server and the user ID. The authentication token would
   * be used for accessing the authenticated APIs.
   *
   * @param userID - The userID of the user to be saved.
   * @param authToken - The authentication token to be saved.
   */
  private void saveAuthenticationTokenForSession(
      User user, String userName, long userID, String authToken) {
    preferencesHelper.setUserName(userName);
    preferencesHelper.setUserId(userID);
    preferencesHelper.saveToken(authToken);
    PrefManager mPrefManager = new PrefManager();
    mPrefManager.saveUser(user, getContext(), activity);
    reInitializeService();
  }

  private void reInitializeService() {
    BaseApiManager.createService(
        preferencesHelper.getBaseUrl(),
        preferencesHelper.getTenant(),
        preferencesHelper.getToken());
  }
}
