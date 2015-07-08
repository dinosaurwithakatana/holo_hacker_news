package io.dwak.holohackernews.app.dagger.module;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.dwak.holohackernews.app.network.HackerNewsService;
import io.dwak.holohackernews.app.network.LoginService;
import io.dwak.holohackernews.app.network.UserService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(includes = {AppModule.class, OkClientModule.class})
public class NetworkServiceModule {

    @Provides
    @Singleton
    HackerNewsService provideService(@Named("retrofit-loglevel")RestAdapter.LogLevel logLevel,
                                     @Named("gson") Gson gson,
                                     @Named("okclient") OkClient okClient){
        return new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setLogLevel(logLevel)
                .setClient(okClient)
                .setEndpoint("https://whispering-fortress-7282.herokuapp.com/")
                .build()
                .create(HackerNewsService.class);
    }

    @Provides
    @Singleton
    LoginService providesLoginService(@Named("retrofit-loglevel")RestAdapter.LogLevel logLevel,
                                      @Named("gson") Gson gson,
                                      @Named("okclient") OkClient okClient) {
        return new RestAdapter.Builder()
                .setClient(okClient)
                .setLogLevel(logLevel)
                .setConverter(new GsonConverter(gson))
                .setEndpoint("https://news.ycombinator.com")
                .build()
                .create(LoginService.class);
    }

    @Provides
    @Singleton
    UserService providesUserService(@Named("retrofit-loglevel")RestAdapter.LogLevel logLevel,
                                    @Named("gson") Gson gson,
                                    @Named("okclient")OkClient okClient){
        return new RestAdapter.Builder()
                .setClient(okClient)
                .setLogLevel(logLevel)
                .setConverter(new GsonConverter(gson))
                .setEndpoint("https://news.ycombinator.com")
                .build()
                .create(UserService.class);
    }
}
