package app.controller;

import app.service.SearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Log4j2
public class RestApiController {

    @Autowired
    SearchService searchService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getInitialPage() {
        return "index";
    }

    @RequestMapping(value = "/startApp", method = RequestMethod.POST)
    public String addEventPost(@RequestParam(value = "initialUrl") String initialUrl,
                               @RequestParam(value = "targetString") String targetString,
                               @RequestParam(value = "threadNumber") String threadNumber,
                               @RequestParam(value = "depthLevel") String depthLevel) {
        searchService.start(initialUrl, targetString, threadNumber, depthLevel);
        return "redirect:/stat";
    }

    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    public String getStat(Model model) {
        model.addAttribute("stat", searchService.returnStat());
        return "statistic";
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public String stop() {
        System.exit(1);
        return "index";
    }

}