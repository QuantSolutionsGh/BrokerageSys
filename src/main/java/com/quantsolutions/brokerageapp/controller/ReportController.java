package com.quantsolutions.brokerageapp.controller;


import com.quantsolutions.brokerageapp.model.PagerModel;
import com.quantsolutions.brokerageapp.model.Report;
import com.quantsolutions.brokerageapp.repository.ReportRepository;
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

//TODO
/*
1. Clients without any policies
2. Lapsed policies
3. Policies Due Renewal
4. Production Report
5. Lapsed Policies
6. Policies with outstanding premiums
7. Commission Statement - Based on receipts
8. Commission Payable - Based on production
 */

@Controller
public class ReportController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = { 5, 10};


    private final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportRepository reportRepository;

    // show update form

    @RequestMapping(value="/reports/{id}/update",method= RequestMethod.GET)
    public String showUpdateReportForm(@PathVariable("id") long id, Model model) {

        logger.debug("showUpdateReportForm() : {}", id);

        Report report =reportRepository.getOne(id);
        model.addAttribute("reportForm",report);

        return "reports/reportForm";



    }

    //show add report form

    @RequestMapping(value = "/reports/add", method = RequestMethod.GET)
    public String showAddReportForm(Model model){

        logger.debug("showAddReportForm()");

        Report report = new Report();

        model.addAttribute("reportForm",report);



        return "reports/reportForm";


    }



    @RequestMapping(value="/reports",method= RequestMethod.GET)
    public String showAllReports(Model model,@RequestParam("pageSize") Optional<Integer> pageSize,
                                 @RequestParam("page") Optional<Integer>page){
        logger.debug("showAllReports()");

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Report> reportList = reportRepository.findAll(PageRequest.of(evalPage, evalPageSize));
        PagerModel pager = new PagerModel(reportList.getTotalPages(),reportList.getNumber(),BUTTONS_TO_SHOW);
        model.addAttribute("reportList",reportList);
        model.addAttribute("selectedPageSize",evalPageSize);
        model.addAttribute("pageSizes",PAGE_SIZES);
        model.addAttribute("pager",pager);
        return "reports/list";
    }


    @RequestMapping(value = "/reports", method = RequestMethod.POST)
    public String saveOrUpdateAgent(@ModelAttribute("reportForm") @Validated Report report,
                                    BindingResult result, Model model, final RedirectAttributes redirectAttributes){
        logger.debug("saveOrUpdateReport() : {}",report);
        if (result.hasErrors()){

            return "reports/reportForm";
        }else{
            redirectAttributes.addFlashAttribute("css","success");
            if (report.getId()==null){
                redirectAttributes.addFlashAttribute("msg","New Report successfully added");
            }else {
                redirectAttributes.addFlashAttribute("msg","Report details updated");
            }

            reportRepository.save(report);

            // POST/REDIRECT/GET
            return "redirect:/reports";

        }

    }



}
