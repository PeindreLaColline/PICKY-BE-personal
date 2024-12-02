package com.ureca.picky_be.base;

import com.ureca.picky_be.global.success.SuccessCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @GetMapping("/success-code-simple")
    public SuccessCode getSimpleSuccessCode() {
        return SuccessCode.GENERAL_SUCCESS;
    }

}
