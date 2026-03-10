package com.minbing.sample.facade.controller;

import com.minbing.common.assertion.AssertUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messageService")
public class MessageController {

    @RequestMapping(value = "/getMessage/{msgId}")
    public String getMessage(@PathVariable String msgId) {
        AssertUtil.isTrue(NumberUtils.isDigits(msgId));
        return "Message:" + msgId;
    }
}
