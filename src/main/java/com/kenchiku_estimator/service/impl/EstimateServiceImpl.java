package com.kenchiku_estimator.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kenchiku_estimator.form.EstimateItemForm;
import com.kenchiku_estimator.model.Estimate;
import com.kenchiku_estimator.model.EstimateItem;
import com.kenchiku_estimator.repository.EstimateMapper;
import com.kenchiku_estimator.service.EstimateItemService;
import com.kenchiku_estimator.service.EstimateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class EstimateServiceImpl implements EstimateService {

	@Autowired
	EstimateMapper estimateMapper;

	@Autowired
	EstimateItemService estimateItemService;

	@Autowired
	ModelMapper modelMapper;

	// 管理者ユーザーなら全見積書を取得、担当者ユーザーなら担当する見積書のみ取得
	@Override
	public List<Estimate> getEstimatesByUser(int createBy, boolean isAdmin, String searchWords) {
		log.info("Service ユーザーに応じた見積書を取得: isAdmin = {}, CreateBy = {}", isAdmin, createBy);

		if (isAdmin) {
			if (searchWords != null && !searchWords.isEmpty()) {
				return getSearchEstimatesAll(searchWords);
			} else {
				return getAllEstimates();
			}
		} else if (searchWords != null && !searchWords.isEmpty()) {
			return getSearchEstimateBycreateBy(createBy, searchWords);
		} else {
			return getEstimatesByCreateBy(createBy);
		}
	}

	//全見積書を取得
	@Override
	public List<Estimate> getAllEstimates() {
		log.info("Service 全見積書を取得");
		return estimateMapper.getAllEstimates();
	}

	// 担当する見積書を取得
	@Override
	public List<Estimate> getEstimatesByCreateBy(int createBy) {
		log.info("Service 担当する見積書を取得: {}", createBy);

		return estimateMapper.findByCreateBy(createBy);
	}

	// 検索ワードに該当する見積書を取得（管理者
	@Override
	public List<Estimate> getSearchEstimatesAll(String searchWords) {
		log.info("Service 検索ワードに該当する見積書を取得: {}", searchWords);

		return estimateMapper.findBySearchWords(searchWords);
	}

	// 検索ワードに該当する見積書を取得（担当者）
	@Override
	public List<Estimate> getSearchEstimateBycreateBy(int createBy, String searchWords) {
		log.info("Service 検索ワードに該当する見積書を取得: {}", searchWords);

		return estimateMapper.findBySearchWordsAndCreatedBy(createBy, searchWords);
	}

	// 該当するIDの見積書を1件取得
	@Override
	public Estimate getEstimateOne(int estimateId) {
		log.info("Service 該当するIDの見積書を取得");

		if (estimateId <= 0) {
			return null;
		}

		log.info("Service 見積書の取得を開始: ID = {}", estimateId);

		Estimate estimate = estimateMapper.findById(estimateId);
		if (estimate == null) {
			return null;
		}
		return estimate;

	}

	// 見積書番号を生成
	@Override
	public String generateEstimateNumber() {
		log.info("Service 新規見積書番号を生成");

		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		int count = estimateMapper.countByEstimateNumberPrefix(today);
		int sequence = count + 1;

		log.info("Service 見積書番号の生成に成功: {}-{}", today, String.format("%02d", sequence));

		return today + "-" + String.format("%02d", sequence);
	}

	// 見積書の新規作成
	@Override
	public void createNewEstimate(Estimate estimate) {
		log.info("Service 見積書の新規作成処理を実行: {}", estimate);

		estimateMapper.insertEstimate(estimate);

		log.info("Service 見積書の新規作成処理に成功: {}", estimate);
	}

	// 見積書の更新
	@Override
	public boolean updateEstimate(Estimate estimate) {
		log.info("Service 見積書の更新処理を実行 {}", estimate);

		boolean isUpdate = estimateMapper.updateEstimate(estimate);

		if (isUpdate) {
			return true;
		}
		return false;
	}

	// 見積書と見積書アイテムを一括保存
	@Transactional
	public void saveEstimateWithItems(Estimate estimate, List<EstimateItemForm> itemForms,
			boolean isNew) {

		if (isNew) {
			String estimateNumber = generateEstimateNumber();
			estimate.setEstimateNumber(estimateNumber);
			estimate.setId(0);

			createNewEstimate(estimate);
		} else {
			updateEstimate(estimate);
			estimateItemService.deleteEstimateItem(estimate.getId());
		}

		for (EstimateItemForm itemForm : itemForms) {
			EstimateItem item = modelMapper.map(itemForm, EstimateItem.class);
			item.setId(0);
			item.setEstimateId(estimate.getId());

			if (isNew || item.getId() == 0) {
				estimateItemService.createEstimateItem(item);
			} else {
				estimateItemService.updateEstimateItem(item);
			}
		}
	}

	// 見積書の削除
	@Override
	public boolean deleteEstimate(int id) {
		log.info("Service 見積書を削除処理を実行: ID = {}", id);

		boolean isDelete = estimateMapper.deleteEstimate(id);

		if (isDelete) {
			return true;
		}
		return true;
	}
}
