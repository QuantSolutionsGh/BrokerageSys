package com.quantsolutions.brokerageapp.controller;


import com.quantsolutions.brokerageapp.model.PagerModel;
import com.quantsolutions.brokerageapp.model.SalesAgent;
import com.quantsolutions.brokerageapp.model.UserRoles;
import com.quantsolutions.brokerageapp.model.Users;
import com.quantsolutions.brokerageapp.repository.UserRepository;
import com.quantsolutions.brokerageapp.repository.UserRolesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class UserController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = { 5, 10};

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserRolesRepository userRolesRepository;

    @RequestMapping(value="/users/passwordchange",method=RequestMethod.GET)
    public String changePassword(HttpServletRequest request, Model model){

        String loggedInUser = request.getUserPrincipal().getName();

        //get user details
        Users user = userRepository.findUsersByUsername(loggedInUser);

        model.addAttribute("userForm",user);

        return "users/passwordchange";

    }

    @RequestMapping(value="/users/passwordchange",method=RequestMethod.POST)
    public String changePasswordPostMethod(Model model,@RequestParam("oldpassword") Optional<String> oldPassword,
                                           @RequestParam("newpassword") Optional<String>newPassword,
                                           @ModelAttribute("userForm") @Validated Users user,
                                           BindingResult result,final RedirectAttributes redirectAttributes
                                           ){

        //get user details especially password
        Users userp = userRepository.findUsersByUsername(user.getUsername());



        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(oldPassword.get(), userp.getPassword())){
            model.addAttribute("msg","Old password is not correct");
            return "users/passwordchange";

        }
        else{
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword.get()));
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("msg","Password changed successfully");
            return "redirect:/customers";
        }

    }



    // show update form

    @RequestMapping(value="/users/{id}/update",method= RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ROLE_DEVELOPER','ROLE_ADMIN')")
    public String showUpdateUsersForm(@PathVariable("id") long id, Model model) {

        logger.debug("showUpdateUsersForm() : {}", id);

        Users users =userRepository.getOne(id);
        model.addAttribute("userForm",users);

        return "users/userForm";



    }

    //show add salesagent form

    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String showAddAgentForm(Model model){

        logger.debug("showAddUsersForm()");

        Users users = new Users();

        model.addAttribute("userForm",users);



        return "users/userForm";


    }



    @RequestMapping(value="/users",method= RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ROLE_DEVELOPER','ROLE_ADMIN')")
    public String showAllUsers(Model model,@RequestParam("pageSize") Optional<Integer> pageSize,
                                @RequestParam("page") Optional<Integer>page){
        logger.debug("showAllUsers()");


        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Users> usersList = userRepository.findAll(PageRequest.of(evalPage, evalPageSize));
        PagerModel pager = new PagerModel(usersList.getTotalPages(),usersList.getNumber(),BUTTONS_TO_SHOW);
        model.addAttribute("usersList",usersList);
        model.addAttribute("selectedPageSize",evalPageSize);
        model.addAttribute("pageSizes",PAGE_SIZES);
        model.addAttribute("pager",pager);
        return "users/list";
    }


    @RequestMapping(value="/users/userform/cancel",method= RequestMethod.GET)
    public String custFormCancel(){
        logger.debug("userformcancel()");
        return "redirect:/users";

    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('ROLE_DEVELOPER','ROLE_ADMIN')")
    public String saveOrUpdateAgent(@ModelAttribute("userForm") @Validated Users user,
                                    BindingResult result, Model model, final RedirectAttributes redirectAttributes){
        logger.debug("saveOrUpdateUser() : {}",user);
        if (result.hasErrors()){

            return "users/userForm";
        }else{
            redirectAttributes.addFlashAttribute("css","success");
            if (user.getId()==null){
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                user.setEnabled(1);

                UserRoles userRoles = new UserRoles();

                userRoles.setRole("ROLE_USER");
                user.addToUserRoles(userRoles);

                redirectAttributes.addFlashAttribute("msg","New User successfully added");
            }else {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                redirectAttributes.addFlashAttribute("msg","User details updated");
            }



            userRepository.save(user);

            // POST/REDIRECT/GET
            return "redirect:/users";

        }

    }


}
