package com.yo.friendis.common.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yo.friendis.common.common.service.ConfigService;

@RestController
@RequestMapping("config")
public class ConfigController {
	@Autowired
	private ConfigService configService;

}
