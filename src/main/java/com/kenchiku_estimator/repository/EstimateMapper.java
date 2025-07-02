package com.kenchiku_estimator.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kenchiku_estimator.model.MEstimate;

@Mapper
public interface EstimateMapper {

    // 全件取得
    List<MEstimate> findAll();

    // IDで取得
    MEstimate findById(int id);

    // 登録
    void insert(MEstimate estimate);

    // 更新
    void update(MEstimate estimate);

    // 削除
    void delete(int id);
}

