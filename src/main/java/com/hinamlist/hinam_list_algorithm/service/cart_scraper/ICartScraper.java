package com.hinamlist.hinam_list_algorithm.service.cart_scraper;

import com.hinamlist.hinam_list_algorithm.service.cart_scraper.exception.APIResponseException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public interface ICartScraper {
    public JSONObject getCartObject(Map<Integer, Float> idQuantityMap) throws IOException, APIResponseException, InterruptedException;
}
