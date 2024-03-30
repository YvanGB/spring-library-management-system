package com.esmt.lms.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.esmt.lms.common.Constants;
import com.esmt.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.esmt.lms.model.User;

@Service
public class UserService implements UserDetailsService {

	private final Constants constants = new Constants();

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public List<User> getAllUsers() {
		return userRepository.findAllByOrderByDisplayNameAsc();
	}
	
	
	public List<User> getAllActiveUsers() { return
		userRepository.findAllByActiveOrderByDisplayNameAsc(1);
	}
	
	public User getByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public User getById(Long id) {
		return userRepository.findById(id).get();
	}
	
	public User addNew(User user) {
		user.setPassword( passwordEncoder.encode(user.getPassword()) );
		user.setCreatedDate( new Date() );
		user.setLastModifiedDate( user.getCreatedDate() );
		user.setActive(1);
		return userRepository.save(user);
	}
	
	public User update(User user) {
		user.setLastModifiedDate( new Date() );
		return userRepository.save( user );
	}
	
	public void delete(User user) {
		userRepository.delete(user);
	}
	
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
		if (!userOptional.isPresent()) {
			// If the user is not found, throw a UsernameNotFoundException
			throw new UsernameNotFoundException("L'utilisateur n'a pas été trouvé: " + username);
		}
		// Convert the User entity to a UserDetails object
		User user = userOptional.get();
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				user.getRole().equals("ROLE_ADMIN") ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) : Collections.singletonList(new SimpleGrantedAuthority("ROLE_LIBRARIAN"))
		);
	}
}
