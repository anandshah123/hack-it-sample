package com.rspl.hackit.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.EvictingQueue;
import com.rspl.hackit.dto.Message;

@Controller
public class IndexController {

	@SuppressWarnings("serial")
	public static final Map<String, Double> userToBalance = new ConcurrentHashMap<String, Double>() {
		{
			put("team-1", 10000d);
			put("team-2", 10000d);
			put("team-3", 10000d);
			put("team-4", 10000d);
			put("team-5", 10000d);
		}
	};

	public static final Map<String, HttpSession> activeSessions = new ConcurrentHashMap<>();

	public static final EvictingQueue<Message> mq = EvictingQueue.create(20);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpSession session, Model model) {
		SecurityContext context = (SecurityContext) session
				.getAttribute("SPRING_SECURITY_CONTEXT");

		User user = (User) context.getAuthentication().getPrincipal();
		model.addAttribute("user", user);
		model.addAttribute("balance", userToBalance.get(user.getUsername()));
		activeSessions.put(user.getUsername() + " - " + session.getId(),
				session);
		model.addAttribute(
				"acno",
				user.getUsername().substring(
						user.getUsername().indexOf("-") + 1)
						+ new SimpleDateFormat("MMYYYYdd").format(new Date()));
		session.setAttribute("otp", generateOTP(6));
		return "index";
	}

	@RequestMapping(value = "/transferFund")
	public synchronized String transferFund(@RequestParam String acno,
			@RequestParam String otp, @RequestParam Double balance,
			HttpSession session) {
		if (StringUtils.equals(otp, (String) session.getAttribute("otp"))
				&& StringUtils.isNotBlank(acno)
				&& acno.substring(1).equals(
						new SimpleDateFormat("MMYYYYdd").format(new Date()))
				&& acno.length() > 1 && balance > 0) {
			String targetAcno = "team-" + acno.substring(0, 1);
			SecurityContext context = (SecurityContext) session
					.getAttribute("SPRING_SECURITY_CONTEXT");
			User user = (User) context.getAuthentication().getPrincipal();
			String sourceAcNo = user.getUsername();
			if (userToBalance.containsKey(sourceAcNo)
					&& userToBalance.containsKey(targetAcno)
					&& userToBalance.get(sourceAcNo) >= balance) {
				userToBalance.put(sourceAcNo, userToBalance.get(sourceAcNo)
						- balance);
				userToBalance.put(targetAcno, userToBalance.get(targetAcno)
						+ balance);
				System.out.println("Balance - " + balance + " transfered from "
						+ sourceAcNo + " to " + targetAcno + " at "
						+ new Date());
			}
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/loadChat", method = RequestMethod.GET)
	public synchronized String loadChat(Model model, HttpSession session) {
		Boolean reloadRequired = (Boolean) session
				.getAttribute("reloadRequired");
		if (reloadRequired != null && reloadRequired) {
			model.addAttribute("reloadRequired", reloadRequired);
			session.removeAttribute("reloadRequired");
		}
		model.addAttribute("msgs", mq);
		return "chat";
	}

	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	public synchronized @ResponseBody
	String sendMsg(@RequestParam String message, HttpSession session) {
		SecurityContext context = (SecurityContext) session
				.getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User) context.getAuthentication().getPrincipal();
		Message msg = new Message(user.getUsername(), message);
		System.out.println("Adding - " + msg);
		mq.add(msg);
		return "success";
	}

	private String generateOTP(int i) {
		String otp = "";
		for (int j = 0; j < i; j++) {
			otp += new Random().nextInt(10);
		}
		return otp;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void login() {
	}

}
