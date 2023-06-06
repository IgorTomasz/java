/**
 *
 *  @author Tomaszewski Igor S25077
 *
 */

package zad1;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Service{

    private String country;
    private String city;
    private String currencyCode;

    public String getCity() {
        return city;
    }

    public Service(String country){
        this.country=country;
        //getCurrencyOfCountry();
    }

    public String getWeather(String city){
        this.city=city;
        String lon="";
        String lat="";
        String coordinatesURL = "https://api.openweathermap.org/geo/1.0/direct?q="+city+"&appid=fae9d09160d2dee80cc2378688d26ef3";
        String jsonWeather="";

        try(InputStream inputCoordinate = new URL(coordinatesURL).openConnection().getInputStream();
            BufferedReader bufferedCoordinate = new BufferedReader(new InputStreamReader(inputCoordinate));
            ) {
            String jsonCoordinate = bufferedCoordinate.lines().collect(Collectors.joining(System.lineSeparator()));

            Gson gson = new Gson();

            CityInfo cityInfo = null;
            JsonArray array = gson.fromJson(jsonCoordinate, JsonArray.class);
            for(JsonElement element:array){
                cityInfo = gson.fromJson(element,CityInfo.class);
            }

            lat= String.valueOf(cityInfo.lat);
            lon= String.valueOf(cityInfo.lon);
            String weatherURL = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=fae9d09160d2dee80cc2378688d26ef3&units=metric";

            try (InputStream inputWeather = new URL(weatherURL).openConnection().getInputStream();
                 BufferedReader bufferedWeather = new BufferedReader(new InputStreamReader(inputWeather));
                 ){
                jsonWeather = bufferedWeather.lines().collect(Collectors.joining(System.lineSeparator()));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonWeather;
    }

    private String getCurrencyOfCountry(){
        Map<String,String> countries = new HashMap<>();

        Locale outLocale = Locale.forLanguageTag("en_GB");
        Locale inLocale = Locale.forLanguageTag("pl-PL");

        for(Locale l : Locale.getAvailableLocales()){
            String temp = l.getDisplayCountry(inLocale);
            if(temp.equals(country)){
                country=l.getDisplayCountry(outLocale);
            }
        }

        for(String el : Locale.getISOCountries()){
            Locale l = new Locale("",el);
            countries.put(l.getDisplayCountry(),el);
        }

        Locale locale = new Locale("",countries.get(country));
        Currency currency = Currency.getInstance(locale);
        return currency.getCurrencyCode();
    }

    public double getRateFor(String currencyCode){
        this.currencyCode=currencyCode;
        String countryCurrency = getCurrencyOfCountry();
        String currencyRateUrl = "https://api.exchangerate.host/latest?base="+currencyCode;
        CurrencyInfo currencyInfo = null;

        try(InputStream inputCoordinate = new URL(currencyRateUrl).openConnection().getInputStream();
            BufferedReader bufferedCoordinate = new BufferedReader(new InputStreamReader(inputCoordinate));
        ) {
            String jsonCurrency = bufferedCoordinate.lines().collect(Collectors.joining(System.lineSeparator()));

            Gson gson = new Gson();
            currencyInfo = gson.fromJson(jsonCurrency,CurrencyInfo.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currencyInfo.rates.get(countryCurrency);
    }

    public double getNBPRate(){
        String countryCurrency = getCurrencyOfCountry();
        if(countryCurrency.equals("PLN")){
            return 1;
        }
        String currencyRateUrl = String.format("http://api.nbp.pl/api/exchangerates/rates/a/%s/?format=json",countryCurrency);
        NBP nbpInfo = null;

        try(InputStream inputCoordinate = new URL(currencyRateUrl).openConnection().getInputStream();
            BufferedReader bufferedCoordinate = new BufferedReader(new InputStreamReader(inputCoordinate));
        ) {
            String jsonNbp = bufferedCoordinate.lines().collect(Collectors.joining(System.lineSeparator()));

            Gson gson = new Gson();
            nbpInfo = gson.fromJson(jsonNbp,NBP.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nbpInfo.rates.get(0).mid;
    }
}

class NBP{
    List<NBPInfo> rates;
}

class NBPInfo{
    double mid;
}

class CurrencyInfo{
    Map<String,Double> rates;
}

class CityInfo{
    String name;
    double lat;
    double lon;
}
