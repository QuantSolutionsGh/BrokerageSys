package com.quantsolutions.brokerageapp.controller;


import com.quantsolutions.brokerageapp.model.Insurer;
import com.quantsolutions.brokerageapp.model.PagerModel;
import com.quantsolutions.brokerageapp.model.SalesAgent;
import com.quantsolutions.brokerageapp.repository.SalesAgentRepository;
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

import java.util.Optional;

@Controller
public class SalesAgentController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = { 5, 10};

    private final Logger logger = LoggerFactory.getLogger(SalesAgentController.class);

    @Autowired
    private SalesAgentRepository salesAgentRepository;


    // show update form

    @RequestMapping(value="/salesagents/{id}/update",method= RequestMethod.GET)
    public String showUpdateSalesAgentsForm(@PathVariable("id") long id, Model model) {

        logger.debug("showUpdateSalesAgentsForm() : {}", id);

        SalesAgent salesAgent =salesAgentRepository.getOne(id);
        model.addAttribute("salesAgentForm",salesAgent);

        return "salesagents/salesAgentsForm";



    }

    @RequestMapping(value="/salesagents/salesagentform/cancel",method= RequestMethod.GET)
    public String custFormCancel(){
        logger.debug("salesAgentFormCancel");
        return "redirect:/salesagents";

    }

    //show add salesagent form

    @RequestMapping(value = "/salesagents/add", method = RequestMethod.GET)
    public String showAddAgentForm(Model model){

        logger.debug("showAddAgentForm()");

        SalesAgent salesAgent = new SalesAgent();

        model.addAttribute("salesAgentForm",salesAgent);



        return "salesagents/salesAgentsForm";


    }



    @RequestMapping(value="/salesagents",method= RequestMethod.GET)
    public String showAllAgents(Model model,@RequestParam("pageSize") Optional<Integer> pageSize,
                                @RequestParam("page") Optional<Integer>page){
        logger.debug("showAllAgents()");
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<SalesAgent> agentList = salesAgentRepository.findAll(PageRequest.of(evalPage, evalPageSize));
        PagerModel pager = new PagerModel(agentList.getTotalPages(),agentList.getNumber(),BUTTONS_TO_SHOW);
        model.addAttribute("agentList",agentList);
        model.addAttribute("selectedPageSize",evalPageSize);
        model.addAttribute("pageSizes",PAGE_SIZES);
        model.addAttribute("pager",pager);
        return "salesagents/list";
    }


    @RequestMapping(value = "/salesagents", method = RequestMethod.POST)
    public String saveOrUpdateAgent(@ModelAttribute("salesAgentForm") @Validated SalesAgent salesAgent,
                                    BindingResult result, Model model, final RedirectAttributes redirectAttributes){
        logger.debug("saveOrUpdateAgent() : {}",salesAgent);
        if (result.hasErrors()){

            return "salesagents/salesAgentsForm";
        }else{
            redirectAttributes.addFlashAttribute("css","success");
            if (salesAgent.getId()==null){
                redirectAttributes.addFlashAttribute("msg","New Agent successfully added");
            }else {
                redirectAttributes.addFlashAttribute("msg","Agent details updated");
            }

            salesAgentRepository.save(salesAgent);

            // POST/REDIRECT/GET
            return "redirect:/salesagents";

        }

    }





}
