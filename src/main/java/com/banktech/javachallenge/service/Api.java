package com.banktech.javachallenge.service;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *
 */
public class Api {
    public static final String MOVE = "moveSubmarine";
    public static final String SONAR = "sonarSubmarine";
    public static final String SHOOT = "shootSubmarine";
    public static final String LIST_GAMES = "listGames";
    public static final String JOIN_GAME = "joinGame";
    public static final String START_GAME = "startGame";
    public static final String LOAD_GAME_INFO = "loadGameInfo";
    public static final String LOAD_OWN_SUBMARINES = "loadOwnSubmarines";
    public static final String EXTENDED_SONAR = "extendedSonar";

    private static String baseUrl = "195.228.45.100";
    private static Integer port = 8080;
    private static Retrofit retrofit;
    private static String token = "9DD0961787BF82E4B2FD97B206D8D5D8";

    private static GameService gameService;
    private static SubmarineService submarineService;


    private Api() {
        setUpRetrofit();
    }

    private Api(String baseUrl, Integer port) {
        Api.baseUrl = baseUrl;
        Api.port = port;
        setUpRetrofit();
    }

    public static void initialize(String baseUrl, Integer port) {
        new Api(baseUrl, port);
    }

    public static void initialize() {
        new Api();
    }

    private void setUpRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + baseUrl + ":" + port + "/jc16-srv/")
                .client(clientFactory())
                .addConverterFactory(gsonFactory())
                .build();
        System.out.println("retrofit initialized with baseUrl: " + retrofit.baseUrl());
        gameService = retrofit.create(GameService.class);
        submarineService = retrofit.create(SubmarineService.class);
    }


    private GsonConverterFactory gsonFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder().create()
        );
    }

    private OkHttpClient clientFactory() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.interceptors().add(chain -> {
            String token1 = getToken();
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();
            requestBuilder.header("Accept", "application/json");
            if (!Strings.isNullOrEmpty(token1)) {
                requestBuilder.addHeader("TEAMTOKEN", token1);
            }

            Request request = requestBuilder.build();
            return chain.proceed(request);

        });

        return builder.build();
    }

    private static String getToken() {
        return token;
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

    public static GameService gameService() {
        return gameService;
    }

    public static SubmarineService submarineService() {
        return submarineService;
    }
}
