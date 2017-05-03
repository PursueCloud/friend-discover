package com.yo.friendis.common.common.service;

import org.springframework.stereotype.Service;

import com.yo.friendis.common.common.model.SysCode;

@Service
public class SysCodeService extends AbstractService<SysCode> {
	
	@Override
	public Class<?> getEntityClass() {
		return SysCode.class;
	}

}
