package com.kenchiku_estimator.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kenchiku_estimator.model.MEstimateItem;

@Mapper
public interface EstimateItemMapper {

    // 見積書アイテムを新規作成
    void createEstimateItem(MEstimateItem estimateItem);

    // 該当の見積書アイテムを取得する
    public List<MEstimateItem> findByEstimateId(int id);

    // 見積書アイテムの削除をする
    void deleteEstimateItem(int id);
}
