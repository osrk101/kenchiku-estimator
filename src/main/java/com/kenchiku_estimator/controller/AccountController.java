package com.kenchiku_estimator.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kenchiku_estimator.form.AccountForm;
import com.kenchiku_estimator.model.Account;
import com.kenchiku_estimator.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	ModelMapper modelMapper;

	// アカウント一覧画面を表示する
	@GetMapping
	public String getAccounts(Account account, Model model) {
		log.info("アカウント一覧画面を表示");

		List<Account> accountsList = accountService.getAllAccounts();
		log.info("アカウント一覧の件数: {}", accountsList.size());

		model.addAttribute("accountsList", accountsList);
		return "accounts";
	}

	// アカウント新規作成画面を表示する
	@GetMapping("/create")
	public String getAccountCreate(@ModelAttribute AccountForm accountForm, Model model) {
		log.info("アカウントの新規作成画面を表示");

		model.addAttribute("accountForm", accountForm);
		return "accounts/create";
	}

	// アカウント新規作成を実行する
	@PostMapping("/create")
	public String postAccountCreate(@Validated(AccountForm.Create.class) AccountForm accountForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		log.info("controller アカウント新規作成を実行");

		if (bindingResult.hasErrors()) {
			log.warn("アカウント新規作成の入力内容にエラー{}", bindingResult.getAllErrors());
			model.addAttribute("accountForm", accountForm);
			return "accounts/create";
		}

		//パスワードをハッシュ化して保存
		accountForm.setPassword(accountService.encodePassword(accountForm.getPassword()));
		Account account = modelMapper.map(accountForm, Account.class);
		accountService.createNewAccount(account);

		redirectAttributes.addFlashAttribute("successMessage", "アカウントの新規作成が完了。");
		redirectAttributes.addFlashAttribute("messageType", "success");

		return "redirect:/accounts";
	}

	// アカウント編集画面を表示する
	@GetMapping("/edit/{id}")
	public String getAccountEdit(@PathVariable int id, AccountForm accountForm,
			RedirectAttributes redirectAttributes, Model model) {

		log.info("controller アカウント編集画面を表示");

		Account account = accountService.getAccountOne(id);
		if (account == null) {
			log.warn("指定されたIDのアカウントが未存在", id);
			redirectAttributes.addAttribute("errorMessage", "指定されたIDのアカウントは存在しません。");
			redirectAttributes.addAttribute("messageType", "error");
			return "redirect:/accounts";
		}

		account.setPassword(null);
		setupAccountForm(account, accountForm, model);
		model.addAttribute("accountForm", accountForm);

		return "accounts/edit";
	}

	// アカウントの更新を実行する
	@PostMapping("/edit/{id}")
	public String postAccountEdit(@PathVariable int id, @Validated(AccountForm.Update.class)  AccountForm accountForm,
			BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

		log.info("controller アカウントの更新を実行");

		if (bindingResult.hasErrors()) {
			log.warn("アカウント更新の入力内容にエラー{}", bindingResult.getAllErrors());
			model.addAttribute("accountForm", accountForm);
			return "accounts/edit";
		}

		Account account = modelMapper.map(accountForm, Account.class);
		if (accountForm.getPassword() == null || accountForm.getPassword().isBlank()) {
			account.setPassword(null);
		}
		boolean updated= accountService.updateAccount(account);
		if (!updated) {
			redirectAttributes.addFlashAttribute("errorMessage", "アカウントの更新に失敗。");
			redirectAttributes.addFlashAttribute("messageType", "error");
			return "redirect:/accounts";
		}
		
		log.info("アカウントの更新が完了ID = {}", account.getId());

		redirectAttributes.addFlashAttribute("successMessage", "アカウントの更新が完了。");
		redirectAttributes.addFlashAttribute("messageType", "success");

		return "redirect:/accounts";
	}

	// アカウント削除確認画面を表示する
	@GetMapping("/confirmDelete/{id}")
	public String getAccountDelete(@PathVariable int id, RedirectAttributes redirectAttributes,
			Model model) {

		log.info("アカウント削除確認画面を表示");

		Account account = accountService.getAccountOne(id);
		if (account == null) {
			log.warn("指定されたIDのアカウントが未存在ID = {}", id);
			redirectAttributes.addFlashAttribute("errorMessage", "指定されたIDのアカウントは存在しません。");
			redirectAttributes.addFlashAttribute("messageType", "error");
			return "redirect:/accounts";
		}

		model.addAttribute("account", account);
		return "accounts/confirmDelete";
	}

	// アカウントの削除を実行する
	@PostMapping("/delete/{id}")
	public String postAccountDelete(@PathVariable int id, RedirectAttributes redirectAttributes,
			HttpServletRequest request,
			HttpServletResponse response) {

		log.info("controller アカウントの削除を実行");

		boolean estimateCount = accountService.deleteAccount(id);
		if (!estimateCount) {
			log.warn("担当している見積書が存在ID = {}", id);
			redirectAttributes.addFlashAttribute("message", "担当している見積書があるため、アカウントを削除できません。");
			redirectAttributes.addFlashAttribute("messageType", "error");
			return "redirect:/accounts";
		}
		// 自分のアカウントを削除した場合、ログアウトする
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getName() != null) {
			Object principal = auth.getPrincipal();
			Integer currentUserId = null;
			if (principal instanceof com.kenchiku_estimator.model.CustomUserDetails cud) {
				currentUserId = cud.getId();
			}
			if (currentUserId != null && currentUserId == id) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
				return "redirect:/index";
			}
		}

		log.info("アカウントの削除が完了ID = {}", id);
		redirectAttributes.addFlashAttribute("message", "アカウントの削除が完了しました。");
		redirectAttributes.addFlashAttribute("messageType", "success");

		return "redirect:/accounts";
	}

	// --------------------- 以下、共通メソッド ---------------------

	// AccountFormをセットアップする
	public void setupAccountForm(Account account, AccountForm accountForm, Model model) {
		modelMapper.map(account, accountForm);
		model.addAttribute("accountForm", accountForm);
	}
}
