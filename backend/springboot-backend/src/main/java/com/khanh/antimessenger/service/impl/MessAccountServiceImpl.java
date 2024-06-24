package com.khanh.antimessenger.service.impl;

import com.khanh.antimessenger.constant.KafkaEventType;
import com.khanh.antimessenger.dto.CreateAccountRequestDto;
import com.khanh.antimessenger.dto.UserKafkaDto;
import com.khanh.antimessenger.dto.UserKafkaEvent;
import com.khanh.antimessenger.dto.dtomapper.DtoMapper;
import com.khanh.antimessenger.exception.InvalidPasswordException;
import com.khanh.antimessenger.exception.ResourceAlreadyInUseException;
import com.khanh.antimessenger.exception.ResourceNotFoundException;
import com.khanh.antimessenger.model.MessAccount;
import com.khanh.antimessenger.model.Role;
import com.khanh.antimessenger.repository.MessAccountRepository;
import com.khanh.antimessenger.utilities.EmailService;
import com.khanh.antimessenger.service.MessAccountService;
import com.khanh.antimessenger.service.RoleService;
import com.khanh.antimessenger.utilities.KafkaProducerService;
import com.khanh.antimessenger.utilities.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import static com.khanh.antimessenger.constant.FileConstant.*;
import static com.khanh.antimessenger.constant.KafkaEventType.*;
import static com.khanh.antimessenger.constant.KafkaTopicName.*;
import static com.khanh.antimessenger.constant.SecurityConstant.PASSWORD_PATTERN;
import static com.khanh.antimessenger.constant.VerificationType.RESET_PASSWORD_BY_ADMIN;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MessAccountServiceImpl implements MessAccountService {
    private final MessAccountRepository messAccountRepository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final EmailService emailService;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public MessAccount createAccount(CreateAccountRequestDto data, MultipartFile profile) {
        DtoMapper<CreateAccountRequestDto, MessAccount> dtoMapper = new DtoMapper<>(CreateAccountRequestDto::new, MessAccount::new);
        MessAccount toCreate = dtoMapper.toEntity(data);
        log.info(String.format("Account nhan duoc la: %s", toCreate.toString()));
        if(!isEmailUnique(toCreate.getEmail())) throw new ResourceAlreadyInUseException("account", "email", toCreate.getEmail());
        if (!isUserNameUnique(toCreate.getUsername())) throw new ResourceAlreadyInUseException("account", "username", toCreate.getUsername());
        if (!isPasswordValid(toCreate.getPassword())) throw new InvalidPasswordException();
        Role toCreateRole = isRoleExisted(data.getRole());

        try {
            toCreate.setPassword(encoder.encode(toCreate.getPassword()));
            toCreate.setRole(toCreateRole);
            messAccountRepository.save(toCreate);
            saveProfileImage(toCreate, profile);

            // send to kafka consumer
            var username = toCreate.getUsername();
            sendKafkaMessage(username, USER_ADD_EVENT, ADD_USER_TOPIC);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred. Please try again");
        }
        return toCreate;
    }

    @Override
    public Collection<MessAccount> getListAccount(int page, int pageSize) {
        Pageable accountPage = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        List<MessAccount> listAccount = messAccountRepository.findAll(accountPage).getContent();
        return listAccount;
    }

    @Override
    public Collection<MessAccount> getAllAccount() {
        return messAccountRepository.findAll();
    }

    @Override
    public MessAccount getAccountByUserName(String username) {
        return messAccountRepository.findMessAccountByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("account", "username", username));
    }

    @Override
    public MessAccount getAccountByAccountId(Long id) {
        return messAccountRepository.findMessAccountByAccountId(id)
                .orElseThrow(() -> new ResourceNotFoundException("account", "id", id));
    }

    @Override
    public MessAccount updateAccount(Long accountId, CreateAccountRequestDto updateData, MultipartFile profileImage) {
        String toCheckUserName = updateData.getUsername();
        String toCheckEmail = updateData.getEmail();
        Role toCheckRole = isRoleExisted(updateData.getRole());

        MessAccount toUpdate = getAccountByAccountId(accountId);
        if (!toCheckUserName.equals(toUpdate.getUsername()) && messAccountRepository.findMessAccountByUsername(toCheckUserName).isPresent()) {
            throw new ResourceAlreadyInUseException("account", "username", toCheckUserName);
        }

        if (!toCheckEmail.equals(toUpdate.getEmail()) && messAccountRepository.findMessAccountByEmail(toCheckEmail).isPresent()) {
            throw new ResourceAlreadyInUseException("account", "email", toCheckEmail);
        }

        DtoMapper<CreateAccountRequestDto, MessAccount> mapper = new DtoMapper<>(CreateAccountRequestDto::new, MessAccount::new);
        toUpdate = mapper.toEntity(updateData, toUpdate);
        toUpdate.setRole(toCheckRole);
//        log.info(String.format("account dc update la: %s", toUpdate.toString()));
        messAccountRepository.save(toUpdate);
        updateProfileImage(toUpdate.getUsername(), profileImage);

        // send to kafka consumer
        var username = toUpdate.getUsername();
        sendKafkaMessage(username, USER_UPDATE_EVENT, UPDATE_USER_TOPIC);

        return toUpdate;
    }

    @Override
    public void deleteAccount(Long id) {
        MessAccount account = getAccountByAccountId(id);
        Path userFolder = Paths.get(USER_FOLDER + account.getUsername()).toAbsolutePath().normalize();
        try {
            FileUtils.deleteDirectory(new File(userFolder.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var username = account.getUsername();

        messAccountRepository.deleteById(id);

        sendKafkaMessage(username, USER_DELETE_EVENT, DELETE_USER_TOPIC);

        // send to kafka consumer
    }

    public void sendKafkaMessage(String username, KafkaEventType type, String topic) {
        UserKafkaDto kafkaDto = UserKafkaDto.builder().build();
        if (type == USER_DELETE_EVENT) {
            kafkaDto.setUsername(username);
        } else {
            var user = getAccountByUserName(username);
            var dtoMapper = new DtoMapper<>(UserKafkaDto::new, MessAccount::new);
            kafkaDto = dtoMapper.fromEntity(user);
            kafkaDto.setUserId(user.getAccountId());
        }
        var event = UserKafkaEvent.builder()
                .type(type)
                .user(kafkaDto)
                .build();
        kafkaProducerService.sendUserInfo(event, topic);
    }

    @Override
    public String resetPassword(Long id) {
        MessAccount toReset = getAccountByAccountId(id);
        String generatedPassword = PasswordService.generate();
        String toReturn = "";
        log.info(String.format("Password generated is: %s",generatedPassword));
        if (isPasswordValid(generatedPassword)) {
            toReturn = generatedPassword;
            toReset.setPassword(encoder.encode(generatedPassword));
            messAccountRepository.save(toReset);
            emailService.sendEmail(toReset.getEmail(), toReset.getFirstName(), generatedPassword, RESET_PASSWORD_BY_ADMIN);
        } else {
            throw new InvalidPasswordException();
        }
        return toReturn;
    }

    @Override
    public MessAccount updateProfileImage(String username, MultipartFile file) {
        MessAccount account = getAccountByUserName(username);
        saveProfileImage(account, file);
        return account;
    }

    private void saveProfileImage(MessAccount account, MultipartFile file) {
        if (file != null) {
            try {
                Path userFolder = Paths.get(USER_FOLDER + account.getUsername()).toAbsolutePath().normalize();
                if (!Files.exists(userFolder)) {
                    Files.createDirectories(userFolder);
                    log.info(DIRECTORY_CREATED + userFolder);
                }
                Files.deleteIfExists(Paths.get(userFolder + account.getUsername() + DOT + JPG_EXTENSION));
                Files.copy(file.getInputStream(), userFolder.resolve(account.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
                String imagePath = setProfileImageUrl(account.getUsername());
                account.setImageUrl(imagePath);
                messAccountRepository.save(account);
                log.info(FILE_SAVED_IN_FILE_SYSTEM + file.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            account.setImageUrl(setProfileImageUrl(null));
            messAccountRepository.save(account);
        }
    }

    private String setProfileImageUrl(String username) {
        // return default if username is null
        if (StringUtils.isNotBlank(username)) {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(USER_IMAGE_URL + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_URL + DEFAULT_IMAGE_FILE_NAME).toUriString();
    }

    private boolean isEmailUnique(String email) {
        return StringUtils.isNotBlank(email.trim()) && messAccountRepository.findMessAccountByEmail(email.trim()).isEmpty();
    }

    private boolean isUserNameUnique(String username) {
        return StringUtils.isNotBlank(username.trim()) && messAccountRepository.findMessAccountByUsername(username.trim()).isEmpty();
    }

    private Role isRoleExisted(String roleName) {
        return roleService.getRoleByRoleName(roleName.trim());
    }

    private boolean isPasswordValid(String password) {
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        return passwordPattern.matcher(Objects.requireNonNull(password)).matches();
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<MessAccount> optionalMessAccount = messAccountRepository.findMessAccountByUserName(username);
//        if (!optionalMessAccount.isPresent()) {
//            log.error(String.format("User not found by username: %s", username));
//            throw new UsernameNotFoundException(String.format("User not found by username: %s", username));
//        } else {
//            MessAccount account = optionalMessAccount.get();
//            account.setLastLoginDateDisplay(account.getLastLoginDate());
//            account.setLastLoginDate(LocalDateTime.now());
//            messAccountRepository.save(account);
//            UserPrincipal userPrincipal = new UserPrincipal(account);
//            log.info(String.format("Returning user found by username: %s", username));
//            return userPrincipal;
//        }
//    }
}
