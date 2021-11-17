/*
 * Author: Seokjin Yoon
 * Filename: StoreController.java
 * Desc:
 */

package com.codrest.teriser.store;

import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codrest.teriser.utils.ApiUtils.success;

@RequestMapping("store")
@RequiredArgsConstructor
@RestController
public class StoreController {
    @GetMapping
    public ApiResult<String[]> findAllEndpoints() {
        String[] endpoints = new String[]{
                "GET /store",
                "GET /store/items",
                "GET /store/point",
                "GET /store/items/{itemId}",
                "POST /store/payment",
                "PUT /store/point",
                "DELETE /store/payment/{orderId}",
                "GET /store/payment/history",
                "GET /store/payment/history/{orderId}",
                "GET /store/purchase/{itemId}"
        };
        return success(endpoints);
    }
}
