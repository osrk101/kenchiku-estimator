package com.kenchiku_estimator.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kenchiku_estimator.model.MEstimate;

@Mapper
public interface EstimateMapper {

    // 全件取得(アカウント氏名を含む)
    List<MEstimate> findAll();

    // IDで取得(アカウント氏名を含む)
    MEstimate findById(int id);

    // 登録
    void createNewEstimate(MEstimate estimate);

    // 更新
    int updateEstimate(MEstimate estimate);

    // 削除
    int deleteEstimate(int id);

    // 見積番号の年月日で件数を取得
    int countByEstimateNumberPrefix(String prefix);

    // 検索ワードに該当する見積書を取得
    List<MEstimate> findBySearchWords(String searchWords);
}
