package com.patent.evaluator.service.impl.user;

import com.patent.evaluator.dao.UserRoleRepository;
import com.patent.evaluator.dao.UsersRepository;
import com.patent.evaluator.domain.Roles;
import com.patent.evaluator.domain.UserRole;
import com.patent.evaluator.domain.Users;
import com.patent.evaluator.dto.ResetPasswordDto;
import com.patent.evaluator.dto.UserInfoResponse;
import com.patent.evaluator.pageablesearch.model.PageableSearchFilterDto;
import com.patent.evaluator.service.api.user.UserService;
import com.patent.evaluator.util.ComboResponseBuilder;
import com.patent.evaluator.util.HashUtils;
import com.patent.evaluator.util.PasswordGenerator;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UsersRepository usersRepository;
    private UserRoleRepository userRoleRepository;
//    private final MailService mailService;

    @Override
    public void save(Users user) {
        usersRepository.save(user);
    }

    @Override
    public Users getUser(Long userId) {
        return usersRepository.getOne(userId);
    }

    @Override
    public List<Users> getAllUsers() {
        return IteratorUtils.toList(usersRepository.findAll().iterator());
    }

    @Override
    public List getComboUsers() {
        List<Object[]> resultList = this.usersRepository.findUsersAsComboValues();
        return ComboResponseBuilder.buildComboResponseList(resultList);
    }

    @Override
    public Page<UserInfoResponse> getUserInfoPage(PageRequest pageRequest) {
        Page<Users> usersPageResult = usersRepository.findUsersBy(pageRequest);
        List<UserInfoResponse> userInfoResponseList = new ArrayList<>();
        //TODO: deleted userlar sayilmamalidir
        buildResultList(usersPageResult, userInfoResponseList);

        long totalUserCount = usersPageResult.getTotalElements();
        return new PageImpl(userInfoResponseList, pageRequest, totalUserCount);
    }

    @Override
    public Page<UserInfoResponse> getUsersFiltered(PageableSearchFilterDto filterDto) {
        //TODO: yazılacak
        return null;
    }

    private void buildResultList(Page<Users> usersPageResult, List<UserInfoResponse> userInfoResponseList) {
        for (Users u : usersPageResult) {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            populateFromUser(u, userInfoResponse);


            List<UserRole> userRoles = userRoleRepository.findByUser(u);
            List<Long> roleIdList = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                roleIdList.add(userRole.getRole().getId());
            }
            userInfoResponse.setRoleIds(roleIdList);
            userInfoResponseList.add(userInfoResponse);
        }
    }

    @Override
    public List<Users> findByRole(Roles role) {
        List<UserRole> userRoles = userRoleRepository.findAllByRole(role);
        Set<Long> userIds = userRoles.stream().map(t -> t.getUser().getId()).collect(Collectors.toSet());
        List<Users> userListByRole = new ArrayList<>();
        for (Long userId : userIds) {
            Users user = usersRepository.getOne(userId);
            userListByRole.add(user);
        }

        return userListByRole;
    }

    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        Users user = this.usersRepository.findByEmail(resetPasswordDto.getEmail());
        String tempPass = passwordGenerator.generate(10);
        if (Objects.nonNull(user.getId())) {
            user.setPassword(HashUtils.sha256(tempPass));
            user.setPasswordTemporary(true);
            this.usersRepository.save(user);

            String content = "Geçici şifreniz: " + tempPass;
            String subject = "Şifre sıfırlama";
            sendMail(subject, content, user.getEmail());
            return "Geçici şifreniz mailinize gönderildi.";
        }

        user = this.usersRepository.findByMobilePhone(resetPasswordDto.getPhone());
        if (Objects.nonNull(user.getId())) {
            user.setPassword(HashUtils.sha256(tempPass));
            user.setPasswordTemporary(true);
            this.usersRepository.save(user);

            return "Sisteme giriş yapabileceğiniz geçici şifreniz telefonunuza gönderild.";
        }

        return "Verdiğiniz bilgilerle sisteme kayıtlı bir kullanıcı bulunamadı.";
    }

    @Override
    public List<Users> getAllUser(Integer locationId) {
        return null;
    }

    @Override
    public String getUserInfo(Users user) {
        StringBuilder userName = new StringBuilder();
        userName.append(" ");
        userName.append(user.getFirstname());
        userName.append(" ");
        userName.append(user.getSurname());
        return userName.toString();
    }

    @Override
    public Users getCurrentUser() {
        return (Users) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    private void sendMail(String subject, String content, String email) {
        // TODO Mail düzeltilecek
        String[] recipients = {"asd@tambilisim.com.tr"};
//        Mail mail = MailBuilder.newMail().setContent(content).setHtml(true).setMultipart(false).setRecipent(recipients).setSubject(subject).build();
//        mailService.sendEmail(mail);
    }

    private void populateFromUser(Users u, UserInfoResponse userInfoResponse) {
        userInfoResponse.setName(u.getFirstname());
        userInfoResponse.setSurname(u.getSurname());
        userInfoResponse.setEmail(u.getEmail());
        userInfoResponse.setMobile(u.getMobilePhone());
        userInfoResponse.setTelephone(u.getTelephone());
        userInfoResponse.setActive(u.getActive());
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
}
