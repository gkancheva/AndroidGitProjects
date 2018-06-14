package com.udacity.gradle.builditbigger.backend;

import com.company.jokeproviderlib.JokeProvider;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    @ApiMethod(name = "provideJoke")
    public MyBean provideJoke() {
        MyBean response = new MyBean();
        String joke = JokeProvider.provideJoke();
        response.setData(joke);
        return response;
    }

}
