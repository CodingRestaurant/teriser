/*
 * Author: Seokjin Yoon
 * Filename: ProductController.java
 * Desc:
 */

package com.codrest.teriser.store.items;

import com.codrest.teriser.errors.NotFoundException;
import com.codrest.teriser.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.codrest.teriser.utils.ApiUtils.success;

@RequestMapping("store/items")
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResult<List<ProductDto>> findAll() {
        return success(
                productService.findAll()
                        .stream()
                        .map(ProductDto::new)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("{id}")
    public ApiResult<ProductDto> findById(@PathVariable Long id) {
        return success(
                productService.findById(id)
                        .map(ProductDto::new)
                        .orElseThrow(() -> new NotFoundException("Could not found product for " + id + "."))
        );
    }
}
