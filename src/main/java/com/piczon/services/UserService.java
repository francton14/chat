package com.piczon.services;

import com.piczon.data.dto.UserUpdate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by franc on 9/19/2016.
 */
public interface UserService {

    public ResponseEntity<?> index(Boolean online);

    public String show(String username, String owner, Model model);

    public FileSystemResource showImage(String username);

    public ResponseEntity<?> currentUser(String username);

    public String update(String username, Model model);

    public String update(String username, UserUpdate userUpdate, BindingResult result, Model model);

    public String manageImage(String username, Model model);

    public String uploadImage(MultipartFile multipartFile, String username);

    public String removeImage(String username);

}
