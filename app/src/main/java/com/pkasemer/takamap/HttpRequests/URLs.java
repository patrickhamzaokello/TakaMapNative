package com.pkasemer.takamap.HttpRequests;
/**
 * Created by Belal on 9/5/2017.
 */

public class URLs {

    private static final String ROOT_URL = "https://zodongofoods.com/mobile/api/v1/";
//    private static final String ROOT_URL = "http://192.168.0.118:8080/projects/ZodongoFoodsAPI/";
    public static final String URL_REGISTER = ROOT_URL + "users/account?apicall=signup";
    public static final String URL_LOGIN= ROOT_URL + "users/account.php?apicall=login";

    public static final String URL_SEND_ORDER = ROOT_URL + "orders/create_order.php";

}
