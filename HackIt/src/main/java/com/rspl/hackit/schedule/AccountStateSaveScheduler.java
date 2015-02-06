package com.rspl.hackit.schedule;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rspl.hackit.controller.IndexController;

@Component
public class AccountStateSaveScheduler {

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void loadLastStateOnStartup() {
		try {
			ObjectInputStream fin = new ObjectInputStream(new FileInputStream(
					"state.data"));
			IndexController.userToBalance
					.putAll((ConcurrentHashMap<String, Double>) fin
							.readObject());
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Scheduled(fixedDelay = 7000)
	public void saveStateToFileSystem() throws IOException {
		Map<String, Double> balMap = new ConcurrentHashMap<String, Double>(
				IndexController.userToBalance);
		ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(
				"state.data"));
		fos.writeObject(balMap);
		fos.close();
	}
}
