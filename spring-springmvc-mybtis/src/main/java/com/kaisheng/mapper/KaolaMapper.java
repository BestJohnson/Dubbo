package com.kaisheng.mapper;

import com.kaisheng.entity.Kaola;

import java.util.List;
import java.util.Map;

public interface KaolaMapper {
    Kaola findById(Integer id);

    List<Kaola> findAllWithType();

    void save(Kaola kaola);

    void deleteById(Integer id);

    void update(Kaola kaola);

    List<Kaola> findAllByParam(Map<String, Object> map);
}
