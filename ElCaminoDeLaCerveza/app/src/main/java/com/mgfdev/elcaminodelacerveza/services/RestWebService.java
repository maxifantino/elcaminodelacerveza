package com.mgfdev.elcaminodelacerveza.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxi on 28/10/2017.
 */
public class RestWebService {
    RestHelper restHelper;
    private static int READ_TO = 15000;
    private static int CONNECT_TO = 15000;
    public static String AUTHENTICATION_USERNAME_KEY= "userName";
    public static String AUTHENTICATION_PASSWORD_KEY= "password";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static RestWebService instance;

    public static RestWebService getInstance(){
        if (instance == null){
            instance = new RestWebService();
        }
        return instance;
    }

    private RestWebService(){
        restHelper = RestHelper.getInstance();
    }

    public Map<String, String>getAuthenticationMap(String username, String password){
        Map<String, String> authenticationMap = new HashMap<>();
        authenticationMap.put(RestWebService.AUTHENTICATION_USERNAME_KEY, username);
        authenticationMap.put(RestWebService.AUTHENTICATION_PASSWORD_KEY, password);
        return authenticationMap;
    }
    public String doGet (String url, Map<String, String> parameters) throws IOException {
        String queryString =( parameters!= null && !parameters.isEmpty()) ? "?" + restHelper.buildQueryString(parameters): "";
        return executeGet(url + queryString, null);
    }

    public String doGet (String url,  Map<String, String> parameters, Map<String, String> authParams) throws IOException {
        String queryString =( parameters!= null && !parameters.isEmpty()) ? "?" + restHelper.buildQueryString(parameters): "";
        return executeGet(url + queryString, authParams);
    }

    public String doPost (String url, Map<String, String> parameters) throws IOException{
        String postParams =( parameters!= null && !parameters.isEmpty()) ? "?" + restHelper.buildQueryString(parameters): "";
        return executePost(url, postParams, null);
    }

    public String doPost (String url, Map<String, String> parameters, Map<String,String> authParams) throws IOException{
        String postParams =( parameters!= null && !parameters.isEmpty()) ? "?" + restHelper.buildQueryString(parameters): "";
        return executePost(url, postParams, authParams);
    }

    private String executeGet(String url, Map<String, String> authenticationHeader) throws IOException {
        String getResult = "";
        URL urlObject = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        if (authenticationHeader != null && !authenticationHeader.isEmpty()){
            String baseAuthStr = authenticationHeader.get(AUTHENTICATION_USERNAME_KEY) + ":" +
                    authenticationHeader.get(AUTHENTICATION_PASSWORD_KEY);
            connection.addRequestProperty("Authorization", "Basic " + baseAuthStr);
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }
        int responseCode = connection.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            getResult = response.toString();

        } else {
            System.out.println("GET request not worked");
        }
        return getResult;
    }

    private String executePost(String url, String parameters, Map<String, String> authenticationHeader) throws IOException { //If you want to use get method to hit server
        URL urlObject = new URL(url);
        String postResponse = "";
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setReadTimeout(READ_TO);
        connection.setConnectTimeout(CONNECT_TO);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (authenticationHeader != null && !authenticationHeader.isEmpty()){
            String baseAuthStr = authenticationHeader.get(AUTHENTICATION_USERNAME_KEY) + ":" +
                    authenticationHeader.get(AUTHENTICATION_PASSWORD_KEY);
            connection.addRequestProperty("Authorization", "Basic " + baseAuthStr);
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }

        OutputStream os = connection.getOutputStream();
        os.write(parameters.getBytes());
        os.flush();
        os.close();
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            postResponse = response.toString();
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
        return postResponse;
    }

}
