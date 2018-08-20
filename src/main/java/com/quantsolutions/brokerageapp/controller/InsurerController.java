package com.quantsolutions.brokerageapp.controller;

import com.quantsolutions.brokerageapp.model.Insurer;
import com.quantsolutions.brokerageapp.model.PagerModel;
import com.quantsolutions.brokerageapp.repository.InsurerRepository;
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
public class InsurerController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = { 5, 10};

    private final Logger logger = LoggerFactory.getLogger(InsurerController.class);

    @Autowired
    private InsurerRepository insurerRepository;


    // show update form

    @RequestMapping(value="/insurers/{id}/update",method= RequestMethod.GET)
    public String showUpdateInsurerForm(@PathVariable("id") long id, Model model) {

        logger.debug("showUpdateInsurerForm() : {}", id);

        Insurer insurer = insurerRepository.getOne(id);

        model.addAttribute("insurerForm",insurer);


        return "insurers/insurerForm";



    }


    //show add insurer form

    @RequestMapping(value = "/insurers/add", method = RequestMethod.GET)
    public String showInsurerForm(Model model){

        logger.debug("showAddInsurersForm()");

        Insurer insurer = new Insurer();

        model.addAttribute("insurerForm",insurer);


        return "insurers/insurerForm";


    }

    @RequestMapping(value="/insurers/insurerform/cancel",method= RequestMethod.GET)
    public String custFormCancel(){
        logger.debug("inurerFormCancel");
        return "redirect:/insurers";

    }


    @RequestMapping(value="/insurers",method= RequestMethod.GET)
    public String showAllInsurers(Model model,@RequestParam("pageSize") Optional<Integer> pageSize,
                                  @RequestParam("page") Optional<Integer>page){
        logger.debug("showAllInsurers()");

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Insurer> insurerList = insurerRepository.findAll(PageRequest.of(evalPage, evalPageSize));
        PagerModel pager = new PagerModel(insurerList.getTotalPages(),insurerList.getNumber(),BUTTONS_TO_SHOW);
        model.addAttribute("insurerList",insurerList);
        model.addAttribute("selectedPageSize",evalPageSize);
        model.addAttribute("pageSizes",PAGE_SIZES);
        model.addAttribute("pager",pager);

        return "insurers/list";
    }

    @RequestMapping(value = "/insurers", method = RequestMethod.POST)
    public String saveOrUpdateAgent(@ModelAttribute("insurerForm") @Validated Insurer insurer,
                                    BindingResult result, Model model, final RedirectAttributes redirectAttributes){
        logger.debug("saveOrUpdateForm() : {}",insurer);
        if (result.hasErrors()){

            return "insurers/insurerForm";
        }else{
            redirectAttributes.addFlashAttribute("css","success");
            if (insurer.getId()==null){
                redirectAttributes.addFlashAttribute("msg","Insurer successfully added");
            }else {
                redirectAttributes.addFlashAttribute("msg","Insurer  updated");
            }

            insurerRepository.save(insurer);

            // POST/REDIRECT/GET
            return "redirect:/insurers";

        }

    }




}
