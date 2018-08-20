package com.quantsolutions.brokerageapp.controller;


import com.quantsolutions.brokerageapp.model.Customer;
import com.quantsolutions.brokerageapp.model.PagerModel;
import com.quantsolutions.brokerageapp.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.concurrent.Callable;


@Controller
public class CustomerController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = { 5, 10};

    @Autowired
    private CustomerRepository custRepo;

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    // save or update customer
    // 1. @ModelAttribute bind form value
    // 2. @Validated form validator
    // 3. RedirectAttributes for flash value
    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public String saveOrUpdateCustomer(@ModelAttribute("custForm") @Validated Customer customer,
                                       BindingResult result, Model model, final RedirectAttributes redirectAttributes){
        logger.debug("saveOrUpdateCustomer() : {}",customer);
        if (result.hasErrors()){
            return "customers/custForm";
        }else{
            redirectAttributes.addFlashAttribute("css","success");
            if (customer.getId()==null){
                redirectAttributes.addFlashAttribute("msg","New customer successfully added");
            }else {
                redirectAttributes.addFlashAttribute("msg","Customer details updated");
            }

            custRepo.save(customer);

            // POST/REDIRECT/GET
            return "redirect:/customers";

        }

    }

    @RequestMapping(value = {"/customers","/index","/"}, method = RequestMethod.GET)

    public Callable<String> showAllCustomers(Model model, @RequestParam("pageSize") Optional<Integer> pageSize,
                                             @RequestParam("page") Optional<Integer>page) {
        return() -> {

            logger.debug("showAllCustomers()");


            // Evaluate page size. If requested parameter is null, return initial
            // page size
            int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
            // Evaluate page. If requested parameter is null or less than 0 (to
            // prevent exception), return initial size. Otherwise, return value of
            // param. decreased by 1.
            int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

            Page<Customer> clientlist = custRepo.findAll(PageRequest.of(evalPage, evalPageSize, Sort.Direction.ASC,"id"));
            System.out.println("client list get total pages" + clientlist.getTotalPages() + "client list get number " + clientlist.getNumber());
            PagerModel pager = new PagerModel(clientlist.getTotalPages(), clientlist.getNumber(), BUTTONS_TO_SHOW);
            model.addAttribute("customers", clientlist);
            model.addAttribute("selectedPageSize", evalPageSize);
            model.addAttribute("pageSizes", PAGE_SIZES);
            model.addAttribute("pager", pager);
            return "customers/list";
        };
    }

    //show add customer form

    @RequestMapping(value = "/customers/add", method = RequestMethod.GET)
    public String showAddUserForm(Model model){

        logger.debug("showAddUserForm()");

        Customer cust = new Customer();

        model.addAttribute("custForm",cust);

        return "customers/custForm";


    }

    @RequestMapping(value="/customers/custform/cancel",method= RequestMethod.GET)
    public String custFormCancel(){
        logger.debug("custFormCancel");
        return "redirect:/customers";

    }


    //delete row
    @RequestMapping(value="/customers/{id}/delete",method=RequestMethod.POST)
    public Callable<String> rowDelete(@PathVariable("id") long id,Model model,
                                      final RedirectAttributes redirectAttributes){

        return() ->{
            logger.debug("deleteCustomer() : {}",id);;

            custRepo.deleteById(id);

            redirectAttributes.addFlashAttribute("msg","Client deleted");

            return "redirect:/customers";

        };

    }


    // show update form

    @RequestMapping(value="/customers/{id}/update",method= RequestMethod.GET)
    public Callable<String> showUpdateCustomerForm(@PathVariable("id") long id, Model model) {

        return() -> {

            logger.debug("showUpdateCustomerForm() : {}", id);

            Customer customer = custRepo.getOne(id);

            model.addAttribute("custForm", customer);

            return "customers/custForm";

        };



    }


    //get all the policies of the client

    @RequestMapping(value="/customers/{id}/policies",method=RequestMethod.GET)
    public Callable<String> showCustomerPolicies(@PathVariable("id") long id, Model model) {

        return () -> {
            logger.debug("showCustomerPolicies() :{}", id);
            Customer customer = custRepo.getOne(id);
            model.addAttribute("cust", customer);

            return "customers/cust_policies";

        };

    }





}
