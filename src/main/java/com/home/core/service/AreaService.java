package com.home.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.common.service.JpaServiceImpl;
import com.home.core.entity.Area;

@Service
@Transactional(readOnly = true)
public class AreaService extends JpaServiceImpl<Area, String> {

}
