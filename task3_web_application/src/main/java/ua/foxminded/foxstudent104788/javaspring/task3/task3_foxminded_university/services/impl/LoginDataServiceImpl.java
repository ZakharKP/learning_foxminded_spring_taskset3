package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.LoginDataRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;

@Service
@Transactional
@Log4j2
public class LoginDataServiceImpl implements LoginDataService, UserDetailsService {

	@Autowired
	private LoginDataRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Start research of user with name=" + username);
		Optional<LoginData> loginOptional = getUser(username);
		if (loginOptional.isPresent()) {
			LoginData login = loginOptional.get();
			List<GrantedAuthority> authorities = new ArrayList<>();

			authorities.add(new SimpleGrantedAuthority(login.getRole().name()));

			log.info("User " + login + "was finded");
			return new User(login.getUserName(), login.getPassword(), authorities);
		} else {
			throw new UsernameNotFoundException("User not found username=" + username);
		}
	}

	@Override
	public LoginData saveNewUser(LoginData loginData) {
		if (loginData == null) {
			log.info("Can't save - user is NULL");
			return null;
		}
		log.info("Start saving new user: " + loginData.toString());

		return repository.save(loginData);
	}

	@Override
	public List<LoginData> saveAllUsers(List<LoginData> loginDatas) {
		if (loginDatas == null) {
			log.info("Can't save - users is NULL");
			return Collections.emptyList();
		}
		log.info(String.format("Start saving List of %s users:", loginDatas.size()));

		List<LoginData> savedUsers = repository.saveAll(loginDatas);

		log.info(savedUsers.size() + " new users was saved");
		return savedUsers;

	}

	@Override
	public Optional<LoginData> getUser(String userName) {
		log.info("Start searching of LoginData by name=" + userName);
		return repository.findById(userName);
	}

	@Override
	public List<LoginData> getAll() {
		log.info("Start research of All Users");
		return repository.findAll();
	}

	@Override
	public LoginData updateUser(LoginData loginData) {
		if (loginData == null) {
			log.info("Can't update - user is NULL");
			return null;
		}
		log.info("Starting update " + loginData.toString());
		return repository.save(loginData);
	}

	@Override
	public void deleteUser(LoginData loginData) {
		if (loginData == null) {
			log.info("Can't delete - user is NULL");
			return;
		}
		log.info("Start deleting user: " + loginData.toString());

		repository.delete(loginData);
	}

	@Override
	public void deleteListOfUsers(List<LoginData> loginDatas) {
		if (loginDatas == null) {
			log.info("Can't delete - list of users is NULL");
			return;
		}
		log.info(String.format("Start deleting list of %s users", loginDatas.size()));

		repository.deleteAll(loginDatas);
	}

	@Override
	public long countUsers() {
		log.info("Counting users");
		return repository.count();
	}

	@Override
	public LoginData setNewRole(String userName, Role role) {
		log.info("Start setting role {}, to user {}", role, userName);
		LoginData login;
		Optional<LoginData> loginOpt = getUser(userName);
		if (loginOpt.isPresent()) {
			login = loginOpt.get();
			login.setRole(role);
			return updateUser(login);
		}
		throw new UsernameNotFoundException("User not found username=" + userName);

	}

	@Override
	public boolean isUserNameUnique(String userName) {
		log.info("Check is user name ({}) unique", userName);

		return !repository.findById(userName).isPresent();
	}

	@Override
	public boolean setNewPassword(String userName, String password) {
		log.info("Set new password to user with userName={}", userName);

		Optional<LoginData> userOptional = repository.findById(userName);
		if (userOptional.isPresent()) {
			LoginData user = userOptional.get();
			user.setPassword(password);
			updateUser(user);
			return true;
		}
		return false;
	}

}
