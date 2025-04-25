package com.example.BunBlog.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.BunBlog.dto.request.UserRequestDTO;
import com.example.BunBlog.dto.response.AuthReponseDTO;
import com.example.BunBlog.dto.response.UserReponseDTO;
import com.example.BunBlog.exception.ValidationException;
import com.example.BunBlog.model.User;
import com.example.BunBlog.model.UserHistory;
import com.example.BunBlog.repository.UserRepository;
import com.example.BunBlog.security.SecurityUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final UserHistoryService userHistoryService;
    private Cloudinary cloudinary;

    // 1: Nomally logical

    public User saveUser(User user){
        return this.userRepository.save(user);
    }

    public void deleteUser(UUID id){
        this.userRepository.deleteById(id);
    }

    public User findByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
    
    public User findByUsername(String name){
        return this.userRepository.findByUsername(name);
    }

    public Optional<User> findById(UUID id){
        return this.userRepository.findById(id);
    }

    public List<User> fetchAllUsers(){
        return this.userRepository.findAll();
    }

    public boolean existsByEmail(String email){
        return this.userRepository.existsByEmail(email);
    }   
    
    public User updateUser(UserRequestDTO user) throws Exception{
        Map<String, List<String>> errorMap = new HashMap<>();

        int countDown = 7; // 7 days
        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime currentTime = LocalDateTime.now();
     
        String userId = this.securityUtil.getCurrentUser().isPresent() ? this.securityUtil.getCurrentUser().get() : "";
        User currentUser = this.findById(UUID.fromString(userId)).get();
 
        UserHistory history = this.userHistoryService.findUserHistoryByUser(currentUser);
        if(history == null){
            history = new UserHistory();
            history.setUser(currentUser);
            history.setChange_time_username(creationTime.minusDays(countDown));
            history.setChange_time_address(creationTime.minusDays(countDown));
        }
        
        //Update username
        if (user.getUsername() != null && user.getUsername().equals(currentUser.getUsername()) && !user.getUsername().isEmpty()) {
            errorMap.computeIfAbsent("username", k -> new ArrayList<>()).add("Tên trùng với tên cũ");
        }

        if(!history.getChange_time_username().plusDays(countDown).minusSeconds(1).isBefore(currentTime) && user.getUsername() != null  && !user.getUsername().isEmpty()){
            Duration duration = Duration.between(currentTime, history.getChange_time_username().plusDays(countDown));
            errorMap.computeIfAbsent("username", k -> new ArrayList<>()).add("Bạn cần đợi " + duration.toDays() + " ngày nữa để đổi tên");  
        }
        
        if(user.getUsername() != null && !user.getUsername().isEmpty()) {
            currentUser.setUsername(user.getUsername());
            history.setChange_time_username(creationTime);
        }

         //Update address
        if(user.getAddress() != null && user.getAddress().equals(currentUser.getAddress()) && !user.getAddress().isEmpty()){
            errorMap.computeIfAbsent("address", k -> new ArrayList<>()).add("Địa chỉ trùng với địa chỉ cũ");
        }

        if(history.getChange_time_address() != null && !history.getChange_time_address().plusDays(countDown).minusSeconds(1).isBefore(currentTime) && user.getAddress() != null && !user.getAddress().isEmpty()){
            Duration duration = Duration.between(currentTime, history.getChange_time_address().plusDays(countDown));
            errorMap.computeIfAbsent("address", k -> new ArrayList<>()).add("Bạn cần đợi " + duration.toDays() + " ngày nữa để đổi địa chỉ");  
        }

        if(user.getAddress() != null && !user.getAddress().isEmpty()) {
            currentUser.setAddress(user.getAddress());
            history.setChange_time_address(currentTime);
        } 

        // Update age
                

        // Update avatar
        if(user.getAvatar() != null) {
            Map uploadResult = cloudinary.uploader().upload(user.getAvatar().getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            currentUser.setAvatar(imageUrl);
        }

        if(errorMap !=null && !errorMap.isEmpty()){
            throw new ValidationException(errorMap);
        }
        this.userHistoryService.saveUserHistory(history);
        
        return this.userRepository.save(currentUser);
    }
    // 2: Convert to DTO

    public AuthReponseDTO convertUserToAuthReponseDTO(User user, String refreshToken, String accessToken){
        //Convert to UserDTO
        AuthReponseDTO.UserDTO userDTO = new AuthReponseDTO.UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setId(user.getId());

        //Save to DTO
        AuthReponseDTO authReponseDTO = new AuthReponseDTO();
        authReponseDTO.setUser(userDTO);
        authReponseDTO.setRefreshToken(refreshToken);
        authReponseDTO.setAccessToken(accessToken);

        return authReponseDTO;
    }

    public UserReponseDTO convertUsertoUserReponseDTO(User user){
        UserReponseDTO userReponseDTO = new UserReponseDTO();
        userReponseDTO.setId(user.getId());
        userReponseDTO.setEmail(user.getEmail());
        userReponseDTO.setUsername(user.getUsername());
        userReponseDTO.setAvatar(user.getAvatar());
        return userReponseDTO;
    }

    public List<UserReponseDTO> convertUsertoUserReponseDTO(List<User> users){
        List<UserReponseDTO> userReponseList = new ArrayList<UserReponseDTO>();
        for (User user : users) {
            userReponseList.add(convertUsertoUserReponseDTO(user));
        }
        return userReponseList;
    }


}
