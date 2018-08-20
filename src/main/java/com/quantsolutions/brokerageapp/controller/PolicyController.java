package com.quantsolutions.brokerageapp.controller;



import com.quantsolutions.brokerageapp.model.*;
import com.quantsolutions.brokerageapp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class PolicyController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = { 5, 10};

    @Autowired
    private PolicyRepository policyRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private InsurerRepository insurerRepo;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesAgentRepository salesAgentRepo;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    private final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    @RequestMapping(value = "/policies", method = RequestMethod.GET)

    public String showAllPolicies(Model model,@RequestParam("pageSize") Optional<Integer> pageSize,
                                  @RequestParam("page") Optional<Integer>page) {
        logger.debug("showAllPolicies()");

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Policy> policyList = policyRepo.findAll(PageRequest.of(evalPage, evalPageSize));
        System.out.println("policy list get total pages" + policyList.getTotalPages() + "policy list get number " + policyList.getNumber());
        PagerModel pager = new PagerModel(policyList.getTotalPages(),policyList.getNumber(),BUTTONS_TO_SHOW);
        model.addAttribute("policyList",policyList);
        model.addAttribute("selectedPageSize",evalPageSize);
        model.addAttribute("pageSizes",PAGE_SIZES);
        model.addAttribute("pager",pager);
        return "policies/list";

    }


    @RequestMapping(value = "/policies", method = RequestMethod.POST)
    public String saveOrUpdatePolicy(@ModelAttribute("policyForm") @Validated Policy policy,
                                     BindingResult result, Model model, final RedirectAttributes redirectAttributes){
        logger.debug("saveOrUpdatePolicy() : {}",policy);
        if (result.hasErrors()){

            return "policies/policyForm";
        }else{
            redirectAttributes.addFlashAttribute("css","success");
            if (policy.getId()==null){
                redirectAttributes.addFlashAttribute("msg","New Policy successfully added");
            }else {
                redirectAttributes.addFlashAttribute("msg","Policy details updated");
            }

            policyRepo.save(policy);

            // POST/REDIRECT/GET
            return "redirect:/policies";

        }

    }

    @RequestMapping(value="/policies/policyform/cancel",method= RequestMethod.GET)
    public String custFormCancel(){
        logger.debug("policyFormCancel");
        return "redirect:/policies";

    }


    @ModelAttribute("customerList")
    public ArrayList<Customer> populateCustomerList(){

        return (ArrayList<Customer>) customerRepo.findAll();

    }


    @ModelAttribute("productList")
    public ArrayList<Product>populateProductList(){
        return (ArrayList<Product>)productRepository.findAll();
    }



    @ModelAttribute("insurerList")
    public ArrayList<Insurer> populateInsurerList(){
        return (ArrayList<Insurer>) insurerRepo.findAll();
    }


    @ModelAttribute("salesAgentList")
    public ArrayList<SalesAgent>populateAgentList(){
        return (ArrayList<SalesAgent>) salesAgentRepo.findAll();
    }


    @RequestMapping(value = "/policies/add", method = RequestMethod.GET)
    public String setupPolicyForm(Model model){

        logger.debug("showAddPolicyForm()");

        Policy policy = new Policy();

        model.addAttribute("policyForm",policy);

        return "policies/policyForm";


    }

    // show update form

    @RequestMapping(value="/policies/{id}/update",method= RequestMethod.GET)
    public String showUpdatePolicyForm(@PathVariable("id") long id, Model model) {

        logger.debug("showUpdatePolicyForm() : {}", id);

        Policy policy = policyRepo.getOne(id);

        model.addAttribute("policyForm",policy);

        return "policies/policyForm";



    }

    @RequestMapping(value="/policies/{id}/payments")
    public String showPolicyPayments(@PathVariable("id") long id, Model model,
                                     @RequestParam("pageSize") Optional<Integer> pageSize,
                                     @RequestParam("page") Optional<Integer>page){
        logger.debug("showPolicyPayments() : {}",id);

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;



        Page<PaymentDetails>paymentDetailsList=policyRepo.findByPolicyId(id,PageRequest.of(evalPage,evalPageSize));
        PagerModel pager = new PagerModel(paymentDetailsList.getTotalPages(),paymentDetailsList.getNumber(),BUTTONS_TO_SHOW);
        model.addAttribute("paymentDetailsList",paymentDetailsList);
        model.addAttribute("selectedPageSize",evalPageSize);
        model.addAttribute("pageSizes",PAGE_SIZES);
        model.addAttribute("pager",pager);
        model.addAttribute("policy",policyRepo.getOne(id));

        return "policies/payment_list";

    }
}
